
package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import util.Conexao;
import model.Funcionario;
import model.Utilizador;


/**
 * <h1>DaoUtilizador</h1>
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

public class DaoUtilizador {
	/**
	 * <h4>Alert</h4>
	 * <p>
	 * A classe <b>Alert</b> é do javafx equivalente ao JOPtionPane do swing<br>
	 * com ela pode se ter altertas tipos diferentes
	 * </p>
	 */

	/** String que sao usadas para operacoes com base de dados */
	private  final String INSERT = "INSERT INTO tbl_utilizador(idFuncionario,status,perfil,username,password,dataRegisto) VALUES(?,?,?,?,?,?)";
	private  final String LIST = "select * from vw_listutilizador";
	private  final String DELETE = "{CALL sp_Delete_Utlizador(?)}";
	private  final String UPDATE = "UPDATE tbl_utilizador SET idFuncionario=?,status=?,perfil=?,username=?,password=? WHERE idUtilizador=? ";
	private  final String sql = "select username, passwordd from utilizador where userName =? and passwordd =? and statuss='activo'";

	private  Connection conn = null;
	private  PreparedStatement stmt;
	/**
	 * <h3>CallableStatement</h3>
	 * <p>
	 * A classe CallableStatement tem um poder maior de processamento,pois a app não
	 * sofre <br>
	 * todos os procedimentos como as view são processadas no proprio servidor
	 * mysql<br>
	 * com CallableStatement deminui os comandos sql's na app.
	 * </p>
	 * 
	 */
	private  CallableStatement cs = null;
	private  ResultSet rs = null;

	/**
	 * <h5>Esta funcao verifica se o utilizador existe ou nao</h5>
	 * 
	 * @return retorno- true ou false
	 * @param idFuncionario - que vem dentro do objecto
	 */ 
	public  boolean isUserNameRecorded(int idFuncionario) {
		boolean retorno = false;
		String sql = "SELECT idFuncionario FROM tbl_Utilizador WHERE idFuncionario ='" + idFuncionario + "'";
		
		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			retorno = rs.next();

		} catch (SQLException e) {
			
		}

		return retorno;
	}

	/**
	 * <h5>Esta funcao persiste um utilizador</h5>
	 * 
	 * @param u
	 * @see Utilizador
	 */
	public  void addUtilizador(Utilizador u) {

		try {
			LocalDate localDate = LocalDate.now();
			String dataRegisto = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);

			conn = Conexao.connect();
			stmt = conn.prepareStatement(INSERT);
			stmt.setInt(1, u.getFuncionario().getIdFuncionario());
			stmt.setString(2, u.getStatus());
			stmt.setString(3, u.getPerfil());
			stmt.setString(4, u.getUsername());
			stmt.setString(5, u.getPassword());
			stmt.setString(6, dataRegisto);
			stmt.executeUpdate();
		}
		catch(SQLException ex) {
			System.out.println("ERRO GRAVAR "+ex);
		}

			

	}

	/**
	 * <h5>Esta funcao elimina o utilizador</h5>
	 * 
	 * @param u
	 */
	public  void deleteUtilizador(Utilizador u) {

		try {
			conn = Conexao.connect();
			cs = conn.prepareCall(DELETE);
			cs.setInt(1, u.getIdUtilizador());
			cs.execute();
			
		}
		catch(SQLException ex) {
			System.out.println("ERRO apagar "+ex);
		}
	}

	/**
	 * <h5>Esta funcao actualiza o utilizador</h5>
	 * 
	 * @param u
	 */
	public  void updateUtilizador(Utilizador u) {
		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(UPDATE);

			stmt.setInt(1, u.getFuncionario().getIdFuncionario());
			stmt.setString(2, u.getStatus());
			stmt.setString(3, u.getPerfil());
			stmt.setString(4, u.getUsername());
			stmt.setString(5, u.getPassword());
			stmt.setInt(6, u.getIdUtilizador());
			stmt.executeUpdate();
			
			
		}

		catch (SQLException ex) {
			System.out.println("ERRO update "+ex);

		}

	}

	/**
	 * <h5>Esta funcao lista todos utilizadores cadastrados</h5>
	 * 
	 * @return retorno utilizadorList
	 * @param username
	 */
	public  List<Utilizador> getUtilizadorList(String username) {
		List<Utilizador> utilizadorList = new ArrayList<>();

		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement("select * from vw_listutilizador WHERE username like'" + username + "%'");
			rs = stmt.executeQuery();

			while (rs.next()) {
				Utilizador utilizador = new Utilizador();
				utilizador.setIdUtilizador(rs.getInt("idUtilizador"));
				Funcionario funcionario = new Funcionario();
				funcionario.setNome(rs.getString("nome"));
				utilizador.setFuncionario(funcionario);
				utilizador.setUsername(rs.getString("username"));
				utilizador.setStatus(rs.getString("status"));
				utilizador.setPerfil(rs.getString("perfil"));
				utilizadorList.add(utilizador);

			}
		} // closes try

		catch (SQLException ex) {
			
		} 
		return utilizadorList;
	}

	/**
	 * <h5>Esta funcao lista todos utilizadores cadastrados</h5>
	 * 
	 * @return retorno utilizadorList
	 * @param nome - recebe o nome 
	 */
	public  List<Utilizador> getAllFuncionario() {
		List<Utilizador> utilizadorList = new ArrayList<>();

		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(LIST);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Utilizador utilizador = new Utilizador();
				Funcionario funcionario = new Funcionario();
				funcionario.setNome(rs.getString("nome"));
				utilizador.setFuncionario(funcionario);
				utilizador.setIdUtilizador(rs.getInt("idUtilizador"));
				utilizador.setUsername(rs.getString("username"));
				utilizador.setStatus(rs.getString("status"));
				utilizador.setPerfil(rs.getString("perfil"));
				utilizadorList.add(utilizador);

			}
		} 

		catch (SQLException ex) {
			System.out.println("ERRO listar "+ex);
			}
		
		return utilizadorList;
	}
	
	//---------------------------entrada na app----------------------------------------------------------
    public  boolean consultar(String user, String pass) throws SQLException, ClassNotFoundException {

  		boolean autenticado = false;
  		Connection conn = null;
  		PreparedStatement stmt = null;
  		ResultSet rs = null;
  		try {
  			conn = Conexao.connect();
  			stmt = conn.prepareStatement(sql);
  			stmt.setString(1, user);
  			stmt.setString(2, pass);
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

  		finally {
  			rs.close();
  			stmt.close();
  		}
  	}
//----------------------------------------------------------------------------------------------------------
	 public Utilizador getUtilizadorById(int idUtilizador) {
			Utilizador utilizador = new Utilizador();
			try {
				
				stmt = conn.prepareStatement("SELECT * FROM vw_listutilizador WHERE idUtilizador=?");
				stmt.setInt(1, idUtilizador);
				 rs = stmt.executeQuery();

				if (rs.next()) {
					Funcionario funcionario = new Funcionario();
					funcionario.setNome(rs.getString("nome"));
					utilizador.setFuncionario(funcionario);
					utilizador.setIdUtilizador(rs.getInt("idUtilizador"));
					utilizador.setUsername(rs.getString("username"));
					utilizador.setStatus(rs.getString("status"));
					utilizador.setPerfil(rs.getString("perfil"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return utilizador;
		}



}
