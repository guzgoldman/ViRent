package Programa;

import java.sql.*;
import java.util.*;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;

public class VehiculoQuery {
	public static boolean insertarVehiculo(Connection conn, Vehiculo vehiculo) throws SQLIntegrityConstraintViolationException, MysqlDataTruncation {
		try {
			String sql = "INSERT INTO vehiculo (marca, modelo, anio, matricula, tipo) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, vehiculo.getMarca());
			stmt.setString(2, vehiculo.getModelo());
			stmt.setInt(3, vehiculo.getAnio());
			stmt.setString(4, vehiculo.getMatricula());
			stmt.setString(5, vehiculo.getTipo());
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
	
	public Vehiculo mostrarVehiculoPorId(Connection conn, int id) {
		Vehiculo vehiculo = null;
		
		try {
			String sql = "SELECT * FROM vehiculo WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				int id_vehiculo = rs.getInt("id");
				String marca = rs.getString("marca");
				String modelo = rs.getString("modelo");
				int anio = rs.getInt("anio");
				String matricula = rs.getString("matricula");
				String tipo = rs.getString("tipo");
				String estado = rs.getString("estado");
				
				vehiculo = new Vehiculo(id_vehiculo, marca, modelo, anio, matricula, tipo, estado);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vehiculo;
	}
	
	public Vehiculo mostrarVehiculoPorMatricula(Connection conn, String matricula) {
		Vehiculo vehiculo = null;
		
		try {
			String sql = "SELECT * FROM vehiculo WHERE matricula = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, matricula);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				int id = rs.getInt("id");
				String marca = rs.getString("marca");
				String modelo = rs.getString("modelo");
				int anio = rs.getInt("anio");
				String matricula_vehiculo = rs.getString("matricula");
				String tipo = rs.getString("tipo");
				String estado = rs.getString("estado");
				
				vehiculo = new Vehiculo(id, marca, modelo, anio, matricula_vehiculo, tipo, estado);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vehiculo;
	}
	
	public ArrayList<Vehiculo> mostrarVehiculosPorTipo(Connection conn, String tipo) {
		ArrayList<Vehiculo> listaVehiculosPorTipo = new ArrayList<>();
		
		try {
			String sql = "SELECT id, marca, modelo, anio, matricula, estado FROM vehiculo WHERE tipo = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, tipo);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				String marca = rs.getString("marca");
				String modelo = rs.getString("modelo");
				int anio = rs.getInt("anio");
				String matricula = rs.getString("matricula");
				String estado = rs.getString("estado");
				
				listaVehiculosPorTipo.add(new Vehiculo(id, marca, modelo, anio, matricula, estado));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaVehiculosPorTipo;
	}
	
	public ArrayList<Vehiculo> mostrarVehiculosPorMarca(Connection conn, String marcaBuscada) {
		ArrayList<Vehiculo> listaVehiculosPorMarca = new ArrayList<>();
		
		try {
			String sql = "SELECT id, marca, modelo, anio, matricula, estado FROM vehiculo WHERE marca LIKE ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, marcaBuscada);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				String marca = rs.getString("marca");
				String modelo = rs.getString("modelo");
				int anio = rs.getInt("anio");
				String matricula = rs.getString("matricula");
				String estado = rs.getString("estado");
				
				listaVehiculosPorMarca.add(new Vehiculo(id, marca, modelo, matricula, anio, estado));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaVehiculosPorMarca;
	}
	
	public ArrayList<Vehiculo> mostrarVehiculosPorEstado(Connection conn, String estado) {
		ArrayList<Vehiculo> listaVehiculosPorEstado = new ArrayList<>();
		
		try {
			String sql = "SELECT id, marca, modelo, anio, matricula, tipo FROM vehiculo WHERE estado = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, estado);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				String marca = rs.getString("marca");
				String modelo = rs.getString("modelo");
				int anio = rs.getInt("anio");
				String matricula = rs.getString("matricula");
				String tipo = rs.getString("tipo");
				
				listaVehiculosPorEstado.add(new Vehiculo(id, marca, modelo, anio, matricula, tipo));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listaVehiculosPorEstado;
	}
	
	public static boolean modificarEstadoVehiculo(Connection conn, String estado, int id) {
		try {
			String sql = "UPDATE vehiculo SET estado = ? WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, estado);
			stmt.setInt(2, id);
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Vehiculo selectorVehiculoDisponiblePorTipo(Connection conn, String tipo, Scanner sc) {
		Vehiculo vehiculoSeleccionado = null;
		ArrayList<Vehiculo> listaVehiculosDisponiblesPorTipo = new ArrayList<>();
		
		try {
			String sql = "SELECT * FROM vehiculo WHERE tipo = ? AND estado = 'disp'";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, tipo);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				String marca = rs.getString("marca");
				String modelo = rs.getString("modelo");
				int anio = rs.getInt("anio");
				String matricula = rs.getString("matricula");
				String tipo_vehiculo = rs.getString("tipo");
				String estado = rs.getString("estado");
				
				Vehiculo vehiculo = new Vehiculo(id, marca, modelo , anio, matricula, tipo_vehiculo, estado);
				listaVehiculosDisponiblesPorTipo.add(vehiculo);
			}
			
			if (listaVehiculosDisponiblesPorTipo.isEmpty()) {
				System.out.println("No existen vehículos de este tipo o se encuentran alquilados " + "(" + tipo + ")");
				return null;
			}
			
			for (Vehiculo v : listaVehiculosDisponiblesPorTipo) {
				v.selectorVehiculoPorId();
			}
			
			sc = new Scanner(System.in);
			System.out.print("Seleccione el vehículo deseado: ");
			int idSeleccionado = sc.nextInt();
			sc.nextLine();
			
			for (Vehiculo v : listaVehiculosDisponiblesPorTipo) {
				if (v.getId_vehiculo() == idSeleccionado) {
					vehiculoSeleccionado = v;
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vehiculoSeleccionado;
	}
	
	public static void borrarVehiculoPorId(Connection conn, int id) {
		try {
			String sql = "DELETE FROM vehiculo WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void borrarVehiculoPorMatricula(Connection conn, String matricula) {
		try {
			String sql = "DELETE FROM vehiculo WHERE matricula = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, matricula);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean actualizarVehiculo(Connection conn, Vehiculo vehiculo, String matricula) throws SQLIntegrityConstraintViolationException, MysqlDataTruncation {
		try {
			String sql = "UPDATE vehiculo SET marca = ?, modelo = ?, anio = ?, tipo = ? WHERE matricula = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, vehiculo.getMarca());
			stmt.setString(2, vehiculo.getModelo());
			stmt.setInt(3, vehiculo.getAnio());
			stmt.setString(4, vehiculo.getTipo());
			stmt.setString(5, matricula);
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
	
	public static boolean hayVehiculosDisponibles(Connection conn) {
		boolean hayVehiculosDisponibles = false;
		int contadorVehiculosDisponibles = 0;
		try {
			String sql = "SELECT COUNT(*) AS cantidad FROM vehiculo WHERE estado = 'disp'";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int cantidadVehiculos = rs.getInt("cantidad");
				contadorVehiculosDisponibles += cantidadVehiculos;
			}
			
			if (contadorVehiculosDisponibles > 0) {
				hayVehiculosDisponibles = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();		
		}
		return hayVehiculosDisponibles;
	}
}
