package greenmove;

/** Facture émise à la fin d'une location. Montant en centimes d'euro. */
public class Facture {

    private final long locationId;
    private final int montantCentimes;
    private final String referencePaiement;

    public Facture(long locationId, int montantCentimes, String referencePaiement) {
        this.locationId = locationId;
        this.montantCentimes = montantCentimes;
        this.referencePaiement = referencePaiement;
    }

    public long locationId() {
        return locationId;
    }

    public int montantCentimes() {
        return montantCentimes;
    }

    public String referencePaiement() {
        return referencePaiement;
    }

    @Override
    public String toString() {
        return "Facture{location=" + locationId
                + ", montant=" + (montantCentimes / 100.0) + " €"
                + ", paiement=" + referencePaiement + "}";
    }
}
