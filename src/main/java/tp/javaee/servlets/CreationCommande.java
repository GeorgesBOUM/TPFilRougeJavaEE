package tp.javaee.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tp.javaee.beans.Client;
import tp.javaee.beans.Commande;
import tp.javaee.dao.ClientDao;
import tp.javaee.dao.CommandeDao;
import tp.javaee.dao.DAOFactory;
import tp.javaee.forms.CreationCommandeForm;

/**
 * Servlet implementation class CreationCommande
 */
@WebServlet(
		name = "creationCommande", urlPatterns = { "/creationCommande" },
		initParams = @WebInitParam(name="chemin", value="/fichiers/images/")
		)
@MultipartConfig(
		fileSizeThreshold = 1048576, location = "C:/Users/geboum/fichiers",
		maxFileSize = 2097152, maxRequestSize = 52428800
		)
public class CreationCommande extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/* Constantes */
	public static final String CONF_DAO_FACTORY      = "daofactory";
	public static final String CHEMIN       = "chemin";
	public static final String ATT_COMMANDE = "commande";
    public static final String ATT_FORM     = "form";
    public static final String SESSION_CLIENTS   = "clients";
    public static final String SESSION_COMMANDES = "commandes";

    public static final String VUE_SUCCES   = "/WEB-INF/afficherCommande.jsp";
    public static final String VUE_FORM     = "/WEB-INF/creerCommande.jsp";
    
    private ClientDao          clientDao;
    private CommandeDao        commandeDao;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreationCommande() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
        /* Récupération d'une instance de nos DAO Client et Commande */
        this.clientDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getClientDao();
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Transmission à la page JSP en charge de l'affichage des données */
        request.getRequestDispatcher( VUE_FORM ).forward( request, response );
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*
         * Lecture du paramètre 'chemin' passé à la servlet via la déclaration
         * dans le web.xml
         */
        String chemin = this.getServletConfig().getInitParameter( CHEMIN );
		
		/* Préparation de l'objet formulaire */
        CreationCommandeForm form = new CreationCommandeForm(clientDao, commandeDao);

        /* Traitement de la requête et récupération du bean en résultant */
        Commande commande = form.creerCommande( request, chemin );

        /* Ajout du bean et de l'objet métier à l'objet requête */
        request.setAttribute( ATT_COMMANDE, commande );
        request.setAttribute( ATT_FORM, form );

        if ( form.getErreurs().isEmpty() ) {
        	/* Alors récupération de la map des clients dans la session */
            HttpSession session = request.getSession();
            Map<Long, Client> clients = (HashMap<Long, Client>) session.getAttribute( SESSION_CLIENTS );
            /* Si aucune map n'existe, alors initialisation d'une nouvelle map */
            if ( clients == null ) {
                clients = new HashMap<Long, Client>();
            }
            /* Puis ajout du client de la commande courante dans la map */
            clients.put( commande.getClient().getId(), commande.getClient() );
            /* Et enfin (ré)enregistrement de la map en session */
            session.setAttribute( SESSION_CLIENTS, clients );

            /* Ensuite récupération de la map des commandes dans la session */
            Map<Long, Commande> commandes = (HashMap<Long, Commande>) session.getAttribute( SESSION_COMMANDES );
            /* Si aucune map n'existe, alors initialisation d'une nouvelle map */
            if ( commandes == null ) {
                commandes = new HashMap<Long, Commande>();
            }
            /* Puis ajout de la commande courante dans la map */
            commandes.put( commande.getId(), commande );
            /* Et enfin (ré)enregistrement de la map en session */
            session.setAttribute( SESSION_COMMANDES, commandes );

            /* Si aucune erreur, alors affichage de la fiche récapitulative */
            this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
        } else {
            /* Sinon, ré-affichage du formulaire de création avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
	}

}
