package Programa;

import java.sql.*;
import java.util.*;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;

public class ClienteQuery {
	public static boolean insertarCliente(Connection conn, Cliente cliente) throws SQLIntegrityConstraintViolationException, MysqlDataTruncation {
		try {
			String sql = "INSERT INTO cliente (nombre, direccion, telefono, dni) VALUES (?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, cliente.getNombre());
			stmt.setString(2, cliente.getDireccion());
			stmt.setString(3, cliente.getTelefono());
			stmt.setString(4, cliente.getDni());
			stmt.executeUpdate();
			return true;
		} catch (SQLIntegrityConstraintViolationException e) {
			throw e;
		} catch (MysqlDataTruncation e) {
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Cliente mostrarClientePorId(Connection conn, int id) {
		Cliente cliente = null;
		try {
			String sql = "SELECT * FROM cliente WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				int id_cliente = rs.getInt("id");
				String nombre = rs.getString("nombre");
				String direccion = rs.getString("direccion");
				String telefono = rs.getString("telefono");
				String dni = rs.getString("dni");
				
				cliente = new Cliente(id_cliente, nombre, direccion, telefono, dni);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cliente;
	}
	
	public Cliente mostrarClientePorDni(Connection conn, String dni) {
		Cliente cliente = null;
		try {
			String sql = "SELECT * FROM cliente WHERE dni = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, dni);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt("id");
				String nombre = rs.getString("nombre");
				String direccion = rs.getString("direccion");
				String telefono = rs.getString("telefono");
				String dni_cliente = rs.getString("dni");
				
				cliente = new Cliente(id, nombre, direccion, telefono, dni_cliente);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cliente;
	}
	
	public Cliente mostrarClientePorNombre(Connection conn, String nombre) {
		Cliente cliente = null;
		try {
			String sql = "SELECT id, nombre, direccion, telefono, dni FROM cliente WHERE nombre LIKE ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, nombre);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt("id");
				String nombre_cliente = rs.getString("nombre");
				String direccion = rs.getString("direccion");
				String telefono = rs.getString("telefono");
				String dni = rs.getString("dni");
				
				cliente = new Cliente(id, nombre_cliente, direccion, telefono, dni);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cliente;
	}
	
	public static void eliminarClientePorId(Connection conn, int id) {
		try {
			String sql = "DELETE FROM cliente WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void eliminarClientePorDni(Connection conn, String dni) {
		try {
			String sql = "DELETE FROM cliente WHERE dni = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, dni);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean actualizarCliente(Connection conn, Cliente cliente) throws SQLIntegrityConstraintViolationException, MysqlDataTruncation {
		ArrayList<String> campos = new ArrayList<>();
		
		try {
			String sql = "UPDATE cliente SET ";
			
			if (cliente.getNombre() != null) campos.add("nombre = ?");
			if (cliente.getDireccion() != null) campos.add("direccion = ?");
			if (cliente.getTelefono() != null) campos.add("telefono = ?");
			if (cliente.getDni() != null) campos.add("dni = ?");
			
			sql += String.join(", ", campos) + " WHERE id = ?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			int index = 1;
			if (cliente.getNombre() != null) stmt.setString(index++, cliente.getNombre());
			if (cliente.getDireccion() != null) stmt.setString(index++, cliente.getDireccion());
			if (cliente.getTelefono() != null) stmt.setString(index++, cliente.getTelefono());
			if (cliente.getDni() != null) stmt.setString(index++, cliente.getDni());
			stmt.setInt(index, cliente.getId());
			stmt.executeUpdate();
			return true;
		} catch (SQLIntegrityConstraintViolationException e) {
			throw e;
		} catch (MysqlDataTruncation e) {
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Cliente selectorClientePorId(Connection conn, Scanner sc) {
		Cliente clienteSeleccionado = null;
		ArrayList<Cliente> listaClientesPorId = new ArrayList<>();
		
		try {
			String sql = "SELECT * FROM cliente";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				String nombre = rs.getString("nombre");
				String direccion = rs.getString("direccion");
				String telefono = rs.getString("telefono");
				String dni = rs.getString("dni");
				
				Cliente cliente = new Cliente(id, nombre, direccion, telefono, dni);
				listaClientesPorId.add(cliente);
			}
			
			if (listaClientesPorId.isEmpty()) {
				System.out.println("La lista de clientes está vacía");
				return null;
			}
			
			for (Cliente c : listaClientesPorId) {
				c.selectorClientePorId();
			}
			
			sc = new Scanner(System.in);
			System.out.print("Seleccione al cliente: ");
			int idSeleccionado = sc.nextInt();
			sc.nextLine();
			
			for (Cliente c : listaClientesPorId) {
				if (c.getId() == idSeleccionado) {
					clienteSeleccionado = c;
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return clienteSeleccionado;
	}
	
	public ArrayList<Cliente> mostrarCliente(Connection conn) {
		ArrayList<Cliente> clientes = new ArrayList<>();
		try {
			String sql = "SELECT * FROM cliente";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				String nombre = rs.getString("nombre");
				String direccion = rs.getString("direccion");
				String telefono = rs.getString("telefono");
				String dni = rs.getString("dni");
				
				clientes.add(new Cliente(id, nombre, direccion, telefono, dni));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return clientes;
	}
	
	public static boolean tieneAlquileresActivos(Connection conn, int idCliente) {
		boolean tieneAlquileresActivos = false;
		int contadorAlquileres = 0;
		
		try {
			String sql = "SELECT COUNT(*) AS cantidad FROM alquiler WHERE id_cliente = ? AND estado = 'act'";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idCliente);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				contadorAlquileres = rs.getInt("cantidad");
			}
			
			if (contadorAlquileres > 0) {
				tieneAlquileresActivos = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tieneAlquileresActivos;
	}
}
