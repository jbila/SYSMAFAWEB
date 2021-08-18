package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import util.Conexao;
import model.*;
/**
 * <h1>DaoEntradaProducto</h1>
 * <p>
 * Esta classe tem metodos de persistencia de dados, ela comunica directamente
 * com a base <br>
 * coma base de dado fazendo as seguintes operacoes
 * </p>
 * <li>CREATE</li>
 * <li>DELETE</li>
 * <li>UPDATE</li>
 * <li>LIST</li>
 * <li>CHECKIFEXIST</li>
 * <h3>Esta classe recebe os objecto vindo das controladoras ou retorna para a
 * controladora desde objecto</h3>
 * <h4>@author JBILA Contacto:848319153 Email:jacinto.billa@gmail.com</h4>
 * 
 */

public class DaoEntradaProducto {
	/**
	 * <h4>Alert</h4>
	 * <p>
	 * A classe <b>Alert</b> é do javafx equivalente ao JOPtionPane do swing<br>
	 * com ela pode se ter altertas tipos diferentes
	 * </p>
	 */

	static Alert alertErro = new Alert(AlertType.ERROR);
	static Alert alertInfo = new Alert(AlertType.INFORMATION);

	private static final String INSERT = "INSERT INTO tbl_entradaProducto(idProducto,idFornecedor,quantidade,valorCompra,valorVenda,validade,dataRegisto) VALUES(?,?,?,?,?,?,?)";
	private static final String LIST = "SELECT E.idEntradaProducto, P.idProducto,P.nome as Producto,F.nome as Fornecedor,E.quantidade,E.valorCompra,E.valorVenda,E.validade,E.dataRegisto\r\n"
			+ "FROM tbl_entradaProducto as E\r\n" + "inner join tbl_producto as P\r\n"
			+ "inner join tbl_fornecedor as F\r\n"
			+ "on E.idProducto=P.idProducto  and E.idFornecedor=F.idFornecedor ORDER BY E.idEntradaProducto desc";
	private static final String DELETE = "{CALL sp_Delete_Producto(?)}";
	private static final String UPDATE = "UPDATE tbl_entradaProducto SET nome=?,codigo=?,descricao=?,quantidade=?,precoFinal,precoFornecedor=?,validade=?,idCategoria=? WHERE idProducto=?";

	private static Connection conn = null;
	// private static CallableStatement cs = null;
	private static ResultSet rs = null;
	private static PreparedStatement stmt;

	/**
	 * <h5>Esta funcao persiste um a entrada do producto</h5>
	 * 
	 * @param entradaProducto
	 * @see EntradaProducto
	 */
	public static void add(EntradaProducto entradaProducto) {

		try {
			LocalDate localDate = LocalDate.now();
			String dataRegisto = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);
			conn = Conexao.connect();
			stmt = conn.prepareStatement(INSERT);
			stmt.setInt(1, entradaProducto.getProducto().getIdProducto());
			stmt.setInt(2, entradaProducto.getFornecedor().getIdFornecedor());
			stmt.setInt(3, entradaProducto.getQuantidade());
			stmt.setDouble(4, entradaProducto.getValorCompra());
			stmt.setDouble(5, entradaProducto.getValorVenda());
			stmt.setString(6, entradaProducto.getValidade());
			stmt.setString(7, dataRegisto);
			alertInfo.setTitle("Informação");
			alertInfo.setHeaderText("Confirmação de Registo");
			alertInfo.setContentText("Entrada  com êxito ");
			alertInfo.showAndWait();
			stmt.executeUpdate();

		} catch (SQLException ex) {
			alertInfo.setHeaderText("Informação");
			alertInfo.setContentText(" " + ex);
			alertInfo.showAndWait();
			ex.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

	}

	public static void update(EntradaProducto entradaProducto) {
		try {

			// nome=?,codigo=?,descricao=?,quantidade=?,precoFinal,precoFornecedor=?,validade=?,idCategoria
			conn = Conexao.connect();
			stmt = conn.prepareStatement(UPDATE);
			stmt.setInt(1, entradaProducto.getProducto().getIdProducto());
			stmt.setInt(2, entradaProducto.getFornecedor().getIdFornecedor());
			stmt.setInt(3, entradaProducto.getQuantidade());
			stmt.setDouble(4, entradaProducto.getValorCompra());
			stmt.setDouble(5, entradaProducto.getValorVenda());
			stmt.setString(6, entradaProducto.getValidade());
			stmt.setInt(7, entradaProducto.getIdEntradaProducto());
			stmt.executeUpdate();
			alertInfo.setTitle("Informação");
			alertInfo.setHeaderText("Confirmação da Actualização");
			alertInfo.setContentText("Registo Actulizado com êxito ");
			alertInfo.showAndWait();
		}

		catch (SQLException ex) {
			alertErro.setHeaderText("Error");
			alertErro.setContentText("Error Updating the the Product " + ex.getMessage());
			alertErro.showAndWait();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}
	/**
	 * <h5>Esta funcao lista EntradaProducto de producto</h5>
	 * @see EntradaProducto
	 * @return entradaProductos - lista de a entrada de productos
	 * 
	 */
	public static List<EntradaProducto> getAll() {
		List<EntradaProducto> entradaProductos = new ArrayList<>();
		try {
			conn = Conexao.connect();
			stmt = conn.prepareCall(LIST);
			rs = stmt.executeQuery();
			while (rs.next()) {

				EntradaProducto entradaProducto = new EntradaProducto();// objecto principal
				Producto producto = new Producto();
				producto.setNome(rs.getString("Producto"));
				producto.setIdProducto(rs.getInt("idProducto"));

				Fornecedor fornecedor = new Fornecedor();
				fornecedor.setNome(rs.getString("Fornecedor"));

				entradaProducto.setIdEntradaProducto(rs.getInt("idEntradaProducto"));
				entradaProducto.setValorCompra(rs.getDouble("valorCompra"));
				entradaProducto.setValorVenda(rs.getDouble("valorVenda"));
				entradaProducto.setQuantidade(rs.getInt("quantidade"));
				entradaProducto.setValidade(rs.getString("validade"));
				entradaProducto.setDataRegisto(rs.getString("dataRegisto"));
				entradaProducto.setProducto(producto);
				entradaProducto.setFornecedor(fornecedor);
				entradaProductos.add(entradaProducto);
			}

		} catch (SQLException ex) {
			alertErro.setHeaderText("Erro");
			alertErro.setContentText("Erro ao listar a entrada de  Producto  " + ex.getMessage());
			alertErro.showAndWait();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}

		}

		return entradaProductos;
	}

