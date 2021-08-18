package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.DaoFuncionario;
import model.Distrito;
import model.Funcao;
import model.Funcionario;


@WebServlet("/FuncionarioController")
public class FuncionarioController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String INSERT_OR_EDIT = "/views/funcionario.jsp";
    private static String LIST_FUNCIONARIO = "/views/listFuncionario.jsp";
    private DaoFuncionario dao;
       
   
    public FuncionarioController() {
        super();
        dao=new DaoFuncionario();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward="";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")){
            int idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
            Funcionario funcionario = new Funcionario();
            funcionario.setIdFuncionario(idFuncionario);
            dao.add(funcionario);
            forward = LIST_FUNCIONARIO;
            request.setAttribute("funcionarios", dao.getAllFuncionario());    
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
            Funcionario funcionario = dao.getFuncionarioById(idFuncionario);
            request.setAttribute("funcionario", funcionario);
        } else if (action.equalsIgnoreCase("listFuncionario")){
            forward = LIST_FUNCIONARIO;
            request.setAttribute("funcionarios", dao.getAllFuncionario());
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Funcionario funcionario = new Funcionario();
		
		Distrito distrito=new Distrito();
		distrito.setIdDistrito(Integer.parseInt(request.getParameter("idDistrito")));
		
		Funcao funcao=new Funcao();
		funcao.setIdFuncao(Integer.parseInt(request.getParameter("idFuncao")));
		
		funcionario.setNome(request.getParameter("nome"));
		funcionario.setApelido(request.getParameter("apelido"));
		funcionario.setGenero(request.getParameter("sexo"));
		funcionario.setEmail(request.getParameter("email"));
		funcionario.setTelefone(request.getParameter("telefone"));
		funcionario.setEndereco(request.getParameter("endereco"));
		funcionario.setNuit(Integer.parseInt(request.getParameter("nuit")));
		funcionario.setNumeroBi("bi");
		funcionario.setSalario(Double.parseDouble(request.getParameter("salario")));
		funcionario.setDistrito(distrito);
		funcionario.setFuncao(funcao);

        String idFuncionario = request.getParameter("idFuncionario");
        
        
        if(idFuncionario == null || idFuncionario.isEmpty())
        {

            dao.add(funcionario);
        }
        else
        {
        	
            funcionario.setIdFuncionario(Integer.parseInt(idFuncionario));
			System.out.println(" DENTRO DE CONTROLLER "+funcionario.toString());

            dao.update(funcionario);
        }
        
        RequestDispatcher view = request.getRequestDispatcher(LIST_FUNCIONARIO);
        request.setAttribute("funcionarios", dao.getAllFuncionario());
        view.forward(request, response);
	}

}
