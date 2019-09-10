package a2piclientaux;

import bkgpi2a.HttpsClient;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ApplicationProperties;
import utils.DBManager;
import utils.DBServer;
import utils.DBServerException;
import utils.GetArgsException;

/**
 * Programme Java auxiliaire de a2pi-client permettant d?échanger des données
 * entre Anstel et Performance Immo (lien montant)
 *
 * @author Thierry Baribaud
 * @version 1.01
 */
public class A2PIClientAux {

    /**
     * apiServerType : prod pour le serveur de production, pre-prod pour le
     * serveur de pré-production. Valeur par défaut : pre-prod.
     */
    private String apiServerType = "pre-prod";

    /**
     * ifxDbServerType : prod pour le serveur de production, pre-prod pour le
     * serveur de pré-production. Valeur par défaut : pre-prod.
     */
    private String ifxDbServerType = "pre-prod";

    /**
     * debugMode : fonctionnement du programme en mode debug (true/false).
     * Valeur par défaut : false.
     */
    private static boolean debugMode = false;

    /**
     * testMode : fonctionnement du programme en mode test (true/false). Valeur
     * par défaut : false.
     */
    private static boolean testMode = false;

    /**
     * Constructeur de la classe
     *
     * @param args paramètres en ligne de commande
     * @throws utils.GetArgsException en cas d'erreur avec les paramètres en ligne de commande
     * @throws java.io.IOException en cas d'erreur du fichier des propriétés
     * @throws utils.DBServerException en cas d'erreur avec le serveur de base de données
     * @throws java.lang.ClassNotFoundException en cas de problème avec une
     * classe inconnue
     * @throws java.sql.SQLException en cas d'erreur SQL
     */
    public A2PIClientAux(String[] args) throws GetArgsException, IOException, DBServerException, ClassNotFoundException, SQLException {
        ApplicationProperties applicationProperties;
        DBServer ifxServer;
        DBManager informixDbManager;
        Connection informixConnection;
        HttpsClient httpsClient;

        System.out.println("Création d'une instance de A2PIClientAux ...");

        System.out.println("Analyse des arguments de la ligne de commande ...");
        this.getArgs(args);
        System.out.println("Argument(s) en ligne de commande lus().");

        System.out.println("Lecture des paramètres d'exécution ...");
        applicationProperties = new ApplicationProperties("A2PIClientAux.prop");
        System.out.println("Paramètres d'exécution lus.");

        System.out.println("Lecture des paramètres du serveur API ...");
//        this.apiRest = new APIREST(apiServerType, applicationProperties);
//        System.out.println("Paramètres du serveur API lus.");
//        if (debugMode) {
//            System.out.println(this.apiRest);
//        }

        System.out.println("Lecture des paramètres du serveur Informix ...");
        ifxServer = new DBServer(ifxDbServerType, "ifxdb", applicationProperties);
        System.out.println("Paramètres du serveur Informix lus.");
        if (debugMode) {
            System.out.println(ifxServer);
        }
        
        if (debugMode) {
            System.out.println(this.toString());
        }

//        System.out.println("Ouverture de la connexion avec le server API" + apiRest.getName() + " ...");
//        httpsClient = new HttpsClient(apiRest, debugMode);
        httpsClient = null;
//        System.out.println("Connexion avec le server API ouverte.");

        System.out.println("Ouverture de la connexion au serveur Informix : " + ifxServer.getName());
        informixDbManager = new DBManager(ifxServer);

        System.out.println("Connexion à la base de données : " + ifxServer.getDbName());
        informixConnection = informixDbManager.getConnection();

        System.out.println("Traitement des événements ...");
        processEvents(httpsClient, informixConnection);
    }

    /**
     * Traitement des événements
     */
    private void processEvents(HttpsClient httpsClient, Connection informixConnection) {
    }

    /**
     * @param ifxDbServerType définit le serveur de base de données
     */
    private void setIfxDbServerType(String ifxDbServerType) {
        this.ifxDbServerType = ifxDbServerType;
    }

    /**
     * @return ifxDbServerType le serveur de base de données
     */
    private String getIfxDbServerType() {
        return (ifxDbServerType);
    }

    /**
     * @param debugMode : fonctionnement du programme en mode debug
     * (true/false).
     */
    public void setDebugMode(boolean debugMode) {
        A2PIClientAux.debugMode = debugMode;
    }

    /**
     * @param testMode : fonctionnement du programme en mode test (true/false).
     */
    public void setTestMode(boolean testMode) {
        A2PIClientAux.testMode = testMode;
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
     * Récupère les paramètres en ligne de commande
     *
     * @param args arguments en ligne de commande
     */
    private void getArgs(String[] args) throws GetArgsException {
        int i;
        int n;
        int ip1;
        String currentParam;
        String nextParam;

        n = args.length;
//        System.out.println("nargs=" + n);
//    for(i=0; i<n; i++) System.out.println("args["+i+"]="+Args[i]);
        i = 0;
        while (i < n) {
//            System.out.println("args[" + i + "]=" + Args[i]);
            currentParam = args[i];
            ip1 = i + 1;
            nextParam = (ip1 < n) ? args[ip1] : null;
            switch (currentParam) {
                case "-apiserver":
                    if (ip1 < n) {
                        if (nextParam.equals("pre-prod") || nextParam.equals("prod")) {
                            this.apiServerType = args[ip1];
                        } else {
                            throw new GetArgsException("ERREUR : Mauvais serveur API : " + nextParam);
                        }
                        i = ip1;
                    } else {
                        throw new GetArgsException("ERREUR : Serveur API non défini");
                    }
                    break;
                case "-ifxserver":
                    if (ip1 < n) {
                        if (nextParam.equals("pre-prod") || nextParam.equals("prod")) {
                            this.ifxDbServerType = nextParam;
                        } else {
                            throw new GetArgsException("ERREUR : Mauvais serveur Informix : " + nextParam);
                        }
                        i = ip1;
                    } else {
                        throw new GetArgsException("ERREUR : Serveur Informix non défini");
                    }
                    break;
                case "-d":
                    setDebugMode(true);
                    break;
                case "-t":
                    setTestMode(true);
                    break;
                default:
                    usage();
                    throw new GetArgsException("ERREUR : Mauvais argument : " + currentParam);
            }
            i++;
        }
    }

    /**
     * Affiche le mode d'utilisation du programme.
     */
    public static void usage() {
        System.out.println("Usage : java A2PIClientAux [-apiserver prod|pre-prod]"
                + " [-ifxdb prod|pre-prod]"
                + " [-d] [-t]");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        A2PIClientAux a2PIClientAux;

        System.out.println("Lancement de A2PIClientAux ...");
        try {
            a2PIClientAux = new A2PIClientAux(args);
        } catch (GetArgsException | IOException | DBServerException | ClassNotFoundException | SQLException exception) {
            Logger.getLogger(A2PIClientAux.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println("Fin de A2ITclient.");
        }
    }

    /**
     * Retourne le contenu de A2PIClientAux
     *
     * @return retourne le contenu de A2PIClientAux
     */
    @Override
    public String toString() {
        return "A2PIClientAux:{"
                + "apiServerType:" + apiServerType
                + ", ifxDbServerType:" + ifxDbServerType
                + ", debugMode:" + debugMode
                + ", testMode:" + testMode
                + "}";
    }

}
