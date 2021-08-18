package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DaoProducto;
import model.Categoria;
import model.Producto;
import model.Utilizador;



@WebServlet("/ProductoController")
public class ProductoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String INSERT_OR_EDIT = "/views/producto.jsp";
    private static String LIST_PRODUCTO = "/views/listProducto.jsp";
    private DaoProducto dao;
       
       
  
    public ProductoController() { 
        super();
        dao=new DaoProducto();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward="";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")){
            int idProducto = Integer.parseInt(request.getParameter("idProducto"));
            Producto producto=new Producto();
            producto.setIdProducto(idProducto);
            dao.delete(producto);
            forward = LIST_PRODUCTO;
            request.setAttribute("productos", dao.getAll());    
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int idProducto = Integer.parseInt(request.getParameter("idProducto"));
            Producto producto = dao.getProductoById(idProducto);
            request.setAttribute("producto", producto);
        } else if (action.equalsIgnoreCase("listProducto")){
            forward = LIST_PRODUCTO;
            request.setAttribute("productos", dao.getAll());
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Producto producto=new Producto();
		producto.setNome(request.getParameter("nome"));
		producto.setCodigo(request.getParameter("codigo"));
		producto.setDescricao(request.getParameter("descricao"));
		producto.setQuantidade(0);
		//producto.setPrecoFinal(0.0);
		//producto.setPrecoFornecedor(0.0);
		
		Categoria  categoria=new Categoria();
		categoria.setIdCategoria(Integer.parseInt(request.getParameter("idCategoria")));
		 producto.setCategoria(categoria);
		 
		 Utilizador utilizador=new Utilizador();
		 utilizador.setIdUtilizador(1); 
		 producto.setUtilizador(utilizador);
		 
		 //Fornecedor fornecedor =new Fornecedor();
		 //fornecedor.setIdFornecedor(2);
		 //producto.setFornecedor(fornecedor);
		 
		 
		
		  
				 
	        String idProducto = request.getParameter("idProducto");
	        
	        
	        if(idProducto == null || idProducto.isEmpty())
	        {
	            dao.add(producto);
	        }
	        else
	        {
	        	producto.setIdProducto(Integer.parseInt(idProducto));
	            dao.update(producto);
	        }
	        
	        RequestDispatcher view = request.getRequestDispatcher(LIST_PRODUCTO);
	        request.setAttribute("productos",dao.getAll());
	        view.forward(request, response);
	}

}
