package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.DaoProvincia;
import model.Provincia;



@WebServlet("/ProvinciaController")
public class ProvinciaController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String INSERT_OR_EDIT = "/views/provincia.jsp";
    private static String LIST_PROVINCIA = "/views/listProvincia.jsp";
    private DaoProvincia dao;
       
  
    public ProvinciaController() {
        super();
        dao=new DaoProvincia();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward="";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")){
            int idProvincia = Integer.parseInt(request.getParameter("idProvincia"));
            Provincia provincia=new Provincia();
            provincia.setIdProvincia(idProvincia);
            dao.delete(provincia);
            forward = LIST_PROVINCIA;
            request.setAttribute("provincias", dao.getAllprovincia());    
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int idProvincia = Integer.parseInt(request.getParameter("idProvincia"));
            Provincia provincia = dao.getProvinciaById(idProvincia);
            request.setAttribute("provincia", provincia);
        } else if (action.equalsIgnoreCase("listProvincia")){
            forward = LIST_PROVINCIA;
            request.setAttribute("provincias", dao.getAllprovincia());
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Provincia provincia = new Provincia();
		provincia.setNome(request.getParameter("nome"));
        String idProvincia = request.getParameter("idProvincia");
        
        
        if(idProvincia == null || idProvincia.isEmpty())
        {
            dao.add(provincia);
        }
        else
        {
            provincia.setIdProvincia(Integer.parseInt(idProvincia));
            dao.update(provincia);
        }
        
        RequestDispatcher view = request.getRequestDispatcher(LIST_PROVINCIA);
        request.setAttribute("provincias", dao.getAllprovincia());
        view.forward(request, response);
    }
	

}
