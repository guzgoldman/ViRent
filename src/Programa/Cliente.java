package Programa;

class Cliente extends Persona {
	private int id_cliente;
	private String dni;
	
	public Cliente(int id_cliente, String nombre, String direccion, String telefono, String dni) {
		super(nombre, direccion, telefono);
		this.setId(id_cliente);
		this.setDni(dni);	
	}
	
	public Cliente(String nombre, String direccion, String telefono, String dni) {
		super(nombre, direccion, telefono);
		this.setDni(dni);	
	}

	@Override
	public void mostrarDatos() {
		System.out.println("[Nº de cliente: " + this.getId() + "]"
							+ " [Nombre: " + this.getNombre() + "]"
							+ " [Dirección: " + this.getDireccion() + "]"
							+ " [Teléfono: " + this.getTelefono() + "]"
							+ " DNI Nº: " + this.getDni() + "]");
	}
	
	public int getId() {
		return id_cliente;
	}
	
	public void setId(int id_cliente) {
		this.id_cliente = id_cliente;
	}
	
	public String getDni() {
		return dni;
	}
	
	public void setDni(String dni) {
		this.dni = dni;
	}
	
	public void selectorClientePorId() {
		System.out.println("[" + this.getId() + "] " + "[Nombre: " + this.getNombre() + " - DNI: " + this.getDni() +"]");
	}
}
