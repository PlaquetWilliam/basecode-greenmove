package greenmove.infrastructure.persistance;

import java.sql.Timestamp;

/** Reflet brut d'une ligne de la table {@code facture}. Ne sort pas de l'infrastructure. */
record LigneFacture(long locationId, int montantCentimes, String referencePaiement, Timestamp emiseLe) {
}
