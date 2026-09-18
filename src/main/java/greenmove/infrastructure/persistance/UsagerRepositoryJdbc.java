package greenmove.infrastructure.persistance;

import greenmove.domaine.Usager;
import greenmove.domaine.port.Usagers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Adapter JDBC du port {@link Usagers} : la table {@code usager}. */
public class UsagerRepositoryJdbc implements Usagers {

    @Override
    public Usager trouverParId(long id) {
        try (Connection connexion = ConnexionH2.ouvrir();
             PreparedStatement select = connexion.prepareStatement(
                     "SELECT id, nom, abonne, moyen_paiement FROM usager WHERE id = ?")) {
            select.setLong(1, id);
            try (ResultSet rs = select.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalStateException("Usager introuvable : " + id);
                }
                LigneUsager ligne = new LigneUsager(
                        rs.getLong("id"),
                        rs.getString("nom"),
                        rs.getBoolean("abonne"),
                        rs.getString("moyen_paiement"));
                return ConvertisseurPersistance.versDomaine(ligne);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Échec de la lecture de l'usager " + id, e);
        }
    }
}
