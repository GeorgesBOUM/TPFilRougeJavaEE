package tp.javaee.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tp.javaee.bdd.TestJDBC;

/**
 * Servlet implementation class GestionTestJDBC
 */
@WebServlet("/testjdbc")
public class GestionTestJDBC extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GestionTestJDBC() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public static final String ATT_MESSAGES = "messages";
    public static final String VUE          = "/WEB-INF/test_jdbc.jsp";

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Initialisation de l'objet Java et récupération des messages */
        TestJDBC test = new TestJDBC();
        List<String> messages = test.executerTests( request );

        /* Enregistrement de la liste des messages dans l'objet requête */
        request.setAttribute( ATT_MESSAGES, messages );

        /* Transmission vers la page en charge de l'affichage des résultats */
        request.getRequestDispatcher( VUE ).forward( request, response );
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
