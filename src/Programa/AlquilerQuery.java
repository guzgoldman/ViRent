package Programa;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class AlquilerQuery {
	public void insertarAlquiler(Connection conn, Alquiler alquiler) {
		try {
			String sql = "INSERT INTO alquiler (id_cliente, id_vehiculo, fecha_alquiler, fecha_devolucion) VALUES (?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, alquiler.getCliente().getId());
			stmt.setInt(2, alquiler.getVehiculo().getId_vehiculo());
			stmt.setDate(3, Date.valueOf(alquiler.getFecha_alquiler()));
			stmt.setDate(4, Date.valueOf(alquiler.getFecha_devolucion()));
			stmt.executeUpdate();
			System.out.println("Alquiler registrado exitosamente.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Alquiler> mostrarAlquileresActivos(Connection conn) {
		ArrayList<Alquiler> alquileres = new ArrayList<>();
		try {
			String sql = "SELECT * FROM alquiler WHERE estado = 'act'";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				int id_cliente = rs.getInt("id_cliente");
				int id_vehiculo = rs.getInt("id_vehiculo");
				String fecha_alquiler_date = rs.getString("fecha_alquiler");
				String fecha_devolucion_date = rs.getString("fecha_devolucion");
				
				LocalDate fecha_alquiler = LocalDate.parse(fecha_alquiler_date);
				LocalDate fecha_devolucion = LocalDate.parse(fecha_devolucion_date);
				
				Cliente cliente = new ClienteQuery().mostrarClientePorId(conn, id_cliente);
				Vehiculo vehiculo = new VehiculoQuery().mostrarVehiculoPorId(conn, id_vehiculo);
				
				alquileres.add(new Alquiler(id, cliente, vehiculo, fecha_alquiler, fecha_devolucion));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return alquileres;
	}
	
	public ArrayList<Alquiler> mostrarAlquileresPorUsuario(Connection conn, int idCliente) {
		ArrayList<Alquiler> listaAlquileresPorUsuario = new ArrayList<>();
		try {
			String sql = "SELECT id, id_cliente, id_vehiculo, fecha_alquiler, fecha_devolucion, estado, CASE WHEN estado = 'act' THEN 'Activo' WHEN estado = 'fin' THEN 'Finalizado' END AS estado_modificado FROM alquiler WHERE id_cliente = ? ORDER BY estado_modificado";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idCliente);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				int idVehiculo = rs.getInt("id_vehiculo");
				String fechaAlquilerString = rs.getString("fecha_alquiler");
				String fechaDevolucionString = rs.getString("fecha_devolucion");
				String estado = rs.getString("estado_modificado");
				
				LocalDate fechaAlquiler = LocalDate.parse(fechaAlquilerString);
				LocalDate fechaDevolucion = LocalDate.parse(fechaDevolucionString);
				
				Cliente cliente = new ClienteQuery().mostrarClientePorId(conn, idCliente);
				Vehiculo vehiculo = new VehiculoQuery().mostrarVehiculoPorId(conn, idVehiculo);
				
				listaAlquileresPorUsuario.add(new Alquiler(id, cliente, vehiculo, fechaAlquiler, fechaDevolucion, estado));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaAlquileresPorUsuario;
	}
	
	public static void finalizarAlquiler(Connection conn, int id) {
		try {
			String sql = "SELECT id, id_vehiculo FROM alquiler WHERE id = ?";
			String actualizarAlquiler = "UPDATE alquiler SET estado = 'fin' WHERE id = ?";
			String actualizarVehiculo = "UPDATE vehiculo SET estado = 'disp' WHERE id = ?";
			PreparedStatement stmtQuery = conn.prepareStatement(sql);
			PreparedStatement stmtActualizarAlquiler = conn.prepareStatement(actualizarAlquiler);
			PreparedStatement stmtActualizarVehiculo = conn.prepareStatement(actualizarVehiculo);
			stmtQuery.setInt(1, id);
			ResultSet rs = stmtQuery.executeQuery();
			
			while (rs.next()) {
				int idVehiculo = rs.getInt("id_vehiculo");
				
				stmtActualizarAlquiler.setInt(1, id);
				stmtActualizarAlquiler.executeUpdate();
				
				stmtActualizarVehiculo.setInt(1, idVehiculo);
				stmtActualizarVehiculo.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Alquiler selectorAlquilerActivo(Connection conn, Scanner sc) {
		Alquiler alquilerSeleccionado = null;
		ArrayList<Alquiler> listaAlquileresActivos = new ArrayList<>();
		
		try {
			String sql = "SELECT id, id_cliente, id_vehiculo, fecha_alquiler, fecha_devolucion FROM alquiler WHERE estado = 'act'";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				int idCliente = rs.getInt("id_cliente");
				int idVehiculo = rs.getInt("id_vehiculo");
				String fechaAlquilerString = rs.getString("fecha_alquiler");
				String fechaDevolucionString = rs.getString("fecha_devolucion");
				
				Cliente cliente = new ClienteQuery().mostrarClientePorId(conn, idCliente);
				Vehiculo vehiculo = new VehiculoQuery().mostrarVehiculoPorId(conn, idVehiculo);
				LocalDate fechaAlquiler = LocalDate.parse(fechaAlquilerString);
				LocalDate fechaDevolucion = LocalDate.parse(fechaDevolucionString);
				
				listaAlquileresActivos.add(new Alquiler(id, cliente, vehiculo, fechaAlquiler, fechaDevolucion));
			}
			if (listaAlquileresActivos.isEmpty()) {
				System.out.println("No hay alquileres disponibles");
				return null;
			}
			for (Alquiler a : listaAlquileresActivos) {
				a.selectorAlquiler();
			}
			sc = new Scanner(System.in);
			System.out.print("Seleccione el alquiler a finalizar: ");
			int idSeleccionado = sc.nextInt();
			sc.nextLine();
			
			for (Alquiler a : listaAlquileresActivos) {
				if (a.getId_alquiler() == idSeleccionado) {
					alquilerSeleccionado = a;
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return alquilerSeleccionado;
	}
	
	public static int cantidadAlquileresActivos(Connection conn) {
		int cantidadAlquileresActivos = 0;
		try {
			String sql = "SELECT COUNT(*) as total FROM alquiler WHERE estado = 'act'";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				cantidadAlquileresActivos = rs.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cantidadAlquileresActivos;
	}
	
	public static void actualizarEstados(Connection conn) {
		try {
			String sql = "SELECT id, id_vehiculo FROM alquiler WHERE estado = 'act' AND fecha_devolucion < ?";
			String actualizarAlquiler = "UPDATE alquiler SET estado = 'fin' WHERE id = ?";
			String actualizarVehiculo = "UPDATE vehiculo SET estado = 'disp' WHERE id = ?";
			PreparedStatement stmtQuery = conn.prepareStatement(sql);
			PreparedStatement stmtActualizarAlquiler = conn.prepareStatement(actualizarAlquiler);
			PreparedStatement stmtActualizarVehiculo = conn.prepareStatement(actualizarVehiculo);
			
			stmtQuery.setDate(1, Date.valueOf(LocalDate.now()));
			ResultSet rs = stmtQuery.executeQuery();
			
			while (rs.next()) {
				int idAlquiler = rs.getInt("id");
				int idVehiculo = rs.getInt("id_vehiculo");
				
				stmtActualizarAlquiler.setInt(1, idAlquiler);
				stmtActualizarAlquiler.executeUpdate();
				
				stmtActualizarVehiculo.setInt(1, idVehiculo);
				stmtActualizarVehiculo.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
