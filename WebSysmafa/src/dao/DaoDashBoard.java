package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import util.Conexao;

public class DaoDashBoard {
	static Alert alertErro = new Alert(AlertType.ERROR);
	static Alert alertInfo = new Alert(AlertType.INFORMATION);

	private static final String COUNT_UTILIZADOR = "select count(idUtilizador) as total from tbl_utilizador";
	private static final String COUNT_CLIENTE = "select count(nome) as total from tbl_cliente";
	private static final String COUNT_FUNCIONARIO = "select count(nome) as total from tbl_funcionario";
	// private static final String COUNT_PRODUCTOVENCIDOEM10 = "select
	// count(idUtilizador) as total from tbl_utilizador";
	// private static final String COUNT_PRODUCTOVENCIDO = "select
	// count(idUtilizador) as total from tbl_utilizador";

	private static Connection conn = null;
	private static ResultSet rs = null;
	// private static CallableStatement cs = null;
	private static PreparedStatement stmt;

	/**
	 * <h1>Quantidade total de Funcionario</h1>
	 * <p>
	 * Esta funcao e usada para dar informacao no dashboard ela retorna a qty
	 * dos<br>
	 * funcionarios existentes no sistema
	 * </P>
	 * 
	 * @author JBILA
	 * @return qty - Numero total de Funcionarios
	 */
	public static int getTotalFuncionario() {
		int total = 0;
		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(COUNT_FUNCIONARIO);
			rs = stmt.executeQuery();

			while (rs.next()) {
				total = rs.getInt("total");
			}

		} catch (SQLException ex) {
			alertErro.setHeaderText("Erro");
			alertErro.setContentText("Error ao obter o Total de Funcionarios " + ex.getMessage());
			alertErro.showAndWait();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				alertErro.setHeaderText("Erro");
				alertErro.setContentText("Erro fechar o resultset ou statement  " + e.getMessage());
				alertErro.showAndWait();

			}

		} // fim do try

		return total;
	}

	/**
	 * <h1>Quantidade total de Utilizadores</h1>
	 * <p>
	 * Esta funcao e usada para dar informacao no dashboar ela retorna a qty dos<br>
	 * utilizadores do sistema existentes
	 * </P>
	 * 
	 * @return qty -Total de Utilizadores existentes
	 * @author JBILA
	 */
	public static int getTotalUtilizador() {
		int total = 0;
		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(COUNT_UTILIZADOR);
			rs = stmt.executeQuery();

			while (rs.next()) {
				total = rs.getInt("total");
			}

		} catch (SQLException ex) {
			alertErro.setHeaderText("Erro");
			alertErro.setContentText("Error ao obter o Total de Utilizador " + ex.getMessage());
			alertErro.showAndWait();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				alertErro.setHeaderText("Erro");
				alertErro.setContentText("Erro fechar o resultset ou statement  " + e.getMessage());
				alertErro.showAndWait();

			}

		} // fim do try

		return total;
	}
	/**Esta funcao retorna a qty total dos productos que estao no nivel baixo do stock
	 * @return total- retorna a quantidade  total de productos que estao no stock baixo
	 * a condicao e de qty menor ou igual a 50
	 * nota: a query desta funcao e uma view que esta na base de dados
	 * */
	public static int getBaixoStock(){
		int total = 0;
		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement("select * from vw_qtyProductoBaixoStock");
			rs = stmt.executeQuery();

			while (rs.next()) {
				total = rs.getInt("total");
			}

		} catch (SQLException ex) {
			alertErro.setHeaderText("Erro");
			alertErro.setContentText("Error ao obter o Total de productos no baixo stock " + ex.getMessage());
			alertErro.showAndWait();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				alertErro.setHeaderText("Erro");
				alertErro.setContentText("Erro fechar o resultset ou statement  " + e.getMessage());
				alertErro.showAndWait();

			}

		} // fim do try

		return total;
	}


	/**
	 * <h1>Quantidade total de Clientes</h1>
	 * <p>
	 * Esta funcao e usada para dar informacao no dashboard ela retorna a qty
	 * dos<br>
	 * clientes existentes no sistema
	 * </p>
	 * 
	 * @return qty -Numero total de clientes
	 * @author JBILA
	 */
	public static int getTotalCliente() {
		int total = 0;
		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(COUNT_CLIENTE);
			rs = stmt.executeQuery();

			while (rs.next()) {
				total = rs.getInt("total");
			}

		} catch (SQLException ex) {
			alertErro.setHeaderText("Erro");
			alertErro.setContentText("Error ao obter o Total de cliente " + ex.getMessage());
			alertErro.showAndWait();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				alertErro.setHeaderText("Erro");
				alertErro.setContentText("Erro fechar o resultset ou statement  " + e.getMessage());
				alertErro.showAndWait();

			}

		} // fim do try

		return total;
	}

	/**
	 * <h1>Quantidade total de Productos a vencer em 10 dias</h1>
	 * <p>
	 * Esta funcao exibe a quantidade de producto existentes no stock que estao<br>
	 * preste a vencer, ou melhor que vao vencer dentro de 10 dias
	 * </p>
	 * 
	 * @return total- retorna a qty todas de producto a vencer em 10 dias
	 */
	public static int getTotalProductoAvencerEm10(String today, String today10) {
		int total = 0;
		try {
			String COUNT_PRODUCTOVENCIDOEM10 = "SELECT DISTINCT count(P.nome) as total  FROM tbl_producto  AS P WHERE p.validade BETWEEN  '"
					+ today + "' AND '" + today10 + "' AND p.quantidade<>0\r\n" + "";
			conn = Conexao.connect();
			stmt = conn.prepareStatement(COUNT_PRODUCTOVENCIDOEM10);
			rs = stmt.executeQuery();

			while (rs.next()) {
				total = rs.getInt("total");
			}

		} catch (SQLException ex) {
			alertErro.setHeaderText("Erro");
			alertErro.setContentText("Error ao obter o Total de producto a vencer em 10 dias " + ex.getMessage());
			alertErro.showAndWait();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				alertErro.setHeaderText("Erro");
				alertErro.setContentText("Erro fechar o resultset ou statement  " + e.getMessage());
				alertErro.showAndWait();

			}

		} // fim do try

		return total;
	}

	/**
	 * <h1>Quantidade total de Productos Vencidos</h1>
	 * <p>
	 * Esta funcao retorna a qty total de productos vencidos
	 * </p>
	 * 
	 * @return qty- Qty de productos vencidos
	 */
	public static int getTotalProductosVencidos(String hoje) {
		int total = 0;
		try {
			String COUNT_PRODUCTOVENCIDO = "SELECT DISTINCT count(P.nome)AS total  FROM tbl_producto  AS P WHERE  P.quantidade<>0 AND p.validade <= '"
					+ hoje + "'";
			conn = Conexao.connect();
			stmt = conn.prepareStatement(COUNT_PRODUCTOVENCIDO);
			rs = stmt.executeQuery();

			while (rs.next()) {
				total = rs.getInt("total");
			}

		} catch (SQLException ex) {
			alertErro.setHeaderText("Erro");
			alertErro.setContentText("Error ao obter o Total de Productos Vencidos " + ex.getMessage());
			alertErro.showAndWait();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				alertErro.setHeaderText("Erro");
				alertErro.setContentText("Erro fechar o resultset ou statement  " + e.getMessage());
				alertErro.showAndWait();
			}

		} // fim do try

		return total;
	}
}
