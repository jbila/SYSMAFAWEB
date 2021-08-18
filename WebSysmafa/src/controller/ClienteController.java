package controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.DaoCliente;
import model.Cliente;
import model.Distrito;
@WebServlet("/ClienteController")
public class ClienteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String INSERT_OR_EDIT = "/views/cliente.jsp";
    private static String LIST_CLIENTE = "/views/listCliente.jsp";
    private DaoCliente dao;
   
    public ClienteController() {
        super();
        dao=new DaoCliente();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward="";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")){
            int idCliente = Integer.parseInt(request.getParameter("idCliente"));
            Cliente cliente=new Cliente();
            cliente.setIdCliente(idCliente);
            dao.deleteCliente(cliente);
            forward = LIST_CLIENTE;
            request.setAttribute("clientes", dao.getAllCliente());    
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
          String idCliente= request.getParameter("idCliente");
            Cliente cliente = dao.getClienteById(Integer.parseInt(idCliente));
            request.setAttribute("cliente", cliente);
        } else if (action.equalsIgnoreCase("listCliente")){
            forward = LIST_CLIENTE;
            request.setAttribute("clientes", dao.getAllCliente());
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cliente cliente = new Cliente();
		Distrito distrito=new Distrito();
		distrito.setIdDistrito(Integer.parseInt(request.getParameter("idDistrito")));
		cliente.setNome(request.getParameter("nome"));
		cliente.setEmail(request.getParameter("email"));
		cliente.setApelido(request.getParameter("apelido"));
		cliente.setGenero(request.getParameter("sexo"));
		cliente.setTelefone(request.getParameter("telefone"));
		cliente.setEndereco(request.getParameter("endereco"));
		cliente.setDistrito(distrito);
        String idCliente = request.getParameter("idCliente");
        
        
        if(idCliente == null || idCliente.isEmpty())
        	
        {
        	
            dao.addCliente(cliente);
        }
        else
        {
            cliente.setIdCliente((Integer.parseInt(idCliente)));
            dao.updateCliente(cliente);
        }
        
        RequestDispatcher view = request.getRequestDispatcher(LIST_CLIENTE);
        request.setAttribute("clientes", dao.getAllCliente());
        view.forward(request, response);
	}	

}
