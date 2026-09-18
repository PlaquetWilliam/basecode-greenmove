package greenmove;

import greenmove.application.LocationService;
import greenmove.domaine.Facture;
import greenmove.infrastructure.persistance.InitialisationBaseH2;

import java.sql.SQLException;

/**
 * Point d'entrée : prépare la base H2 en mémoire, puis termine deux locations
 * pour montrer que le code fonctionne.
 *
 * <pre>mvn -q compile exec:java -Dexec.mainClass=greenmove.JeuDEssai</pre>
 *
 * ou, plus simplement, exécuter cette classe depuis l'IDE.
 */
public class JeuDEssai {

    public static void main(String[] args) throws SQLException {
        InitialisationBaseH2.preparer();

        Facture facture = new LocationService().terminerLocation(1L);
        System.out.println(facture);

        Facture facture2 = new LocationService().terminerLocation(2L);
        System.out.println(facture2);
    }
}
