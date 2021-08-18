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
import model.Distrito;
import model.Provincia;

public class DaoDistrito {

	 

	private  final String INSERT = "INSERT INTO tbl_distrito(nome,idProvincia,dataRegisto) values(?,?,?)";
	private  final String DELETE = "DELETE FROM tbl_distrito WHERE idDistrito=?";
	private  final String UPDATE = "UPDATE tbl_distrito SET  SET idProvincia=?,nome=? WHERE idDistrito=?";
	private  final String LIST = "SELECT * from vw_listDistrito LIMIT 17";

	private  Connection conn = null;
	private  ResultSet rs = null;
	// private  CallableStatement cs = null;
	private  PreparedStatement stmt;

	public  void add(Distrito distrito) {

		try {
			LocalDate localDate = LocalDate.now();
			String dataRegisto = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);

			conn = Conexao.connect();
			stmt = conn.prepareStatement(INSERT);
			stmt.setString(1, distrito.getNome());
			stmt.setInt(2, distrito.getProvincia().getIdProvincia());
			stmt.setString(3, dataRegisto);
			stmt.executeUpdate();
			
		} catch (SQLException ex) {
			System.out.println("ERROR ADICIONANDO "+ex);
		} 

	}

	// ========================Update=================================
	public  void update(Distrito distrito) {

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(UPDATE);
			stmt.setInt(1, distrito.getProvincia().getIdProvincia());
			stmt.setString(2, distrito.getNome());
			stmt.setInt(3, distrito.getIdDistrito());
			stmt.executeUpdate();

		} catch (SQLException ex) {
			System.out.println("ERROR Actualizando "+ex);

					} 

	}
	// =======================Delete=================================

	public  void delete(Distrito distrito) {

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(DELETE);
			stmt.setInt(1, distrito.getIdDistrito());
			stmt.executeUpdate();

		} catch (SQLException ex) {
			System.out.println("ERROR Apagando "+ex);

		} 

	}

//========================================================================================================    
	public  List<Distrito> getAlldistrito() {

		List<Distrito> distritos = new ArrayList<>();

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(LIST);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Distrito distrito = new Distrito();
				Provincia provincia = new Provincia();
				provincia.setNome(rs.getString("Provincia"));
				distrito.setIdDistrito(rs.getInt("idDistrito"));
				distrito.setNome(rs.getString("Distrito"));
				distrito.setProvincia(provincia);
				distritos.add(distrito);

			}
		}

		catch (SQLException ex) {
			
			System.out.println("ERROR Listando "+ex);
			}
		return distritos;
	}

	// ---------------------------------------------------------------------------------
	public  List<Distrito> search(Provincia nome) {

		List<Distrito> distritos = new ArrayList<>();

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement("SELECT * FROM vw_listdistrito WHERE provincia ='" + nome + "'");
			rs = stmt.executeQuery();

			while (rs.next()) {
				Distrito distrito = new Distrito();
				Provincia provincia = new Provincia();
				provincia.setNome(rs.getString("Provincia"));
				distrito.setIdDistrito(rs.getInt("idDistrito"));
				distrito.setNome(rs.getString("Distrito"));
				distrito.setProvincia(provincia);
				distritos.add(distrito);


			}

		} catch (SQLException ex) {
			System.out.println("ERROR procurando "+ex);

		} 

		return distritos;
	}
	public Distrito getDistritoById(int idDistrito) {
		Distrito distrito = new Distrito();
		try {
			
			stmt = conn.prepareStatement("SELECT * FROM vw_listdistrito WHERE idDistrito=?");
			stmt.setInt(1, idDistrito);
			 rs = stmt.executeQuery();

			if (rs.next()) {
				
				Provincia provincia = new Provincia();
				provincia.setNome(rs.getString("Provincia"));
				
				distrito.setIdDistrito(rs.getInt("idDistrito"));
				distrito.setNome(rs.getString("Distrito"));
				distrito.setProvincia(provincia);	

			}
		} catch (SQLException e) {
			System.out.println("ERRO FOUND LISTING DISTRICT "+e);
			e.printStackTrace();
		}

		return distrito;
	}
}
