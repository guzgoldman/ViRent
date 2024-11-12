package Programa;

public class Vehiculo {
	private int id_vehiculo;
	private String marca;
	private String modelo;
	private int anio;
	private String matricula;
	private String tipo; //Auto, Camioneta, SUV, etc.
	private String estado;
	
	public Vehiculo(int id_vehiculo, String marca, String modelo, String matricula, int anio, String estado) {
		this.setId_vehiculo(id_vehiculo);
		this.setMarca(marca);
		this.setModelo(modelo);
		this.setAnio(anio);
		this.setMatricula(matricula);
		this.setEstado(estado);
	}
	
	public Vehiculo(int id_vehiculo, String marca, String modelo, int anio, String matricula, String tipo) {
		this.setId_vehiculo(id_vehiculo);
		this.setMarca(marca);
		this.setModelo(modelo);
		this.setAnio(anio);
		this.setMatricula(matricula);
		this.setTipo(tipo);
	}
	
	public Vehiculo(int id_vehiculo, String marca, String modelo, int anio, String matricula, String tipo, String estado) {
		this.setId_vehiculo(id_vehiculo);
		this.setMarca(marca);
		this.setModelo(modelo);
		this.setAnio(anio);
		this.setMatricula(matricula);
		this.setTipo(tipo);
		this.setEstado(estado);
	}
	
	public Vehiculo(String marca, String modelo, int anio, String matricula, String tipo) {
		this.setMarca(marca);
		this.setModelo(modelo);
		this.setAnio(anio);
		this.setMatricula(matricula);
		this.setTipo(tipo);
	}

	public int getId_vehiculo() {
		return id_vehiculo;
	}

	public void setId_vehiculo(int id_vehiculo) {
		this.id_vehiculo = id_vehiculo;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	
	public String getMarcaModelo() {
		return this.getMarca() + " " + this.getModelo() + " - Matrícula: " + this.getMatricula();
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public void mostrarDatos() {
		System.out.println("[" + this.getMarca() + " " + this.getModelo() + " (" + this.getAnio() + ") - " + "Matrícula: " + this.getMatricula() + "]");
	}
	
	public void selectorVehiculoPorId() {
		System.out.println("["+this.getId_vehiculo()+"]"
						+ " [" + this.getMarca() + " " + this.getModelo() + " - Matrícula: " + this.getMatricula() + "]");
	}
}
