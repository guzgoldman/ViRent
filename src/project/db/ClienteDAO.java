package project.db;
import project.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClienteDAO implements DAO<Cliente> {
	private Connection conn;
	
	public ClienteDAO(Connection conn) {
		this.conn = conn;
	}
	
	public void insertar(Cliente cliente) {
		String sql = "INSERT INTO Cliente(Nombre, Direccion, Telefono, DNI) VALUES (?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, cliente.getNombre());
			stmt.setString(2, cliente.getDireccion());
			stmt.setString(3, cliente.getTelefono());
			stmt.setString(4, cliente.getDni());
			stmt.execute();
		} catch (SQLException e) {
			System.err.println("Error al dar de alta al cliente: " + e.getMessage());
		}
	}

	public void actualizar(Cliente cliente) {
		List<String> campos = new ArrayList<>();
		String sql = "UPDATE Cliente SET ";
		if (cliente.getNombre() != null) campos.add("Nombre = ?");
		if (cliente.getDireccion() != null) campos.add("Direccion = ?");
		if (cliente.getTelefono() != null) campos.add("Telefono = ?");
		if (cliente.getDni() != null) campos.add("DNI = ?");
		
		sql += String.join(", ", campos) + " WHERE ID = ?";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			int indice = 1;
			if (cliente.getNombre() != null) stmt.setString(indice++, cliente.getNombre());
			if (cliente.getDireccion() != null) stmt.setString(indice++, cliente.getDireccion());
			if (cliente.getTelefono() != null) stmt.setString(indice++, cliente.getTelefono());
			if (cliente.getDni() != null) stmt.setString(indice++, cliente.getDni());
			stmt.setInt(indice, cliente.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error al actualizar los datos del cliente: " + e.getMessage());
		}
	}
	
	public boolean darDeBaja(int id) {
		String sql = "UPDATE Cliente SET Activo = 0 WHERE ID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			int filasAfectadas = stmt.executeUpdate();
			
			if (filasAfectadas > 0) {
				System.out.println("Cliente ID: " + id + " dado de baja correctamente");
				return true;
			} else {
				System.out.println("Cliente con ID: " + id + " no encontrado");
				return false;
			}
		} catch (SQLException e) {
			System.err.println("Error al eliminar al cliente ID " + id + ": " + e.getMessage());
			return false;
		}
	}
	
	public Boolean estaInactivo(int id) {
		String sql = "SELECT Activo FROM Cliente WHERE ID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				boolean activo = rs.getBoolean("Activo");
				return !activo;
			} else {
				System.out.println("Cliente con ID " + id + " no encontrado");
				return null;
			}
		} catch (SQLException e) {
			System.out.println("Error al obtener la informacion del cliente ID " + id + ": " + e.getMessage());
			return null;
		}
	}

	public Cliente buscarPorId(int id) {
		Cliente cliente = new Cliente();
		String sql = "SELECT ID, Nombre, Direccion, Telefono, DNI, Activo FROM Cliente WHERE ID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				cliente.setId(rs.getInt("ID"));
				cliente.setNombre(rs.getString("Nombre"));
				cliente.setDireccion(rs.getString("Direccion"));
				cliente.setTelefono(rs.getString("Telefono"));
				cliente.setDni(rs.getString("DNI"));
				return cliente;
			}
		} catch (SQLException e) {
			System.err.println("Error al recuperar al cliente: " + e.getMessage());
		}
		return null;
	}
	
	public List<Cliente> listarActivos() {
		List<Cliente> listaClientes = new ArrayList<>();
		String sql = "SELECT ID, Nombre, Direccion, Telefono, DNI FROM Cliente WHERE Activo = 1";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Cliente cliente = new Cliente();
				cliente.setId(rs.getInt("ID"));
				cliente.setNombre(rs.getString("Nombre"));
				cliente.setDireccion(rs.getString("Direccion"));
				cliente.setTelefono(rs.getString("Telefono"));
				cliente.setDni(rs.getString("DNI"));				
				listaClientes.add(cliente);
			}
		} catch (SQLException e) {
			System.err.println("Error al listar a los clientes: " + e.getMessage());
		} 
		return listaClientes;
	}

	public List<Cliente> listarTodos() {
		List<Cliente> listaClientes = new ArrayList<>();
		String sql = "SELECT ID, Nombre, Direccion, Telefono, DNI, Activo FROM Cliente";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Cliente cliente = new Cliente();
				cliente.setId(rs.getInt("ID"));
				cliente.setNombre(rs.getString("Nombre"));
				cliente.setDireccion(rs.getString("Direccion"));
				cliente.setTelefono(rs.getString("Telefono"));
				cliente.setDni(rs.getString("DNI"));
				cliente.setActivo(rs.getBoolean("Activo"));
				
				listaClientes.add(cliente);
			}
		} catch (SQLException e) {
			System.err.println("Error al listar a los clientes: " + e.getMessage());
		} 
		return listaClientes;
	}

	public Cliente buscarPorIdentificador(String dni) {
		Cliente cliente = new Cliente();
		String sql = "SELECT ID, Nombre, Direccion, Telefono, DNI FROM Cliente WHERE DNI = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, dni);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				cliente.setId(rs.getInt("ID"));
				cliente.setNombre(rs.getString("Nombre"));
				cliente.setDireccion(rs.getString("Direccion"));
				cliente.setTelefono(rs.getString("Telefono"));
				cliente.setDni(rs.getString("DNI"));
				return cliente;
			}
		} catch (SQLException e) {
			System.err.println("Error al recuperar el cliente: " + e.getMessage());
		}
		return null;
	}
	
	public boolean tieneAlquilerActivo(int idCliente) {
		String sql = "SELECT COUNT(*) AS Cantidad FROM Alquiler WHERE IDCliente = ? AND (Estado = 'act' OR Estado = 'venc')";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, idCliente);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				int contadorAlquiler = rs.getInt("Cantidad");
				return contadorAlquiler > 0;
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener la información: " + e.getMessage());
		}
		return false;
	}
	
	public Cliente seleccionarCliente(Scanner sc) {
		Cliente clienteSeleccionado = null;
		List<Cliente> listaClientes = this.listarActivos();
		
		if (listaClientes.isEmpty()) {
			System.out.println("La lista de clientes esta vacia");
			return null;
		}
		
		for (Cliente c : listaClientes) {
			c.mostrarEnSelector();
		}
		
		System.out.print("Seleccion al cliente: ");
		int idSeleccionado = sc.nextInt();
		sc.nextLine();
		
		for (Cliente c : listaClientes) {
			if (c.getId() == idSeleccionado) {
				clienteSeleccionado = c;
				break;
			}
		}
		return clienteSeleccionado;
	}
}
