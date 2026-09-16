package greenmove;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Crée le schéma et quelques lignes dans la base H2 en mémoire, puis termine
 * une location pour montrer que le code de départ fonctionne.
 *
 * <pre>mvn -q compile exec:java -Dexec.mainClass=greenmove.JeuDEssai</pre>
 *
 * ou, plus simplement, exécuter cette classe depuis l'IDE.
 */
public class JeuDEssai {

    private static final String URL = "jdbc:h2:mem:greenmove;DB_CLOSE_DELAY=-1";

    public static void main(String[] args) throws SQLException {
        preparer();

        Facture facture = new LocationService().terminerLocation(1L);
        System.out.println(facture);

        Facture facture2 = new LocationService().terminerLocation(2L);
        System.out.println(facture2);
    }

    static void preparer() throws SQLException {
        try (Connection c = DriverManager.getConnection(URL, "sa", "");
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
