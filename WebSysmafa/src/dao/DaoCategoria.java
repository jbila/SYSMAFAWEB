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
import model.Categoria;
import model.Utilizador;

/**
 * <h1>DaoCategoria</h1>
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
public class DaoCategoria {
	/**
	 * <h4>Alert</h4>
	 * <p>
	 * A classe <b>Alert</b> é do javafx equivalente ao JOPtionPane do swing<br>
	 * com ela pode se ter altertas tipos diferentes
	 * </p>
	 */

	private  final String INSERT = "INSERT INTO tbl_categoria(nome,descricao,dataRegisto) values(?,?,?)";
	private  final String DELETE = "DELETE FROM tbl_categoria WHERE idCategoria=?";
	private  final String UPDATE = "UPDATE tbl_categoria SET nome=?,descricao=? WHERE idCategoria=?";
	private  final String LIST = "select * from tbl_categoria";

	private Connection conn = null;
	private ResultSet rs = null;
	// private static CallableStatement cs = null;
	private  PreparedStatement stmt;

	/**
	 * Este metodo insere um objecto do tipo categoria na base de dado
	 * 
	 * @param categoria- recebe um objecto
	 * @exception SQLException -esta funcao pode geral excecpoes desse tipo
	 * 
	 */
	public void add(Categoria categoria) {

		try {
			LocalDate localDate = LocalDate.now();
			String dataRegisto = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);

			conn = Conexao.connect();
			stmt = conn.prepareStatement(INSERT);
			stmt.setString(1, categoria.getNome());
			stmt.setString(2, categoria.getDescricao());
			stmt.setString(3, dataRegisto);
			stmt.executeUpdate();

		} catch (SQLException ex) {
				ex.printStackTrace();
		} 

	}

	// ========================Update=================================
	/**
	 * Esta funcao actualiza categoria
	 * 
	 * @param categoria - recebe um objecto que contema os dados alterados
	 * @exception SQLException
	 * @see Categoria
	 */
	public void update(Categoria categoria) {

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(UPDATE);
			stmt.setString(1, categoria.getNome());
			stmt.setString(2, categoria.getDescricao());
			stmt.setInt(3, categoria.getIdCategoria());
			stmt.executeUpdate();

		} 
		catch (SQLException ex) {
			ex.printStackTrace();
		}


	}

	// =======================Delete=================================
	/**
	 * Esta funcao permite a remocao de uma tupla
	 * 
	 * @param categoria- vai conter o id para a remocao
	 * @see Categoria
	 */

	public void delete(Categoria categoria) {

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(DELETE);
			stmt.setInt(1, categoria.getIdCategoria());
			stmt.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();

		}

	}

	/**
	 * <h5>Esta funcao lista Categorias cadastradas</h5>
	 * 
	 * @see Categoria
	 * @return categorias- retorna uma lista de categorias
	 * 
	 */
	public List<Categoria> getAllCategoria() {

		List<Categoria> categorias = new ArrayList<>();

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(LIST);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Categoria categoria = new Categoria();
				categoria.setIdCategoria(rs.getInt("idCategoria"));
				categoria.setNome(rs.getString("nome"));
				categoria.setDescricao(rs.getString("descricao"));
				categorias.add(categoria);

			}

		} catch (SQLException ex) {
			ex.printStackTrace();

		} 

		return categorias;
	}

	/**
	 * <h5>Esta funcao procura Categorias cadastradas</h5>
	 * 
	 * @see Categoria
	 * @return categorias- retorna uma lista de categorias
	 * @param nome- recebe nome da categoria
	 * 
	 */
	public List<Categoria> search(String nome) {

		List<Categoria> categorias = new ArrayList<>();

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement("SELECT * FROM vw_listcategoria WHERE nome LIKE'%" + nome + "'");
			rs = stmt.executeQuery();

			while (rs.next()) {
				Categoria categoria = new Categoria();

				Utilizador utilizador = new Utilizador();
				utilizador.setUsername(rs.getString("username"));

				categoria.setIdCategoria(rs.getInt("idCategoria"));
				categoria.setNome(rs.getString("nome"));
				categoria.setDescricao(rs.getString("descricao"));
				categoria.setUtilizador(utilizador);
				categorias.add(categoria);

			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return categorias;
	}

// =============CONTROLO DE EXISTENCIA DA   CATEGORIA NA BASE DE DADOS==================

	public boolean isRecorded(String nome) {
		boolean retorno = false;

		String sql = "SELECT nome FROM tbl_categoria WHERE nome ='" + nome + "'";

		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			retorno = rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorno;
	}

//========================GET BY ID============================
	public Categoria getCategoriaById(int idCategoria) {
		Categoria categoria = new Categoria();
		try {
			
			stmt = conn.prepareStatement("select idCategoria,nome,descricao from tbl_categoria where idCategoria=?");
			stmt.setInt(1, idCategoria);
			 rs = stmt.executeQuery();

			if (rs.next()) {
				categoria.setIdCategoria(rs.getInt("idCategoria"));
				categoria.setNome(rs.getString("nome"));
				categoria.setDescricao(rs.getString("descricao"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return categoria;
	}
}
