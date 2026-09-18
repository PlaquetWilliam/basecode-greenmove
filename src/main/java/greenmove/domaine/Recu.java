package greenmove.domaine;

import java.util.Objects;

/**
 * Reçu d'un débit accepté : la preuve, côté métier, que l'usager a été débité.
 *
 * <p>Objet valeur renvoyé par le port {@code Paiement}. Il ne porte que ce dont le
 * domaine a besoin — la référence de la transaction — et rien du format de réponse
 * du prestataire.</p>
 */
public final class Recu {

    private final String reference;

    private Recu(String reference) {
        if (reference == null || reference.isBlank()) {
            throw new IllegalArgumentException("Un reçu doit porter une référence de transaction");
        }
        this.reference = reference;
    }

    public static Recu de(String reference) {
        return new Recu(reference);
    }

    public String reference() {
        return reference;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Recu autre && autre.reference.equals(reference);
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
