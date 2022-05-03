package tp.javaee.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tp.javaee.beans.Client;
import tp.javaee.dao.ClientDao;
import tp.javaee.dao.DAOException;
import tp.javaee.dao.DAOFactory;

/**
 * Servlet implementation class SuppressionClient
 */
@WebServlet(name = "suppressionClient", urlPatterns = { "/suppressionClient" })
public class SuppressionClient extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String PARAM_ID_CLIENT  = "idClient";
	public static final String PARAM_NOM_CLIENT = "nomClient";
    public static final String SESSION_CLIENTS  = "clients";

    public static final String VUE              = "/listeClients";
    
    private ClientDao          clientDao;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SuppressionClient() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.clientDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getClientDao();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Récupération du paramètre */
        String idClient = getValeurParametre( request, PARAM_ID_CLIENT );
        Long id = Long.parseLong( idClient );

        /* Récupération de la Map des clients enregistrés en session */
        HttpSession session = request.getSession();
        Map<Long, Client> clients = (HashMap<Long, Client>) session.getAttribute( SESSION_CLIENTS );

        /* Si l'id du client et la Map des clients ne sont pas vides */
        if ( id != null && clients != null ) {
            try {
                /* Alors suppression du client de la BDD */
                clientDao.supprimer( clients.get( id ) );
                /* Puis suppression du client de la Map */
                clients.remove( id );
            } catch ( DAOException e ) {
                e.printStackTrace();
            }
            /* Et remplacement de l'ancienne Map en session par la nouvelle */
            session.setAttribute( SESSION_CLIENTS, clients );
        }

        /* Redirection vers la fiche récapitulative */
        response.sendRedirect( request.getContextPath() + VUE );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	/*
     * Méthode utilitaire qui retourne null si un paramètre est vide, et son
     * contenu sinon.
     */
    private static String getValeurParametre( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }

}
