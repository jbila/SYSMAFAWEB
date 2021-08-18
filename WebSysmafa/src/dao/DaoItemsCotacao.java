package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import util.Conexao;
import model.*;

public class DaoItemsCotacao {
	/**
	 * <h4>Alert</h4>
	 * <p>
	 * A classe <b>Alert</b> é do javafx equivalente ao JOPtionPane do swing<br>
	 * com ela pode se ter altertas tipos diferentes
	 * </p>
	 */

	static Alert alertErro = new Alert(AlertType.ERROR);
	static Alert alertInfo = new Alert(AlertType.INFORMATION);
	private static final String INSERT = "INSERT INTO tbl_ItemsCotacao(idProducto,idCotacao,quantidade,precoUnitario) VALUES(?,?,?,?)";
	//private static final String LIST = "select * from tbl_ItemsCotacao order by id desc";
	private static final String DELETE = "{CALL ps_Delete_User(?)}";
	private static final String UPDATE = "UPDATE tbl_ItemsCotacao  ";

	private static Connection conn = null;
	private static PreparedStatement stmt;
	//private static CallableStatement cs = null;
	private static ResultSet rs = null;

	public static void add(List<ItemsCotacao> itemsCotacao) {
			List<ItemsCotacao> items=itemsCotacao;
		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(INSERT);
		for (ItemsCotacao i : items) {
			stmt.setInt(1, i.getProducto().getIdProducto());
			stmt.setInt(2, i.getCotacao().getIdCotacao());
			stmt.setInt(3, i.getQty());
			stmt.setDouble(4, i.getPrecoUnitario());
			stmt.executeUpdate();
				}
			alertInfo.setHeaderText("Informação");
			alertInfo.setHeaderText("Validação da  Cotacao");
			alertInfo.setContentText("Cotacao Feita Com êxito ");
			alertInfo.showAndWait();
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
	}
//-------------------------------------------------------------------

	public static void delete(ItemsCotacao itemsCotacao) {

		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(DELETE);
			stmt.setInt(1, itemsCotacao.getIdItemsCotacao());
			stmt.execute();
			alertInfo.setHeaderText("Information");
			alertInfo.setContentText("item cotacao Removida");
			alertInfo.showAndWait();

		} catch (SQLException e) {

			alertErro.setHeaderText("Erro");
			alertErro.setContentText("Erro ao eliminar a  Item: " + e.getMessage());
			alertErro.showAndWait();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}

	// ------------------------------------------------------------------------------
	public static void update(ItemsCotacao itemsCotacao) {
		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(UPDATE);
			stmt.setInt(1, itemsCotacao.getProducto().getIdProducto());
			stmt.setInt(2, itemsCotacao.getQty());
			stmt.setDouble(3, itemsCotacao.getPrecoUnitario());
			stmt.setInt(4, itemsCotacao.getIdItemsCotacao());
			stmt.executeUpdate();
			alertInfo.setHeaderText("Information");
			alertInfo.setContentText("Updated ");
			alertInfo.showAndWait();
		}

		catch (SQLException ex) {
			alertErro.setHeaderText("Error");
			alertErro.setContentText("Erro ao actualizar a items: " + ex.getMessage());
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

	public static List<ItemsCotacao> getAll(int codigoVenda) {
		List<ItemsCotacao> itemsCotacaoes = new ArrayList<>();
		try {
			String SQL="SELECT IP.id,P.nome as Producto,IP.precoUnitario as Preco,IP.quantidade \r\n" + 
					"FROM tbl_itemsPedidos AS IP\r\n" + 
					"inner join tbl_producto as P\r\n" + 
					"on  IP.idProducto=P.idProducto\r\n" + 
					"where idPedido='"+codigoVenda+"';";
			conn = Conexao.connect();
			stmt = conn.prepareCall(SQL);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ItemsCotacao itemsCotacao=new ItemsCotacao();
				Producto producto =new Producto();
				itemsCotacao.setIdItemsCotacao((rs.getInt("id")));
				producto.setNome(rs.getString("Producto"));
				itemsCotacao.setQty(rs.getInt("quantidade"));
				itemsCotacao.setPrecoUnitario(rs.getInt("Preco"));
				itemsCotacao.setProducto(producto);
				itemsCotacaoes.add(itemsCotacao);
			}

		} catch (SQLException ex) {
			alertErro.setHeaderText("Erro");
			alertErro.setContentText("Erro ao listar  items do cotacao  " + ex.getMessage());
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

		return itemsCotacaoes;
	}

}
