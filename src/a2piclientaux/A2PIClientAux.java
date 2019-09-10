package a2piclientaux;

import bdd.Fa2pi;
import bdd.Fa2piDAO;
import bkgpi2a.AssociateProviderContactWithPatrimony;
import bkgpi2a.DissociateProviderContactFromPatrimony;
import bkgpi2a.HttpsClient;
import bkgpi2a.Identifiants;
import bkgpi2a.WebServer;
import bkgpi2a.WebServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
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
 * @version 1.02
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
     * apiId : identifiants pour se connecter au serveur API courant. Pas de
     * valeur par défaut, ils doivent être fournis dans le fichier
     * A2PIClientAux.prop.
     */
    private Identifiants apiId;

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
     * @throws utils.GetArgsException en cas d'erreur avec les paramètres en
     * ligne de commande
     * @throws java.io.IOException en cas d'erreur du fichier des propriétés
     * @throws utils.DBServerException en cas d'erreur avec le serveur de base
     * de données
     * @throws java.lang.ClassNotFoundException en cas de problème avec une
     * classe inconnue
     * @throws java.sql.SQLException en cas d'erreur SQL
     * @throws WebServerException en cas d'erreur avec le serveur API.
     */
    public A2PIClientAux(String[] args) throws GetArgsException, IOException, DBServerException, ClassNotFoundException, SQLException, WebServerException {
        ApplicationProperties applicationProperties;
        DBServer ifxServer;
        DBManager informixDbManager;
        Connection informixConnection;
        HttpsClient httpsClient;
        WebServer apiServer;

        System.out.println("Création d'une instance de A2PIClientAux ...");

        System.out.println("Analyse des arguments de la ligne de commande ...");
        this.getArgs(args);
        System.out.println("Argument(s) en ligne de commande lus().");

        System.out.println("Lecture des paramètres d'exécution ...");
        applicationProperties = new ApplicationProperties("A2PIClientAux.prop");
        System.out.println("Paramètres d'exécution lus.");

        System.out.println("Lecture des paramètres du serveur API ...");
        apiServer = new WebServer(getApiServerType(), applicationProperties);
        if (debugMode) {
            System.out.println(apiServer);
        }
        setApiId(applicationProperties);
        if (debugMode) {
            System.out.println(apiId);
        }
        System.out.println("Paramètres du serveur API lus.");

        System.out.println("Lecture des paramètres du serveur Informix ...");
        ifxServer = new DBServer(ifxDbServerType, "ifxdb", applicationProperties);
        if (debugMode) {
            System.out.println(ifxServer);
        }
        System.out.println("Paramètres du serveur Informix lus.");

        if (debugMode) {
            System.out.println(this.toString());
        }

        System.out.println("Ouverture de la connexion au site API : " + apiServer.getName());
        httpsClient = new HttpsClient(apiServer.getIpAddress(), apiId, debugMode, testMode);
        System.out.println("Connexion avec le server API ouverte.");

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
    private void processEvents(HttpsClient httpsClient, Connection informixConnection) throws ClassNotFoundException, SQLException {
        Fa2piDAO fa2piDAO;
        Fa2pi fa2pi;
        int i;
        int retcode;
//        ProviderContactAssociatedWithPatrimony providerContactAssociatedWithPatrimony;
//        ProviderContactDissociatedFromPatrimony providerContactDissociatedFromPatrimony;
        AssociateProviderContactWithPatrimony associateProviderContactWithPatrimony;
        DissociateProviderContactFromPatrimony dissociateProviderContactFromPatrimony;

        fa2piDAO = new Fa2piDAO(informixConnection);
        fa2piDAO.filterByStatus(400);
        fa2piDAO.setSelectPreparedStatement();
        fa2piDAO.setUpdatePreparedStatement();
        System.out.println("  SelectStatement=" + fa2piDAO.getSelectStatement());
        i = 0;
        while ((fa2pi = fa2piDAO.select()) != null) {
            i++;
            retcode = -1;
            System.out.println("Fa2pi(" + i + ")=" + fa2pi);
            
            switch(fa2pi.getA10evttype()) {
                case 470:   // ProviderContactAssociatedWithPatrimony
//                    providerContactAssociatedWithPatrimony = new ProviderContactAssociatedWithPatrimony(fa2pi);
//                    associateProviderContactWithPatrimony = new AssociateProviderContactWithPatrimony(providerContactAssociatedWithPatrimony);
                    retcode = 1;
                    break;
                    
                case 475:   // ProviderContactDissociatedFromPatrimony
//                    providerContactDissociatedFromPatrimony = new ProviderContactDissociatedFromPatrimony(fa2pi);
//                    dissociateProviderContactFromPatrimony = new DissociateProviderContactFromPatrimony(providerContactDissociatedFromPatrimony);
                    retcode = 1;
                    break;
            }
            
            fa2pi.setA10status(retcode);
            if (retcode != 1) {
                fa2pi.setA10nberr(1);
            }

            fa2pi.setA10update(new Timestamp(new java.util.Date().getTime()));
            fa2piDAO.update(fa2pi);
        }
        fa2piDAO.closeSelectPreparedStatement();
        fa2piDAO.closeUpdatePreparedStatement();

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
     * @param apiServerType définit le serveur API
     */
    private void setApiServerType(String apiServerType) {
        this.apiServerType = apiServerType;
    }

    /**
     * @return apiServerType le serveur API
     */
    private String getApiServerType() {
        return (apiServerType);
    }

    /**
     * @return les identifiants pour accéder au serveur API
     */
    public Identifiants getApiId() {
        return apiId;
    }

    /**
     * @param apiId définit les identifiants pour accéder au serveur API
     */
    public void setApiId(Identifiants apiId) {
        this.apiId = apiId;
    }

    /**
     * @param applicationProperties définit les identifiants pour accéder au
     * serveur API
     * @throws WebServerException en cas d'erreur sur la lecteur des
     * identifiants
     */
    public void setApiId(ApplicationProperties applicationProperties) throws WebServerException {
        String value;
        Identifiants identifiants = new Identifiants();

        value = applicationProperties.getProperty(getApiServerType() + ".apiserver.login");
        if (value != null) {
            identifiants.setLogin(value);
        } else {
            throw new WebServerException("Nom utilisateur pour l'accès API non défini");
        }

        value = applicationProperties.getProperty(getApiServerType() + ".apiserver.passwd");
        if (value != null) {
            identifiants.setPassword(value);
        } else {
            throw new WebServerException("Mot de passe pour l'accès API non défini");
        }
        A2PIClientAux.this.setApiId(identifiants);
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
                        if ("pre-prod".equals(nextParam) || "prod".equals(nextParam)) {
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
                        if ("pre-prod".equals(nextParam) || "prod".equals(nextParam)) {
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
        } catch (GetArgsException | IOException | DBServerException | ClassNotFoundException | SQLException | WebServerException exception) {
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
