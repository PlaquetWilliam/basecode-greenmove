package greenmove.domaine.port;

import greenmove.domaine.Usager;

/**
 * Port : les usagers inscrits au service, vus par le domaine.
 *
 * <p>Interface volontairement étroite (ségrégation des interfaces) : le cas
 * d'utilisation « terminer une location » n'a besoin que de retrouver un usager.</p>
 */
public interface Usagers {

    /**
     * Retrouve un usager par son identifiant.
     *
     * @throws IllegalStateException si aucun usager ne porte cet identifiant
     */
    Usager trouverParId(long id);
}
