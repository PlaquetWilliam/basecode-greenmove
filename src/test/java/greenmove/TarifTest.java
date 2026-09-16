package greenmove;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Point de départ des tests du TP.
 *
 * <p>Pour l'instant, ce test ne vérifie rien d'utile : il calcule le tarif à la main,
 * parce que la règle tarifaire est enfermée dans {@link LocationService}, qui a besoin
 * d'une base de données pour s'exécuter.</p>
 *
 * <p><b>Votre objectif :</b> après refactoring, remplacer ce test par trois tests qui
 * appellent directement l'objet de domaine porteur du calcul — sans base, sans réseau,
 * sans conteneur. Par exemple :</p>
 *
 * <pre>
 * Montant m = new Tarif().calculer(Duree.enMinutes(24), TypeVehicule.VELO);
 * assertEquals(460, m.centimes());
 * </pre>
 *
 * <p>Cas à couvrir au minimum : le cas nominal, une durée nulle, et un second type de
 * véhicule. Le plafond journalier et la réduction abonné sont d'excellents tests en plus.</p>
 */
class TarifTest {

    @Test
    void temoin_a_remplacer_apres_refactoring() {
        // 24 minutes de vélo : 100 centimes de prise en charge + 24 × 15
        int attendu = 100 + 24 * 15;
        assertEquals(460, attendu);
    }
}
