
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import model.Cliente;
import model.Distrito;
import util.Conexao;


/**
 * <h1>DaoCliente</h1>
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
public class DaoCliente {
	/**
	 * <h4>Alert</h4>
	 * <p>
	 * A classe <b>Alert</b> é do javafx equivalente ao JOPtionPane do swing<br>
	 * com ela pode se ter altertas tipos diferentes
	 * </p>
	 */
	

	private  final String INSERT = "INSERT INTO tbl_cliente(nome,apelido,genero,email,telefone,endereco,idDistrito,dataRegisto) VALUES(?,?,?,?,?,?,?,?)";
	private  final String LIST = "SELECT * FROM vw_listAllCliente ORDER BY idCliente DESC";
	private  final String DELETE = "DELETE FROM tbl_cliente WHERE idCliente=?";
	private  final String UPDATE = "UPDATE tbl_cliente SET nome=?,apelido=?,genero=?,email=?,telefone=?,endereco=?,idDistrito=? WHERE idCliente=? ";

	private  Connection conn = null;
	private  PreparedStatement stmt;
	private  ResultSet rs = null;

	public  boolean isRecorded(String telefone, String email) {
		boolean retorno = false;
		String sql = "SELECT telefone,email FROM tbl_cliente WHERE telefone ='" + telefone + "'OR email= '" + email + "'";

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
	 * <h5>Esta funcao persiste um Cliente</h5>
	 * @param cliente
	 * @see Cliente
	 */
	public  void addCliente(Cliente cliente) {

		try {
			LocalDate localDate = LocalDate.now();
			String dataRegisto = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);

			conn = Conexao.connect();
			stmt = conn.prepareStatement(INSERT);
			// nome,apelido,genero,email,telefone,endereco,idUtilizador,dataRegisto
			stmt.setString(1, cliente.getNome());
			stmt.setString(2, cliente.getApelido());
			stmt.setString(3, cliente.getGenero());System.out.println("Genero" +cliente.getGenero());
			stmt.setString(4, cliente.getEmail());
			stmt.setString(5, cliente.getTelefone());
			stmt.setString(6, cliente.getEndereco());
			stmt.setInt(7, cliente.getDistrito().getIdDistrito());
			stmt.setString(8, dataRegisto);
			stmt.executeUpdate();
			
		} catch (SQLException ex) {
			System.out.println("ERRO CCURRED DURING THE PROCESS OF SAVING DATA"+ex);
		}

	}

	/**
	 * <h5>Esta funcao elimina o Cliente</h5>
	 * @param cliente
	 * @see Cliente
	 */
	public  void deleteCliente(Cliente cliente) {
		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(DELETE);
			stmt.setInt(1, cliente.getIdCliente());
			stmt.execute();
			
		} catch (SQLException e) {
			System.out.println("ERRO DELITING"+e);

		}
			
	}

	/**
	 * <h5>Esta funcao actualiza  Cliente</h5>
	 * @param cliente - recebe um objecto to tipo cliente
	 * @see Cliente
	 * 
	 */
	public  void updateCliente(Cliente cliente) {
		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(UPDATE);
			stmt.setString(1, cliente.getNome());
			stmt.setString(2, cliente.getApelido());
			stmt.setString(3, cliente.getGenero());
			stmt.setString(4, cliente.getEmail());
			stmt.setString(5, cliente.getTelefone());
			stmt.setString(6, cliente.getEndereco());
			stmt.setInt(7, cliente.getDistrito().getIdDistrito());
			stmt.setInt(8, cliente.getIdCliente());
			stmt.executeUpdate();	
		}

		catch (SQLException ex) {
			System.out.println("ERRO CCURRED DURING THE PROCESS OF UPDATING"+ex);	
		}
	}

	/**
	 * <h5>Esta funcao procura Cliente cadastrados</h5>
	 * @see Cliente
	 * @return retorno clientes- lista de clientes
	 * 
	 */
	public  List<Cliente> getAllCliente() {
		List<Cliente> clientes = new ArrayList<Cliente>();
		try {
//String list="SELECT C.idCliente,C.nome,C.apelido,C.genero,C.email,C.telefone,C.endereco,D.nome Distrito FROM tbl_cliente C INNER JOIN tbl_distrito D ON C.idDistrito=D.idDistrito";
			conn = Conexao.connect();
			stmt = conn.prepareStatement(LIST);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Cliente cliente = new Cliente();
				Distrito distrito = new Distrito();
				distrito.setNome(rs.getString("Distrito"));
				cliente.setIdCliente(rs.getInt("idCliente"));
				cliente.setNome(rs.getString("nome"));
				cliente.setApelido(rs.getString("apelido"));
				cliente.setGenero(rs.getString("genero"));
				cliente.setEmail(rs.getString("email"));
				cliente.setTelefone(rs.getString("telefone"));
				cliente.setDistrito(distrito);
				cliente.setEndereco(rs.getString("endereco"));
				//cliente.setDataRegisto(rs.getString("dataRegisto"));
				clientes.add(cliente);
			}

		} catch (SQLException ex) {
				System.out.println("ERRO"+ex);
			}
		

		return clientes;
	}

	/**
	 * <h5>Esta funcao procura Clientes cadastrados</h5>
	 * @see Cliente
	 * @return clientes- retorna uma lista de clientes
	 * @param nome- rece nome do cliente
	 * 
	 */
	public  List<Cliente> search(String nome) {
		List<Cliente> clientes = new ArrayList<Cliente>();
		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement("SELECT * FROM vw_listAllCliente WHERE nome LIKE '" + nome + "%' ");
			rs = stmt.executeQuery();

			while (rs.next()) {

				// nome,apelido,genero,email,telefone,endereco,idUtilizador
				Cliente cliente = new Cliente();
				cliente.setIdCliente(rs.getInt("idCliente"));
				cliente.setNome(rs.getString("nome"));
				cliente.setApelido(rs.getString("apelido"));
				cliente.setGenero(rs.getString("genero"));
				cliente.setEmail(rs.getString("email"));
				cliente.setTelefone(rs.getString("telefone"));
				cliente.setEndereco(rs.getString("endereco"));
				cliente.setDataRegisto(rs.getString("dataRegisto"));
				clientes.add(cliente);
			}

		} catch (SQLException ex) {
			
		}

		return clientes;
	}
/*-------------------------------get by id----------------------------------------------------------*/
		public Cliente getClienteById(int idCliente) {
			Cliente cliente = new Cliente();
			try {
				
				stmt = conn.prepareStatement("SELECT * FROM vw_listAllCliente WHERE idCliente=?");
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
				System.out.println("ERROR FOUND "+e);
			}

			return cliente;
		}

}
