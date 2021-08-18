package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Funcionario;
import model.Utilizador;
import util.Conexao;

public class DaoLogin {
	/**
	 * <h4>Alert</h4>
	 * <p>
	 * A classe <b>Alert</b> é do javafx equivalente ao JOPtionPane do swing<br>
	 * com ela pode se ter altertas tipos diferentes
	 * </p>
	 */
	public  int idUtilizador = 0;
	

	private  final String sql = "SELECT * FROM tbl_utilizador WHERE username =? and password =? and status='ACTIVO'";
	private  Connection conn = null;
	private  PreparedStatement stmt = null;
	private  ResultSet rs = null;

	public  boolean consultar(Utilizador utilizador) throws SQLException {
		boolean autenticado = false;

		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, utilizador.getUsername());
			stmt.setString(2, utilizador.getPassword());
			
			rs = stmt.executeQuery();
			if (rs.next()) {
				autenticado = true;

				return autenticado;

			} else {
				
				autenticado = false;
				return autenticado;
			}

		} catch (SQLException ex) {
			return false;
		}
		
	}
//=============================================================================================
	public  int getIdUtilizador(String user) {

		int id = 0;

		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement("select * from tbl_utilizador where idUtilizador=?");
			stmt.setString(1, user);
			rs = stmt.executeQuery();

			if (rs.next()) {
				id = rs.getInt(1);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		

		
		return id;
	}
/*--------------------------GET ONE OBJECT------------------------------------------*/
	 public Utilizador getUtilizadorByUserPass(Utilizador user) {
			Utilizador utilizador = new Utilizador();
			try {
				stmt = conn.prepareStatement("SELECT * FROM vw_login WHERE status='ACTIVO' AND  username=? AND password=?");//SELECT * FROM vw_login WHERE status='ACTIVO' AND  username=? AND password=?
				stmt.setString(1, user.getUsername());
				stmt.setString(2, user.getPassword());
				 rs = stmt.executeQuery();

				while (rs.next()) {
					Funcionario funcionario = new Funcionario();
					funcionario.setNome(rs.getString("nome"));
					utilizador.setFuncionario(funcionario);
					utilizador.setIdUtilizador(rs.getInt("idUtilizador"));
					utilizador.setUsername(rs.getString("username"));
					utilizador.setStatus(rs.getString("status"));
					utilizador.setPerfil(rs.getString("perfil"));
					utilizador.setPassword(rs.getString("password"));
				}
			} catch (SQLException e) {
				System.out.println("Error trying to login"+e);
				
			}
			catch (NullPointerException e) {
				System.out.println("Nulpoint found"+e);
			}

			return utilizador;
		}

}
