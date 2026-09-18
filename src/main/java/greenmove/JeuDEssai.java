package greenmove;

import greenmove.application.LocationService;
import greenmove.domaine.Facture;
import greenmove.domaine.port.Factures;
import greenmove.domaine.port.Locations;
import greenmove.domaine.port.Paiement;
import greenmove.domaine.port.Usagers;
import greenmove.infrastructure.paiement.PaiementPrestataire;
import greenmove.infrastructure.paiement.PaymentApi;
import greenmove.infrastructure.persistance.FactureRepositoryJdbc;
import greenmove.infrastructure.persistance.InitialisationBaseH2;
import greenmove.infrastructure.persistance.LocationRepositoryJdbc;
import greenmove.infrastructure.persistance.UsagerRepositoryJdbc;

import java.sql.SQLException;

/**
 * Point d'entrée et <b>racine de composition</b> : le seul endroit où l'on choisit
 * les adapters concrets et où l'on fait {@code new} sur une classe d'infrastructure.
 * Le domaine et l'application n'y touchent jamais.
 *
 * <pre>mvn -q compile exec:java -Dexec.mainClass=greenmove.JeuDEssai</pre>
 *
 * ou, plus simplement, exécuter cette classe depuis l'IDE.
 */
public class JeuDEssai {

    public static void main(String[] args) throws SQLException {
        InitialisationBaseH2.preparer();

        // Câblage : les adapters d'infrastructure branchés sur les ports du domaine.
        Locations locations = new LocationRepositoryJdbc();
        Usagers usagers = new UsagerRepositoryJdbc();
        Factures factures = new FactureRepositoryJdbc();
        Paiement paiement = new PaiementPrestataire(new PaymentApi());

        LocationService service = new LocationService(locations, usagers, factures, paiement);

        Facture facture = service.terminerLocation(1L);
        System.out.println(facture);

        Facture facture2 = service.terminerLocation(2L);
        System.out.println(facture2);
    }
}
