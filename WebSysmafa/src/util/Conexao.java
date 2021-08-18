
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;
/**@author JBILA 
 * Classe conexao e responsavel por estabelcer a conexao com a base de dado
 * ela contem metodos de  conectar e desconectar a base de dado
 * */
public class Conexao {
 private  static final String USERNAME="jbila";
 private  static final  String PASSWORD="MellannieBila";
 private  static final String CONN_STRING
     =  "jdbc:mysql://localhost:3306/db_simafa";

 /**Este metodo abre a conexao
  * @param Conexao
  * 
  * */
 public static Connection connect(){
	   Connection conn=null;
	  		try {
					Class.forName("com.mysql.jdbc.Driver");
					conn=DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			        // JOptionPane.showMessageDialog(null,"DATABASE CONNECTED!");
					//System.out.println("CONNECTED");
				} catch (ClassNotFoundException e) {
				
					e.printStackTrace();
					}
				catch(SQLException ex) {
					ex.printStackTrace();
				}
	   	
	   	   return conn;

	}

 //---------------------------------------------------------------------------------------
   /**Este metodo desconecta a conexao 
    * ou melhor ela  fecha a conexao
    * @param Conenextio
    * */
   public static void disconect(Connection conn){
   	{
   		try {
   			if(conn!=null)
				conn.close();
   			JOptionPane.showMessageDialog(null,"DATABASE  DISCONNECTED");
			} 
   		catch (SQLException e) {
				JOptionPane.showMessageDialog(null,"Houve um erro ao Tentar Fechar a Base de Dados" +e);
				
			}
   		finally{
   			
   		}
   		
   	}
   }

     
}
