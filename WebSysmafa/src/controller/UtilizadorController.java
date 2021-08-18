package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DaoUtilizador;
import model.Funcionario;
import model.Utilizador;;


@WebServlet("/UtilizadorController")
public class UtilizadorController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static String INSERT_OR_EDIT = "/views/utilizador.jsp";
    private static String LIST_UTILIZADOR = "/views/listUtilizador.jsp";
    private DaoUtilizador dao;
       
  
    public UtilizadorController() {
        super();
       dao=new DaoUtilizador();
       
    }

	
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward="";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")){
            int idUtilizador = Integer.parseInt(request.getParameter("idUtilizador"));
            Utilizador utilizador=new Utilizador();
            utilizador.setIdUtilizador(idUtilizador);
            dao.deleteUtilizador(utilizador);
            forward = LIST_UTILIZADOR;
            request.setAttribute("utilizadores", dao.getAllFuncionario());    
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int idUtilizador = Integer.parseInt(request.getParameter("idUtilizador"));
            Utilizador utilizador = dao.getUtilizadorById(idUtilizador);
            request.setAttribute("utilizador", utilizador);
        } else if (action.equalsIgnoreCase("listUtilizador")){
            forward = LIST_UTILIZADOR;
            request.setAttribute("utilizadores", dao.getAllFuncionario());
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);

	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		   Utilizador utilizador=new Utilizador();
		   Funcionario funcionario=new Funcionario();
		   funcionario.setIdFuncionario(Integer.parseInt(request.getParameter("idFuncionario")));
		   utilizador.setUsername(request.getParameter("username"));
		   utilizador.setPerfil(request.getParameter("perfil"));
		   utilizador.setStatus(request.getParameter("status"));
		   utilizador.setPassword(request.getParameter("password"));
		   utilizador.setFuncionario(funcionario);
	        String idUtilizador = request.getParameter("idUtilizador");
	        
	        
	        if(idUtilizador == null || idUtilizador.isEmpty())
	        {
	            dao.addUtilizador(utilizador);
	        }
	        else
	        {
	        	utilizador.setIdUtilizador(Integer.parseInt(idUtilizador));
	            dao.updateUtilizador(utilizador);
	        }
	        
	        RequestDispatcher view = request.getRequestDispatcher(LIST_UTILIZADOR);
	        request.setAttribute("utilizadores",dao.getAllFuncionario());
	        view.forward(request, response);
	}


}