	/**
	 * <h5>Esta funcao lista a entrada de producto</h5>
	 * 
	 * @see EntradaProducto
	 * @return entradaProductos- retorna uma lista de de todos os productos que
	 *         estao no armazem
	 * @param nome- recebe o objecto de entradaProducto
	 * 
	 */
	public static void delete(EntradaProducto entradaProducto) {
		conn = Conexao.connect();
		try {
			stmt = conn.prepareStatement(DELETE);
			stmt.setInt(1, entradaProducto.getIdEntradaProducto());
			stmt.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();
		}

		alertInfo.setTitle("Informação");
		alertInfo.setHeaderText("Confirmação da remoção");
		alertInfo.setContentText("Registo Eliminado com êxito ");
		alertInfo.showAndWait();
	}

	/**
	 * <h5>Esta funcao Faz a reposicao de productos nostock</h5>
	 * <p>
	 * Isto é quando a qty do producto estiver zero, entao este producto <br>
	 * Este producto pode ser introduzido, ou melhor, no Menu PVD o producto que tem
	 * zero pode ser<br>
	 * Adicionado
	 * </p>
	 * <h6>Onde vou actualizar a quantidade de validade na tabela producto que faz
	 * papel de stock
	 * <h6/>
	 * 
	 * @see Producto
	 * @param producto- recebe o objecto de productos
	 * 
	 */
	public static void reposicaoStock(Producto producto) {
		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(
					"UPDATE tbl_producto SET quantidade=?, validade=?,precoFinal=? WHERE quantidade =0 AND idProducto=?;");
			stmt.setInt(1, producto.getQuantidade());
			stmt.setString(2, producto.getValidade());
			stmt.setDouble(3, producto.getPrecoFinal());
			stmt.setInt(4, producto.getIdProducto());
			stmt.executeUpdate();
			/*
			 * alertInfo.setTitle("INFORMAÇÃO");
			 * alertInfo.setHeaderText("CONFIRMAÇÃO DA REPOSIÇÃO");
			 * alertInfo.setContentText("REPOSIÇÃO FEITA COM ÊXITO ");
			 * alertInfo.showAndWait();
			 */
		}

		catch (SQLException ex) {
			alertErro.setHeaderText("Error");
			alertErro.setContentText("Erro ao Repor o Stock " + ex.getMessage());
			alertErro.showAndWait();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}

	/**
	 * <h5>Esta funcao Faz a Actualizacao as Entradas/Compras de productos</h5>
	 * <p>
	 * Quando o producto e adicionado no stock, ele sai do armazem para la<br>
	 * Portanto a que se actualizar a remocao fazendo actual=actual-saida<br>
	 * </p>
	 * 
	 * @see EntradaProducto
	 * @param EntradaProducto- recebe o objecto de entradaProducto
	 * @exception SQLException- esta funcao pode gerar exepcaoes do tipo sql
	 * 
	 */
	public static void actualizarEntradas(EntradaProducto entradaProducto) {
		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(
					"UPDATE tbl_entradaproducto SET 	quantidade=quantidade-? WHERE  idEntradaProducto=?");
			stmt.setInt(1, entradaProducto.getQuantidade());
			stmt.setInt(2, entradaProducto.getIdEntradaProducto());
			stmt.executeUpdate();
			alertInfo.setTitle("INFORMAÇÃO");
			alertInfo.setHeaderText("CONFIRMAÇÃO DA ACTUALIZAÇÃO DAS ENTRADAS");
			alertInfo.setContentText("ENTRADA FEITA COM ÊXITO ");
			alertInfo.showAndWait();
		}

		catch (SQLException ex) {
			alertErro.setHeaderText("Error");
			alertErro.setContentText("Erro ao Actualizar o Entradas " + ex.getMessage());
			alertErro.showAndWait();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}

	/**
	 * <h5>Esta funcao Faz verificacao da Quantidade</h5>
	 * 
	 * @see Producto
	 * @param idProducto- recebe o id do Producto
	 * @exception SQLException- esta funcao pode gerar exepcaoes do tipo sql
	 * @return qty- retorna a qty que actul, o Objectivo da funcao pegar a qty
	 *         quando for igual a 0
	 * 
	 */
	public static int verQtyProducto(int idProducto) {
		int qty = -1;

		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(
					"SELECT quantidade FROM tbl_producto	WHERE quantidade=0 AND  idProducto= '" + idProducto + "'");
			rs = stmt.executeQuery();
			if (rs.next()) {
				qty = (rs.getInt("quantidade"));
			}
		}

		catch (SQLException ex) {
			alertErro.setHeaderText("Error");
			alertErro.setContentText("Erro ao verificar Qty " + ex.getMessage());
			alertErro.showAndWait();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		return qty;
	}

}
