package project.db;
import project.model.Vehiculo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VehiculoDAO implements DAO<Vehiculo> {
	private Connection conn;
	
	public VehiculoDAO(Connection conn) {
		this.conn = conn;
	}
	
	public void insertar(Vehiculo vehiculo) {
		String sql = "INSERT INTO Vehiculo(Marca, Modelo, Anio, Matricula, Tipo) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, vehiculo.getMarca());
			stmt.setString(2, vehiculo.getModelo());
			stmt.setInt(3, vehiculo.getAnio());
			stmt.setString(4, vehiculo.getMatricula());
			stmt.setString(5, vehiculo.getTipo());
			int filasCreadas = stmt.executeUpdate();
			
			if (filasCreadas == 0) {
				throw new SQLException();
			}
		} catch (SQLException e) {
			System.err.println("Error al registrar el vehiculo: " + e.getMessage());
		}
	}

	public void actualizar(Vehiculo vehiculo) {
		String sql = "UPDATE Vehiculo SET Marca = ?, Modelo = ?, Anio = ?, Tipo = ? WHERE Matricula = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, vehiculo.getMarca());
			stmt.setString(2, vehiculo.getModelo());
			stmt.setInt(3, vehiculo.getAnio());
			stmt.setString(4, vehiculo.getTipo());
			stmt.setString(5, vehiculo.getMatricula());
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error al actualizar los datos del vehiculo: " + e.getMessage());
		}
	}
	
	public boolean modificarEstado(int id, String estadoAnterior, String estadoActual, String descripcion) {
		String sql = "{CALL CambiarEstadoVehiculo(?, ?, ?, ?)}";
		try (CallableStatement stmt = conn.prepareCall(sql)) {
			stmt.setInt(1, id);
			stmt.setString(2, estadoAnterior);
			stmt.setString(3, estadoActual);
			stmt.setString(4, descripcion);
			boolean hayResultados = stmt.execute();
			boolean correcto = false;
			
			if (hayResultados) {
				ResultSet rs = stmt.getResultSet();
				if (rs.next()) {
					correcto = rs.getInt("filasAfectadas") > 0;
				}
			}
			
			if (correcto) {
				switch (estadoActual) {
					case "baja":
						System.out.println("Vehiculo ID " + id + " dado de baja correctamente");
						break;
					case "rep":
						System.out.println("Vehiculo ID " + id + " asignado a reparacion correctamente");
						break;
					case "disp":
						System.out.println("Vehiculo ID " + id + " puesto a disponibilidad correctamente");
						break;
				}
				return true;
			} else {
				System.out.println("Vehiculo ID: " + id + " no encontrado");
				return false;
			}
		} catch (SQLException e) {
			System.err.println("Error al eliminar al vehiculo ID " + id + ": " + e.getMessage());
			return false;
		}
	}

	public Vehiculo buscarPorId(int id) {
		String sql = "SELECT ID, Marca, Modelo, Anio, Matricula, Tipo, Estado "
				+ "FROM Vehiculo WHERE ID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				Vehiculo vehiculo = new Vehiculo();
				vehiculo.setId(rs.getInt("ID"));
				vehiculo.setMarca(rs.getString("Marca"));
				vehiculo.setModelo(rs.getString("Modelo"));
				vehiculo.setAnio(rs.getInt("Anio"));
				vehiculo.setMatricula(rs.getString("Matricula"));
				vehiculo.setTipo(rs.getString("Tipo"));
				vehiculo.setEstado(rs.getString("Estado"));
				return vehiculo;
			}
		} catch (SQLException e) {
			System.err.println("Error al recuperar el vehículo: " + e.getMessage());
		}
		return null;
	}

	public List<Vehiculo> listarTodos() {
		List<Vehiculo> listaVehiculos = new ArrayList<>();
		String sql = "SELECT ID, Marca, Modelo, Anio, Matricula, Tipo, "
				+ "CASE WHEN Estado = 'disp' THEN 'Disponible' "
				+ "WHEN Estado = 'alq' THEN 'Ocupado' "
				+ "WHEN Estado = 'rep' THEN 'En reparación' "
				+ "WHEN Estado = 'baja' THEN 'Dado de baja' END AS Estado";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Vehiculo vehiculo = new Vehiculo();
				vehiculo.setId(rs.getInt("ID"));
				vehiculo.setMarca(rs.getString("Marca"));
				vehiculo.setModelo(rs.getString("Modelo"));
				vehiculo.setAnio(rs.getInt("Anio"));
				vehiculo.setMatricula(rs.getString("Matricula"));
				vehiculo.setTipo(rs.getString("Tipo"));
				vehiculo.setEstado(rs.getString("Estado"));
				listaVehiculos.add(vehiculo);
			}
		} catch (SQLException e) {
			System.err.println("Error al listar los vehiculos: " + e.getMessage());
		}
		return listaVehiculos;
	}
	
	public List<Vehiculo> listarDisponiblesPorTipo(String tipo) {
		List<Vehiculo> listaVehiculos = new ArrayList<>();
		String sql = "SELECT ID, Marca, Modelo, Anio, Matricula, Tipo, Estado FROM Vehiculo "
				+ "WHERE Estado = 'disp' AND Tipo = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, tipo);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Vehiculo vehiculo = new Vehiculo();
				vehiculo.setId(rs.getInt("ID"));
				vehiculo.setMarca(rs.getString("Marca"));
				vehiculo.setModelo(rs.getString("Modelo"));
				vehiculo.setAnio(rs.getInt("Anio"));
				vehiculo.setMatricula(rs.getString("Matricula"));
				vehiculo.setTipo(rs.getString("Tipo"));
				vehiculo.setEstado(rs.getString("Estado"));
				listaVehiculos.add(vehiculo);
			}
		} catch (SQLException e) {
			System.err.println("Error al listar los vehiculos disponibles: " + e.getMessage());
		}
		return listaVehiculos;
	}
	
	public List<Vehiculo> listarPorMarca(String marca) {
		List<Vehiculo> listaVehiculos = new ArrayList<>();
		String sql = "SELECT ID, Marca, Modelo, Anio, Matricula, Tipo, Estado "
				+ "FROM Vehiculo WHERE UPPER(Marca) LIKE ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, "%" + marca.toUpperCase() + "%");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Vehiculo vehiculo = new Vehiculo();
				vehiculo.setId(rs.getInt("ID"));
				vehiculo.setMarca(rs.getString("Marca"));
				vehiculo.setModelo(rs.getString("Modelo"));
				vehiculo.setAnio(rs.getInt("Anio"));
				vehiculo.setMatricula(rs.getString("Matricula"));
				vehiculo.setTipo(rs.getString("Tipo"));
				vehiculo.setEstado(rs.getString("Estado"));
				listaVehiculos.add(vehiculo);
			}
		} catch (SQLException e) {
			System.err.println("Error al listar los vehiculos por marca: " + e.getMessage());
		}
		return listaVehiculos;
	}
	
	public List<Vehiculo> listarPorEstado(String estado) {
		List<Vehiculo> listaVehiculos = new ArrayList<>();
		String sql = "SELECT ID, Marca, Modelo, Anio, Matricula, Tipo, Estado "
				+ "FROM Vehiculo WHERE Estado = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, estado);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Vehiculo vehiculo = new Vehiculo();
				vehiculo.setId(rs.getInt("ID"));
				vehiculo.setMarca(rs.getString("Marca"));
				vehiculo.setModelo(rs.getString("Modelo"));
				vehiculo.setAnio(rs.getInt("Anio"));
				vehiculo.setMatricula(rs.getString("Matricula"));
				vehiculo.setTipo(rs.getString("Tipo"));
				vehiculo.setEstado(rs.getString("Estado"));
				listaVehiculos.add(vehiculo);
			}
		} catch (SQLException e) {
			System.err.println("Error al listar los vehiculos por estado: " + e.getMessage());
		}
		return listaVehiculos;
	}
	
	public List<Vehiculo> listarPorTipo(String tipo) {
		List<Vehiculo> listaVehiculos = new ArrayList<>();
		String sql = "SELECT ID, Marca, Modelo, Anio, Matricula, Tipo, Estado "
				+ "FROM Vehiculo WHERE Tipo = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, tipo);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Vehiculo vehiculo = new Vehiculo();
				vehiculo.setId(rs.getInt("ID"));
				vehiculo.setMarca(rs.getString("Marca"));
				vehiculo.setModelo(rs.getString("Modelo"));
				vehiculo.setAnio(rs.getInt("Anio"));
				vehiculo.setMatricula(rs.getString("Matricula"));
				vehiculo.setTipo(rs.getString("Tipo"));
				vehiculo.setEstado(rs.getString("Estado"));
				listaVehiculos.add(vehiculo);
			}
		} catch (SQLException e) {
			System.err.println("Error al listar los vehiculos por marca: " + e.getMessage());
		}
		return listaVehiculos;
	}

	public Vehiculo buscarPorIdentificador(String matricula) {
		String sql = "SELECT ID, Marca, Modelo, Anio, Tipo, Estado FROM Vehiculo WHERE Matricula = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, matricula);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				Vehiculo vehiculo = new Vehiculo();
				vehiculo.setId(rs.getInt("ID"));
				vehiculo.setMarca(rs.getString("Marca"));
				vehiculo.setModelo(rs.getString("Modelo"));
				vehiculo.setAnio(rs.getInt("Anio"));
				vehiculo.setTipo(rs.getString("Tipo"));
				vehiculo.setEstado(rs.getString("Estado"));
				return vehiculo;
			}
		} catch (SQLException e) {
			System.err.println("Error al recuperar el vehiculo: " + e.getMessage());
		}
		return null;
	}

	public boolean hayDisponibilidad() {
		String sql = "SELECT COUNT(*) AS Cantidad FROM Vehiculo WHERE Estado = 'disp'";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				int contadorDisponibles= rs.getInt("Cantidad");
				return contadorDisponibles > 0;
			}
		} catch (SQLException e) {
			System.err.println("Error al recuperar los vehiculos disponibles: " + e.getMessage());
		}
		return false;
	}
	
	public Vehiculo seleccionarVehiculo(Scanner sc, String tipo) {
		Vehiculo vehiculoSeleccionado = null;
		List<Vehiculo> listaVehiculosDisponibles = this.listarDisponiblesPorTipo(tipo);
		
		if (listaVehiculosDisponibles.isEmpty()) {
			System.out.println("No hay vehiculos disponibles");
			return null;
		}
		
		for (Vehiculo v : listaVehiculosDisponibles) {
			v.mostrarEnSelector();
		}
		
		System.out.print("Seleccion el vehiculo: ");
		int idSeleccionado = sc.nextInt();
		sc.nextLine();
		
		for (Vehiculo v : listaVehiculosDisponibles) {
			if (v.getId() == idSeleccionado) {
				vehiculoSeleccionado = v;
				break;
			}
		}
		return vehiculoSeleccionado;
	}
	
	public boolean estaEnReparacion(int id) {
		String sql = "SELECT COUNT(*) FROM Vehiculo WHERE ID = ? AND Estado = 'rep'";
		boolean enReparacion = false;
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				enReparacion = rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			System.err.println("Error al recuperar la informacion del vehiculo: " + e.getMessage());
		}
		return enReparacion;
	}
	
	public boolean estaDadoDeBaja(int id) {
		String sql = "SELECT COUNT(*) FROM Vehiculo WHERE ID = ? AND Estado = 'baja'";
		boolean dadoDeBaja = false;
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				dadoDeBaja = rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			System.err.println("Error al recuperar la informacion del vehiculo: " + e.getMessage());
		}
		return dadoDeBaja;
	}
	
	public boolean estaDisponible(int id) {
		String sql = "SELECT COUNT(*) FROM Vehiculo WHERE ID = ? AND Estado = 'disp'";
		boolean disponible = false;
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				disponible = rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			System.err.println("Error al recuperar la informacion del vehiculo: " + e.getMessage());
		}
		return disponible;
	}
	
	public boolean estaAlquilado(int id) {
		String sql = "SELECT COUNT(*) FROM Vehiculo WHERE ID = ? AND Estado = 'alq'";
		boolean alquilado = false;
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				alquilado = rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			System.err.println("Error al recuperar la informacion del vehiculo: " + e.getMessage());
		}
		return alquilado;
	}
}
