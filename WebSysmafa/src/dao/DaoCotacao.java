package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import util.Conexao;
import model.Cliente;
import model.Cotacao;
import model.ItemsCotacao;
import model.Producto;
import model.Utilizador;

public class DaoCotacao {
	/**
	 * <h4>Alert</h4>
	 * <p>
	 * A classe <b>Alert</b> é do javafx equivalente ao JOPtionPane do swing<br>
	 * com ela pode se ter altertas tipos diferentes
	 * </p>
	 */

	static Alert alertErro = new Alert(AlertType.ERROR);
	static Alert alertInfo = new Alert(AlertType.INFORMATION);
	
	/**
	 * <h4>Consultas SQL</h4>
	 * <p>
	 * As<b>Strings abaixo</b> sao usadas para interagir coma basse de dados no acto da remocao,adicionar...<br>
	 * listar e mais
	 * </p>
	 */
	private static final String INSERT = "INSERT INTO tbl_cotacao(idCliente,idUtilizador,valorDaCotacao,dataRegisto) VALUES(?,?,?,?)";
	private static final String LIST = "SELECT * FROM  vw_cotacoes";
	private static final String DELETE = "{CALL ps_Cotacao(?)}";
	private static final String UPDATE = "UPDATE tbl_cotacao set idCliente=?,idUtilizador=?,valorDaCotacao=? WHERE idCotacao=?";

	private static Connection conn = null;
	private static PreparedStatement stmt;
	// private static CallableStatement cs=null;
	private static ResultSet rs = null;

	public static int add(Cotacao cotacao) {
		int lastId = 0;
		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(INSERT);
			final PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			//idCliente,idUtilizador,valorDaCotacao,dataRegisto
			stmt.setInt(1, cotacao.getCliente().getIdCliente());
			stmt.setInt(2, cotacao.getUtilizador().getIdUtilizador());
			stmt.setDouble(3, cotacao.getValorTotal());
			stmt.setObject(4, cotacao.getDataRegisto());
			
			stmt.executeUpdate();
			final ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				lastId = rs.getInt(1);

			}
			/*
			alertInfo.setHeaderText("Information");
			alertInfo.setContentText("pedido Feito ");
			alertInfo.showAndWait();*/
			//
		} catch (SQLException ex) {
			alertInfo.setHeaderText("Information");
			alertInfo.setContentText(" " + ex);
			alertInfo.showAndWait();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}

		}
		return lastId;
	}
//-------------------------------------------------------------------

	public static void delete(Cotacao cotacao) {

		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(DELETE);
			stmt.setInt(1, cotacao.getIdCotacao());
			stmt.execute();
			alertInfo.setHeaderText("Information");
			alertInfo.setContentText("Cotacao Removida");
			alertInfo.showAndWait();

		} catch (SQLException e) {

			alertErro.setHeaderText("Erro");
			alertErro.setContentText("Erro ao eliminar a  Cotacao: " + e.getMessage());
			alertErro.showAndWait();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}

//------------------------------------------------------------------------------
	public static void update(Cotacao cotacao) {
		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(UPDATE);

			stmt.setInt(1, cotacao.getCliente().getIdCliente());
			stmt.setInt(2, cotacao.getUtilizador().getIdUtilizador());
			stmt.setDouble(3, cotacao.getValorTotal());
			stmt.setInt(4, cotacao.getIdCotacao());
			stmt.executeUpdate();

			alertInfo.setHeaderText("Information");
			alertInfo.setContentText("Cotacao Updated ");
			alertInfo.showAndWait();
		}

		catch (SQLException ex) {
			alertErro.setHeaderText("Error");
			alertErro.setContentText("Erro ao actualizar a cotacao: " + ex.getMessage());
			alertErro.showAndWait();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}
