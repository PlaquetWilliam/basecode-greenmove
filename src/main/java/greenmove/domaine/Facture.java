package greenmove.domaine;

/** Facture émise à la fin d'une location. */
public class Facture {

    private final long locationId;
    private final Montant montant;
    private final String referencePaiement;

    public Facture(long locationId, Montant montant, String referencePaiement) {
        this.locationId = locationId;
        this.montant = montant;
        this.referencePaiement = referencePaiement;
    }

    public long locationId() {
        return locationId;
    }

    public Montant montant() {
        return montant;
    }

    public String referencePaiement() {
        return referencePaiement;
    }

    @Override
    public String toString() {
        return "Facture{location=" + locationId
                + ", montant=" + montant
                + ", paiement=" + referencePaiement + "}";
    }
}
