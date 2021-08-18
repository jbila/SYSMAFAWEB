package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DaoPedido;
import dao.DaoProducto;
import model.Cliente;
import model.FormasDePagamento;
import model.Pedido;
import model.Producto;
import model.Utilizador;


@WebServlet("/PedidoController")
public class PedidoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String INSERT_OR_EDIT = "/views/pedido.jsp";
    private static String LIST_PEDIDO = "/views/listPedido.jsp";
    private static String LIST_PRODUCTO = "/views/pedido.jsp";
    private DaoPedido dao;
    private DaoProducto daoProducto;
       
   
    public PedidoController() {
        super();
        dao=new DaoPedido(); 
        daoProducto=new DaoProducto();
        
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward="";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")){
            int idPedido = Integer.parseInt(request.getParameter("idPedido"));
            Pedido pedido=new Pedido();
            pedido.setIdPedido(idPedido);
            dao.delete(pedido);
            forward = LIST_PEDIDO;
            request.setAttribute("pedidos", dao.getAll());    
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
          String idPedido= request.getParameter("idPedido");
            Pedido pedido = dao.getPedidoById(Integer.parseInt(idPedido));
            request.setAttribute("pedido", pedido);
        } else if (action.equalsIgnoreCase("listPedido")){
            forward = LIST_PEDIDO;
            request.setAttribute("pedidos", dao.getAll());
        }
     
        else if(action.equalsIgnoreCase("addChart")) {
        	String idProducto=request.getParameter("idProducto");
        	Producto pro=new Producto();
        	pro.setIdProducto(Integer.parseInt(idProducto));
        	Producto producto =daoProducto.get(pro);
        	//forward=LIST_PRODUCTO;
        	 request.setAttribute("producto", producto);
        	// System.out.println( producto.toString());
        
        }
        
        else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);	
        }

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Pedido pedido = new Pedido(); 
		/*CLIENTE*/
		Cliente cliente=new Cliente();
		cliente.setIdCliente(Integer.parseInt(request.getParameter("idCliente")));
		pedido.setCliente(cliente);
		
		/*UTILIZADOR*/
		Utilizador utilizador=new Utilizador();
		utilizador.setIdUtilizador(1);
		pedido.setUtilizador(utilizador);
		
		/*FORMAS DE PAGAMENTO*/
		FormasDePagamento formasDepagamento=new FormasDePagamento();
		formasDepagamento.setId(Integer.parseInt(request.getParameter("idFormaDePagamento")));
		pedido.setFormasDepagamento(formasDepagamento);
		/*TIPO*/
		pedido.setTipo("C");
		/*VALOR PAGO*/
		pedido.setValorPago(Double.parseDouble(request.getParameter("valorPago")));
		/*VALOR DO PEDIDO*/
		pedido.setValorPedido(Double.parseDouble(request.getParameter("valorPedido")));
		
        String idPedido = request.getParameter("idPedido");
        
        
        if(idPedido == null || idPedido.isEmpty())
        	
        {
        	
            dao.add(pedido);
        }
        else
        {
            pedido.setIdPedido((Integer.parseInt(idPedido)));
            dao.update(pedido);
        }
        
        RequestDispatcher view = request.getRequestDispatcher(LIST_PEDIDO);
        request.setAttribute("pedidos", dao.getAll());
        view.forward(request, response);
	}	
	

}
