package greenmove.domaine;

/**
 * Règle tarifaire GreenMove.
 *
 * <p>Prise en charge + prix à la minute selon le véhicule, plafonné à la journée,
 * puis réduction abonné appliquée <b>après</b> le plafond.</p>
 */
public class Tarif {

    // Tarifs en centimes
    static final int PRISE_EN_CHARGE_VELO = 100;
    static final int PRIX_MINUTE_VELO = 15;
    static final int PRISE_EN_CHARGE_TROTTINETTE = 100;
    static final int PRIX_MINUTE_TROTTINETTE = 22;
    static final Montant PLAFOND_JOURNALIER = Montant.enCentimes(1500);
    static final int REDUCTION_ABONNE_POURCENT = 20;

    /** Montant dû pour un usager non abonné. */
    public Montant calculer(Duree duree, TypeVehicule type) {
        long brut = switch (type) {
            case VELO -> PRISE_EN_CHARGE_VELO + duree.minutes() * PRIX_MINUTE_VELO;
            case TROTTINETTE -> PRISE_EN_CHARGE_TROTTINETTE + duree.minutes() * PRIX_MINUTE_TROTTINETTE;
        };
        long plafonne = Math.min(brut, PLAFOND_JOURNALIER.centimes());
        return Montant.enCentimes((int) plafonne);
    }

    /** Montant dû en tenant compte de l'abonnement de l'usager. */
    public Montant calculer(Duree duree, TypeVehicule type, boolean abonne) {
        Montant montant = calculer(duree, type);
        return abonne ? montant.reduitDe(REDUCTION_ABONNE_POURCENT) : montant;
    }
}
