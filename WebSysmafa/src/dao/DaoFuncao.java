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
 * <h1>DaoFuncao</h1>
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
public class DaoFuncao {
	/**
	 * <h4>Alert</h4>
	 * <p>
	 * A classe <b>Alert</b> é do javafx equivalente ao JOPtionPane do swing<br>
	 * com ela pode se ter altertas tipos diferentes
	 * </p>
	 */
	

	private  final String INSERT = "INSERT INTO tbl_funcao(nome,dataRegisto) values(?,?)";
	private  final String DELETE = "DELETE FROM tbl_funcao WHERE idFuncao=?";
	private  final String UPDATE = "UPDATE tbl_funcao SET nome=? WHERE idFuncao=?";
	private  final String LIST = "SELECT * from tbl_funcao";

	private  Connection conn = null;
	private  ResultSet rs = null;
	// private  CallableStatement cs = null;
	private  PreparedStatement stmt;
	/**
	 * <h5>Esta funcao persiste um Funcao</h5>
	 * @param funcao -recebe um objecto do tipo funcao
	 * @see Funcao
	 */
	public  void add(Funcao funcao) {

		try {
			LocalDate localDate = LocalDate.now();
			String dataRegisto = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);

			conn = Conexao.connect();
			stmt = conn.prepareStatement(INSERT);
			stmt.setString(1, funcao.getNome());
			stmt.setString(2, dataRegisto);
			stmt.executeUpdate();	
		} 
		catch (SQLException ex) {
			ex.printStackTrace();
		} 

	}

	/**
	 * <h5>Esta funcao actualiza funcao existente </h5>
	 * @see Funcao
	 * @param nome- recebe nome da funcao com os dados a serem alterados
	 * 
	 */	public  void update(Funcao funcao) {
		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(UPDATE);
			stmt.setString(1, funcao.getNome());
			stmt.setInt(2, funcao.getIdFuncao());
			stmt.executeUpdate();
		} 
		catch (SQLException ex) {
			ex.printStackTrace();
		} 

	}
	/**
	 * <h5>Esta funcao remove uma Funcao cadastrada</h5>
	 * @see Funcao
	 * @param nome- recebe nome da funcao atraves do objecto
	 * 
	 */
	public  void delete(Funcao funcao) {
		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(DELETE);
			stmt.setInt(1, funcao.getIdFuncao());
			stmt.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
		} 
	}

	/**
	 * <h5>Esta funcao lista Funcoes cadastradas</h5>
	 * @see Funcao
	 * @return funcoes- retorna uma lista de funcoes
	 * 
	 */	public  List<Funcao> getAllFuncao() {
		List<Funcao> funcoes = new ArrayList<>();
		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(LIST);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Funcao funcao=new Funcao();
				funcao.setIdFuncao(rs.getInt("idFuncao"));
				funcao.setNome(rs.getString("nome"));
				funcoes.add(funcao);
			}
			} 
		catch (SQLException ex) {
			ex.printStackTrace();
		} 

		return funcoes;
	}

	/**
	 * <h5>Esta funcao procura Funcoes cadastradas</h5>
	 * @see Funcao
	 * @return funcoes- retorna uma lista de funcoes
	 * @param nome- recebe nome da funcao
	 * 
	 */	public  List<Funcao> search(String nome) {
		List<Funcao> funcoes = new ArrayList<>();
		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement("SELECT * FROM tbl_funcao where nome like '"+nome+"'");
			rs = stmt.executeQuery();

			while (rs.next()) {
				Funcao funcao=new Funcao();
				funcao.setIdFuncao(rs.getInt("idFuncao"));
				funcao.setNome(rs.getString("nome"));
				funcoes.add(funcao);
			}
			}
		catch (SQLException ex) {
			ex.printStackTrace();
		} 

		return funcoes;
		}
//-----------------------get by id----------------------------------------------------------
		public Funcao getFuncaoById(int idFuncao) {
			Funcao funcao = new Funcao();
			try {
				
				stmt = conn.prepareStatement("select * from tbl_Funcao where idFuncao=?");
				stmt.setInt(1, idFuncao);
				 rs = stmt.executeQuery();

				if (rs.next()) {
					funcao.setIdFuncao(rs.getInt("idFuncao"));
					funcao.setNome(rs.getString("nome"));
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return funcao;
		}


}
