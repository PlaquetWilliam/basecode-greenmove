package greenmove.domaine.sonde;

import greenmove.infrastructure.persistance.ConnexionH2;

/**
 * Classe de test <b>volontairement fautive</b> : elle se présente comme une classe du
 * domaine ({@code greenmove.domaine..}) et dépend pourtant de l'infrastructure.
 *
 * <p>Elle n'existe que pour prouver que la règle ArchUnit mord vraiment. Elle vit dans
 * les sources de test, donc {@code ImportOption.DoNotIncludeTests} l'écarte de l'analyse
 * du code de production : elle ne peut pas faire rougir la règle par accident.</p>
 *
 * @see greenmove.ArchitectureTest
 */
public class SondeDeViolation {

    /** La dépendance interdite, et la seule raison d'être de cette classe. */
    private final ConnexionH2 connexionInterdite = null;

    public ConnexionH2 connexionInterdite() {
        return connexionInterdite;
    }
}
