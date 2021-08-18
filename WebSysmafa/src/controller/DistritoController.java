package controller;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DaoDistrito;
import model.Distrito;
import model.Provincia;

@WebServlet("/DistritoController")
public class DistritoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String INSERT_OR_EDIT = "/views/distrito.jsp";
    private static String LIST_DISTRITO = "/views/listDistrito.jsp";
    private DaoDistrito dao;
       
   
    public DistritoController() {
        super();
        dao=new DaoDistrito();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward="";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")){
            int idDistrito = Integer.parseInt(request.getParameter("idDistrito"));
            Distrito distrito=new Distrito();
            distrito.setIdDistrito(idDistrito);
            dao.delete(distrito);
            forward = LIST_DISTRITO;
            request.setAttribute("distritos", dao.getAlldistrito());    
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int idDistrito = Integer.parseInt(request.getParameter("idDistrito"));
            Distrito distrito = dao.getDistritoById(idDistrito);
            request.setAttribute("distrito", distrito);
        } else if (action.equalsIgnoreCase("listDistrito")){
            forward = LIST_DISTRITO;
            request.setAttribute("distritos", dao.getAlldistrito());
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
	}

	
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Distrito distrito = new Distrito();
        distrito.setNome(request.getParameter("nome"));
        
        Provincia provincia=new Provincia();
		provincia.setIdProvincia(Integer.parseInt(request.getParameter("idProvincia")));
        distrito.setProvincia(provincia);
        
        String idDistrito = request.getParameter("idDistrito");
        
        
        if(idDistrito == null || idDistrito.isEmpty())
        {
            dao.add(distrito);
        }
        else
        {
            distrito.setIdDistrito(Integer.parseInt(idDistrito));
            System.out.println("Dentro de controller "+distrito.toString());
            dao.update(distrito);
        }
        
        RequestDispatcher view = request.getRequestDispatcher(LIST_DISTRITO);
        request.setAttribute("distritos", dao.getAlldistrito());
        view.forward(request, response);
    }
	

}
