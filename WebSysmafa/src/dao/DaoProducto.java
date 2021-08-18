package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import util.Conexao;
import model.*;;

public class DaoProducto {

	 
	private  final String INSERT = "INSERT INTO tbl_producto(nome,codigo,descricao,quantidade,idCategoria,idUtilizador,dataRegisto) VALUES(?,?,?,?,?,?,?)";
	//private  final String LIST = "SELECT * FROM vw_listProducto";
	private  final String DELETE = "{CALL sp_Delete_Producto(?)}";
	private  final String UPDATE = "UPDATE tbl_producto SET nome=?,codigo=?,descricao=?,idCategoria=? WHERE idProducto=?";

	private  Connection conn = null;
	private  CallableStatement cs = null;
	private  ResultSet rs = null;
	private  PreparedStatement stmt;

	public  boolean isRecorded(String codigo) {
		boolean retorno = false;
		final String sql = "SELECT codigo FROM tbl_producto WHERE nome ='" + codigo + "'";

		try {
			conn = Conexao.connect();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			retorno = rs.next();

		} catch (SQLException e) {

			

		}

		return retorno;
	}
// ---------------------------------------------------------------------------------------------------

	public  int add(Producto producto) {
		int lastId=0;
		try {
			LocalDate localDate = LocalDate.now();
			String dataRegisto = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);
			conn = Conexao.connect();
			//stmt = conn.prepareStatement(INSERT);
			//final PreparedStatement 
			stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, producto.getNome().toUpperCase());
			stmt.setString(2, producto.getCodigo().toUpperCase());
			stmt.setString(3, producto.getDescricao().toUpperCase());
			stmt.setInt(4, producto.getQuantidade());
			//stmt.setDouble(5, producto.getPrecoFinal());
			//stmt.setDouble(6, producto.getPrecoFornecedor());
			stmt.setInt(5, producto.getCategoria().getIdCategoria());
			stmt.setInt(6, producto.getUtilizador().getIdUtilizador());
			//stmt.setInt(7, producto.getFornecedor().getIdFornecedor());
			stmt.setString(7, dataRegisto);
			System.out.println("Quase a gravar no DAO "+producto.toString());
			stmt.executeUpdate();
			final ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
			   lastId = rs.getInt(1);   
			}
		} catch (SQLException ex) {
			System.out.println("Error ao Adicionar "+ex);
			
		}
		
return lastId;
	}

// ------------------------------------------------------------------------------------------------
	public  void delete(Producto producto) {

		try {
			conn = Conexao.connect();
			cs = conn.prepareCall(DELETE);
			cs.setInt(1, producto.getIdProducto());
			cs.execute();
			
		} catch (SQLException e) {
			System.out.println("ERROR deleting the "+e);
		}
	}

//----------------------------------------------------------------------------------------------------
	public  void update(Producto producto) {
		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement(UPDATE);
			// nome=?,codigo=?,descricao=?,quantidade=?,precoFinal,precoFornecedor=?,validade=?,idCategoria=?,idFornecedor=?
			stmt.setString(1, producto.getNome().toUpperCase());
			stmt.setString(2, producto.getCodigo().toUpperCase());
			stmt.setString(3, producto.getDescricao().toUpperCase());
			//stmt.setInt(4, producto.getQuantidade());
			//stmt.setDouble(5, producto.getPrecoFinal());
			//stmt.setDouble(6, producto.getPrecoFornecedor());
			//stmt.setString(7, producto.getValidade());
			stmt.setInt(4, producto.getCategoria().getIdCategoria());
			//stmt.setInt(9, producto.getFornecedor().getIdFornecedor());
			stmt.setInt(5, producto.getIdProducto());
			stmt.executeUpdate();

			
		}

		catch (SQLException ex) {
			System.out.println("error updating "+ex);
		}
	}
//--------------------------------------------------------------------------------------------
	public void updateQty(List<Producto> producto) {
		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement("UPDATE tbl_producto SET quantidade=quantidade-? WHERE idProducto=?");
			for (Producto producto2 : producto) {
				stmt.setInt(1, producto2.getQuantidade());
				stmt.setInt(2, producto2.getIdProducto());
				stmt.executeUpdate();
			}

		} 
		catch (SQLException ex) {
			System.out.println("Error updating the qty"+ex);
		}

	}

	

//------------------------------------------------------------------------------------------------
/**Esta função lista a lista dos productos existentes na tabela
 * @return productos- esta e a lista de todos os productos existentes na tabela
 * 
 *  */
	public  List<Producto> getAll() {
		List<Producto> productos = new ArrayList<>();
		try {
			conn = Conexao.connect();
			stmt = conn.prepareCall("SELECT * FROM tbl_producto  WHERE  (quantidade >0) AND (precoFinal >0)  LIMIT 9");
			rs = stmt.executeQuery();
			while (rs.next()) {

				Producto producto = new Producto();// objecto principal
				producto.setIdProducto(rs.getInt("idProducto"));
				producto.setCodigo(rs.getString("codigo"));
				producto.setNome(rs.getString("nome"));
				producto.setQuantidade(rs.getInt("quantidade"));
				producto.setPrecoFinal(rs.getDouble(("precoFinal")));
				productos.add(producto);

			}

		} catch (SQLException ex) {
				System.out.println("error lisiting "+ex);
			}

		
		return productos;
	}

