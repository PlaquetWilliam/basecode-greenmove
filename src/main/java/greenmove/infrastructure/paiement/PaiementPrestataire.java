package greenmove.infrastructure.paiement;

import greenmove.domaine.Montant;
import greenmove.domaine.MoyenPaiement;
import greenmove.domaine.Recu;
import greenmove.domaine.port.Paiement;

import java.util.Objects;

/**
 * Adapter du port {@link Paiement} vers le prestataire de paiement externe.
 *
 * <p>Toute la traduction se fait ici : un {@link Montant} du domaine devient un
 * montant en centimes attendu par le prestataire, et sa référence de transaction
 * redevient un {@link Recu}. C'est la seule classe à modifier quand le prestataire
 * change son API — une à deux fois par an.</p>
 */
public class PaiementPrestataire implements Paiement {

    private final PaymentApi api;

    public PaiementPrestataire(PaymentApi api) {
        this.api = Objects.requireNonNull(api, "api");
    }

    @Override
    public Recu debiter(MoyenPaiement moyen, Montant montant) {
        return Recu.de(api.charger(moyen.reference(), montant.centimes()));
    }
}
