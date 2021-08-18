package controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.DaoFuncao;
import model.Funcao;



@WebServlet("/FuncaoController")
public class FuncaoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String INSERT_OR_EDIT = "/views/funcao.jsp";
    private static String LIST_FUNCAO = "/views/listFuncao.jsp";
    private DaoFuncao dao;
    
    public FuncaoController() {
        super();
        dao=new DaoFuncao();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward="";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")){
            int idFuncao = Integer.parseInt(request.getParameter("idFuncao"));
            Funcao funcao=new Funcao();
            funcao.setIdFuncao(idFuncao);
            dao.delete(funcao);
            forward = LIST_FUNCAO;
            request.setAttribute("funcoes", dao.getAllFuncao());    
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int idFuncao = Integer.parseInt(request.getParameter("idFuncao"));
            Funcao funcao = dao.getFuncaoById(idFuncao);
            request.setAttribute("funcao", funcao);
        } else if (action.equalsIgnoreCase("listFuncao")){
            forward = LIST_FUNCAO;
            request.setAttribute("funcoes", dao.getAllFuncao());
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 	Funcao funcao = new Funcao();
	        funcao.setNome(request.getParameter("nome"));
	        String idFuncao = request.getParameter("idFuncao");
	        
	        
	        if(idFuncao == null || idFuncao.isEmpty())
	        {
	            dao.add(funcao);
	        }
	        else
	        {
	            funcao.setIdFuncao(Integer.parseInt(idFuncao));
	            dao.update(funcao);
	        }
	        
	        RequestDispatcher view = request.getRequestDispatcher(LIST_FUNCAO);
	        request.setAttribute("funcoes", dao.getAllFuncao());
	        view.forward(request, response);
	    }
	}