//-------------------------------------------------------------------------------------------
	public  List<Producto> searchAll(String nome) {
		List<Producto> productos = new ArrayList<>();
		try {
			conn = Conexao.connect();
			stmt = conn.prepareCall("SELECT * FROM vw_listproducto WHERE nome LIKE '"+nome+"%' OR codigo LIKE'"+nome+"%' ");
			rs = stmt.executeQuery();
			while (rs.next()) {

				Producto producto = new Producto();// objecto principal

				Categoria categoria = new Categoria();
				categoria.setNome(rs.getString("categoria"));

				Fornecedor fornecedor = new Fornecedor();
				fornecedor.setNome(rs.getString("fornecedor"));
				Utilizador utilizador = new Utilizador(); // OBJECTO SECUNDARIO
				utilizador.setUsername(rs.getString("utilizador"));

				producto.setIdProducto(rs.getInt("idProducto"));
				producto.setCodigo(rs.getString("codigo"));
				producto.setNome(rs.getString("nome"));
				producto.setQuantidade(rs.getInt("quantidade"));
				producto.setPrecoFinal(Double.parseDouble(rs.getString("precoFinal")));
				producto.setPrecoFornecedor(Double.parseDouble(rs.getString("precoFornecedor")));
				producto.setValidade(rs.getString("validade"));
				producto.setDescricao(rs.getString("descricao"));
				producto.setDataRegisto(rs.getString("dataRegisto"));
				producto.setCategoria(categoria);
				producto.setFornecedor(fornecedor);
				producto.setUtilizador(utilizador);
				productos.add(producto);

			}

		} catch (SQLException ex) {
			
		}

		return productos;
	}
//--------------------------------------------------------------------------------------------------
		public  List<Producto> searchAllPratileira(String nome) {
			List<Producto> productos = new ArrayList<>();
			try {
				conn = Conexao.connect();
				stmt = conn.prepareCall("SELECT * FROM vw_listproductopratileira WHERE nome LIKE '"+nome+"%' OR codigo LIKE'"+nome+"%' ");
				rs = stmt.executeQuery();
				while (rs.next()) {

					Producto producto = new Producto();// objecto principal

					Categoria categoria = new Categoria();
					categoria.setNome(rs.getString("categoria"));

					Fornecedor fornecedor = new Fornecedor();
					fornecedor.setNome(rs.getString("fornecedor"));
					Utilizador utilizador = new Utilizador(); // OBJECTO SECUNDARIO
					utilizador.setUsername(rs.getString("utilizador"));

					producto.setIdProducto(rs.getInt("idProducto"));
					producto.setCodigo(rs.getString("codigo"));
					producto.setNome(rs.getString("nome"));
					producto.setQuantidade(rs.getInt("quantidade"));
					producto.setPrecoFinal(Double.parseDouble(rs.getString("precoFinal")));
					producto.setPrecoFornecedor(Double.parseDouble(rs.getString("precoFornecedor")));
					producto.setValidade(rs.getString("validade"));
					producto.setDescricao(rs.getString("descricao"));
					producto.setDataRegisto(rs.getString("dataRegisto"));
					producto.setCategoria(categoria);
					producto.setFornecedor(fornecedor);
					producto.setUtilizador(utilizador);
					productos.add(producto);

				}

			} catch (SQLException ex) {
			
			}

			return productos;
		}
	//--------------------------------------------------------------------------------------------------

	public  List<Producto> getAllPratileira() {
		List<Producto> productos = new ArrayList<>();
		try {
			conn = Conexao.connect();
			stmt = conn.prepareCall("SELECT * FROM vw_listproductopratileira");
			rs = stmt.executeQuery();
			while (rs.next()) {

				Producto producto = new Producto();// objecto principal

				Categoria categoria = new Categoria();
				categoria.setNome(rs.getString("categoria"));

				Fornecedor fornecedor = new Fornecedor();
				fornecedor.setNome(rs.getString("fornecedor"));
				Utilizador utilizador = new Utilizador(); // OBJECTO SECUNDARIO
				utilizador.setUsername(rs.getString("utilizador"));

				producto.setIdProducto(rs.getInt("idProducto"));
				producto.setCodigo(rs.getString("codigo"));
				producto.setNome(rs.getString("nome"));
				producto.setQuantidade(rs.getInt("quantidade"));
				producto.setPrecoFinal(Double.parseDouble(rs.getString("precoFinal")));
				producto.setPrecoFornecedor(Double.parseDouble(rs.getString("precoFornecedor")));
				producto.setValidade(rs.getString("validade"));
				producto.setDescricao(rs.getString("descricao"));
				producto.setDataRegisto(rs.getString("dataRegisto"));
				producto.setCategoria(categoria);
				producto.setFornecedor(fornecedor);
				producto.setUtilizador(utilizador);
				productos.add(producto);

			}

		} catch (SQLException ex) {
			

		}

		return productos;
	}

