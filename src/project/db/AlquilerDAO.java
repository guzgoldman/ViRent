package project.db;
import project.model.Alquiler;
import project.model.Cliente;
import project.model.Vehiculo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class AlquilerDAO implements DAO<Alquiler> {
	private Connection conn;
	private ClienteDAO clienteDAO;
	private VehiculoDAO vehiculoDAO;
	
	public AlquilerDAO(Connection conn, ClienteDAO clienteDAO, VehiculoDAO vehiculoDAO) {
		this.conn = conn;
		this.clienteDAO = clienteDAO;
		this.vehiculoDAO = vehiculoDAO;
	}
	
    public void insertar(Alquiler alquiler) {
        String sql = "{CALL IniciarAlquiler(?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, alquiler.getCliente().getId());
            stmt.setInt(2, alquiler.getVehiculo().getId());
            stmt.setDate(3, Date.valueOf(alquiler.getFechaInicio()));
            stmt.setDate(4, Date.valueOf(alquiler.getFechaDevolucionPactada()));
            stmt.execute();
            System.out.println("Alquiler registrado exitosamente.");
        } catch (SQLException e) {
			rollback(conn);
			System.err.println("Error al iniciar el alquiler: " + e.getMessage());
        }
    }

	public void actualizar(Alquiler alquiler) {
		String sql = "{CALL ActualizarEstadosAlquiler(?, ?, ?)}";
		try (CallableStatement stmt = conn.prepareCall(sql)) {
			stmt.setInt(1, alquiler.getId());
			stmt.setInt(2, alquiler.getVehiculo().getId());
			stmt.setString(3, alquiler.getMotivoBaja());
			stmt.execute();
			System.out.println("El alquiler Nº " + alquiler.getId() + " ha sido finalizado correctamente.");
		} catch (SQLException e) {
			rollback(conn);
			System.err.println("Error al finalizar el alquiler: " + e.getMessage());
		}
	}

	public Alquiler buscarPorId(int id) {
		String sql = "SELECT ID, IDCliente, IDVehiculo, FechaInicio, FechaDevolucionPactada, FechaDevolucionFinal, "
				+ "CASE WHEN Estado = 'act' THEN 'Activo' WHEN Estado = 'fin' THEN 'Finalizado' WHEN Estado = 'venc' THEN 'Vencido' END AS Estado, "
				+ "CASE WHEN MotivoBaja IS NULL THEN 'N/A' ELSE MotivoBaja END AS MotivoBaja "
				+ "FROM Alquiler WHERE ID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int idCliente = rs.getInt("IDCliente");
				int idVehiculo = rs.getInt("IDVehiculo");
				String fechaInicioStr = rs.getString("FechaInicio");
				String fechaDevolucionPactadaStr = rs.getString("FechaDevolucionPactada");
				String fechaDevolucionFinalStr = rs.getString("FechaDevolucionFinal");
				String estado = rs.getString("Estado");
				String motivoBaja = rs.getString("MotivoBaja");
				
				Cliente cliente = clienteDAO.buscarPorId(idCliente);
				Vehiculo vehiculo = vehiculoDAO.buscarPorId(idVehiculo);
				LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
				LocalDate fechaDevolucionPactada = LocalDate.parse(fechaDevolucionPactadaStr);
				LocalDate fechaDevolucionFinal = null;
				
				if (fechaDevolucionFinalStr == null) {
					try {
						fechaDevolucionFinal = LocalDate.parse(fechaDevolucionFinalStr);
					} catch (DateTimeParseException e) {
						System.err.println("Error al parsear la fecha de devolución: " + e.getMessage());
					}
				}
				
				Alquiler alquiler = new Alquiler(id, cliente, vehiculo, fechaInicio, fechaDevolucionPactada, fechaDevolucionFinal, estado, motivoBaja);
				return alquiler;
			}
		} catch (SQLException e) {
			System.err.println("Error al recuperar el alquiler: " + e.getMessage());
		}
		return null;
	}

	public List<Alquiler> listarTodos() {
		List<Alquiler> listaAlquileres = new ArrayList<>();
		String sql = "SELECT ID, IDCliente, IDVehiculo, FechaInicio, FechaDevolucionPactada, "
				+ "CASE WHEN FechaDevolucionFinal IS NULL THEN 'No finalizado' ELSE FechaDevolucionFinal END, "
				+ "CASE WHEN Estado = 'act' THEN 'Activo' WHEN Estado = 'fin' THEN 'Finalizado' WHEN Estado = 'venc' THEN 'Vencido' END AS Estado, "
				+ "CASE WHEN MotivoBaja IS NULL THEN 'N/A' ELSE MotivoBaja END AS MotivoBaja "
				+ "FROM Alquiler";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("ID");
				int idCliente = rs.getInt("IDCliente");
				int idVehiculo = rs.getInt("IDVehiculo");
				String fechaInicioStr = rs.getString("FechaInicio");
				String fechaDevolucionPactadaStr = rs.getString("FechaDevolucionPactada");
				String fechaDevolucionFinalStr = rs.getString("FechaDevolucionFinal");
				String estado = rs.getString("Estado");
				String motivoBaja = rs.getString("MotivoBaja");
				
				Cliente cliente = clienteDAO.buscarPorId(idCliente);
				Vehiculo vehiculo = vehiculoDAO.buscarPorId(idVehiculo);
				LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
				LocalDate fechaDevolucionPactada = LocalDate.parse(fechaDevolucionPactadaStr);
				LocalDate fechaDevolucionFinal = null;
				
				if (!"No finalizado".equalsIgnoreCase(fechaDevolucionFinalStr)) {
					try {
						fechaDevolucionFinal = LocalDate.parse(fechaDevolucionFinalStr);
					} catch (DateTimeParseException e) {
						System.err.println("Error al parsear la fecha de devolución: " + e.getMessage());
					}
				}
				
				Alquiler alquiler = new Alquiler(id, cliente, vehiculo, fechaInicio, fechaDevolucionPactada, fechaDevolucionFinal, estado, motivoBaja);
				listaAlquileres.add(alquiler);
			}
		} catch (SQLException e) {
			System.err.println("Error al recuperar la lista de alquileres: " + e.getMessage());
		}
		return listaAlquileres;
	}
	
	public List<Alquiler> listarPorCliente(int idCliente) {
		List<Alquiler> listaAlquileres = new ArrayList<>();
		String sql = "SELECT ID, IDCliente, IDVehiculo, FechaInicio, FechaDevolucionPactada, "
				+ "CASE WHEN FechaDevolucionFinal IS NULL THEN 'No finalizado' ELSE FechaDevolucionFinal END AS FechaDevolucionFinal, "
				+ "CASE WHEN Estado = 'act' THEN 'Activo' WHEN Estado = 'fin' THEN 'Finalizado' WHEN Estado = 'venc' THEN 'Vencido' END AS Estado, "
				+ "CASE WHEN MotivoBaja IS NULL THEN 'N/A' ELSE MotivoBaja END AS MotivoBaja "
				+ "FROM Alquiler WHERE IDCliente = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, idCliente);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("ID");
				int idVehiculo = rs.getInt("IDVehiculo");
				String fechaInicioStr = rs.getString("FechaInicio");
				String fechaDevolucionPactadaStr = rs.getString("FechaDevolucionPactada");
				String fechaDevolucionFinalStr = rs.getString("FechaDevolucionFinal");
				String estado = rs.getString("Estado");
				String motivoBaja = rs.getString("MotivoBaja");
				
				Cliente cliente = clienteDAO.buscarPorId(idCliente);
				Vehiculo vehiculo = vehiculoDAO.buscarPorId(idVehiculo);
				LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
				LocalDate fechaDevolucionPactada = LocalDate.parse(fechaDevolucionPactadaStr);
				LocalDate fechaDevolucionFinal = null;
				
				if (!"No finalizado".equalsIgnoreCase(fechaDevolucionFinalStr)) {
					try {
						fechaDevolucionFinal = LocalDate.parse(fechaDevolucionFinalStr);
					} catch (DateTimeParseException e) {
						System.err.println("Error al parsear la fecha de devolución: " + e.getMessage());
					}
				}
				
				Alquiler alquiler = new Alquiler(id, cliente, vehiculo, fechaInicio, fechaDevolucionPactada, fechaDevolucionFinal, estado, motivoBaja);
				listaAlquileres.add(alquiler);
			}
		} catch (SQLException e) {
			System.err.println("Error al recuperar la lista de alquileres del cliente: " + e.getMessage());
		}
		return listaAlquileres;
	}
	
	public List<Alquiler> listarNoFinalizados() {
		List<Alquiler> listaAlquileres = new ArrayList<>();
		String sql = "SELECT ID, IDCliente, IDVehiculo, FechaInicio, FechaDevolucionPactada, Estado "
				+ "FROM Alquiler WHERE Estado = 'act'";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("ID");
				int idCliente = rs.getInt("IDCliente");
				int idVehiculo = rs.getInt("IDVehiculo");
				LocalDate fechaInicio = LocalDate.parse(rs.getString("FechaInicio"));
				LocalDate fechaDevolucionPactada = LocalDate.parse(rs.getString("FechaDevolucionPactada"));
				String estado = rs.getString("Estado");
				
				Cliente cliente = clienteDAO.buscarPorId(idCliente);
				Vehiculo vehiculo = vehiculoDAO.buscarPorId(idVehiculo);
				
				long diasAtraso = calcularDiasAtraso(fechaDevolucionPactada);
				
				Alquiler alquiler = new Alquiler(id, cliente, vehiculo, fechaInicio, fechaDevolucionPactada, estado, diasAtraso);
				listaAlquileres.add(alquiler);
			}
		} catch (SQLException e) {
			System.err.println("Error al recuperar la lista de alquileres activos: " + e.getMessage());
		}
		return listaAlquileres;
	}
	
	public List<Alquiler> listarAtrasados() {
		List<Alquiler> listaAtrasados = new ArrayList<>();
		String sql = "SELECT ID, IDCliente, IDVehiculo, FechaInicio, FechaDevolucionPactada, Estado "
				+ "FROM Alquiler WHERE Estado = 'act'";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("ID");
				int idCliente = rs.getInt("IDCliente");
				int idVehiculo = rs.getInt("IDVehiculo");
				LocalDate fechaInicio = LocalDate.parse(rs.getString("FechaInicio"));
				LocalDate fechaDevolucionPactada = LocalDate.parse(rs.getString("FechaDevolucionPactada"));
				String estado = rs.getString("Estado");
				
				long diasAtraso = calcularDiasAtraso(fechaDevolucionPactada);
				if (diasAtraso > 0) {
					Cliente cliente = clienteDAO.buscarPorId(idCliente);
					Vehiculo vehiculo = vehiculoDAO.buscarPorId(idVehiculo);
					
					Alquiler alquiler = new Alquiler(id, cliente, vehiculo, fechaInicio, fechaDevolucionPactada, estado, diasAtraso);
					listaAtrasados.add(alquiler);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al recuperar los alquileres atrasados: " + e.getMessage());
		}
		return listaAtrasados;
	}
	
	public Alquiler buscarPorIdentificador(String idUsuario) {
		Integer userId = Integer.parseInt(idUsuario);
		String sql = "SELECT ID, IDCliente, IDVehiculo, FechaInicio, FechaDevolucionPactada, FechaDevolucionFinal, "
				+ "CASE WHEN Estado = 'act' THEN 'Activo WHEN Estado = 'fin' THEN 'Finalizado' WHEN Estado = 'venc' THEN 'Vencido' END AS Estado, "
				+ "CASE WHEN MotivoBaja IS NULL THEN 'N/A ELSE MotivoBaja END AS MotivoBaja"
				+ "WHERE IDCliente = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("ID");
				int idCliente = rs.getInt("IDCliente");
				int idVehiculo = rs.getInt("IDVehiculo");
				String fechaInicioStr = rs.getString("FechaInicio");
				String fechaDevolucionPactadaStr = rs.getString("FechaDevolucionPactada");
				String fechaDevolucionFinalStr = rs.getString("FechaDevolucionFinal");
				String estado = rs.getString("Estado");
				String motivoBaja = rs.getString("MotivoBaja");
				
				Cliente cliente = clienteDAO.buscarPorId(idCliente);
				Vehiculo vehiculo = vehiculoDAO.buscarPorId(idVehiculo);
				LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
				LocalDate fechaDevolucionPactada = LocalDate.parse(fechaDevolucionPactadaStr);
				LocalDate fechaDevolucionFinal = null;
				
				if (fechaDevolucionFinalStr == null) {
					try {
						fechaDevolucionFinal = LocalDate.parse(fechaDevolucionFinalStr);
					} catch (DateTimeParseException e) {
						System.err.println("Error al parsear la fecha de devolución: " + e.getMessage());
					}
				}
				
				Alquiler alquiler = new Alquiler(id, cliente, vehiculo, fechaInicio, fechaDevolucionPactada, fechaDevolucionFinal, estado, motivoBaja);
				return alquiler;
			}
		} catch (SQLException e) {
			System.err.println("Error al recuperar el alquiler: " + e.getMessage());
		}
		return null;
	}
	
	public Alquiler seleccionarAlquilerActivo(Scanner sc) {
		Alquiler alquilerSeleccionado = null;
		List<Alquiler> listaAlquileres = this.listarNoFinalizados();
		
		if (listaAlquileres.isEmpty()) {
			System.out.println("No hay alquileres activos ni vencidos");
			return null;
		}
		
		for (Alquiler a : listaAlquileres) {
			a.mostrarEnSelector();
		}
		
		System.out.print("Seleccione el alquiler a finalizar: ");
		int idSeleccionado = sc.nextInt();
		sc.nextLine();
		
		for (Alquiler a : listaAlquileres) {
			if (a.getId() == idSeleccionado) {
				alquilerSeleccionado = a;
				break;
			}
		}
		return alquilerSeleccionado;
	}
	
	public long calcularDiasAtraso(LocalDate fechaDevolucionPactada) {
		LocalDate hoy = LocalDate.now();
		if (hoy.isAfter(fechaDevolucionPactada)) {
			return ChronoUnit.DAYS.between(fechaDevolucionPactada, hoy);
		}
		return 0;
	}

	private void rollback(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
				System.out.println("Transaccion revertida debido a un error");
			} catch (SQLException rollbackEx) {
				System.err.println("Error al intentar revertir la transaccion: " + rollbackEx.getMessage());
			}
		}
	}
}
