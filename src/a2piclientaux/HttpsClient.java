package a2piclientaux;

import bkgpi2a.Identifiants;
import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.HttpsClientException;

/**
 * Classe réalisant des requetes GET/POST/PATCH en HTTPS. Evolution de la classe
 * HttpsClient du projet Bkgpi2a avec Unirest.
 *
 * @author Thierry Baribaud
 * @version 1.08
 */
public class HttpsClient {

    private final static String USER_AGENT = "Mozilla/5.0";

    /**
     * URL du site Web
     */
    private static String baseUrl;

    /**
     * Identifiants pour accéder au site Web
     */
    private static Identifiants identifiants;

    /**
     * Chemin vers l'API REST en lecture
     */
    public final static String REST_API_PATH = "/api/v1/";

    /**
     * Chemin vers l'API événementielle
     */
    public final static String EVENT_API_PATH = "/api/vEvent/";

    /**
     * Commande pour la connexion à l'API
     */
    public static final String LOGIN_CMDE = "login";

    /**
     * Commande pour accéder aux sociétés
     */
    public static final String COMPANIES_CMDE = "companies";

    /**
     * Commande pour accéder aux sociétés
     */
    public static final String CLIENT_COMPANIES_CMDE = "clientcompanies";

    /**
     * Commande pour accéder aux filiales
     */
    public static final String SUBSIDIARIES_CMDE = "subsidiaries";

    /**
     * Commande pour accéder aux agences
     */
    public static final String AGENCIES_CMDE = "agencies";

    /**
     * Commande pour accéder aux utilisateurs
     */
    public static final String USERS_CMDE = "users";

    /**
     * Commande pour accéder aux patrimonies
     */
    public static final String PATRIMONIES_CMDE = "patrimonies";

    /**
     * Commande pour accéder aux intervenants/fournisseurs
     */
    public static final String PROVIDER_CONTACTS_CMDE = "providercontacts";

    /**
     * Commande pour accéder aux événement des tickets
     */
    public static final String TICKETS_CMDE = "tickets";

    /**
     * Commande pour accéder aux demandes d'intervention émises depuis l'application mobile
     */
    public static final String REQUESTS_CMDE = "simplifiedrequests";

    /**
     * Cookie pour l'authentification
     */
    private String cookies = null;

    /**
     * Code de réponse suite à l'envoi de la requête HTTPS
     */
    private int responseCode = 0;

    /**
     * Réponse suite à l'envoi de la requête HTTPS
     */
    private String response = null;

    /**
     * Nombre d'enregistrements renvoyés par consultation
     */
    private String acceptRange = null;

    /**
     * Nombre d'enregistrements récupérés
     */
    private String contentRange = null;

    /**
     * debugMode : fonctionnement du programme en mode debug (true/false).
     * Valeur par défaut : false.
     */
    private boolean debugMode = false;

    /**
     * testMode : fonctionnement du programme en mode test (true/false). Valeur
     * par défaut : false.
     */
    private boolean testMode = false;

    /**
     * Constructeur principal de la classe
     *
     * @param baseUrl URL du site Web
     * @param identifiants identifiants pour accéder au site Web
     */
    public HttpsClient(String baseUrl, Identifiants identifiants) {
        HttpsClient.baseUrl = baseUrl;
        HttpsClient.identifiants = identifiants;
    }

    /**
     * Constructeur alternatif de la classe
     *
     * @param baseUrl URL du site Web
     * @param identifiants identifiants pour accéder au site Web
     * @param debugMode indicateur du mode debug
     * @param testMode indicateur du mode test
     */
    public HttpsClient(String baseUrl, Identifiants identifiants, boolean debugMode, boolean testMode) {
        HttpsClient.baseUrl = baseUrl;
        HttpsClient.identifiants = identifiants;
        this.debugMode = debugMode;
        this.testMode = testMode;
    }

    /**
     * Méthode pour envoyer une requête HTTPS GET
     *
     * @param Command commande à exécuter
     * @throws Exception en cas d'erreur
     */
    public void sendGet(String Command) throws Exception {

        HttpResponse<JsonNode> jsonResponse;

        jsonResponse = Unirest.get(baseUrl + Command)
                .header("User-Agent", USER_AGENT)
                .header("Content-Type", "application/json")
                .header("Set-Cookie", cookies)
                .asJson();

        responseCode = jsonResponse.getStatus();
        response = jsonResponse.getBody().toString();
        if (debugMode) {
            System.out.println("responseCode:" + responseCode);
            System.out.println("response:" + response);
        }

        if (responseCode != 200) {
            throw new HttpsClientException();
        }
    }

