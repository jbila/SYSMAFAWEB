package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.DaoCategoria;
import model.Categoria;


@WebServlet("/CategoriaController")
public class CategoriaController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String INSERT_OR_EDIT = "/views/categoria.jsp";
    private static String LIST_CATEGORIA = "/views/listCategoria.jsp";
    private DaoCategoria dao;
       
  
    public CategoriaController() {
        super();
       dao=new DaoCategoria();
       
    }

	
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward="";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")){
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            Categoria categoria=new Categoria();
            categoria.setIdCategoria(idCategoria);
            dao.delete(categoria);
            forward = LIST_CATEGORIA;
            request.setAttribute("categorias", dao.getAllCategoria());    
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            Categoria categoria = dao.getCategoriaById(idCategoria);
            request.setAttribute("categoria", categoria);
        } else if (action.equalsIgnoreCase("listCategoria")){
            forward = LIST_CATEGORIA;
            request.setAttribute("categorias", dao.getAllCategoria());
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);

	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		   Categoria categoria=new Categoria();
		   categoria.setNome(request.getParameter("nome"));
		   categoria.setDescricao(request.getParameter("descricao"));
	        String idCategoria = request.getParameter("idCategoria");
	        
	        
	        if(idCategoria == null || idCategoria.isEmpty())
	        {
	            dao.add(categoria);
	        }
	        else
	        {
	        	categoria.setIdCategoria(Integer.parseInt(idCategoria));
	            dao.update(categoria);
	        }
	        
	        RequestDispatcher view = request.getRequestDispatcher(LIST_CATEGORIA);
	        request.setAttribute("categorias", dao.getAllCategoria());
	        view.forward(request, response);
	}

}