//----------------------------------------------------------------------------------------------------
	

	public  Integer ultimoIdProducto() {
		int id = 0;

		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement("SELECT MAX(idProducto) FROM tbl_producto");
			rs = stmt.executeQuery();
			if (rs.next()) {
				id = (rs.getInt(1));
			}
		} catch (SQLException ex) {
					}

		return id;
	}

//---------------------------------------------------------------------------------------------------
	public  void addForeignKeys(FornecedorProducto fornecedorProducto) {
		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement("INSERT INTO tbl_fornecedorProducto(idProduto,idFornecedor)value(?,?)");
			stmt.setInt(1, fornecedorProducto.getProducto().getIdProducto());
			stmt.setInt(2, fornecedorProducto.getFornecedor().getIdFornecedor());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException ex) {
			
		}
	}
//----------------------------------------------------------------------------------------------------
	public  void updateForeignKeys(FornecedorProducto fornecedorProducto) {
		try {

			conn = Conexao.connect();
			stmt = conn.prepareStatement("UPDATE tbl_fornecedorProducto SET idProduto=?,idFornecedor=?)");
			stmt.setInt(1, fornecedorProducto.getProducto().getIdProducto());
			stmt.setInt(2, fornecedorProducto.getFornecedor().getIdFornecedor());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException ex) {
			
		}
	}
//----------------GET BY ID---------------------------------------------------------------------------

		public Producto getProductoById(int idProducto) {
			Producto producto = new Producto();// objecto principal

			try {
				stmt = conn.prepareStatement("SELECT * FROM vw_listProducto WHERE idProducto=? ");
				stmt.setInt(1, idProducto);
				 rs = stmt.executeQuery();

				if (rs.next()) {
				

					Categoria categoria = new Categoria();
					categoria.setNome(rs.getString("categoria"));

					Fornecedor fornecedor = new Fornecedor();
					fornecedor.setNome(rs.getString("fornecedor"));
					Utilizador utilizador = new Utilizador(); // OBJECTO SECUNDARIO
					utilizador.setUsername(rs.getString("utilizador"));

					producto.setIdProducto(rs.getInt("idProducto"));
					producto.setCodigo(rs.getString("codigo"));
					producto.setNome(rs.getString("nome"));
					producto.setQuantidade(rs.getInt("quantidade"));
					producto.setPrecoFinal(Double.parseDouble(rs.getString("precoFinal")));
					producto.setPrecoFornecedor(Double.parseDouble(rs.getString("precoFornecedor")));
					producto.setValidade(rs.getString("validade"));
					producto.setDescricao(rs.getString("descricao"));
					producto.setDataRegisto(rs.getString("dataRegisto"));
					producto.setCategoria(categoria);
					producto.setFornecedor(fornecedor);
					producto.setUtilizador(utilizador);
					
				}
			} catch (SQLException e) {
				System.out.println("error geting of object of product "+e);
			}

			return producto;
		}
		//get product by id second----------------------------------------------------------
		public Producto getProductoByIdSecond(Producto pro) {
			// objecto principal
			Producto producto = new Producto();
			try {
			 stmt = conn.prepareCall("SELECT idProducto,nome,quantidade,precoFinal FROM tbl_producto WHERE idProducto=?");
				stmt.setInt(1,pro.getIdProducto());
				 rs = stmt.executeQuery();
				if (rs.next()) {
					
					producto.setIdProducto(rs.getInt("idProducto"));
					producto.setNome(rs.getString("nome"));
					producto.setQuantidade(rs.getInt("quantidade"));
					producto.setPrecoFinal(rs.getDouble("precoFinal"));	
					
				}
			} catch (SQLException e) {
				System.out.println("error geting of object of product2 "+e);
			}
			catch(NullPointerException n) 
			{
				//System.out.println("Erro found "+n.getMessage());
				n.printStackTrace();
			}

			return producto;
		}
//--------------------------------------------------------------------------------------
		public Producto get(Producto pro) {
			Producto producto = new Producto();
			String sql="select * from tbl_producto where idProducto=?";
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, pro.getIdProducto());
				 rs = stmt.executeQuery();
				if (rs.next()) {
					producto.setIdProducto(rs.getInt("idProducto"));
					producto.setNome(rs.getString("nome"));
					producto.setPrecoFinal(rs.getDouble("precoFinal"));
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return producto;
		}
}
