# GreenMove — code de départ du TP d'architecture

EADL-BC01FM01 — Conception d'architectures logicielles

## Ce que fait ce code

`LocationService.terminerLocation(id)` clôture une location : il relit la location en base,
calcule le montant dû, va chercher si l'usager est abonné, débite chez le prestataire de
paiement, enregistre la facture et passe la location en `TERMINEE`.

Tout cela dans une seule méthode, qui parle à la fois JDBC, HTTP et métier.

## Démarrer

Java 17 et Maven suffisent. Aucune base à installer : H2 tourne en mémoire.

Le plus simple : ouvrir le projet dans l'IDE et exécuter la classe `greenmove.JeuDEssai`.

En ligne de commande :

```bash
mvn -q compile
mvn -q dependency:build-classpath -Dmdep.outputFile=cp.txt
java -cp "target/classes:$(cat cp.txt)" greenmove.JeuDEssai
```

Sortie attendue :

```
Facture{location=1, montant=4.6 €, paiement=SIMU-XXXXXXXX}
Facture{location=2, montant=3.97 €, paiement=SIMU-XXXXXXXX}
```

Le client de paiement est en mode hors ligne par défaut : il renvoie une référence simulée
sans appel réseau. Pour le mode réel : `-Dpaiement.horsLigne=false` (l'URL est fictive,
c'est normal qu'elle échoue).

## Les règles métier à préserver

Le refactoring ne doit **rien changer** au comportement :

| Règle | Valeur |
|---|---|
| Prise en charge (vélo et trottinette) | 1,00 € |
| Prix à la minute — vélo | 0,15 € |
| Prix à la minute — trottinette | 0,22 € |
| Plafond journalier | 15,00 € |
| Réduction abonné | 20 %, appliquée après le plafond |
| Durée négative | erreur |
| Type de véhicule inconnu | erreur |

Deux vérifications tirées du jeu d'essai :

- 24 minutes de vélo, usager non abonné → 100 + 24 × 15 = 460 centimes, soit 4,60 €.
- 18 minutes de trottinette, usager abonné → 100 + 18 × 22 = 496, moins 20 % = 397 centimes, soit 3,97 €.

## Votre travail

1. Faire apparaître les paquets `domaine`, `application` et `infrastructure`.
2. Extraire les règles métier — le calcul du tarif en premier — dans le domaine.
3. Définir dans le domaine les interfaces dont il a besoin, les implémenter dans l'infrastructure.
4. Injecter les dépendances par constructeur.
5. Écrire au moins trois tests unitaires du calcul tarifaire, **sans base de données ni réseau**.
6. Bonus : une règle ArchUnit qui échoue si le domaine dépend de l'infrastructure.

ArchUnit et JUnit 5 sont déjà déclarés dans le `pom.xml`.

## Le critère qui ne trompe pas

Quand un test du calcul tarifaire s'exécute en quelques millisecondes, sans rien démarrer,
le découpage tient. Tant que ce test a besoin de H2, il ne tient pas.
