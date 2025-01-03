package project.db;
import java.sql.*;

public class Conn {
	private String url;
	private String username;
	private String password;
	
	public Conn(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			System.out.println("Error al conectar a la base de datos: " + e.getMessage());
			throw e;
		}
		return conn;
	}
}
