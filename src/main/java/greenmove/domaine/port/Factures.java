package greenmove.domaine.port;

import greenmove.domaine.Facture;

/**
 * Port : les factures émises par GreenMove, vues du domaine.
 *
 * <p>« Enregistrer » est un verbe métier : la facture est conservée, peu importe
 * qu'elle finisse dans une table SQL, un fichier ou un service d'archivage.</p>
 */
public interface Factures {

    /** Conserve la facture émise à la clôture d'une location. */
    void enregistrer(Facture facture);
}
