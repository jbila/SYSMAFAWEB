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
 * <h1>DaoFuncionario</h1>
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
public class DaoFuncionario {
	/**
	 * <h4>Alert</h4>
	 * <p>
	 * A classe <b>Alert</b> é do javafx equivalente ao JOPtionPane do swing<br>
	 * com ela pode se ter altertas tipos diferentes
	 * </p>
	 */
	
	/** String que sao usadas para operacoes com base de dados */
	private  final String INSERT = "INSERT INTO tbl_Funcionario(nome,apelido,genero,email,telefone,endereco,nuit,numeroBi,idDistrito,idFuncao,salario,dataRegisto) values(?,?,?,?,?,?,?,?,?,?,?,?)";
	private  final String DELETE = "DELETE FROM tbl_Funcionario WHERE idFuncionario=?";
	private  final String UPDATE = "UPDATE tbl_Funcionario SET nome=?,apelido=?,genero=?,email=?,telefone=?,endereco=?,nuit=?,numeroBi=?,idDistrito=?,idFuncao=?,salario=? WHERE idFuncionario=?";
	private  final String LIST = "SELECT * from vw_listFuncionario";

	private  Connection conn = null;
	private  ResultSet rs = null;
	// private  CallableStatement cs = null;
	private  PreparedStatement stmt;

