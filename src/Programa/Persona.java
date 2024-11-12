package Programa;

abstract class Persona {
	private String nombre;
	private String direccion;
	private String telefono;
	
	public Persona(String nombre, String direccion, String telefono) {
		this.setNombre(nombre);
		this.setDireccion(direccion);
		this.setTelefono(telefono);
	}
	
	public abstract void mostrarDatos();

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		if (nombre != null && !nombre.isEmpty()) {
			this.nombre = nombre;
		} else {
			System.out.println("Error: Nombre inválido");
		}
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
}