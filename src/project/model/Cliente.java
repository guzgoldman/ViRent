package project.model;

public class Cliente extends Persona implements Seleccionable {
	private int id;
	private String dni;
	private boolean activo;
	
	public Cliente() {
		super();
	}
	
	public Cliente(int id, String nombre, String direccion, String telefono, String dni) {
		super(nombre, direccion, telefono);
		this.setId(id);
		this.setDni(dni);	
	}
	
	public Cliente(String nombre, String direccion, String telefono, String dni) {
		super(nombre, direccion, telefono);
		this.setDni(dni);	
	}

	public void mostrarDatos() {
		System.out.println("[Numero: " + this.getId() + "]"
							+ " [Nombre: " + this.getNombre() + "]"
							+ " [Direccion: " + this.getDireccion() + "]"
							+ " [Telefono: " + this.getTelefono() + "]"
							+ " DNI: " + this.getDni() + "]");
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getDni() {
		return dni;
	}
	
	public void setDni(String dni) {
		this.dni = dni;
	}

	public void mostrarEnSelector() {
		System.out.println("[" + this.getId() + "] " + "[Nombre: " + this.getNombre() + " - DNI: " + this.getDni() +"]");
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
}
