package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.Conexao;
import model.*;

public class DaoFormasDePagamento {
	/**
	 * <h4>Alert</h4>
	 * <p>
	 * A classe <b>Alert</b> é do javafx equivalente ao JOPtionPane do swing<br>
	 * com ela pode se ter altertas tipos diferentes
	 * </p>
	 */

	private final String LIST = "select * from tbl_formasDePagamento";

	private Connection conn = null;
	private PreparedStatement stmt = null;
	// private CallableStatement cs=null;
	private ResultSet rs = null;

	/**
	 * <h5>Esta funcao lista FormasDePagamento</h5>
	 * 
	 * @see FormasDePagamento
	 * @return FormasDePagamento- retorna uma lista de formas de pagamento
	 * 
	 */
	public List<FormasDePagamento> getAll() {
		List<FormasDePagamento> formasDePagamentos = new ArrayList<>();

		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(LIST);
			rs = stmt.executeQuery();

			while (rs.next()) {
				FormasDePagamento formasDePagamento = new FormasDePagamento();

				formasDePagamento.setId(rs.getInt("id"));
				formasDePagamento.setNome(rs.getString("nome"));
				formasDePagamento.setDescricao(rs.getString("descricao"));
				formasDePagamentos.add(formasDePagamento);

			}

		} catch (SQLException ex) {

			System.out.println("ERROR LISTING " + ex);
		}

		return formasDePagamentos;
	}
//------------------------------------------------------------------------------

}
