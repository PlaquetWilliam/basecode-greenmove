package greenmove.domaine;

import java.util.Objects;

/**
 * Moyen de paiement enregistré par un usager, désigné par sa référence.
 *
 * <p>Objet valeur : le domaine manipule un moyen de paiement, pas une chaîne de
 * caractères dont il ignorerait le sens.</p>
 */
public final class MoyenPaiement {

    private final String reference;

    private MoyenPaiement(String reference) {
        if (reference == null || reference.isBlank()) {
            throw new IllegalArgumentException("Un moyen de paiement doit porter une référence");
        }
        this.reference = reference;
    }

    public static MoyenPaiement de(String reference) {
        return new MoyenPaiement(reference);
    }

    public String reference() {
        return reference;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MoyenPaiement autre && autre.reference.equals(reference);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(reference);
    }

    @Override
    public String toString() {
        return reference;
    }
}
