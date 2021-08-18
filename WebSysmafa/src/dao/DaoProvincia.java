package dao;
/**
 * <h1>DaoProvincia</h1>
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

public class DaoProvincia {
	

	private  final String INSERT = "INSERT INTO tbl_provincia(nome,dataRegisto) values(?,?)";
	private  final String DELETE = "DELETE FROM tbl_provincia WHERE idProvincia=?";
	private  final String UPDATE = "UPDATE tbl_Provincia SET nome=? WHERE idProvincia=?";
	private  final String LIST = "select * from tbl_provincia;";

	private  Connection conn = null;
	private  ResultSet rs = null;
	// private  CallableStatement cs = null;
	private  PreparedStatement stmt;
	/**
	 * <h5>Esta funcao persiste uma provincia</h5>
	 * @param Provincia
	 * @see provincia
	 */
	public  void add(Provincia provincia) {

		try {
			LocalDate localDate = LocalDate.now();
			String dataRegisto = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);

			conn = Conexao.connect();
			stmt = conn.prepareStatement(INSERT);
			stmt.setString(1, provincia.getNome());
			stmt.setString(2, dataRegisto);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			System.out.println("ERROR FOUND DURING THE PROCESS OF SAVING "+ex);
		}
		
	}

	/**
	 * <h5>Esta funcao actualiza uma provincia</h5>
	 * @param Provincia
	 * @see provincia
	 */	public  void update(Provincia provincia) {

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(UPDATE);
			stmt.setString(1, provincia.getNome());
			stmt.setInt(2, provincia.getIdProvincia());
			stmt.executeUpdate();
		} catch (SQLException ex) {
			System.out.println("ERROR FOUND DURING THE PROCESS OF updating "+ex);

			
		} // fim do try

	}
	 /**
		 * <h5>Esta funcao remove uma provincia</h5>
		 * @param Provincia
		 * @see provincia
		 */
	public  void delete(Provincia provincia) {

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(DELETE);
			stmt.setInt(1, provincia.getIdProvincia());
			stmt.executeUpdate();

		} catch (SQLException ex) 
		{
			System.out.println("ERROR FOUND DURING THE PROCESS OF deleting "+ex);

		}

	}

	/**
	 * <h5>Esta funcao lista todas provincia</h5>
	 * @see provincia
	 * @return provincias - retorna uma lista de provincias
	 */	public  List<Provincia> getAllprovincia() {

		List<Provincia> provincias = new ArrayList<>();

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(LIST);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Provincia provincia = new Provincia();
				provincia.setIdProvincia(rs.getInt("idProvincia"));
				provincia.setNome(rs.getString("nome"));
				provincias.add(provincia);
			}
		}
		catch (SQLException ex) {
			System.out.println("ERROR FOUND DURING THE PROCESS OF listing "+ex);

			}

		

		return provincias;
	}
	/**
	 * <h5>Esta funcao procura Provincia cadastradas</h5>
	 * @see Provincia
	 * @return provincias- retorna uma lista de provincias
	 * @param nome- rece nome do da provincia
	 * 
	 */	public  List<Provincia> search(String nome) {

		List<Provincia> provincias = new ArrayList<>();

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement("SELECT * FROM vw_listprovincia WHERE nome LIKE'%"+nome+"'");
			rs = stmt.executeQuery();

			while (rs.next()) {
				Provincia provincia = new Provincia();
				provincia.setNome(rs.getString("nome"));
				provincias.add(provincia);
			}

		} catch (SQLException ex) {
		
			}

		return provincias;
	}
/*=========================================================================================================*/
	 public Provincia getProvinciaById(int idProvincia) {
			Provincia provincia = new Provincia();
			try {
				
				stmt = conn.prepareStatement("select idProvincia,nome from tbl_Provincia where idProvincia=?");
				stmt.setInt(1, idProvincia);
				 rs = stmt.executeQuery();

				if (rs.next()) {
					provincia.setIdProvincia(rs.getInt("idProvincia"));
					provincia.setNome(rs.getString("nome"));

				}
			} catch (SQLException e) {
				System.out.println("ERROR FOUND DURING THE PROCESS OF GET BY ID "+e);

			}

			return provincia;
		}

}
