package Programa;

import java.time.LocalDate;

public class Alquiler {
	private int id_alquiler;
	private Cliente cliente;
	private Vehiculo vehiculo;
	private LocalDate fecha_alquiler;
	private LocalDate fecha_devolucion;
	private String estado;
	
	public Alquiler (int id_alquiler, Cliente cliente, Vehiculo vehiculo, LocalDate fecha_alquiler, LocalDate fecha_devolucion) {
		this.setId_alquiler(id_alquiler);
		this.setCliente(cliente);
		this.setVehiculo(vehiculo);
		this.setFecha_alquiler(fecha_alquiler);
		this.setFecha_devolucion(fecha_devolucion);
	}
	
	public Alquiler (int id_alquiler, Cliente cliente, Vehiculo vehiculo, LocalDate fecha_alquiler, LocalDate fecha_devolucion, String estado) {
		this.setId_alquiler(id_alquiler);
		this.setCliente(cliente);
		this.setVehiculo(vehiculo);
		this.setFecha_alquiler(fecha_alquiler);
		this.setFecha_devolucion(fecha_devolucion);
		this.setEstado(estado);
	}
	
	public Alquiler (Cliente cliente, Vehiculo vehiculo, LocalDate fecha_alquiler, LocalDate fecha_devolucion) {
		this.setCliente(cliente);
		this.setVehiculo(vehiculo);
		this.setFecha_alquiler(fecha_alquiler);
		this.setFecha_devolucion(fecha_devolucion);
	}

	public int getId_alquiler() {
		return id_alquiler;
	}

	public void setId_alquiler(int id_alquiler) {
		this.id_alquiler = id_alquiler;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public LocalDate getFecha_alquiler() {
		return fecha_alquiler;
	}

	public void setFecha_alquiler(LocalDate fecha_alquiler) {
		this.fecha_alquiler = fecha_alquiler;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public LocalDate getFecha_devolucion() {
		return fecha_devolucion;
	}

	public void setFecha_devolucion(LocalDate fecha_devolucion) {
		this.fecha_devolucion = fecha_devolucion;
	}
	
	public void selectorAlquiler() {
		System.out.println("[" + this.getId_alquiler() + "] [" + this.getCliente().getNombre() + " - " + this.getVehiculo().getMarcaModelo() + "] [Inicio del alquiler: " + this.getFecha_alquiler() + " - Fin del alquiler: " + this.getFecha_devolucion() + "]");
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
