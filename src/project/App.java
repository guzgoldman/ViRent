package project;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

// import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;

import project.db.Conn;
import project.db.AlquilerDAO;
import project.db.ClienteDAO;
import project.db.VehiculoDAO;
import project.model.Alquiler;
import project.model.Cliente;
import project.model.Vehiculo;

public class App {

	public static void main(String[] args) {
		Conn cnx = new Conn("jdbc:mysql://localhost:3306/database","root","password");
		Scanner sc = new Scanner(System.in);
		
		try (Connection conn = cnx.getConnection()) {
			ClienteDAO clienteDAO = new ClienteDAO(conn);
			VehiculoDAO vehiculoDAO = new VehiculoDAO(conn);
			AlquilerDAO alquilerDAO = new AlquilerDAO(conn, clienteDAO, vehiculoDAO);
			while (true) {
				byte opcion = menuOpciones(sc);
				
				switch (opcion) {
					case 1:
						insertarCliente(conn, clienteDAO);
						continue;
					case 2:
						bajaCliente(sc, conn, clienteDAO);
						continue;
					case 3:
						buscarCliente(sc, conn, clienteDAO);
						continue;
					case 4:
						mostrarClientes(conn, clienteDAO);
						continue;
					case 5:
						actualizarDatosCliente(sc, conn, clienteDAO);
						continue;
					case 6:
						insertarVehiculo(sc, conn, vehiculoDAO);
						continue;
					case 7:
						modificarEstadoVehiculo(sc, conn, vehiculoDAO);
						continue;
					case 8:
						buscarVehiculo(sc, conn, vehiculoDAO);
						continue;
					case 9:
						actualizarDatosVehiculo(sc, conn, vehiculoDAO);
						continue;
					case 10:
						iniciarAlquiler(sc, conn, vehiculoDAO, clienteDAO, alquilerDAO);
						continue;
					case 11:
						mostrarAlquileresActivos(conn, alquilerDAO);
						continue;
					case 12:
						mostrarAlquileresAtrasados(conn, alquilerDAO);
						continue;
					case 13:
						mostrarAlquileresPorUsuario(conn, sc, alquilerDAO, clienteDAO);
						continue;
					case 14:
						finalizarAlquiler(conn, sc, alquilerDAO);
						continue;
					case 15:
						break;
					default:
						System.out.println("Advertencia: Opcion no valida");
						continue;
				}
				break;
			}
		} catch (Exception e) {
			System.err.println("Error al iniciar la aplicación: " + e.getMessage());
		}
		sc.close();
	}
	
	private static byte menuOpciones(Scanner sc) {
		System.out.println("Menu de opciones:");
		System.out.println("[1] Registrar cliente");
		System.out.println("[2] Dar de baja a cliente");
		System.out.println("[3] Buscar cliente");
		System.out.println("[4] Listado de clientes registrados");
		System.out.println("[5] Actualizar datos de cliente");
		System.out.println("[6] Registrar vehiculo");
		System.out.println("[7] Actualizar estado de vehiculo");
		System.out.println("[8] Buscar vehiculo");
		System.out.println("[9] Actualizar datos de vehiculo");
		System.out.println("[10] Iniciar proceso de alquiler");
		System.out.println("[11] Listado de alquileres activos");
		System.out.println("[12] Listado de alquileres atrasados");
		System.out.println("[13] Listado de alquileres por usuario");
		System.out.println("[14] Finalizar alquiler (activo o vencido)");
		System.out.println("[15] Salir del programa");
		byte opcion = selectorOpcion(sc);
		return opcion;
	}
	
