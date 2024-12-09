package Programa;

import java.time.LocalDate;

public class Alquiler {
	private int id_alquiler;
	private Cliente cliente;
	private Vehiculo vehiculo;
	private LocalDate fecha_alquiler;
	private LocalDate fecha_devolucion_pactada;
	private LocalDate fecha_devolucion;
	private String estado;
	private String motivo_baja;
	
	public Alquiler (int id_alquiler, Cliente cliente, Vehiculo vehiculo, LocalDate fecha_alquiler, LocalDate fecha_devolucion_pactada, LocalDate fecha_devolucion, String estado, String motivo_baja) {
		this.setId_alquiler(id_alquiler);
		this.setCliente(cliente);
		this.setVehiculo(vehiculo);
		this.setFecha_alquiler(fecha_alquiler);
		this.setFecha_devolucion_pactada(fecha_devolucion_pactada);
		this.setFecha_devolucion(fecha_devolucion);
		this.setMotivo_baja(motivo_baja);
		this.setEstado(estado);
	}
	
	public Alquiler (int id_alquiler, Cliente cliente, Vehiculo vehiculo, LocalDate fecha_alquiler, LocalDate fecha_devolucion_pactada) {
		this.setId_alquiler(id_alquiler);
		this.setCliente(cliente);
		this.setVehiculo(vehiculo);
		this.setFecha_alquiler(fecha_alquiler);
		this.setFecha_devolucion_pactada(fecha_devolucion_pactada);
	}
	
	public Alquiler (int id_alquiler, Cliente cliente, Vehiculo vehiculo, LocalDate fecha_alquiler, LocalDate fecha_devolucion_pactada, LocalDate fecha_devolucion, String motivo_baja) {
		this.setId_alquiler(id_alquiler);
		this.setCliente(cliente);
		this.setVehiculo(vehiculo);
		this.setFecha_alquiler(fecha_alquiler);
		this.setFecha_devolucion_pactada(fecha_devolucion_pactada);
		this.setFecha_devolucion(fecha_devolucion);
		this.setMotivo_baja(motivo_baja);
	}
	
	public Alquiler (int id_alquiler, Cliente cliente, Vehiculo vehiculo, LocalDate fecha_alquiler, LocalDate fecha_devolucion, String estado, String motivo_baja) {
		this.setId_alquiler(id_alquiler);
		this.setCliente(cliente);
		this.setVehiculo(vehiculo);
		this.setFecha_alquiler(fecha_alquiler);
		this.setFecha_devolucion(fecha_devolucion);
		this.setEstado(estado);
		this.setMotivo_baja(motivo_baja);
	}
	
	public Alquiler (Cliente cliente, Vehiculo vehiculo, LocalDate fecha_alquiler, LocalDate fecha_devolucion_pactada) {
		this.setCliente(cliente);
		this.setVehiculo(vehiculo);
		this.setFecha_alquiler(fecha_alquiler);
		this.setFecha_devolucion_pactada(fecha_devolucion_pactada);
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

	public LocalDate getFecha_devolucion_pactada() {
		return fecha_devolucion_pactada;
	}

	public void setFecha_devolucion_pactada(LocalDate fecha_devolucion_pactada) {
		this.fecha_devolucion_pactada = fecha_devolucion_pactada;
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

	public String getMotivo_baja() {
		return motivo_baja;
	}

	public void setMotivo_baja(String motivo_baja) {
		this.motivo_baja = motivo_baja;
	}
}
