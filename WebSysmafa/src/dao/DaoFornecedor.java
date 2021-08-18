
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import util.Conexao;
import model.*;

/**
 * <h1>DaoFornecedor</h1>
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
public class DaoFornecedor {
	/**
	 * <h4>Alert</h4>
	 * <p>
	 * A classe <b>Alert</b> é do javafx equivalente ao JOPtionPane do swing<br>
	 * com ela pode se ter altertas tipos diferentes
	 * </p>
	 */
	
	/** String que sao usadas para operacoes com base de dados */

	private  final String INSERT = "INSERT INTO tbl_fornecedor(nome,apelido,genero,email,telefone,endereco,dataRegisto) VALUES(?,?,?,?,?,?,?)";
	private  final String LIST = "select * from tbl_fornecedor";
	private  final String DELETE = "DELETE FROM tbl_fornecedor WHERE idFornecedor=?";
	private  final String UPDATE = "UPDATE tbl_fornecedor SET nome=?,apelido=?,genero=?,email=?,telefone=?,endereco=? WHERE idFornecedor=?";

	private  Connection conn = null;
	private  PreparedStatement stmt = null;
	// private  CallableStatement cs=null;
	private  ResultSet rs = null;

	/**
	 * <h5>Esta funcao persiste um DaoFornecedor</h5>
	 * 
	 * @param DaoFornecedor
	 * @see DaoFornecedor
	 */
	public  boolean isRecorder(String telefone,String email) {
		boolean retorno = false;

		String sql = "SELECT telefone,email FROM tbl_fornecedor WHERE telefone ='" + telefone + "' OR '"+email+"'";

		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			retorno = rs.next();
		}
		catch (SQLException e) {
			
			}
		return retorno;
	}

	/**
	 * <h5>Esta funcao persiste um Fornecedor</h5>
	 * 
	 * @param fornecedor
	 * @see Fornecedor
	 */
	public  void addFornecedor(Fornecedor fornecedor) {

		try {
			LocalDate localDate = LocalDate.now();
			String dataRegisto = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);
			conn = Conexao.connect();

			stmt = conn.prepareStatement(INSERT);
			stmt.setString(1, fornecedor.getNome());
			stmt.setString(2, fornecedor.getApelido());
			stmt.setString(3, fornecedor.getGenero());
			stmt.setString(4, fornecedor.getEmail());
			stmt.setString(5, fornecedor.getTelefone());
			stmt.setString(6, fornecedor.getEndereco());
			stmt.setString(7, dataRegisto);
			stmt.executeUpdate();
		} 
		catch (SQLException ex) {
		}

	}

	/**
	 * <h5>Esta funcao elimina o Fornecedor</h5>
	 * 
	 * @param fornecedor
	 * @see Fornecedor
	 */
	public  void deleteFornecedor(Fornecedor fornecedor) {

		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(DELETE);
			stmt.setInt(1, fornecedor.getIdFornecedor());
			stmt.execute();
			

		} catch (SQLException e) {
		}
	}

	/**
	 * <h5>Esta funcao actualiza Fornecedor cadastrados</h5>
	 * 
	 * @see Funcionario
	 * 
	 */
	public  void updateFornecedor(Fornecedor fornecedor) {

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(UPDATE);
			stmt.setString(1, fornecedor.getNome());
			stmt.setString(2, fornecedor.getApelido());
			stmt.setString(3, fornecedor.getGenero());
			stmt.setString(4, fornecedor.getEmail());
			stmt.setString(5, fornecedor.getTelefone());
			stmt.setString(6, fornecedor.getEndereco());
			stmt.setInt(7, fornecedor.getIdFornecedor());
			stmt.executeUpdate();
			
		}

		catch (SQLException ex) {
			
		}

	}

	/**
	 * <h5>Esta funcao lista todos Fornecedor cadastrados</h5>
	 * 
	 * @see Fornecedor
	 * @return retorno Fornecedores
	 * 
	 */
	public  List<Fornecedor> getAllFornecedor() {
		List<Fornecedor> fornecedores = new ArrayList<>();

		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(LIST);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Fornecedor fornecedor = new Fornecedor();

				
				fornecedor.setIdFornecedor(rs.getInt("idFornecedor"));
				fornecedor.setNome(rs.getString("nome"));
				fornecedor.setEmail(rs.getString("email"));
				fornecedor.setTelefone(rs.getString("telefone"));
				fornecedor.setEndereco(rs.getString("endereco"));
				fornecedores.add(fornecedor);

			}

		} catch (SQLException ex) {

		}

		return fornecedores;
	}

	/**
	 * <h5>Esta funcao procura Fornecedor cadastrados</h5>
	 * 
	 * @see Fornecedor
	 * @return retorno Fornecedores
	 * 
	 */
	public  List<Fornecedor> search(String nome) {
		List<Fornecedor> fornecedores = new ArrayList<>();

		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement("SELECT * FROM vw_listFornecedor WHERE nome LIKE'%" + nome + "%'");
			rs = stmt.executeQuery();

			while (rs.next()) {
				Fornecedor fornecedor = new Fornecedor();
				fornecedor.setIdFornecedor(rs.getInt("idFornecedor"));
				fornecedor.setNome(rs.getString("nome"));
				fornecedor.setEmail(rs.getString("email"));
				fornecedor.setTelefone(rs.getString("telefone"));
				fornecedor.setEndereco(rs.getString("endereco"));
				fornecedores.add(fornecedor);

			}

		} catch (SQLException ex) {
			
		}

		return fornecedores;
	}
//==============================================================================
	public Fornecedor getFornecedorById(int idFornecedor) {
		Fornecedor fornecedor = new Fornecedor();
		try {
			
			stmt = conn.prepareStatement("select idFornecedor,nome,email,telefone,endereco FROM tbl_fornecedor where idFornecedor=?");
			stmt.setInt(1, idFornecedor);
			 rs = stmt.executeQuery();

			if (rs.next()) {
				fornecedor.setIdFornecedor(rs.getInt("idFornecedor"));
				fornecedor.setNome(rs.getString("nome"));
				fornecedor.setEmail(rs.getString("email"));
				fornecedor.setTelefone(rs.getString("telefone"));
				fornecedor.setEndereco(rs.getString("endereco"));


			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return fornecedor;
	}

}