//------------------------------------------------------------------------------

	public static List<Cotacao> getAll() {
		List<Cotacao> cotacoes = new ArrayList<>();
		try {
			conn = Conexao.connect();
			stmt = conn.prepareCall(LIST);
			rs = stmt.executeQuery();
			while (rs.next()) {

				Cotacao cotacao = new Cotacao();// objecto principal
				Cliente cliente = new Cliente();// OBJECTO SECUNDARIO
				cliente.setNome(rs.getString("Cliente"));
				Utilizador utilizador = new Utilizador();// OBJECTO SECUNDARIO
				utilizador.setUsername(rs.getString("Utilizador"));
				cotacao.setIdCotacao(rs.getInt("Id"));
				cotacao.setValorTotal((rs.getDouble("valorDaCotacao")));
				cotacao.setCliente(cliente);
				cotacao.setUtilizador(utilizador);
				//LocalDate localDate = rs.getObject ( "dataRegisto", LocalDate.class );
				cotacao.setDataRegisto(rs.getObject("dataRegisto",LocalDate.class));
				cotacoes.add(cotacao);
			}

		} catch (SQLException ex) {
			alertErro.setHeaderText("Erro");
			alertErro.setContentText("Erro ao listar  Cotacoes  " + ex.getMessage());
			alertErro.showAndWait();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
			try {
				stmt.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}

		}

		return cotacoes;
				
	}
//------------------------------------------------------------------------------------

	public static List<Cotacao> searchCotacoesDoCliente(String Nomecliente) {
		List<Cotacao> cotacoes = new ArrayList<>();
		try {
			conn = Conexao.connect();
			stmt = conn.prepareCall("SELECT * FROM  vw_cotacoes WHERE cliente LIKE'%"+Nomecliente+"%'");
			rs = stmt.executeQuery();
			while (rs.next()) {

				Cotacao cotacao = new Cotacao();// objecto principal
				Cliente cliente = new Cliente();// OBJECTO SECUNDARIO
				cliente.setNome(rs.getString("Cliente"));
				Utilizador utilizador = new Utilizador();// OBJECTO SECUNDARIO
				utilizador.setUsername(rs.getString("Utilizador"));
				cotacao.setIdCotacao(rs.getInt("Id"));
				cotacao.setValorTotal((rs.getDouble("valorDaCotacao")));
				cotacao.setCliente(cliente);
				cotacao.setUtilizador(utilizador);
				//LocalDate localDate = rs.getObject ( "dataRegisto", LocalDate.class );
				cotacao.setDataRegisto(rs.getObject("dataRegisto",LocalDate.class));
				cotacoes.add(cotacao);
			}

		} catch (SQLException ex) {
			alertErro.setHeaderText("Erro");
			alertErro.setContentText("Erro ao pesquisar  cotacoes do cliente  " + ex.getMessage());
			alertErro.showAndWait();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
			try {
				stmt.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}

		}

		return cotacoes;
	}
	/**Esta funcao retorna os items da cotacao em cause
	 * recebendo o numero de cotacao  por parametro*/
	public static List<ItemsCotacao> itemsDaCotacao(int idCotacao) {
		List<ItemsCotacao> itemsCotacoes = new ArrayList<>();
		try {
			conn = Conexao.connect();
			stmt = conn.prepareCall("select * from vw_itemsCotacao where idCotacao='"+idCotacao+"'");
			rs = stmt.executeQuery();
			while (rs.next()) {
				ItemsCotacao items=new ItemsCotacao();
				Cotacao cotacao = new Cotacao();// objecto principal
				Producto producto =new Producto();
				producto.setNome(rs.getString("producto"));
				items.setQty(rs.getInt("qty"));
				items.setPrecoUnitario(rs.getDouble("precoUnitario"));
				items.setProducto(producto);
				items.setCotacao(cotacao);
				
				itemsCotacoes.add(items);
			}

		} catch (SQLException ex) {
			alertErro.setHeaderText("Erro");
			alertErro.setContentText("Erro ao listar  Items da Cotacao  " + ex.getMessage());
			alertErro.showAndWait();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
			try {
				stmt.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}

		}

		return itemsCotacoes;
	}


}
