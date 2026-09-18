package greenmove.infrastructure.persistance;

import greenmove.domaine.Facture;
import greenmove.domaine.port.Factures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

/** Adapter JDBC du port {@link Factures} : la table {@code facture}. */
public class FactureRepositoryJdbc implements Factures {

    @Override
    public void enregistrer(Facture facture) {
        LigneFacture ligne = ConvertisseurPersistance.versLigne(facture, LocalDateTime.now());
        try (Connection connexion = ConnexionH2.ouvrir();
             PreparedStatement insert = connexion.prepareStatement(
                     "INSERT INTO facture (location_id, montant_centimes, reference_paiement, emise_le) "
                             + "VALUES (?, ?, ?, ?)")) {
            insert.setLong(1, ligne.locationId());
            insert.setInt(2, ligne.montantCentimes());
            insert.setString(3, ligne.referencePaiement());
            insert.setTimestamp(4, ligne.emiseLe());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Échec de l'enregistrement de la facture de la location "
                    + facture.locationId(), e);
        }
    }
}
