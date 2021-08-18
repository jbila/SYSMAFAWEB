package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import util.Conexao;
import model.*;

public class DaoPedido {
	/**
	 * <h4>Alert</h4>
	 * <p>
	 * A classe <b>Alert</b> é do javafx equivalente ao JOPtionPane do swing<br>
	 * com ela pode se ter altertas tipos diferentes
	 * </p>
	 */

	
	
	/**
	 * <h4>Consultas SQL</h4>
	 * <p>
	 * As<b>Strings abaixo</b> sao usadas para ineragir coma basse de dados no acto da remocao,adicionar...<br>
	 * listar e mais
	 * </p>
	 */
	private  final String INSERT = "INSERT INTO tbl_pedido(idCliente,idUtilizador,idFormasPagamento,tipo,valorPago,valorDoPedido,dataRegisto) VALUES(?,?,?,?,?,?,?)";
	private  final String LIST = "SELECT * FROM  vw_pedidos order by id DESC LIMIT 10";
	private  final String DELETE = "{CALL ps_Pedido(?)}";
	private  final String UPDATE = "UPDATE tbl_pedido set idCliente=?,idUtilizador=?,idFformasDepagamento=?,tipo=?,valorPago=?valorDoPedido=? WHERE idPedido=?";

	private  Connection conn = null;
	private  PreparedStatement stmt;
	// private  CallableStatement cs=null;
	private  ResultSet rs = null;

	public  int add(Pedido pedido) {
		int lastId = 0;
		try {
			LocalDate localDate = LocalDate.now();
			String dataRegisto = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);

			conn = Conexao.connect();
			stmt = conn.prepareStatement(INSERT);
			final PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			// cliente,utilizador,formasPagamento,estado,valorPedido
			stmt.setInt(1, pedido.getCliente().getIdCliente());
			stmt.setInt(2, pedido.getUtilizador().getIdUtilizador());
			stmt.setInt(3, pedido.getFormasDepagamento().getId());
			stmt.setString(4, pedido.getTipo());
			stmt.setDouble(5, pedido.getValorPago());
			stmt.setDouble(6, pedido.getValorPedido());
			stmt.setString(7, dataRegisto);
			stmt.executeUpdate();

			final ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				lastId = rs.getInt(1);

			}
			
		} catch (SQLException ex) {
			System.out.println("ERROR FOUND  adding "+ex);

		}
		return lastId;
	}
//-------------------------------------------------------------------

	public  void delete(Pedido pedido) {

		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(DELETE);
			stmt.setInt(1, pedido.getIdPedido());
			stmt.execute();
			

		} catch (SQLException e) {
			System.out.println("ERROR FOUND "+e);

		
		}
	}

//------------------------------------------------------------------------------
	public  void update(Pedido pedido) {
		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(UPDATE);

			stmt.setInt(1, pedido.getCliente().getIdCliente());
			stmt.setInt(2, pedido.getUtilizador().getIdUtilizador());
			stmt.setInt(3, pedido.getFormasDepagamento().getId());
			stmt.setString(4, pedido.getTipo());
			stmt.setDouble(5, pedido.getValorPago());
			stmt.setDouble(6, pedido.getValorPedido());
			stmt.setInt(7, pedido.getIdPedido());
			stmt.executeUpdate();
		}

		catch (SQLException ex) {
			System.out.println("Venda updating"+ex);
		
		}
	}
//------------------------------------------------------------------------------

	public  List<Pedido> getAll() {
		List<Pedido> pedidos = new ArrayList<>();
		try {
			conn = Conexao.connect();
			stmt = conn.prepareCall(LIST);
			rs = stmt.executeQuery();
			while (rs.next()) {

				Pedido pedido = new Pedido();// objecto principal

				Cliente cliente = new Cliente();// OBJECTO SECUNDARIO
				cliente.setNome(rs.getString("Cliente"));

				Utilizador utilizador = new Utilizador();// OBJECTO SECUNDARIO
				utilizador.setUsername(rs.getString("Utilizador"));

				FormasDePagamento formasDepagamento = new FormasDePagamento();// OBJECTO SECUNDARIO
				formasDepagamento.setNome(rs.getString("Pago_via"));

				pedido.setIdPedido(rs.getInt("Id"));
				pedido.setTipo(rs.getString("tipo"));
				pedido.setDataRegisto(rs.getString("Data"));
				pedido.setValorPago(rs.getDouble("Pago"));
				pedido.setValorPedido(rs.getDouble("Total"));
				pedido.setCliente(cliente);
				pedido.setFormasDepagamento(formasDepagamento);
				pedido.setUtilizador(utilizador);
				pedidos.add(pedido);
			}

		} catch (SQLException ex) {
			System.out.println("Erro found"+ex);
		}

		return pedidos;
	}
//------------------------------------------------------------------------------------

	public  List<Pedido> search(int numero) {
		List<Pedido> pedidos = new ArrayList<>();
		try {
			conn = Conexao.connect();
			stmt = conn.prepareCall("SELECT * FROM  vw_pedidos WHERE id LIKE'%"+numero+"'");
			rs = stmt.executeQuery();
			while (rs.next()) {

				Pedido pedido = new Pedido();// objecto principal

				Cliente cliente = new Cliente();// OBJECTO SECUNDARIO
				cliente.setNome(rs.getString("cliente"));

				Utilizador utilizador = new Utilizador();// OBJECTO SECUNDARIO
				utilizador.setUsername(rs.getString("utilizador"));

				FormasDePagamento formasDepagamento = new FormasDePagamento();// OBJECTO SECUNDARIO
				formasDepagamento.setNome(rs.getString("pago_via"));

				pedido.setIdPedido(rs.getInt("id"));
				pedido.setCliente(cliente);
				pedido.setTipo(rs.getString("tipo"));
				pedido.setDataRegisto(rs.getString("data"));
				pedido.setValorPago(rs.getDouble("pago"));
				pedido.setValorPedido(rs.getDouble("total"));
				pedido.setFormasDepagamento(formasDepagamento);
				pedido.setUtilizador(utilizador);
				pedidos.add(pedido);

			}
		}
		 catch (SQLException ex) {
			System.out.println("Erro ao listar  Pedido  " + ex.getMessage());
			}

		

		return pedidos;
	}
//----------------------------------------------------------------------------------------
	public  void updateValorPago(Pedido pedido) {
		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement("UPDATE tbl_pedido SET valorPago=valorPago+? WHERE idPedido =?");
			stmt.setDouble(1,pedido.getValorPago());
			stmt.setInt(2,pedido.getIdPedido());
			stmt.executeUpdate();
		}

		catch (SQLException ex) {
			System.out.println("updateValorPago " + ex.getMessage());
	}

}
	//========================GET BY ID============================
		public Pedido getPedidoById(int idPedido) {
			Pedido pedido = new Pedido();
			try {
				
				stmt = conn.prepareStatement("select idPedido,nome,descricao from tbl_Pedido where idPedido=?");
				stmt.setInt(1, idPedido);
				 rs = stmt.executeQuery();

				if (rs.next()) {
					

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return pedido;
		}

}
