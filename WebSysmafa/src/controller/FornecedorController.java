package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DaoFornecedor;
import model.Fornecedor;



@WebServlet("/FornecedorController")
public class FornecedorController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String INSERT_OR_EDIT = "/views/fornecedor.jsp";
    private static String LIST_FORNECEDOR = "/views/listFornecedor.jsp";
    private DaoFornecedor dao;
       
   
    public FornecedorController() {
        super();
        dao=new DaoFornecedor();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward="";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")){
            int idFornecedor = Integer.parseInt(request.getParameter("idFornecedor"));
            Fornecedor fornecedor=new Fornecedor();
            fornecedor.setIdFornecedor(idFornecedor);
            dao.deleteFornecedor(fornecedor);
            forward = LIST_FORNECEDOR;
            request.setAttribute("fornecedores", dao.getAllFornecedor());    
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int idFornecedor = Integer.parseInt(request.getParameter("idFornecedor"));
            Fornecedor fornecedor = dao.getFornecedorById(idFornecedor);
            request.setAttribute("fornecedor", fornecedor);
        } else if (action.equalsIgnoreCase("listFornecedor")){
            forward = LIST_FORNECEDOR;
            request.setAttribute("fornecedores", dao.getAllFornecedor());
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setNome(request.getParameter("nome"));
		fornecedor.setEmail(request.getParameter("email"));
		fornecedor.setTelefone(request.getParameter("telefone"));
		fornecedor.setEndereco(request.getParameter("endereco"));
        String idFornecedor = request.getParameter("idFornecedor");
        
        
        if(idFornecedor == null || idFornecedor.isEmpty())
        {
            dao.addFornecedor(fornecedor);
        }
        else
        {
            fornecedor.setIdFornecedor((Integer.parseInt(idFornecedor)));
            dao.updateFornecedor(fornecedor);
        }
        
        RequestDispatcher view = request.getRequestDispatcher(LIST_FORNECEDOR);
        request.setAttribute("fornecedores", dao.getAllFornecedor());
        view.forward(request, response);
	}

}
