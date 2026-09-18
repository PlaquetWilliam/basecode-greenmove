package greenmove.domaine;

/** Somme d'argent en centimes d'euro. Objet valeur immuable. */
public final class Montant {

    private final int centimes;

    private Montant(int centimes) {
        this.centimes = centimes;
    }

    public static Montant enCentimes(int centimes) {
        return new Montant(centimes);
    }

    public int centimes() {
        return centimes;
    }

    /** Ramène le montant au plafond s'il le dépasse. */
    public Montant plafonneA(Montant plafond) {
        return centimes > plafond.centimes ? plafond : this;
    }

    /** Retire un pourcentage (la réduction est tronquée au centime). */
    public Montant reduitDe(int pourcent) {
        return new Montant(centimes - (centimes * pourcent / 100));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Montant autre && autre.centimes == centimes;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(centimes);
    }

    @Override
    public String toString() {
        return (centimes / 100.0) + " €";
    }
}
