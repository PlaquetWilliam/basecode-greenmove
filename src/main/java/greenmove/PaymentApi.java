package greenmove;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

/**
 * Client du prestataire de paiement externe.
 *
 * <p>Son API change une à deux fois par an : le nom des champs, le format du
 * montant et l'URL ont déjà bougé deux fois depuis 2023. Aujourd'hui, cette
 * classe est appelée directement depuis {@link LocationService}, c'est-à-dire
 * depuis le code qui porte les règles métier.</p>
 *
 * <p>Le mode hors ligne évite d'avoir besoin du réseau pendant le TP : il ne
 * change rien au problème de conception.</p>
 */
public class PaymentApi {

    private static final String ENDPOINT = "https://api.prestataire-paiement.example/v3/charges";
    private static final boolean HORS_LIGNE = Boolean.parseBoolean(
            System.getProperty("paiement.horsLigne", "true"));

    public String charger(String moyenPaiement, int montantCentimes) {
        if (montantCentimes <= 0) {
            throw new IllegalArgumentException("Montant à débiter invalide : " + montantCentimes);
        }

        if (HORS_LIGNE) {
            // Simulation : le prestataire renvoie une référence de transaction.
            return "SIMU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }

        String corps = "{\"payment_method\":\"" + moyenPaiement + "\","
                + "\"amount_minor\":" + montantCentimes + ","
                + "\"currency\":\"EUR\"}";

        HttpRequest requete = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(corps))
                .build();

        try {
            HttpResponse<String> reponse = HttpClient.newHttpClient()
                    .send(requete, HttpResponse.BodyHandlers.ofString());
            if (reponse.statusCode() >= 300) {
                throw new IllegalStateException("Paiement refusé : " + reponse.body());
            }
            return extraireReference(reponse.body());
        } catch (Exception e) {
            throw new RuntimeException("Le prestataire de paiement n'a pas répondu", e);
        }
    }

    private String extraireReference(String json) {
        int i = json.indexOf("\"id\":\"");
        if (i < 0) {
            throw new IllegalStateException("Réponse illisible du prestataire : " + json);
        }
        return json.substring(i + 6, json.indexOf('"', i + 6));
    }
}
