package greenmove.infrastructure.persistance;

/** Reflet brut d'une ligne de la table {@code usager}. Ne sort pas de l'infrastructure. */
record LigneUsager(long id, String nom, boolean abonne, String moyenPaiement) {
}
