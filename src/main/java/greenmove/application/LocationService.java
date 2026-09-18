package greenmove.application;

import greenmove.domaine.Facture;
import greenmove.domaine.Location;
import greenmove.domaine.Montant;
import greenmove.domaine.Recu;
import greenmove.domaine.Tarif;
import greenmove.domaine.Usager;
import greenmove.domaine.port.Factures;
import greenmove.domaine.port.Locations;
import greenmove.domaine.port.Paiement;
import greenmove.domaine.port.Usagers;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Cas d'utilisation « terminer une location » : orchestre le domaine et ses ports,
 * sans porter lui-même de règle métier ni de code technique (SQL, HTTP).
 *
 * <p>Ce service ne connaît que des interfaces déclarées par le domaine. Il ne construit
 * aucune de ses dépendances : il les reçoit par constructeur. Le câblage des adapters
 * concrets se fait dans la racine de composition ({@code greenmove.JeuDEssai}).</p>
 */
public class LocationService {

    private final Locations locations;
    private final Usagers usagers;
    private final Factures factures;
    private final Paiement paiement;
    private final Tarif tarif;

    /** Câblage courant : la règle tarifaire est un objet du domaine, pas une dépendance technique. */
    public LocationService(Locations locations, Usagers usagers, Factures factures, Paiement paiement) {
        this(locations, usagers, factures, paiement, new Tarif());
    }

    public LocationService(Locations locations, Usagers usagers, Factures factures,
                           Paiement paiement, Tarif tarif) {
        this.locations = Objects.requireNonNull(locations, "locations");
        this.usagers = Objects.requireNonNull(usagers, "usagers");
        this.factures = Objects.requireNonNull(factures, "factures");
        this.paiement = Objects.requireNonNull(paiement, "paiement");
        this.tarif = Objects.requireNonNull(tarif, "tarif");
    }

    public Facture terminerLocation(long locationId) {
        // 1. relire la location et la clôturer
        Location location = locations.trouverParId(locationId)
                .terminer(LocalDateTime.now());

        // 2. connaître l'usager
        Usager usager = usagers.trouverParId(location.usagerId());

        // 3. calculer le montant (règle métier portée par le domaine)
        Montant montant = tarif.calculer(location.duree(), location.typeVehicule(), usager.estAbonne());

        // 4. débiter l'usager (port métier, adapter côté infrastructure)
        Recu recu = paiement.debiter(usager.moyenPaiement(), montant);

        // 5. enregistrer la facture et la clôture
        Facture facture = new Facture(location.id(), montant, recu.reference());
        factures.enregistrer(facture);
        locations.enregistrerCloture(location);

        return facture;
    }
}
