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

import tp.javaee.beans.Commande;
import tp.javaee.dao.CommandeDao;
import tp.javaee.dao.DAOException;
import tp.javaee.dao.DAOFactory;

/**
 * Servlet implementation class SuppressionCommande
 */
@WebServlet(name = "suppressionCommande", urlPatterns = { "/suppressionCommande" })
public class SuppressionCommande extends HttpServlet {
	public static final String CONF_DAO_FACTORY  = "daofactory";
    public static final String PARAM_ID_COMMANDE = "idCommande";
    public static final String SESSION_COMMANDES = "commandes";

    public static final String VUE               = "/listeCommandes";

    private CommandeDao        commandeDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Récupération du paramètre */
        String idCommande = getValeurParametre( request, PARAM_ID_COMMANDE );
        Long id = Long.parseLong( idCommande );

        /* Récupération de la Map des commandes enregistrées en session */
        HttpSession session = request.getSession();
        Map<Long, Commande> commandes = (HashMap<Long, Commande>) session.getAttribute( SESSION_COMMANDES );

        /* Si l'id de la commande et la Map des commandes ne sont pas vides */
        if ( id != null && commandes != null ) {
            try {
                /* Alors suppression de la commande de la BDD */
                commandeDao.supprimer( commandes.get( id ) );
                /* Puis suppression de la commande de la Map */
                commandes.remove( id );
            } catch ( DAOException e ) {
                e.printStackTrace();
            }
            /* Et remplacement de l'ancienne Map en session par la nouvelle */
            session.setAttribute( SESSION_COMMANDES, commandes );
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
	
	private static String getValeurParametre( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }

}
