package greenmove;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * La règle de dépendance, rendue exécutable.
 *
 * <p>Un schéma d'architecture se périme ; un test ne se périme pas. Ces règles échouent
 * à la première classe du domaine qui redeviendrait dépendante d'un détail technique,
 * et elles s'exécutent en même temps que les tests unitaires.</p>
 *
 * <p>Pour démontrer manuellement qu'elles mordent : ajouter un
 * {@code import greenmove.infrastructure.persistance.ConnexionH2;} utilisé dans
 * n'importe quelle classe de {@code greenmove.domaine} et relancer {@code mvn test}.
 * La preuve automatique, elle, est faite par le dernier test de cette classe.</p>
 */
@DisplayName("Règle de dépendance entre les couches")
class ArchitectureTest {

    /** Le code de production seul : les classes de test sont volontairement exclues. */
    private static final JavaClasses CLASSES_DE_PRODUCTION = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages("greenmove");

    /** La règle demandée par le TP. */
    static final ArchRule DOMAINE_INDEPENDANT_DE_L_INFRASTRUCTURE =
            noClasses()
                    .that().resideInAPackage("greenmove.domaine..")
                    .should().dependOnClassesThat().resideInAPackage("greenmove.infrastructure..")
                    .because("le domaine déclare ses ports, l'infrastructure les implémente : "
                            + "la flèche de dépendance ne va que dans ce sens");

    @Test
    @DisplayName("Aucune classe du domaine ne dépend de l'infrastructure")
    void le_domaine_ne_depend_pas_de_l_infrastructure() {
        DOMAINE_INDEPENDANT_DE_L_INFRASTRUCTURE.check(CLASSES_DE_PRODUCTION);
    }

    @Test
    @DisplayName("Le domaine ignore aussi JDBC et les clients HTTP")
    void le_domaine_ignore_les_technologies() {
        noClasses()
                .that().resideInAPackage("greenmove.domaine..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "java.sql..", "javax.sql..", "java.net.http..")
                .because("une règle métier se teste sans base de données ni réseau")
                .check(CLASSES_DE_PRODUCTION);
    }

    @Test
    @DisplayName("La couche application passe par les ports, jamais par les adapters")
    void l_application_ne_depend_pas_de_l_infrastructure() {
        noClasses()
                .that().resideInAPackage("greenmove.application..")
                .should().dependOnClassesThat().resideInAPackage("greenmove.infrastructure..")
                .because("le choix des adapters appartient à la racine de composition")
                .check(CLASSES_DE_PRODUCTION);
    }

    @Test
    @DisplayName("Preuve : la règle échoue bien sur une violation délibérée")
    void la_regle_echoue_sur_une_violation_deliberee() {
        // Une classe de test qui se fait passer pour du domaine et importe ConnexionH2.
        JavaClasses sonde = new ClassFileImporter().importPackages("greenmove.domaine.sonde");

        AssertionError echec = assertThrows(AssertionError.class,
                () -> DOMAINE_INDEPENDANT_DE_L_INFRASTRUCTURE.check(sonde));

        assertTrue(echec.getMessage().contains("SondeDeViolation"),
                "la règle doit nommer la classe fautive, message obtenu : " + echec.getMessage());
        assertTrue(echec.getMessage().contains("ConnexionH2"),
                "la règle doit nommer la dépendance interdite, message obtenu : " + echec.getMessage());
    }
}