    /**
     * Méthode pour envoyer une requête HTTPS POST
     *
     * @param command commande à exécuter
     * @param json paramètres au format Json
     * @throws Exception en cas d'erreur
     */
    public void sendPost(String command, String json) throws Exception {

        HttpResponse<JsonNode> jsonResponse;

        jsonResponse = Unirest.patch(baseUrl + command)
                .header("User-Agent", USER_AGENT)
                .header("Content-Type", "application/json")
                .header("Set-Cookie", cookies)
                .body(json)
                .asJson();

        responseCode = jsonResponse.getStatus();
        response = jsonResponse.getBody().toString();
        if (debugMode) {
            System.out.println("responseCode:" + responseCode);
            System.out.println("response:" + response);
        }

        if (responseCode != 200) {
            throw new HttpsClientException();
        }
    }

    /**
     * Méthode pour envoyer une requête HTTPS PATCH
     *
     * @param command commande à exécuter
     * @param json paramètres au format Json
     * @throws Exception en cas d'erreur
     */
    public void sendPatch(String command, String json) throws Exception {

        HttpResponse<JsonNode> jsonResponse;

        jsonResponse = Unirest.patch(baseUrl + command)
                .header("User-Agent", USER_AGENT)
                .header("Content-Type", "application/json")
                .header("Set-Cookie", cookies)
                .body(json)
                .asJson();

        responseCode = jsonResponse.getStatus();
        response = jsonResponse.getBody().toString();
        if (debugMode) {
            System.out.println("responseCode:" + responseCode);
            System.out.println("response:" + response);
        }

        if (responseCode != 200) {
            throw new HttpsClientException();
        }
    }

    private void getCookies(HttpsURLConnection MyConnection) {
        String cookies;

        cookies = MyConnection.getHeaderField("Set-Cookie");
        if (cookies != null) {
            if (debugMode) {
                System.out.println("Cookie(s)=" + cookies);
            }
            setCookies(cookies);
        }
    }

    private void getResponseCode(HttpsURLConnection MyConnection) {
        setResponseCode(0);
        try {
            setResponseCode(MyConnection.getResponseCode());
            System.out.println("Response Code : " + getResponseCode());
        } catch (IOException ex) {
            Logger.getLogger(HttpsClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getResponse(HttpsURLConnection MyConnection) {
        BufferedReader bufferedReader;
        StringBuffer response = null;
        String myInputLine;

        setResponse(null);
        try {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(MyConnection.getInputStream()));
            response = new StringBuffer();

            while ((myInputLine = bufferedReader.readLine()) != null) {
                response.append(myInputLine);
            }
            bufferedReader.close();

            if (response != null) {
                setResponse(response.toString());
                //print result
                if (debugMode) {
                    System.out.println(response.toString());
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(HttpsClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the cookies
     */
    public String getCookies() {
        return cookies;
    }

    /**
     * @param cookies the cookies to set
     */
    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    /**
     * @return the responseCode
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * @param responseCode the responseCode to set
     */
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * @return the response
     */
    public String getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * @return the acceptRange
     */
    public String getAcceptRange() {
        return acceptRange;
    }

    /**
     * @param acceptRange the acceptRange to set
     */
    public void setAcceptRange(String acceptRange) {
        this.acceptRange = acceptRange;
    }

    /**
     * @return the contentRange
     */
    public String getContentRange() {
        return contentRange;
    }

    /**
     * @param contentRange the contentRange to set
     */
    public void setContentRange(String contentRange) {
        this.contentRange = contentRange;
    }

    /**
     * @param debugMode : fonctionnement du programme en mode debug
     * (true/false).
     */
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * @param testMode : fonctionnement du programme en mode test (true/false).
     */
    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    /**
     * @return debugMode : retourne le mode de fonctionnement debug.
     */
    public boolean getDebugMode() {
        return (debugMode);
    }

    /**
     * @return testMode : retourne le mode de fonctionnement test.
     */
    public boolean getTestMode() {
        return (testMode);
    }

    /**
     * Connexion à l'API
     *
     * @param debugMode indicateur du mode debug
     * @param testMode indicateur du mode test
     * @throws com.mashape.unirest.http.exceptions.UnirestException
     * @throws utils.HttpsClientException
     */
    public void login(boolean debugMode, boolean testMode) throws UnirestException, HttpsClientException {
        HttpResponse<JsonNode> jsonResponse;
        Headers headers;

        jsonResponse = Unirest.post(baseUrl + REST_API_PATH + LOGIN_CMDE)
                .header("User-Agent", USER_AGENT)
                .header("Content-Type", "application/json")
                .body(identifiants.toJson())
                .asJson();
        responseCode = jsonResponse.getStatus();
        response = jsonResponse.getBody().toString();
        if (debugMode) {
            System.out.println("responseCode:" + responseCode);
            System.out.println("response:" + response);
        }

        if (responseCode == 200) {
            headers = jsonResponse.getHeaders();
            if (debugMode) {
                System.out.println("headers" + headers);
            }
//            cookies = headers.get("Set-Cookie").get(0);
            cookies = headers.get("set-cookie").get(0);
            if (debugMode) {
                System.out.println("cookies:" + cookies);
            }
        } else {
            throw new HttpsClientException();
        }
    }
}