    public static void insertarCliente(Connection conn, ClienteDAO clienteDAO) {
        JFrame frame = new JFrame("Registrar nuevo cliente");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblNombre = new JLabel("Nombre completo:");
        JTextField txtNombre = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        frame.add(lblNombre, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(txtNombre, gbc);

        JLabel lblDireccion = new JLabel("Direccion:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        frame.add(lblDireccion, gbc);

        JLabel lblCalle = new JLabel("Calle:");
        JTextField txtCalle = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        frame.add(lblCalle, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        frame.add(txtCalle, gbc);

        JLabel lblAltura = new JLabel("Altura:");
        JTextField txtAltura = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        frame.add(lblAltura, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        frame.add(txtAltura, gbc);

        JLabel lblAlturaInfo1 = new JLabel("(Ejemplo: 1797, 'S/N', 'Km. 21')");
        lblAlturaInfo1.setFont(new Font("Arial", Font.ITALIC, 10));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        frame.add(lblAlturaInfo1, gbc);

        JLabel lblCiudad = new JLabel("Ciudad:");
        JTextField txtCiudad = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        frame.add(lblCiudad, gbc);
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        frame.add(txtCiudad, gbc);

        JLabel lblProvincia = new JLabel("Provincia:");
        String[] provincias = {"Ciudad Autonoma de Buenos Aires", "Provincia de Buenos Aires", "Cordoba", "Santa Fe", "Mendoza", "Tucuman", "Salta", "Jujuy", "Chaco", "Misiones", "Formosa", "San Luis", "Santiago del Estero", "San Juan", "Catamarca", "La Rioja", "Corrientes", "Entre Rios", "La Pampa", "Neuquen", "Rio Negro", "Chubut", "Santa Cruz", "Tierra del Fuego"};
        JComboBox<String> cbProvincia = new JComboBox<>(provincias);
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        frame.add(lblProvincia, gbc);
        gbc.gridx = 3;
        gbc.gridy = 6;
        frame.add(cbProvincia, gbc);
        
        JLabel lblTelefono = new JLabel("Telefono:");
        JTextField txtTelefono = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        frame.add(lblTelefono, gbc);
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        frame.add(txtTelefono, gbc);

        JLabel lblDNI = new JLabel("DNI:");
        JTextField txtDNI = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        frame.add(lblDNI, gbc);
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        frame.add(txtDNI, gbc);

        JButton btnGuardar = new JButton("Dar de alta");
        JButton btnCancelar = new JButton("Cancelar");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 3;
        frame.add(buttonPanel, gbc);

        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = txtNombre.getText().trim();
                String calle = txtCalle.getText().trim();
                String altura = txtAltura.getText().trim();
                String ciudad = txtCiudad.getText().trim();
                String provincia = (String) cbProvincia.getSelectedItem();
                String telefono = txtTelefono.getText().trim();
                String dni = txtDNI.getText().trim();
                
                String direccion = calle + " " + altura + ", " + ciudad + ", " + provincia;

                if (!validarNombre(nombre)) {
                    JOptionPane.showMessageDialog(frame, "Error en el ingreso del nombre. Verifique la informacion ingresada", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!validarDireccion(direccion)) {
                    JOptionPane.showMessageDialog(frame, "Error en el ingreso de la direccion. Verifique la informacion ingresada", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!validarTelefono(telefono)) {
                    JOptionPane.showMessageDialog(frame, "Error en el ingreso del telefono. Verifique la informacion ingresada", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!validarDNI(dni)) {
                    JOptionPane.showMessageDialog(frame, "Error en el ingreso del DNI. Verifique la informacion ingresada", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Cliente cliente = new Cliente(nombre, direccion, telefono, dni);

                try {
            		clienteDAO.insertar(cliente);
                    JOptionPane.showMessageDialog(frame, "El cliente ha sido dado de alta", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                } catch (Exception ex) { // Excepcion de PK duplicadas eliminada, hay que manejarla para tener más info de un error.
                    JOptionPane.showMessageDialog(frame, "Error al dar de alta al nuevo cliente. Intentelo nuevamente o contacte al soporte tecnico.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancelar.addActionListener(e -> frame.dispose());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
	
	private static void bajaCliente(Scanner sc, Connection conn, ClienteDAO clienteDAO) {
		while (true) {
			int id = 0;
			Cliente cliente;
			System.out.println("[1] Buscar por ID");
			System.out.println("[2] Regresar al menu principal");
			byte opcion = selectorOpcion(sc);
			
			switch (opcion) {
				case 1:
					try {
						System.out.print("Ingrese el ID del cliente: ");
						id = sc.nextInt();
					} catch (InputMismatchException e) {
						System.out.println("Error: Este campo solo admite numeros");
						sc.nextLine();
					}
					
					cliente = clienteDAO.buscarPorId(id);
					if (cliente != null) {
						clienteDAO.darDeBaja(id);
						System.out.println("El cliente ha sido dado de baja");
					} else {
						System.out.println("Cliente no encontrado");
						continue;
					}
					break;
				case 3:
					break;
				default:
					System.out.println("Advertencia: Opcion no valida");
					continue;
			}
			break;
		}
	}
	
	private static void buscarCliente(Scanner sc, Connection conn, ClienteDAO clienteDAO) {
		while (true) {
			String dni = null;
			int id = 0;
			Cliente cliente = null;
			System.out.println("[1] Buscar por ID");
			System.out.println("[2] Buscar por DNI");
			System.out.println("[3] Regresar al menu principal");
			byte opcion = selectorOpcion(sc);
			
			switch (opcion) {
				case 1:
					try {
						System.out.print("Ingrese el ID del cliente: ");
						id = sc.nextInt();
					} catch (InputMismatchException e) {
						System.out.println("Error: Este campo solo admite numeros");
						sc.nextLine();
					}
					
					cliente = clienteDAO.buscarPorId(id);
					
					if (cliente != null) {
						cliente.mostrarDatos();
					} else {
						System.out.println("Cliente no encontrado");
					}
					break;
				case 2:
					while (true) {
						System.out.print("Ingrese el DNI del cliente: ");
						dni = sc.nextLine();
						if (validarDNI(dni)) {
							cliente = clienteDAO.buscarPorIdentificador(dni);
							break;
						} else {
							System.out.println("Error: El DNI debe contener entre 6 y 9 caracteres numericos, sin puntos ni espacios");
						}
					}
					
					if (cliente != null) {
						cliente.mostrarDatos();
					} else {
						System.out.println("Cliente no encontrado");
					}
					break;
				case 3:
					break;
				default:
					continue;
			}
			break;
		}
	}
	
	private static void mostrarClientes(Connection conn, ClienteDAO clienteDAO) {
		JFrame frame = new JFrame("Listado de clientes");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1100, 300);
		
		DefaultTableModel model = new DefaultTableModel();
		JTable table = new JTable(model);
		
		model.addColumn("ID del cliente");
		model.addColumn("Nombre completo");
		model.addColumn("Direccion");
		model.addColumn("Telefono");
		model.addColumn("DNI");
		
		List<Cliente> clientes = clienteDAO.listarTodos();
		
		for (Cliente c : clientes) {
			model.addRow(new Object[] {
					c.getId(),
					c.getNombre(),
					c.getDireccion(),
					c.getTelefono(),
					c.getDni()
			});
		}
		
		frame.add(new JScrollPane(table));
		frame.setVisible(true);
	}
	
	private static void actualizarDatosCliente(Scanner sc, Connection conn, ClienteDAO clienteDAO) {
		int id;
		Cliente cliente;
		while (true) {
			try {
				System.out.print("Ingresa el ID del cliente a modificar: ");
				id = sc.nextInt();
				sc.nextLine();
				break;
			} catch (InputMismatchException e) {
				System.out.println("Error: Este campo solo admite numeros");
				continue;
			}
		}
		
		cliente = clienteDAO.buscarPorId(id);
	    
	    if (cliente != null) {
	    	JFrame frame = new JFrame("Modificar datos del cliente: " + cliente.getNombre());
		    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		    
		    JPanel panel = new JPanel();
		    panel.setLayout(new GridLayout(5, 2, 10, 10));
		    
		    JLabel lblNombre = new JLabel("Nombre:");
		    JTextField txtNombre = new JTextField(cliente.getNombre());
		    JLabel lblDireccion = new JLabel("Direccion:");
		    JTextField txtDireccion = new JTextField(cliente.getDireccion());
		    JLabel lblTelefono = new JLabel("Telefono:");
		    JTextField txtTelefono = new JTextField(String.valueOf(cliente.getTelefono()));
		    JLabel lblDni = new JLabel("DNI:");
		    JTextField txtDni = new JTextField(String.valueOf(cliente.getDni()));
		    
		    panel.add(lblNombre);
		    panel.add(txtNombre);
		    panel.add(lblDireccion);
		    panel.add(txtDireccion);
		    panel.add(lblTelefono);
		    panel.add(txtTelefono);
		    panel.add(lblDni);
		    panel.add(txtDni);
		    
		    JButton btnGuardar = new JButton("Guardar cambios");
		    final int idCliente = id;
		    btnGuardar.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		            Cliente clienteActualizado = new Cliente(
		                idCliente,
		                txtNombre.getText().isEmpty() ? null : txtNombre.getText(),
		                txtDireccion.getText().isEmpty() ? null : txtDireccion.getText(),
		                txtTelefono.getText().isEmpty() ? null : txtTelefono.getText(),
		                txtDni.getText().isEmpty() ? null : txtDni.getText()
		            );
		            
		            try {
		            	clienteDAO.actualizar(clienteActualizado);
		                JOptionPane.showMessageDialog(frame, "Datos actualizados correctamente");
		                frame.dispose();
		    		} catch (Exception ex) { // Eliminadas excepciones para duplicados y exceso de chars, manejarlas.
		    			System.err.println("Error al modificar datos del cliente: " + ex.getMessage());
		    		}
		        }
		    });
		    
		    panel.add(new JLabel());
		    panel.add(btnGuardar);
		    
		    frame.getContentPane().add(panel, BorderLayout.CENTER);
		    frame.pack();
		    frame.setLocationRelativeTo(null);
		    frame.setVisible(true);
	    } else {
	    	System.out.println("Cliente no encontrado");
	    }
	}
	
	private static void insertarVehiculo(Scanner sc, Connection conn, VehiculoDAO vehiculoDAO) {
		int anio;
		int anioActual = LocalDate.now().getYear();
		int anioLimite = anioActual - 10;
		String tipo;
		String matricula;
		System.out.print("Ingrese la marca del vehiculo: ");
		String marca = sc.nextLine();
		 
		System.out.print("Ingrese el modelo del vehiculo: ");
		String modelo = sc.nextLine();
		
		while (true) {
			try {
				System.out.print("Ingrese el anio de fabricacion del vehiculo: ");
				anio = sc.nextInt();
				sc.nextLine();
					if (anio >= anioLimite) {
						break;
					} else {
						System.out.println("Error: Solo se admiten vehiculos fabricados a partir de " + anioLimite);
					}
			} catch (InputMismatchException e) {
				System.out.println("Error: Este campo solo admite numeros");
				sc.nextLine();
			}
		}
		
		while (true) {
			System.out.print("Ingrese la matricula del vehiculo: ");
			matricula = sc.nextLine();
			if (validarMatricula(matricula)) {
				break;
			} else {
				System.out.println("Error: Formato de matricula no valido");
			}
		}
		
		while (true) {
			System.out.println("Seleccione el tipo de vehiculo");
			System.out.println("[1] Auto");
			System.out.println("[2] Camioneta");
			System.out.println("[3] Moto");
			byte opcion = selectorOpcion(sc);
			
			switch (opcion) {
				case 1:
					tipo = "Auto";
					break;
				case 2:
					tipo = "Camioneta";
					break;
				case 3:
					tipo = "Moto";
					break;
				default:
					System.out.println("Error: Opcion no valida");
					continue;
			}
			break;
		}
		
		Vehiculo vehiculo = new Vehiculo(marca, modelo, anio, matricula, tipo);
		try {
			vehiculoDAO.insertar(vehiculo);
			System.out.println("Vehiculo registrado correctamente");
		} catch (Exception e) {
			System.err.println("Error al insertar el vehiculo: " + e.getMessage());
		}
		
	}

	private static void modificarEstadoVehiculo(Scanner sc, Connection conn, VehiculoDAO vehiculoDAO) {
		while (true) {
			String matricula;
			int id = 0;
			Vehiculo vehiculo;
			System.out.println("[1] Buscar vehiculo por ID");
			System.out.println("[2] Buscar vehiculo por matricula");
			System.out.println("[3] Regresar al menu principal");
			byte opcion = selectorOpcion(sc);
			
			switch (opcion) {
				case 1:
					try {
						System.out.print("Ingrese el ID del vehiculo: ");
						id = sc.nextInt();
					} catch (InputMismatchException e) {
						System.out.println("Error: Este campo solo admite numeros");
						sc.nextLine();
					}
					
					vehiculo = vehiculoDAO.buscarPorId(id);
					if (vehiculo != null) {
						procesoModificarEstadoVehiculo(sc, vehiculo, vehiculoDAO);
					} else {
						System.out.println("Vehiculo no encontrado");
						continue;
					}
					break;
				case 2:
					while (true) {
						System.out.print("Ingrese la matricula del vehiculo: ");
						matricula = sc.nextLine();
						if (validarMatricula(matricula)) {
							break;
						} else {
							System.out.println("Error: Formato de matricula no valido");
						}
					}
					
					vehiculo = vehiculoDAO.buscarPorIdentificador(matricula);
					if (vehiculo != null) {
						procesoModificarEstadoVehiculo(sc, vehiculo, vehiculoDAO);
					} else {
						System.out.println("Vehiculo no encontrado");
						continue;
					}
					break;
				case 3:
					break;
				default:
					System.out.println("Error: Opcion no valida");
					continue;
			}
			break;
		}
	}
	
	private static void procesoModificarEstadoVehiculo(Scanner sc, Vehiculo vehiculo, VehiculoDAO vehiculoDAO) {
		String descripcion = null;
		int id = vehiculo.getId();
		String estadoActual = vehiculo.getEstado();
		System.out.println("[1] Registrar baja de vehiculo (reversible)");
		System.out.println("[2] Registrar vehiculo en reparacion");
		System.out.println("[3] Registrar disponibilidad de vehiculo");
		System.out.print("Ingrese una opcion: ");
		byte opcion = sc.nextByte();
		sc.nextLine();
		
		switch (opcion) {
			case 1:
				if ("baja".equals(estadoActual)) {
					System.out.println("El vehiculo ya se encuentra dado de baja");
					break;
				}
				if ("alq".equals(estadoActual)) {
					System.out.println("Error: para dar de baja el vehiculo, este debe estar disponible");
					break;
				}
				System.out.print("Indique el motivo de la baja: ");
				descripcion = sc.nextLine();
				vehiculoDAO.modificarEstado(id, estadoActual, "baja", descripcion);
				break;
			case 2:
				if ("rep".equals(estadoActual)) {
					System.out.println("El vehiculo ya se encuentra en reparacion");
					break;
				}
				System.out.print("Indique detalles de la reparacion a realizar: ");
				descripcion = sc.nextLine();
				vehiculoDAO.modificarEstado(id, estadoActual, "rep", descripcion);
				break;
			case 3:
				if ("disp".equals(estadoActual)) {
					System.out.println("El vehiculo ya se encuentra disponible");
					break;
				}
				if ("baja".equals(estadoActual)){
					System.out.println("El vehiculo esta dado de baja, para darlo de alta primero registre la/s reparacion/es realizadas");
					break;
				}
				System.out.print("Indique el motivo de la alta: ");
				descripcion = sc.nextLine();
				vehiculoDAO.modificarEstado(id, estadoActual, "disp", descripcion);
				break;
			default:
				System.out.println("Error: opcion no valida");
				break;
			}
	}

	private static void buscarVehiculo(Scanner sc, Connection conn, VehiculoDAO vehiculoDAO) {
		while (true) {
			List<Vehiculo> vehiculosPorMarca = new ArrayList<>();
			List<Vehiculo> vehiculosPorTipo = new ArrayList<>();
			List<Vehiculo> vehiculosPorEstado = new ArrayList<>();
			System.out.println("[1] Buscar por marca");
			System.out.println("[2] Buscar por tipo");
			System.out.println("[3] Buscar por estado");
			System.out.println("[4] Regresar al menu principal");
			byte opcion = selectorOpcion(sc);
			
			switch (opcion) {
			case 1:
				System.out.print("Ingrese la marca a buscar: ");
				String marca = sc.nextLine();
				vehiculosPorMarca = vehiculoDAO.listarPorMarca(marca);
				if (!vehiculosPorMarca.isEmpty()) {
					for (Vehiculo v : vehiculosPorMarca) {
						v.mostrarDatos();
					}
				} else {
					System.out.println("No se han encontrado vehiculos de la marca ingresada");
				}
				break;
			case 2:
				while (true) {
					System.out.println("[1] Auto");
					System.out.println("[2] Camioneta");
					System.out.println("[3] Moto");
					byte opcion_tipo = selectorOpcion(sc);
					
					switch (opcion_tipo) {
						case 1:
							vehiculosPorTipo = vehiculoDAO.listarPorTipo("Auto");
							if (!vehiculosPorTipo.isEmpty()) {
								for (Vehiculo v : vehiculosPorTipo) {
									v.mostrarDatos();
								}
							} else {
								System.out.println("No hay vehiculos disponibles para la categoria 'Auto'");
							}
							break;
						case 2:
							vehiculosPorTipo = vehiculoDAO.listarPorTipo("Camioneta");
							if (!vehiculosPorTipo.isEmpty()) {
								for (Vehiculo v : vehiculosPorTipo) {
									v.mostrarDatos();
								}
							} else {
								System.out.println("No hay vehiculos disponibles para la categoria 'Camioneta'");
							}
							break;
						case 3:
							vehiculosPorTipo = vehiculoDAO.listarPorTipo("Moto");
							if (!vehiculosPorTipo.isEmpty()) {
								for (Vehiculo v : vehiculosPorTipo) {
									v.mostrarDatos();
								}
							} else {
								System.out.println("No hay vehiculos disponibles para la categoria 'Moto'");
							}
							break;
						default:
							continue;
					}
					break;
				}
				break;
			case 3:
				System.out.println("[1] Mostrar vehiculos disponibles");
				System.out.println("[2] Mostrar vehiculos alquilados");
				System.out.println("[3] Regresar al menu de opciones");
				byte opcion_estado = selectorOpcion(sc);
				
				switch (opcion_estado) {
					case 1:
						vehiculosPorEstado = vehiculoDAO.listarPorEstado("disp");
						if (!vehiculosPorEstado.isEmpty()) {
							for (Vehiculo v : vehiculosPorEstado) {
								v.mostrarDatos();
							}
						} else {
							System.out.println("No hay vehiculos disponibles");
						}
						break;
					case 2:
						vehiculosPorEstado = vehiculoDAO.listarPorEstado("alq");
						if (!vehiculosPorEstado.isEmpty()) {
							for (Vehiculo v : vehiculosPorEstado) {
								v.mostrarDatos();
							}
						} else {
							System.out.println("No hay vehiculos alquilados");
						}
						break;
					case 3:
						break;
					default:
						System.out.println("Advertencia: Opcion no valida");
						continue;
				}
				break;
			case 4:
				break;
			default:
				continue;
			}
			break;
		}
	}

	private static void actualizarDatosVehiculo(Scanner sc, Connection conn, VehiculoDAO vehiculoDAO) {
		String matricula = null;
		int anioActual = LocalDate.now().getYear();
		int anioLimite = anioActual - 10;
		while (true) {
			System.out.print("Ingrese la matricula del vehiculo a modificar: ");
			matricula = sc.nextLine();
			if (validarMatricula(matricula)) {
				break;
			} else {
				System.out.println("Error: Formato de matricula no valido");
			}
		}
		Vehiculo vehiculo = vehiculoDAO.buscarPorIdentificador(matricula);;
		
		if (vehiculo != null) {
			System.out.print("Ingrese la marca del vehiculo: ");
			vehiculo.setMarca(sc.nextLine());
			
			System.out.print("Ingrese el modelo del vehiculo: ");
			vehiculo.setModelo(sc.nextLine());
			
			while (true) {
				try {
					System.out.print("Ingrese el anio de fabricacion del vehiculo: ");
					vehiculo.setAnio(sc.nextInt());
					sc.nextLine();
					if (vehiculo.getAnio() >= anioLimite) {
						break;
					} else {
						System.out.println("Error: No se admiten vehiculos fabricados antes de " + anioLimite);
						continue;
					}
				} catch (InputMismatchException e) {
					System.out.println("Error: Este campo solo admite numeros");
					sc.nextLine();
				}
			}
			
			System.out.print("Ingrese el tipo de vehiculo (Auto/Camioneta/Moto): ");
			vehiculo.setTipo(sc.nextLine());
			
			try {
				vehiculoDAO.actualizar(vehiculo);
				System.out.println("Registro modificado con �xito");
			} catch (Exception e) {
				System.err.println("Error al actualizar datos del vehiculo: " + e.getMessage());
			}
		} else {
			System.out.println("Vehiculo no encontrado");
		}
	}

	private static void iniciarAlquiler(Scanner sc, Connection conn, VehiculoDAO vehiculoDAO, ClienteDAO clienteDAO, AlquilerDAO alquilerDAO) {
		Cliente cliente = null;
		String dni = null;
		int idCliente = 0;
		while (true) {
			if (vehiculoDAO.hayDisponibilidad()) {
				System.out.println("[1] Registrar alquiler por ID del cliente");
				System.out.println("[2] Registrar alquiler por DNI del cliente");
				System.out.println("[3] Regresar al menu principal");
				byte opcion = selectorOpcion(sc);
	
				switch(opcion) {
					case 1:
						System.out.println("[1] Ingresar ID");
						System.out.println("[2] Mostrar lista de clientes existentes");
						byte opcion_cliente = selectorOpcion(sc);
						
						switch (opcion_cliente) {
							case 1:
								try {
									System.out.print("Ingrese el ID de cliente: ");
									idCliente = sc.nextInt();
									sc.nextLine();
								} catch (InputMismatchException e) {
									System.out.println("Error: Este campo solo admite numeros");
									sc.nextLine();
								}
								cliente = clienteDAO.buscarPorId(idCliente);
								break;
							case 2:
								cliente = clienteDAO.seleccionarCliente(sc);
								break;
							default:
								System.out.println("Advertencia: Opcion no valida");
								continue;
						}
						
						if (cliente != null) {
							if (clienteDAO.tieneAlquilerActivo(cliente.getId())) {
								System.out.println("Alquiler cancelado: El cliente ya tiene un alquiler activo");
							} else {
								procesoAlquiler(conn, sc, cliente, vehiculoDAO, alquilerDAO);
							}
						} else {
							if (manejoClienteNoExiste(sc, conn, clienteDAO)) {
								continue;
							} else {
								continue;
							}
						}
						break;
					case 2:
						while (true) {
							System.out.print("Ingrese el DNI del cliente: ");
							dni = sc.nextLine();
							if (validarDNI(dni)) {
								break;
							} else {
								System.out.println("El DNI debe contener solo numeros y entre 6 a 9 caracteres");
							}
						}
						
						cliente = clienteDAO.buscarPorIdentificador(dni);
						if (cliente != null) {
							if (clienteDAO.tieneAlquilerActivo(cliente.getId())) {
								System.out.println("El cliente ya cuenta con un alquiler activo");
							} else {
								procesoAlquiler(conn, sc, cliente, vehiculoDAO, alquilerDAO);
							}
						} else {
							if (manejoClienteNoExiste(sc, conn, clienteDAO)) {
								continue;
							} else {
								continue;
							}
						}
						break;
					case 3:
						break;
					default:
						System.out.println("Advertencia: Opcion no valida");
						continue;
				}
				break;
			} else {
				System.out.println("Error: No hay vehiculos disponibles");
			}
			break;
		}
	}
	
	private static void procesoAlquiler(Connection conn, Scanner sc, Cliente cliente, VehiculoDAO vehiculoDAO, AlquilerDAO alquilerDAO) {
		LocalDate fechaInicio = null;
		LocalDate fechaDevolucionPactada = null;
		Vehiculo vehiculo = null;
		
		while (true) {
			System.out.println("Seleccione el tipo de alquiler");
			System.out.println("[1] Alquiler rapido");
			System.out.println("[2] Alquiler programado");
			byte opcion_tipo = selectorOpcion(sc);
			
			switch(opcion_tipo) {
				case 1:
					fechaInicio = LocalDate.now();
					System.out.println("El alquiler comenzara hoy (" + fechaInicio + ")");
					break;
				case 2:
					while (true) {
						System.out.print("Ingrese la fecha de comienzo del alquiler (formato: aaaa-MM-dd): ");
						String inicioProgramado = sc.nextLine();
						
						try {
							fechaInicio = LocalDate.parse(inicioProgramado);
							
							if (fechaInicio.isBefore(LocalDate.now())) {
								System.out.println("Error: La fecha de inicio del alquiler no puede ser previa a hoy.");
								continue;
							} else {
								System.out.println("El alquiler comenzara el dia " + inicioProgramado);
								break;
							}
						} catch (DateTimeParseException e) {
							System.out.println("Error: Formato de fecha invalido (formato esperado: aaaa-MM-dd)");
							continue;
						}
					}
					break;
				default:
					System.out.println("Advertencia: Opcion no valida");
					continue;
			}
			break;
		}
		
		while (true) {
			System.out.println("Seleccione el tipo de vehiculo deseado");
			System.out.println("[1] Auto");
			System.out.println("[2] Camioneta");
			System.out.println("[3] Moto");
			byte opcion_vehiculo = selectorOpcion(sc);
			
			vehiculo = (opcion_vehiculo == 1) ? vehiculoDAO.seleccionarVehiculo(sc, "Auto") :
				       (opcion_vehiculo == 2) ? vehiculoDAO.seleccionarVehiculo(sc, "Camioneta") :
			    	   (opcion_vehiculo == 3) ? vehiculoDAO.seleccionarVehiculo(sc, "Moto") :
		    		   null;
			
			if (vehiculo == null) {
				System.out.println("Advertencia: Opcion no valida");
				continue;
			}
			break;
		}
		
		while (true) {
			System.out.println("Fecha de devolucion pactada");
			System.out.println("[1] Devolucion en un dia");
			System.out.println("[2] Devolucion en tres dias");
			System.out.println("[3] Devolucion en siete dias");
			System.out.println("[4] Establecer una fecha de devolucion personalizada");
			byte opcion_devolucion = selectorOpcion(sc);
			
			switch (opcion_devolucion) {
				case 1:
					fechaDevolucionPactada = fechaInicio.plusDays(1);
					break;
				case 2:
					fechaDevolucionPactada = fechaInicio.plusDays(3);
					break;
				case 3:
					fechaDevolucionPactada = fechaInicio.plusDays(7);
					break;
				case 4:
					while (true) {
						System.out.print("Ingrese la fecha de devolucion (formato: aaaa-MM-dd): ");
						String fechaDevolucionPactadaStr = sc.nextLine();
						
						try {
							fechaDevolucionPactada = LocalDate.parse(fechaDevolucionPactadaStr);
							LocalDate fechaLimite = fechaInicio.plusDays(90);
							
							if (fechaDevolucionPactada.isBefore(fechaInicio)) {
								System.out.println("Error: La fecha de finalizacion no puede ser anterior a la fecha de inicio");
								continue;
							}
			
							if (fechaDevolucionPactada.isAfter(fechaLimite)) {
								System.out.println("Error: La fecha de finalizacion del alquiler no debe ser superior a 90 dias");
								continue;
							}
							break;
						} catch (DateTimeParseException e) {
							System.out.println("Error: Formato de fecha invalido (formato esperado: aaaa-MM-dd)");
							continue;
						}
					}
					break;
				default:
					System.out.println("Advertencia: Opcion no valida");
					continue;
			}
			break;
		}
		System.out.println("Fecha de devolucion pactada: " + fechaDevolucionPactada);
		
		System.out.println("Confirmacion de alquiler");
		System.out.println("[1] Confirmar alquiler");
		System.out.println("[2] Cancelar alquiler");
		byte opcion_confirmar = selectorOpcion(sc);
		
		switch (opcion_confirmar) {
			case 1:
				Alquiler alquiler = new Alquiler(cliente, vehiculo, fechaInicio, fechaDevolucionPactada);
				alquilerDAO.insertar(alquiler);
				break;
			case 2:
				System.out.println("Alquiler cancelado");
				break;
			default:
				System.out.println("Advertencia: Opcion no valida");
				break;
		}
	}

	private static void mostrarAlquileresActivos(Connection conn, AlquilerDAO alquilerDAO) {
		JFrame frame = new JFrame("Listado de alquileres activos");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1100, 300);
		
		DefaultTableModel model = new DefaultTableModel();
		JTable table = new JTable(model);
		
		model.addColumn("ID del alquiler");
		model.addColumn("ID del Cliente");
		model.addColumn("Nombre del cliente");
		model.addColumn("ID del vehiculo");
		model.addColumn("Descripcion del veh�culo");
		model.addColumn("Fecha de inicio del alquiler");
		model.addColumn("Fecha de finalizacion pactada del alquiler");
		model.addColumn("Dias de atraso");
		
		List<Alquiler> alquileres = alquilerDAO.listarNoFinalizados();
		
		for (Alquiler a : alquileres) {
			long diasAtraso = a.getDiasAtraso();
			model.addRow(new Object[] {
					a.getId(),
					a.getCliente().getId(),
					a.getCliente().getNombre(),
					a.getVehiculo().getId(),
					a.getVehiculo().getMarcaModelo(),
					a.getFechaInicio(),
					a.getFechaDevolucionPactada(),
					diasAtraso > 0 ? diasAtraso : "Sin atraso"
			});
		}
		
		frame.add(new JScrollPane(table));
		frame.setVisible(true);
	}
	
	private static void mostrarAlquileresAtrasados(Connection conn, AlquilerDAO alquilerDAO) {
		List<Alquiler> atrasados = alquilerDAO.listarAtrasados();
		
		if (!atrasados.isEmpty()) {
			JFrame frame = new JFrame("Alquileres atrasados");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setSize(800, 300);
			
			DefaultTableModel model = new DefaultTableModel();
			JTable table = new JTable(model);
			
			model.addColumn("ID del alquiler");
			model.addColumn("ID del cliente");
			model.addColumn("Nombre del cliente");
			model.addColumn("Telefono");
			model.addColumn("DNI");
			model.addColumn("Dias de atraso");
			
			for (Alquiler a : atrasados) {
				model.addRow(new Object[] {
						a.getId(),
						a.getCliente().getId(),
						a.getCliente().getNombre(),
						a.getCliente().getTelefono(),
						a.getCliente().getDni(),
						a.getDiasAtraso()
				});
			}
			
			frame.add(new JScrollPane(table));
			frame.setVisible(true);
		} else {
			System.out.println("No hay alquileres atrasados");
		}
	}
	
	private static void mostrarAlquileresPorUsuario(Connection conn, Scanner sc, AlquilerDAO alquilerDAO, ClienteDAO clienteDAO) {
		while (true) {
			Cliente cliente = null;
			int idCliente = 0;
			System.out.println("[1] Buscar por ID del Cliente");
			System.out.println("[2] Mostrar lista de clientes existentes");
			System.out.println("[3] Regresar al menu principal");
			byte opcion = selectorOpcion(sc);
			
			switch (opcion) {
				case 1:
					try {
						System.out.print("Ingrese el ID del cliente: ");
						idCliente = sc.nextInt();
					} catch (InputMismatchException e) {
						System.out.println("Error: Este campo solo admite numeros");
						sc.nextLine();
					}
					cliente = clienteDAO.buscarPorId(idCliente);
					break;
				case 2:
					cliente = clienteDAO.seleccionarCliente(sc);
					break;
				case 3:
					break;
				default:
					System.out.println("Advertencia: Opcion no valida");
					continue;
			}
			
			if (cliente != null) {
				JFrame frame = new JFrame("Listado de alquileres de " + cliente.getNombre() + "(ID: " + cliente.getId() + ")");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setSize(1100, 300);
				
				DefaultTableModel model = new DefaultTableModel();
				JTable table = new JTable(model);
				
				model.addColumn("ID del Alquiler");
				model.addColumn("ID del Vehiculo");
				model.addColumn("Descripcion del veh�culo");
				model.addColumn("Fecha de inicio del alquiler");
				model.addColumn("Fecha de finalizacion pactada del alquiler");
				model.addColumn("Fecha real de finalizacion del alquiler");
				model.addColumn("Estado");
				model.addColumn("Motivo de finalizacion/cancelacion");
				
				List<Alquiler> listaAlquileresCliente = alquilerDAO.listarPorCliente(idCliente);
				
				for (Alquiler a : listaAlquileresCliente) {
					model.addRow(new Object[] {
							a.getId(),
							a.getVehiculo().getId(),
							a.getVehiculo().getMarcaModelo(),
							a.getFechaInicio(),
							a.getFechaDevolucionPactada(),
							a.getFechaDevolucionFinal(),
							a.getEstado(),
							a.getMotivoBaja()
					});
				}
				
				frame.add(new JScrollPane(table));
				frame.setVisible(true);
			} else {
				if (manejoClienteNoExiste(sc, conn, clienteDAO)) {
					continue;
				} else {
					continue;
				}
			}
			break;
		}
	}
	
	private static void finalizarAlquiler(Connection conn, Scanner sc, AlquilerDAO alquilerDAO) {
		while (true) {
			Alquiler alquiler = null;
			System.out.println("[1] Mostrar lista de alquileres activos y/o vencidos");
			System.out.println("[2] Regresar al menu principal");
			byte opcion = selectorOpcion(sc);
			
			switch (opcion) {
				case 1:
					alquiler = alquilerDAO.seleccionarAlquilerActivo(sc);
					if (alquiler != null) {
						System.out.print("Indique el motivo de la finalizacion del alquiler: ");
						alquiler.setMotivoBaja(sc.nextLine());
						alquilerDAO.actualizar(alquiler);
					}
					break;
				case 2:
					break;
				default:
					continue;
			}
			break;
		}
	}

	private static byte selectorOpcion(Scanner sc) {
		byte opcion = 0;
		try {
			System.out.print("Ingrese una opcion: ");
			opcion = sc.nextByte();
			sc.nextLine();
		} catch (InputMismatchException e) {
			System.out.println("Error: El selector de opciones solo admite numeros");
			sc.nextLine();
		}
		return opcion;
	}
	
	private static boolean manejoClienteNoExiste(Scanner sc, Connection conn, ClienteDAO clienteDAO) {
		System.out.println("Error: El cliente no existe");
		System.out.println("[1] Registrar nuevo cliente");
		System.out.println("[2] Regresar a opciones");
		byte opcion_error = selectorOpcion(sc);
		
		switch (opcion_error) {
			case 1:
				insertarCliente(conn, clienteDAO);
				return true;
			case 2:
				return false;
			default:
				System.out.println("Advertencia: Opcion no valida");
				return false;
		}
	}
	
	private static boolean validarDNI(String dni) {
		// Verifica que solo tenga numeros entre 6 y 9 caracteres
		return dni.matches("\\d{6,9}");
	}
	
	private static boolean validarNombre(String nombre) {
		// Verifica que solo tenga letras (sin importar acentos) y que tenga como minimo 2 palabras separadas
		return nombre.matches("^[\\p{L}]+(\\s[\\p{L}]+)+$");
	}
	
	private static boolean validarDireccion(String direccion) {
		// Verifica que no contenga solo numeros, que tenga como minimo 2 palabras separadas
	    return direccion.matches(".*[\\p{L}]+.*") && direccion.matches("^[\\p{L}\\d\\s,./]+$") && direccion.matches(".*\\b\\p{L}+\\b.*\\s+\\b\\p{L}+\\b.*");
	}
	
	private static boolean validarTelefono(String telefono) {
		// Verifica solo numeros entre 10 y 13 caracteres
		return telefono.matches("^\\d{10,13}$");
	}
	
	private static boolean validarMatricula(String matricula) {
		// Verifica los formatos AB123CD o ABC123
		return matricula.matches("^[A-Z]{2}\\d{3}[A-Z]{2}$|^[A-Z]{3}\\d{3}$|^\\d{3}[A-Z]{3}$");
	}
}
