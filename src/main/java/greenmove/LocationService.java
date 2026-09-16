package greenmove;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Termine une location GreenMove : relit la location, calcule le montant dû,
 * débite l'usager chez le prestataire de paiement et enregistre la facture.
 *
 * <p>C'est le code actuellement en production. Il fonctionne. Il est aussi
 * parfaitement impossible à tester sans base de données ni réseau, et la règle
 * tarifaire y est noyée au milieu de la plomberie technique.</p>
 *
 * <p><b>Votre travail :</b> faire apparaître les couches, extraire le domaine,
 * inverser les dépendances — sans changer le comportement fonctionnel.</p>
 */
public class LocationService {

    private static final String URL = "jdbc:h2:mem:greenmove;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    // Tarifs en centimes. Oui, ils sont en dur, et oui, le marketing les change tous les six mois.
    private static final int PRISE_EN_CHARGE_VELO = 100;
    private static final int PRIX_MINUTE_VELO = 15;
    private static final int PRISE_EN_CHARGE_TROTTINETTE = 100;
    private static final int PRIX_MINUTE_TROTTINETTE = 22;
    private static final int PLAFOND_JOURNALIER = 1500;
    private static final int REDUCTION_ABONNE_POURCENT = 20;

    public Facture terminerLocation(long locationId) {
        try (Connection connexion = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // ---- 1. relire la location
            PreparedStatement select = connexion.prepareStatement(
                    "SELECT id, usager_id, type_vehicule, debut, fin FROM location WHERE id = ?");
            select.setLong(1, locationId);
            ResultSet rs = select.executeQuery();
            if (!rs.next()) {
                throw new IllegalArgumentException("Location introuvable : " + locationId);
            }

            long usagerId = rs.getLong("usager_id");
            String typeVehicule = rs.getString("type_vehicule");
            LocalDateTime debut = rs.getTimestamp("debut").toLocalDateTime();
            Timestamp finBrute = rs.getTimestamp("fin");
            LocalDateTime fin = finBrute != null ? finBrute.toLocalDateTime() : LocalDateTime.now();

            // ---- 2. calculer le montant (la règle métier, perdue au milieu du reste)
            long minutes = Duration.between(debut, fin).toMinutes();
            if (minutes < 0) {
                throw new IllegalStateException("Fin antérieure au début pour la location " + locationId);
            }

            int montant;
            if ("VELO".equals(typeVehicule)) {
                montant = (int) (PRISE_EN_CHARGE_VELO + minutes * PRIX_MINUTE_VELO);
            } else if ("TROTTINETTE".equals(typeVehicule)) {
                montant = (int) (PRISE_EN_CHARGE_TROTTINETTE + minutes * PRIX_MINUTE_TROTTINETTE);
            } else {
                throw new IllegalStateException("Type de véhicule inconnu : " + typeVehicule);
            }

            if (montant > PLAFOND_JOURNALIER) {
                montant = PLAFOND_JOURNALIER;
            }

            // ---- 3. l'usager est-il abonné ? une requête de plus, au milieu du calcul
            PreparedStatement selectUsager = connexion.prepareStatement(
                    "SELECT abonne, moyen_paiement FROM usager WHERE id = ?");
            selectUsager.setLong(1, usagerId);
            ResultSet rsUsager = selectUsager.executeQuery();
            if (!rsUsager.next()) {
                throw new IllegalStateException("Usager introuvable : " + usagerId);
            }
            boolean abonne = rsUsager.getBoolean("abonne");
            String moyenPaiement = rsUsager.getString("moyen_paiement");

            if (abonne) {
                montant = montant - (montant * REDUCTION_ABONNE_POURCENT / 100);
            }

            // ---- 4. débiter chez le prestataire externe
            String referencePaiement = new PaymentApi().charger(moyenPaiement, montant);

            // ---- 5. enregistrer la facture
            PreparedStatement insert = connexion.prepareStatement(
                    "INSERT INTO facture (location_id, montant_centimes, reference_paiement, emise_le) "
                            + "VALUES (?, ?, ?, ?)");
            insert.setLong(1, locationId);
            insert.setInt(2, montant);
            insert.setString(3, referencePaiement);
            insert.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            insert.executeUpdate();

            PreparedStatement cloture = connexion.prepareStatement(
                    "UPDATE location SET fin = ?, statut = 'TERMINEE' WHERE id = ?");
            cloture.setTimestamp(1, Timestamp.valueOf(fin));
            cloture.setLong(2, locationId);
            cloture.executeUpdate();

            return new Facture(locationId, montant, referencePaiement);

        } catch (SQLException e) {
            throw new RuntimeException("Échec de la clôture de la location " + locationId, e);
        }
    }
}
