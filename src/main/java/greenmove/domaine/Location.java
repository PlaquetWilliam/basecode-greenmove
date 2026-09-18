package greenmove.domaine;

import java.time.Duration;
import java.time.LocalDateTime;

/** Location d'un véhicule par un usager. */
public class Location {

    private final long id;
    private final long usagerId;
    private final TypeVehicule typeVehicule;
    private final LocalDateTime debut;
    private final LocalDateTime fin;          // null tant que la location n'est pas clôturée
    private final StatutLocation statut;

    public Location(long id, long usagerId, TypeVehicule typeVehicule,
                    LocalDateTime debut, LocalDateTime fin, StatutLocation statut) {
        this.id = id;
        this.usagerId = usagerId;
        this.typeVehicule = typeVehicule;
        this.debut = debut;
        this.fin = fin;
        this.statut = statut;
    }

    /**
     * Clôture la location. Si aucune fin n'est connue, on prend l'instant fourni.
     * Refuse une fin antérieure au début.
     */
    public Location terminer(LocalDateTime maintenant) {
        LocalDateTime finEffective = fin != null ? fin : maintenant;
        if (Duration.between(debut, finEffective).toMinutes() < 0) {
            throw new IllegalStateException("Fin antérieure au début pour la location " + id);
        }
        return new Location(id, usagerId, typeVehicule, debut, finEffective, StatutLocation.TERMINEE);
    }

    /** Durée de la location. Nécessite une fin connue. */
    public Duree duree() {
        if (fin == null) {
            throw new IllegalStateException("La location " + id + " n'a pas encore de fin");
        }
        return Duree.entre(debut, fin);
    }

    public long id() {
        return id;
    }

    public long usagerId() {
        return usagerId;
    }

    public TypeVehicule typeVehicule() {
        return typeVehicule;
    }

    public LocalDateTime debut() {
        return debut;
    }

    public LocalDateTime fin() {
        return fin;
    }

    public StatutLocation statut() {
        return statut;
    }
}
