package greenmove.domaine.port;

import greenmove.domaine.Montant;
import greenmove.domaine.MoyenPaiement;
import greenmove.domaine.Recu;

/**
 * Port : le débit d'un usager, exprimé en vocabulaire métier.
 *
 * <p>C'est la couche anti-corruption vis-à-vis du prestataire externe, dont l'API
 * change une à deux fois par an : ni son URL, ni le nom de ses champs, ni son format
 * de montant n'apparaissent ici. Quand le prestataire bouge, seul l'adapter change
 * et aucun test métier ne bouge.</p>
 */
public interface Paiement {

    /**
     * Débite le moyen de paiement de l'usager du montant dû.
     *
     * @return le reçu du débit, qui porte la référence de la transaction
     * @throws IllegalArgumentException si le montant à débiter n'est pas exigible
     */
    Recu debiter(MoyenPaiement moyen, Montant montant);
}
