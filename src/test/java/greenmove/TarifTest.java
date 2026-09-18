package greenmove;

import greenmove.domaine.Duree;
import greenmove.domaine.Montant;
import greenmove.domaine.Tarif;
import greenmove.domaine.TypeVehicule;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests de la règle tarifaire GreenMove.
 *
 * <p>Aucune base de données, aucun réseau, aucun conteneur : {@link Tarif} est un objet
 * du domaine, construit avec {@code new} et interrogé directement. C'est le critère du
 * TP — si ces tests avaient besoin de démarrer H2, le découpage ne tiendrait pas.</p>
 *
 * <p>Barème vérifié (en centimes) : prise en charge 100, vélo 15/min, trottinette 22/min,
 * plafond journalier 1500, réduction abonné 20 % appliquée <b>après</b> le plafond.</p>
 */
@DisplayName("Calcul du tarif d'une location")
class TarifTest {

    private final Tarif tarif = new Tarif();

    @Test
    @DisplayName("Cas nominal : 24 minutes de vélo pour un usager non abonné → 4,60 €")
    void cas_nominal_velo_non_abonne() {
        Montant montant = tarif.calculer(Duree.enMinutes(24), TypeVehicule.VELO);

        // 100 de prise en charge + 24 × 15
        assertEquals(460, montant.centimes());
    }

    @Test
    @DisplayName("Durée nulle : seule la prise en charge est due")
    void duree_nulle_ne_facture_que_la_prise_en_charge() {
        assertEquals(100, tarif.calculer(Duree.enMinutes(0), TypeVehicule.VELO).centimes());
        assertEquals(100, tarif.calculer(Duree.enMinutes(0), TypeVehicule.TROTTINETTE).centimes());
    }

    @Test
    @DisplayName("Autre véhicule : 18 minutes de trottinette coûtent plus cher que le vélo")
    void tarif_trottinette_distinct_du_velo() {
        Duree duree = Duree.enMinutes(18);

        // 100 + 18 × 22
        assertEquals(496, tarif.calculer(duree, TypeVehicule.TROTTINETTE).centimes());
        // 100 + 18 × 15
        assertEquals(370, tarif.calculer(duree, TypeVehicule.VELO).centimes());
    }

    @Test
    @DisplayName("Plafond journalier : une très longue location ne dépasse jamais 15,00 €")
    void plafond_journalier_applique() {
        // 100 + 300 × 15 = 4600 centimes sans plafond
        assertEquals(1500, tarif.calculer(Duree.enMinutes(300), TypeVehicule.VELO).centimes());
        // 100 + 300 × 22 = 6700 centimes sans plafond
        assertEquals(1500, tarif.calculer(Duree.enMinutes(300), TypeVehicule.TROTTINETTE).centimes());
    }

    @Test
    @DisplayName("Abonné : 20 % de réduction sur le montant dû")
    void reduction_abonne() {
        // 100 + 18 × 22 = 496, moins 20 % → 397
        assertEquals(397, tarif.calculer(Duree.enMinutes(18), TypeVehicule.TROTTINETTE, true).centimes());
        // Sans abonnement, rien n'est retiré
        assertEquals(496, tarif.calculer(Duree.enMinutes(18), TypeVehicule.TROTTINETTE, false).centimes());
    }

    @Test
    @DisplayName("La réduction abonné s'applique après le plafond, pas avant")
    void reduction_abonne_appliquee_apres_le_plafond() {
        // Plafond 1500, puis −20 % → 1200. Si l'ordre était inversé : 4600 − 20 % = 3680, donc 1500.
        assertEquals(1200, tarif.calculer(Duree.enMinutes(300), TypeVehicule.VELO, true).centimes());
    }

    @Test
    @DisplayName("Une durée négative est refusée")
    void duree_negative_refusee() {
        assertThrows(IllegalArgumentException.class, () -> Duree.enMinutes(-1));
    }
}
