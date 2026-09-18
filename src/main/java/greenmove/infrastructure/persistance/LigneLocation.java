package greenmove.infrastructure.persistance;

import java.sql.Timestamp;

/** Reflet brut d'une ligne de la table {@code location}. Ne sort pas de l'infrastructure. */
record LigneLocation(long id, long usagerId, String typeVehicule,
                     Timestamp debut, Timestamp fin, String statut) {
}
