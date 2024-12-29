package project.model;

import java.time.LocalDate;

public class Alquiler implements Seleccionable {
	private int id;
	private Cliente cliente;
	private Vehiculo vehiculo;
	private LocalDate fechaInicio;
	private LocalDate fechaDevolucionPactada;
	private LocalDate fechaDevolucionFinal;
	private String estado;
	private String motivoBaja;
	private long diasAtraso;
	
	public Alquiler() {}
	
	public Alquiler(Cliente cliente, Vehiculo vehiculo, LocalDate fechaInicio, LocalDate fechaDevolucionPactada) {
		this.setCliente(cliente);
		this.setVehiculo(vehiculo);
		this.setFechaInicio(fechaInicio);
		this.setFechaDevolucionPactada(fechaDevolucionPactada);
	}
	
	public Alquiler(int id, Cliente cliente, Vehiculo vehiculo, LocalDate fechaInicio, LocalDate fechaDevolucionPactada) {
		this.setId(id);
		this.setCliente(cliente);
		this.setVehiculo(vehiculo);
		this.setFechaInicio(fechaInicio);
		this.setFechaDevolucionPactada(fechaDevolucionPactada);
	}
	
	public Alquiler(int id, Cliente cliente, Vehiculo vehiculo, LocalDate fechaInicio, LocalDate fechaDevolucionPactada, String estado, long diasAtraso) {
		this.setId(id);
		this.setCliente(cliente);
		this.setVehiculo(vehiculo);
		this.setFechaInicio(fechaInicio);
		this.setFechaDevolucionPactada(fechaDevolucionPactada);
		this.setEstado(estado);
		this.setDiasAtraso(diasAtraso);
	}
	
	public Alquiler (int id, Cliente cliente, Vehiculo vehiculo, LocalDate fechaInicio, LocalDate fechaDevolucionPactada, LocalDate fechaDevolucionFinal, String estado, String motivoBaja) {
		this.setId(id);
		this.setCliente(cliente);
		this.setVehiculo(vehiculo);
		this.setFechaInicio(fechaInicio);
		this.setFechaDevolucionPactada(fechaDevolucionPactada);
		this.setFechaDevolucionFinal(fechaDevolucionFinal);
		this.setMotivoBaja(motivoBaja);
		this.setEstado(estado);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public LocalDate getFechaDevolucionPactada() {
		return fechaDevolucionPactada;
	}

	public void setFechaDevolucionPactada(LocalDate fechaDevolucionPactada) {
		this.fechaDevolucionPactada = fechaDevolucionPactada;
	}

	public LocalDate getFechaDevolucionFinal() {
		return fechaDevolucionFinal;
	}

	public void setFechaDevolucionFinal(LocalDate fechaDevolucionFinal) {
		this.fechaDevolucionFinal = fechaDevolucionFinal;
	}
	
	public void selectorAlquiler() {
		System.out.println("[" + this.getId() + "] [" + this.getCliente().getNombre() + " - " + this.getVehiculo().getMarcaModelo() + "] [Inicio del alquiler: " + this.getFechaInicio() + " - Fin del alquiler: " + this.getFechaDevolucionFinal() + "]");
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getMotivoBaja() {
		return motivoBaja;
	}

	public void setMotivoBaja(String motivoBaja) {
		this.motivoBaja = motivoBaja;
	}

	public void mostrarEnSelector() {
		System.out.println("[" + this.getId() + "] [" + this.getCliente().getNombre() + " - " + this.getVehiculo().getMarcaModelo() + "] [Inicio del alquiler: " + this.getFechaInicio() + " - Fin del alquiler: " + this.getFechaDevolucionPactada() + "]");
	}

	public long getDiasAtraso() {
		return diasAtraso;
	}

	public void setDiasAtraso(long diasAtraso) {
		this.diasAtraso = diasAtraso;
	}
}
