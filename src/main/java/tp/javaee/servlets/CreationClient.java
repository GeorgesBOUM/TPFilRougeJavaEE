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
import tp.javaee.forms.CreationClientForm;

/**
 * Servlet implementation class CreationClient
 */
@WebServlet(
		name = "creationClient", 
		urlPatterns = { "/creationClient" },
		initParams = {@WebInitParam(name="chemin", value = "/fichier/images/")}
		)
@MultipartConfig(
		fileSizeThreshold = 1048576, location = "C:/Users/geboum/fichiers",
		maxFileSize = 2097152, maxRequestSize = 52428800
		)
public class CreationClient extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/* Constantes */
	public static final String CHEMIN     = "chemin";
	public static final String ATT_CLIENT = "client";
    public static final String ATT_FORM   = "form";
    public static final String SESSION_CLIENTS = "clients";

    public static final String VUE_SUCCES = "/WEB-INF/afficherClient.jsp";
    public static final String VUE_FORM   = "/WEB-INF/creerClient.jsp";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreationClient() {
        super();
        // TODO Auto-generated constructor stub
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
        CreationClientForm form = new CreationClientForm();

        /* Traitement de la requête et récupération du bean en résultant */
        Client client = form.creerClient( request, chemin );

        /* Ajout du bean et de l'objet métier à l'objet requête */
        request.setAttribute( ATT_CLIENT, client );
        request.setAttribute( ATT_FORM, form );

        if ( form.getErreurs().isEmpty() ) {
        	/* Alors récupération de la map des clients dans la session */
            HttpSession session = request.getSession();
            Map<String, Client> clients = (HashMap<String, Client>) session.getAttribute( SESSION_CLIENTS );
            /* Si aucune map n'existe, alors initialisation d'une nouvelle map */
            if ( clients == null ) {
                clients = new HashMap<String, Client>();
            }
            /* Puis ajout du client courant dans la map */
            clients.put( client.getNom(), client );
            /* Et enfin (ré)enregistrement de la map en session */
            session.setAttribute( SESSION_CLIENTS, clients );
            /* Si aucune erreur, alors affichage de la fiche récapitulative */
            this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
        } else {
            /* Sinon, ré-affichage du formulaire de création avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
	}

}
