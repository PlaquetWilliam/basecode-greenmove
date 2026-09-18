package greenmove.domaine;

import java.time.Duration;
import java.time.LocalDateTime;

/** Durée d'une location, exprimée en minutes entières. Jamais négative. */
public final class Duree {

    private final long minutes;

    private Duree(long minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Une durée ne peut pas être négative : " + minutes);
        }
        this.minutes = minutes;
    }

    public static Duree enMinutes(long minutes) {
        return new Duree(minutes);
    }

    public static Duree entre(LocalDateTime debut, LocalDateTime fin) {
        return new Duree(Duration.between(debut, fin).toMinutes());
    }

    public long minutes() {
        return minutes;
    }
}
