package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DaoLogin;
import model.Utilizador;


@WebServlet(urlPatterns = "/Login")
public class Login extends HttpServlet {
	int iduser=0;
	private static final long serialVersionUID = 1L;
	private DaoLogin dao;
	public void init(ServletConfig config) throws ServletException {
	}
	public Login() {
		super();
		dao=new DaoLogin();
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		Utilizador utilizador = new Utilizador();
		utilizador.setUsername(username);
		utilizador.setPassword(password);
		
		
	
		
				//Utilizador user= dao.getUtilizadorByUserPass(utilizador);
				
				//user.getUsername().equals(username)&&user.getPassword().equals(password)
				//dao.consultar(utilizador)
				try {
					if (dao.consultar(utilizador)) {
						iduser=dao.getIdUtilizador(username);
						System.out.println(iduser);

						response.sendRedirect("/WebSysmafa/views/menu.jsp");
					} 
					else {
						response.sendRedirect("/WebSysmafa/views/error.jsp");
						}
				} catch (SQLException e) {
					
					System.out.println("NULL POINTER EX FOUND "+e);e.printStackTrace();
				}
			
			
		
	
	}

}
