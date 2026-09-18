package greenmove.application;

import greenmove.domaine.Facture;
import greenmove.domaine.Location;
import greenmove.domaine.Montant;
import greenmove.domaine.Tarif;
import greenmove.domaine.Usager;
import greenmove.infrastructure.paiement.PaymentApi;
import greenmove.infrastructure.persistance.FactureRepositoryJdbc;
import greenmove.infrastructure.persistance.LocationRepositoryJdbc;
import greenmove.infrastructure.persistance.UsagerRepositoryJdbc;

import java.time.LocalDateTime;

/**
 * Cas d'utilisation « terminer une location » : orchestre le domaine et l'infrastructure,
 * sans porter lui-même de règle métier ni de code technique (SQL, HTTP).
 *
 * <p>Étape suivante du TP : remplacer les classes concrètes de l'infrastructure par
 * des interfaces définies dans le domaine, injectées par constructeur.</p>
 */
public class LocationService {

    private final LocationRepositoryJdbc locations = new LocationRepositoryJdbc();
    private final UsagerRepositoryJdbc usagers = new UsagerRepositoryJdbc();
    private final FactureRepositoryJdbc factures = new FactureRepositoryJdbc();
    private final PaymentApi paiement = new PaymentApi();
    private final Tarif tarif = new Tarif();

    public Facture terminerLocation(long locationId) {
        // 1. relire la location et la clôturer
        Location location = locations.trouverParId(locationId)
                .terminer(LocalDateTime.now());

        // 2. connaître l'usager
        Usager usager = usagers.trouverParId(location.usagerId());

        // 3. calculer le montant (règle métier portée par le domaine)
        Montant montant = tarif.calculer(location.duree(), location.typeVehicule(), usager.estAbonne());

        // 4. débiter chez le prestataire externe
        String referencePaiement = paiement.charger(usager.moyenPaiement(), montant.centimes());

        // 5. enregistrer la facture et la clôture
        Facture facture = new Facture(location.id(), montant, referencePaiement);
        factures.enregistrer(facture);
        locations.enregistrerCloture(location);

        return facture;
    }
}
