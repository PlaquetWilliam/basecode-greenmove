package greenmove.infrastructure.persistance;

import greenmove.domaine.Facture;
import greenmove.domaine.Location;
import greenmove.domaine.MoyenPaiement;
import greenmove.domaine.StatutLocation;
import greenmove.domaine.TypeVehicule;
import greenmove.domaine.Usager;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/** Conversions entre les lignes de la base et les objets du domaine. */
final class ConvertisseurPersistance {

    private ConvertisseurPersistance() {
    }

    static Location versDomaine(LigneLocation ligne) {
        return new Location(
                ligne.id(),
                ligne.usagerId(),
                typeVehicule(ligne.typeVehicule()),
                ligne.debut().toLocalDateTime(),
                ligne.fin() != null ? ligne.fin().toLocalDateTime() : null,
                ligne.statut() != null ? StatutLocation.valueOf(ligne.statut()) : StatutLocation.EN_COURS);
    }

    static Usager versDomaine(LigneUsager ligne) {
        return new Usager(ligne.id(), ligne.nom(), ligne.abonne(),
                MoyenPaiement.de(ligne.moyenPaiement()));
    }

    static LigneFacture versLigne(Facture facture, LocalDateTime emiseLe) {
        return new LigneFacture(
                facture.locationId(),
                facture.montant().centimes(),
                facture.referencePaiement(),
                Timestamp.valueOf(emiseLe));
    }

    private static TypeVehicule typeVehicule(String code) {
        try {
            return TypeVehicule.valueOf(code);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalStateException("Type de véhicule inconnu : " + code);
        }
    }
}
