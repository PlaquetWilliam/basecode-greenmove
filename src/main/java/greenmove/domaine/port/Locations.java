package greenmove.domaine.port;

import greenmove.domaine.Location;

/**
 * Port : l'ensemble des locations de GreenMove, vu par le domaine.
 *
 * <p>Le domaine déclare ici ce dont il a besoin — retrouver une location, enregistrer
 * sa clôture — sans rien dire de la manière dont c'est rangé. Une base relationnelle,
 * un fichier ou une collection en mémoire sont autant d'adapters possibles.</p>
 */
public interface Locations {

    /**
     * Retrouve une location par son identifiant.
     *
     * @throws IllegalArgumentException si aucune location ne porte cet identifiant
     */
    Location trouverParId(long id);

    /** Mémorise la clôture d'une location : sa fin et son nouveau statut. */
    void enregistrerCloture(Location location);
}
