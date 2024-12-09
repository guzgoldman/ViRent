package Programa;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class AlquilerQuery {
	public void insertarAlquiler(Connection conn, Alquiler alquiler) {
		try {
			String sql = "INSERT INTO alquiler (id_cliente, id_vehiculo, fecha_alquiler, fecha_devolucion_pactada) VALUES (?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, alquiler.getCliente().getId());
			stmt.setInt(2, alquiler.getVehiculo().getId_vehiculo());
			stmt.setDate(3, Date.valueOf(alquiler.getFecha_alquiler()));
			stmt.setDate(4, Date.valueOf(alquiler.getFecha_devolucion_pactada()));
			stmt.executeUpdate();
			System.out.println("Alquiler registrado exitosamente.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Alquiler> mostrarAlquileresActivos(Connection conn) {
		ArrayList<Alquiler> alquileres = new ArrayList<>();
		try {
			String sql = "SELECT id, id_cliente, id_vehiculo, fecha_alquiler, fecha_devolucion_pactada FROM alquiler WHERE estado = 'act'";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				int id_cliente = rs.getInt("id_cliente");
				int id_vehiculo = rs.getInt("id_vehiculo");
				String fechaAlquilerString = rs.getString("fecha_alquiler");
				String fechaDevolucionPactadaString = rs.getString("fecha_devolucion_pactada");
				
				LocalDate fechaAlquiler = LocalDate.parse(fechaAlquilerString);
				LocalDate fechaDevolucionPactada = LocalDate.parse(fechaDevolucionPactadaString);
				
				Cliente cliente = new ClienteQuery().mostrarClientePorId(conn, id_cliente);
				Vehiculo vehiculo = new VehiculoQuery().mostrarVehiculoPorId(conn, id_vehiculo);
				
				alquileres.add(new Alquiler(id, cliente, vehiculo, fechaAlquiler, fechaDevolucionPactada));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return alquileres;
	}
	
	public ArrayList<Alquiler> mostrarAlquileresPorUsuario(Connection conn, int idCliente) {
		ArrayList<Alquiler> listaAlquileresPorUsuario = new ArrayList<>();
		try {
			String sql = "SELECT * FROM v_alquileres_por_usuario WHERE id_cliente = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idCliente);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				int idVehiculo = rs.getInt("id_vehiculo");
				String fechaAlquilerString = rs.getString("fecha_alquiler");
				String fechaDevolucionPactadaString = rs.getString("fecha_devolucion_pactada");
				String fechaDevolucionString = rs.getString("fecha_devolucion");
				String estado = rs.getString("estado_modificado");
				String motivo_baja = rs.getString("motivo_baja");
				
				LocalDate fechaAlquiler = LocalDate.parse(fechaAlquilerString);
				LocalDate fechaDevolucionPactada = LocalDate.parse(fechaDevolucionPactadaString);
				LocalDate fechaDevolucion = LocalDate.parse(fechaDevolucionString);
				
				Cliente cliente = new ClienteQuery().mostrarClientePorId(conn, idCliente);
				Vehiculo vehiculo = new VehiculoQuery().mostrarVehiculoPorId(conn, idVehiculo);
				
				listaAlquileresPorUsuario.add(new Alquiler(id, cliente, vehiculo, fechaAlquiler, fechaDevolucionPactada, fechaDevolucion, estado, motivo_baja));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaAlquileresPorUsuario;
	}
	
	public static void finalizarAlquiler(Connection conn, int id, String motivo_baja) {
		try {
			String sql = "SELECT id, id_vehiculo FROM alquiler WHERE id = ?";
			String actualizarAlquiler = "UPDATE alquiler SET estado = 'fin', fecha_devolucion = NOW(), motivo_baja = ? WHERE id = ?";
			String actualizarVehiculo = "UPDATE vehiculo SET estado = 'disp' WHERE id = ?";
			PreparedStatement stmtQuery = conn.prepareStatement(sql);
			PreparedStatement stmtActualizarAlquiler = conn.prepareStatement(actualizarAlquiler);
			PreparedStatement stmtActualizarVehiculo = conn.prepareStatement(actualizarVehiculo);
			stmtQuery.setInt(1, id);
			ResultSet rs = stmtQuery.executeQuery();
			
			while (rs.next()) {
				int idVehiculo = rs.getInt("id_vehiculo");
				
				stmtActualizarAlquiler.setString(1, motivo_baja);
				stmtActualizarAlquiler.setInt(2, id);
				stmtActualizarAlquiler.executeUpdate();
				
				stmtActualizarVehiculo.setInt(1, idVehiculo);
				stmtActualizarVehiculo.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Alquiler selectorAlquiler(Connection conn, Scanner sc) {
		Alquiler alquilerSeleccionado = null;
		ArrayList<Alquiler> listaAlquileresActivos = new ArrayList<>();
		
		try {
			String sql = "SELECT id, id_cliente, id_vehiculo, fecha_alquiler, fecha_devolucion_pactada FROM alquiler WHERE estado = 'act' OR estado = 'venc'";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				int idCliente = rs.getInt("id_cliente");
				int idVehiculo = rs.getInt("id_vehiculo");
				String fechaAlquilerString = rs.getString("fecha_alquiler");
				String fechaDevolucionPactadaString = rs.getString("fecha_devolucion_pactada");
				
				Cliente cliente = new ClienteQuery().mostrarClientePorId(conn, idCliente);
				Vehiculo vehiculo = new VehiculoQuery().mostrarVehiculoPorId(conn, idVehiculo);
				LocalDate fechaAlquiler = LocalDate.parse(fechaAlquilerString);
				LocalDate fechaDevolucionPactada = LocalDate.parse(fechaDevolucionPactadaString);
				
				listaAlquileresActivos.add(new Alquiler(id, cliente, vehiculo, fechaAlquiler, fechaDevolucionPactada));
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
	
	public static void actualizarEstadosAlquiler(Connection conn, int idAlquiler, int idVehiculo, String motivoBaja) {
		String sql = "{CALL sp_actualizar_estados_alquileres(?, ?, ?)}";
		try (CallableStatement stmt = conn.prepareCall(sql)) {
			stmt.setInt(1, idAlquiler);
			stmt.setInt(2, idVehiculo);
			stmt.setString(3, motivoBaja);
			stmt.execute();
			System.out.println("El alquiler Nº " + idAlquiler + " ha sido finalizado correctamente");
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
					System.out.println("Transacci�n revertida debido a un error");
				} catch (SQLException rollbackEx) {
					System.err.println("Error al intentar revertir la transacci�n: " + rollbackEx);
				}
			}
			System.err.println("Error al ejecutar la transacci�n: " + e.getMessage());
		}
	}
	
	public static void vencimientoAlquileres(Connection conn) {
		String sql = "UPDATE alquiler SET estado = 'venc' WHERE id IS NOT NULL AND fecha_devolucion IS NULL "
				+ "AND fecha_devolucion_pactada < CURDATE()";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void mostrarAlquileresVencidos(Connection conn) {
		ArrayList<Alquiler> listaAlquileresVencidos = new ArrayList<>();
		String sql = "SELECT id, id_cliente, id_vehiculo, fecha_alquiler, fecha_devolucion_pactada FROM Alquiler WHERE Estado = 'venc'";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				int idCliente = rs.getInt("id_cliente");
				int idVehiculo = rs.getInt("id_vehiculo");
				String fechaAlquilerString = rs.getString("fecha_alquiler");
				String fechaDevolucionPactadaString = rs.getString("fecha_devolucion_pactada");
				
				Cliente cliente = new ClienteQuery().mostrarClientePorId(conn, idCliente);
				Vehiculo vehiculo = new VehiculoQuery().mostrarVehiculoPorId(conn, idVehiculo);
				LocalDate fechaAlquiler = LocalDate.parse(fechaAlquilerString);
				LocalDate fechaDevolucionPactada = LocalDate.parse(fechaDevolucionPactadaString);
				
				listaAlquileresVencidos.add(new Alquiler(id, cliente, vehiculo, fechaAlquiler, fechaDevolucionPactada));
			}
			
			if (listaAlquileresVencidos.isEmpty()) {
				System.out.println("------------------------------");
				System.out.println("No hay alquileres vencidos");
				System.out.println("------------------------------");
			} else {
				System.out.println("----------------------------------------");
				System.out.println("Hay alquileres vencidos\nConsulte el siguiente listado:");
				for (Alquiler a : listaAlquileresVencidos) {
					a.selectorAlquiler();
				}
				System.out.println("Cont�ctese con estos clientes a la brevedad");
				System.out.println("----------------------------------------");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
