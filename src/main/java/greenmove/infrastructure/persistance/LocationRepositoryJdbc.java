package greenmove.infrastructure.persistance;

import greenmove.domaine.Location;
import greenmove.domaine.port.Locations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/** Adapter JDBC du port {@link Locations} : la table {@code location}. */
public class LocationRepositoryJdbc implements Locations {

    @Override
    public Location trouverParId(long id) {
        try (Connection connexion = ConnexionH2.ouvrir();
             PreparedStatement select = connexion.prepareStatement(
                     "SELECT id, usager_id, type_vehicule, debut, fin, statut FROM location WHERE id = ?")) {
            select.setLong(1, id);
            try (ResultSet rs = select.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalArgumentException("Location introuvable : " + id);
                }
                LigneLocation ligne = new LigneLocation(
                        rs.getLong("id"),
                        rs.getLong("usager_id"),
                        rs.getString("type_vehicule"),
                        rs.getTimestamp("debut"),
                        rs.getTimestamp("fin"),
                        rs.getString("statut"));
                return ConvertisseurPersistance.versDomaine(ligne);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Échec de la lecture de la location " + id, e);
        }
    }

    @Override
    public void enregistrerCloture(Location location) {
        try (Connection connexion = ConnexionH2.ouvrir();
             PreparedStatement cloture = connexion.prepareStatement(
                     "UPDATE location SET fin = ?, statut = ? WHERE id = ?")) {
            cloture.setTimestamp(1, Timestamp.valueOf(location.fin()));
            cloture.setString(2, location.statut().name());
            cloture.setLong(3, location.id());
            cloture.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Échec de la clôture de la location " + location.id(), e);
        }
    }
}