	/**
	 * <h5>Esta funcao persiste um Funcionario</h5>
	 * 
	 * @param funcionario
	 * @see Funcionario
	 */
	public  void add(Funcionario funcionario) {

		try {
			LocalDate localDate = LocalDate.now();
			String dataRegisto = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);
			conn = Conexao.connect();
			stmt = conn.prepareStatement(INSERT);
			stmt.setString(1, funcionario.getNome());
			stmt.setString(2, funcionario.getApelido());
			stmt.setString(3, funcionario.getGenero());
			stmt.setString(4, funcionario.getEmail());
			stmt.setString(5, funcionario.getTelefone());
			stmt.setString(6, funcionario.getEndereco());
			stmt.setInt(7, funcionario.getNuit());
			stmt.setString(8, funcionario.getNumeroBi());
			stmt.setInt(9, funcionario.getDistrito().getIdDistrito());
			stmt.setInt(10, funcionario.getFuncao().getIdFuncao());
			stmt.setDouble(11, funcionario.getSalario());
			stmt.setString(12, dataRegisto);
			stmt.executeUpdate();
			
		} catch (SQLException ex) {
			System.out.println("Erro encontrado ao fazer Insert "+ex);

			}
		
	}

	/**
	 * <h5>Esta funcao elimina o Funcionario</h5>
	 * 
	 * @param funcionario
	 * @see Funcionario
	 */
	public  void update(Funcionario funcionario) {

		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(UPDATE);
			stmt.setString(1, funcionario.getNome());
			stmt.setString(2, funcionario.getApelido());
			stmt.setString(3, funcionario.getGenero());
			stmt.setString(4, funcionario.getEmail());
			stmt.setString(5, funcionario.getTelefone());
			stmt.setString(6, funcionario.getEndereco());
			stmt.setInt(7, funcionario.getNuit());
			stmt.setString(8, funcionario.getNumeroBi());
			stmt.setInt(9, funcionario.getDistrito().getIdDistrito());
			stmt.setInt(10, funcionario.getFuncao().getIdFuncao());
			stmt.setDouble(11, funcionario.getSalario());
			stmt.setInt(12, funcionario.getIdFuncionario());
			System.out.println(" ANTES DE SER SALVO"+funcionario.toString());
			stmt.executeUpdate();

		} catch (SQLException ex) {
			
			System.out.println("Erro encontrado ao fazer update "+ex);
		} // fim do try

	}

	// =======================Delete=================================
	/**
	 * <h5>Esta funcao lista todos utilizadores cadastrados</h5>
	 * 
	 * @return retorno utilizadorList
	 * @param username
	 */
	public  void delete(Funcionario funcionario) {

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(DELETE);
			stmt.setInt(1, funcionario.getIdFuncionario());
			stmt.executeUpdate();

		} catch (SQLException ex) {
			
		} // fim do try

	}

	/**
	 * <h5>Esta funcao lista todos Funcionarios cadastrados</h5>
	 * 
	 * @see Funcionario
	 * @return retorno funcionarios
	 * 
	 */
	public  List<Funcionario> getAllFuncionario() {

		List<Funcionario> funcionarios = new ArrayList<>();

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(LIST);
			rs = stmt.executeQuery();

			while (rs.next()) {
				// nome,apelido,genero,email,telefone,endereco,nuit,numeroBi,idDistrito,idFuncao,salario,dataRegisto
				Funcionario funcionario = new Funcionario();
				Funcao funcao = new Funcao();
				funcao.setNome(rs.getString("Funcao"));
				Distrito distrito = new Distrito();
				distrito.setNome(rs.getString("Distrito"));
				funcionario.setIdFuncionario(rs.getInt("idFuncionario"));
				funcionario.setNome(rs.getString("nome"));
				funcionario.setApelido(rs.getString("apelido"));
				funcionario.setGenero(rs.getString("genero"));
				funcionario.setEmail(rs.getString("email"));
				funcionario.setTelefone(rs.getString("telefone"));
				funcionario.setEndereco(rs.getString("endereco"));
				funcionario.setNuit(rs.getInt("nuit"));
				funcionario.setNumeroBi(rs.getString("numeroBi"));
				funcionario.setSalario(rs.getDouble("salario"));
				funcionario.setDataRegisto(rs.getString("dataRegisto"));
				funcionario.setFuncao(funcao);
				funcionario.setDistrito(distrito);
				funcionarios.add(funcionario);

			}

		} catch (SQLException ex) {
			
			System.out.println("Erro de listagem "+ex);
			}

		

		return funcionarios;
	}

	/**
	 * <h5>Esta funcao pesquisa Funcionarios cadastrados</h5>
	 * 
	 * @param nome
	 * @return retorno funcionarios
	 * 
	 */
	public  List<Funcionario> search(String nome) {

		List<Funcionario> funcionarios = new ArrayList<>();

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement("SELECT * from vw_listFuncionario WHERE nome LIKE '"+nome+"%' ");
			rs = stmt.executeQuery();

			while (rs.next()) {
				Funcionario funcionario = new Funcionario();
				Funcao funcao = new Funcao();
				funcao.setNome(rs.getString("Funcao"));
				Distrito distrito = new Distrito();
				distrito.setNome(rs.getString("Distrito"));
				funcionario.setIdFuncionario(rs.getInt("idFuncionario"));
				funcionario.setNome(rs.getString("Funcionario"));
				funcionario.setApelido(rs.getString("apelido"));
				funcionario.setGenero(rs.getString("genero"));
				funcionario.setEmail(rs.getString("email"));
				funcionario.setTelefone(rs.getString("telefone"));
				funcionario.setEndereco(rs.getString("endereco"));
				funcionario.setNuit(rs.getInt("nuit"));
				funcionario.setNumeroBi(rs.getString("numeroBi"));
				funcionario.setSalario(rs.getDouble("salario"));
				funcionario.setDataRegisto(rs.getString("dataRegisto"));
				funcionario.setFuncao(funcao);
				funcionario.setDistrito(distrito);
				funcionarios.add(funcionario);

			}

		} catch (SQLException ex) {
			
		} 

		return funcionarios;
	}
	/*-------------------------------get by id---------------------------------------------*/
	public Cliente getClienteById(int idCliente) {
		Cliente cliente = new Cliente();
		try {
			
			stmt = conn.prepareStatement("select * from tbl_Cliente where idCliente=?");
			stmt.setInt(1, idCliente);
			 rs = stmt.executeQuery();

			if (rs.next()) {
				
				cliente.setIdCliente(rs.getInt("idCliente"));
				cliente.setNome(rs.getString("nome"));
				cliente.setApelido(rs.getString("apelido"));
				cliente.setGenero(rs.getString("genero"));
				cliente.setEmail(rs.getString("email"));
				cliente.setTelefone(rs.getString("telefone"));
				cliente.setEndereco(rs.getString("endereco"));
				cliente.setDataRegisto(rs.getString("dataRegisto"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cliente;
	}
	/*-------------------------------get by id---------------------------------------------*/
	public 	 Funcionario  getFuncionarioById(int idFuncionario ) {
		 Funcionario  	 funcionario  = new 	 Funcionario ();
		try {
			
			stmt = conn.prepareStatement("SELECT * from vw_listFuncionario WHERE idFuncionario =?");
			stmt.setInt(1, idFuncionario);
			 rs = stmt.executeQuery();

			if (rs.next()) {
				
			
				Funcao funcao = new Funcao();
				funcao.setNome(rs.getString("Funcao"));
				Distrito distrito = new Distrito();
				distrito.setNome(rs.getString("Distrito"));
				funcionario.setIdFuncionario(rs.getInt("idFuncionario"));
				funcionario.setNome(rs.getString("nome"));
				funcionario.setApelido(rs.getString("apelido"));
				funcionario.setGenero(rs.getString("genero"));
				funcionario.setEmail(rs.getString("email"));
				funcionario.setTelefone(rs.getString("telefone"));
				funcionario.setEndereco(rs.getString("endereco"));
				funcionario.setNuit(rs.getInt("nuit"));
				funcionario.setNumeroBi(rs.getString("numeroBi"));
				funcionario.setSalario(rs.getDouble("salario"));
				funcionario.setDataRegisto(rs.getString("dataRegisto"));
				funcionario.setFuncao(funcao);
				funcionario.setDistrito(distrito);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 	 funcionario ;
	}


}
