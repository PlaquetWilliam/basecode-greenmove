package greenmove.domaine;

/** Usager du service de location. */
public class Usager {

    private final long id;
    private final String nom;
    private final boolean abonne;
    private final MoyenPaiement moyenPaiement;

    public Usager(long id, String nom, boolean abonne, MoyenPaiement moyenPaiement) {
        this.id = id;
        this.nom = nom;
        this.abonne = abonne;
        this.moyenPaiement = moyenPaiement;
    }

    public long id() {
        return id;
    }

    public String nom() {
        return nom;
    }

    public boolean estAbonne() {
        return abonne;
    }

    public MoyenPaiement moyenPaiement() {
        return moyenPaiement;
    }
}
