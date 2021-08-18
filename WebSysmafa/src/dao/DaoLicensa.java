package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import util.Conexao;
import model.*;;

public class DaoLicensa {
	
	static Alert alertErro = new Alert(AlertType.ERROR);
	static Alert alertInfo = new Alert(AlertType.INFORMATION);
	/**Este comando sao usadas 
	 * para manipular as base de dado
	 * */

	//private static final String INSERT = "INSERT INTO tbl_categoria(nome,descricao,idUtilizador,dataRegisto) values(?,?,?,?)";
	//private static final String DELETE = "DELETE FROM tbl_categoria WHERE idCategoria=?";
	//private static final String UPDATE = "UPDATE tbl_categoria SET nome=?,descricao=?,idUtilizador=? WHERE idCategoria=?";
	private static final String LIST = "select * from tbl_licensa";

	private static Connection conn = null;
	private static ResultSet rs = null;
	private static PreparedStatement stmt = null;
	public static List<Licensa> getAllLicensa() {

		List<Licensa> licensas = new ArrayList<>();

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(LIST);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Licensa licensa = new Licensa();
				licensa.setStartDate(rs.getObject("startDate",LocalDate.class));
				licensa.setEndDate(rs.getObject("endDate",LocalDate.class));
				licensa.setKey(rs.getString("key"));
				licensas.add(licensa);

			}

		} catch (SQLException ex) {
			alertErro.setHeaderText("Erro");
			alertErro.setContentText("Error ao listar a licensa " + ex.getMessage());
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

		return licensas;
	}


}
