package greenmove.infrastructure.persistance;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/** Crée le schéma de la base H2 en mémoire et y insère le jeu d'essai. */
public final class InitialisationBaseH2 {

    private InitialisationBaseH2() {
    }

    public static void preparer() throws SQLException {
        try (Connection c = ConnexionH2.ouvrir();
             Statement s = c.createStatement()) {

            s.execute("""
                CREATE TABLE IF NOT EXISTS usager (
                    id BIGINT PRIMARY KEY,
                    nom VARCHAR(100),
                    abonne BOOLEAN,
                    moyen_paiement VARCHAR(50)
                )""");

            s.execute("""
                CREATE TABLE IF NOT EXISTS location (
                    id BIGINT PRIMARY KEY,
                    usager_id BIGINT,
                    type_vehicule VARCHAR(20),
                    debut TIMESTAMP,
                    fin TIMESTAMP,
                    statut VARCHAR(20)
                )""");

            s.execute("""
                CREATE TABLE IF NOT EXISTS facture (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    location_id BIGINT,
                    montant_centimes INT,
                    reference_paiement VARCHAR(50),
                    emise_le TIMESTAMP
                )""");

            s.execute("DELETE FROM facture");
            s.execute("DELETE FROM location");
            s.execute("DELETE FROM usager");

            s.execute("INSERT INTO usager VALUES (10, 'Camille Ferrand', false, 'card_4242')");
            s.execute("INSERT INTO usager VALUES (11, 'Yanis Morel', true, 'card_9310')");

            // 24 minutes de vélo, usager non abonné
            s.execute("INSERT INTO location VALUES "
                    + "(1, 10, 'VELO', TIMESTAMP '2026-09-14 08:02:00', "
                    + "TIMESTAMP '2026-09-14 08:26:00', 'EN_COURS')");

            // 18 minutes de trottinette, usager abonné
            s.execute("INSERT INTO location VALUES "
                    + "(2, 11, 'TROTTINETTE', TIMESTAMP '2026-09-14 17:40:00', "
                    + "TIMESTAMP '2026-09-14 17:58:00', 'EN_COURS')");
        }
    }
}
