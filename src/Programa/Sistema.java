package Programa;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;

public class Sistema {

	public static void main(String[] args) {
		Conexion conexion = new Conexion("jdbc:mysql://192.168.100.129:3306/TPGuzman","Guzman","Lujan");
		Scanner sc = new Scanner(System.in);
		
		try (Connection conn = conexion.getConnection()) {
			while (true) {
				if (AlquilerQuery.cantidadAlquileresActivos(conn) > 0) {
					AlquilerQuery.actualizarEstados(conn);
				}
				byte opcion = menuOpciones(sc);
				
				switch (opcion) {
					case 1:
						insertarCliente(sc, conn);
						continue;
					case 2:
						eliminarCliente(sc, conn);
						continue;
					case 3:
						buscarCliente(sc, conn);
						continue;
					case 4:
						mostrarClientes(conn);
						continue;
					case 5:
						modificarCliente(sc, conn);
						continue;
					case 6:
						insertarVehiculo(sc, conn);
						continue;
					case 7:
						eliminarVehiculo(sc, conn);
						continue;
					case 8:
						buscarVehiculo(sc, conn);
						continue;
					case 9:
						modificarVehiculo(sc, conn);
						continue;
					case 10:
						iniciarAlquiler(sc, conn);
						continue;
					case 11:
						mostrarAlquileres(conn);
						continue;
					case 12:
						mostrarAlquileresPorUsuario(conn, sc);
						continue;
					case 13:
						finalizarAlquiler(conn, sc);
						continue;
					case 15:
						break;
					default:
						System.out.println("Advertencia: Opción no válida");
						continue;
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sc.close();
	}
	
	private static byte menuOpciones(Scanner sc) {
		System.out.println("Menú de opciones:");
		System.out.println("[1] Registrar cliente");
		System.out.println("[2] Dar de baja a cliente");
		System.out.println("[3] Buscar cliente");
		System.out.println("[4] Listado de clientes registrados");
		System.out.println("[5] Modificar datos de cliente");
		System.out.println("[6] Registrar vehículo");
		System.out.println("[7] Dar de baja a vehículo");
		System.out.println("[8] Buscar vehículo");
		System.out.println("[9] Modificar datos de vehículo");
		System.out.println("[10] Iniciar proceso de alquiler");
		System.out.println("[11] Listado de alquileres activos");
		System.out.println("[12] Listado de alquileres por usuario");
		System.out.println("[13] Finalizar alquiler");
		System.out.println("[15] Salir del programa");
		byte opcion = selectorOpcion(sc);
		return opcion;
	}
	
	private static void insertarCliente(Scanner sc, Connection conn) {
		String nombre;
		String dni;
		String direccion;
		String telefono;
		
		while (true) {
			System.out.print("Ingrese el nombre completo del cliente: ");
			nombre = sc.nextLine();
			
			if (validarNombre(nombre)) {
				break;
			} else {
				System.out.println("Error: El nombre solo puede contener letras con y sin acento");
			}
		}
		
		while (true) {
			System.out.print("Ingrese la dirección del cliente: ");
			direccion = sc.nextLine();
			
			if (validarDireccion(direccion)) {
				break;
			} else {
				System.out.println("Error: La dirección no puede contener solo números");
			}
		}
		
		while (true) {
			System.out.print("Ingrese el teléfono del cliente: ");
			telefono = sc.nextLine();
			
			if(validarTelefono(telefono)) {
				break;
			} else {
				System.out.println("Error: El teléfono solo puede contener números");
			}
		}
		
		while (true) {
			System.out.print("Ingrese el Nº de DNI del cliente: ");
			dni = sc.nextLine();
			
			if (validarDNI(dni)) {
				break;
			} else {
				System.out.println("Error: El DNI debe contener solo números y entre 7 a 9 caracteres");
			}
		}
		
		Cliente cliente = new Cliente(nombre, direccion, telefono, dni);
		try {
			boolean exito = ClienteQuery.insertarCliente(conn, cliente);
			if (exito) {
				System.out.println("Cliente creado exitosamente");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("Error: El número de teléfono y/o DNI ya han sido registrados");
		} catch (MysqlDataTruncation e) {
			System.out.println("Error: Ocurrió un problema en el ingreso de datos. Inténtelo nuevamente o contacte al soporte técnico");
		}
	}
	
	private static void eliminarCliente(Scanner sc, Connection conn) {
		while (true) {
			int id = 0;
			String dni = null;
			Cliente cliente;
			System.out.println("[1] Eliminar por Nº de cliente");
			System.out.println("[2] Eliminar por DNI/Licencia");
			System.out.println("[3] Regresar al menú principal");
			byte opcion = selectorOpcion(sc);
			
			switch (opcion) {
				case 1:
					try {
						System.out.print("Ingrese el Nº de cliente: ");
						id = sc.nextInt();
					} catch (InputMismatchException e) {
						System.out.println("Error: Este campo solo admite números");
						sc.nextLine();
					}
					
					cliente = new ClienteQuery().mostrarClientePorId(conn, id);
					if (cliente != null) {
						ClienteQuery.eliminarClientePorId(conn, id);
						System.out.println("Cliente eliminado");
					} else {
						System.out.println("Cliente no encontrado.");
						continue;
					}
					break;
				case 2:
					while (true) {
						System.out.print("Ingrese el Nº de DNI del cliente: ");
						dni = sc.nextLine();
						if (validarDNI(dni)) {
							break;
						} else {
							System.out.println("El DNI debe contener solo números y entre 7 a 9 caracteres. Intentelo nuevamente");
						}
					}
					
					cliente = new ClienteQuery().mostrarClientePorDni(conn, dni);
					if (cliente != null) {
						ClienteQuery.eliminarClientePorDni(conn, dni);
						System.out.println("Cliente eliminado");
					} else {
						System.out.println("Cliente no encontrado.");
						continue;
					}
					break;
				case 3:
					break;
				default:
					System.out.println("Advertencia: Opción no válida");
					continue;
			}
			break;
		}
	}
	
	private static void buscarCliente(Scanner sc, Connection conn) {
		while (true) {
			String dni = null;
			int id = 0;
			Cliente cliente = null;
			System.out.println("[1] Buscar por Nº de Cliente");
			System.out.println("[2] Buscar por DNI/Nº Licencia");
			System.out.println("[3] Buscar por nombre");
			System.out.println("[4] Regresar al menú principal");
			byte opcion = selectorOpcion(sc);
			
			switch (opcion) {
				case 1:
					try {
						System.out.print("Ingrese el Nº de Cliente: ");
						id = sc.nextInt();
					} catch (InputMismatchException e) {
						System.out.println("Error: Este campo solo admite números");
						sc.nextLine();
					}
					
					cliente = new ClienteQuery().mostrarClientePorId(conn, id);
					
					if (cliente != null) {
						cliente.mostrarDatos();
					} else {
						System.out.println("Cliente no encontrado");
					}
					break;
				case 2:
					while (true) {
						System.out.print("Ingrese el Nº de DNI del cliente: ");
						dni = sc.nextLine();
						if (validarDNI(dni)) {
							cliente = new ClienteQuery().mostrarClientePorDni(conn, dni);
							break;
						} else {
							System.out.println("El DNI debe contener solo números y entre 7 a 9 caracteres. Intentelo nuevamente");
						}
					}
					
					if (cliente != null) {
						cliente.mostrarDatos();
					} else {
						System.out.println("Cliente no encontrado");
					}
					break;
				case 3:
					while (true) {
						System.out.print("Ingrese el nombre del cliente: ");
						String nombre = sc.nextLine();
						if (validarNombre(nombre)) {
							cliente = new ClienteQuery().mostrarClientePorNombre(conn, nombre);
							break;
						} else {
							System.out.println("Error: El nombre solo puede contener letras con y sin acento");
						}
					}
					
					if (cliente != null) {
						cliente.mostrarDatos();
					} else {
						System.out.println("Cliente no encontrado");
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
	
	private static void mostrarClientes(Connection conn) {
		JFrame frame = new JFrame("Listado de clientes");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1100, 300);
		
		DefaultTableModel model = new DefaultTableModel();
		JTable table = new JTable(model);
		
		model.addColumn("Nº de Cliente");
		model.addColumn("Nombre completo");
		model.addColumn("Dirección");
		model.addColumn("Telefono");
		model.addColumn("DNI/Nº Licencia");
		
		ArrayList<Cliente> clientes = new ClienteQuery().mostrarCliente(conn);
		
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
	
	private static void modificarCliente(Scanner sc, Connection conn) {
		int id;
		Cliente cliente;
		while (true) {
			try {
				System.out.print("Ingresa el Nº de Cliente a modificar: ");
				id = sc.nextInt();
				sc.nextLine();
				break;
			} catch (InputMismatchException e) {
				System.out.println("Error: Este campo solo admite números");
				continue;
			}
		}
		
		cliente = new ClienteQuery().mostrarClientePorId(conn, id);
	    
	    if (cliente != null) {
	    	JFrame frame = new JFrame("Modificar datos del cliente");
		    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		    
		    JPanel panel = new JPanel();
		    panel.setLayout(new GridLayout(5, 2, 10, 10));
		    
		    JLabel lblNombre = new JLabel("Nombre:");
		    JTextField txtNombre = new JTextField(cliente.getNombre());
		    JLabel lblDireccion = new JLabel("Dirección:");
		    JTextField txtDireccion = new JTextField(cliente.getDireccion());
		    JLabel lblTelefono = new JLabel("Teléfono:");
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
		        @Override
		        public void actionPerformed(ActionEvent e) {
		            Cliente clienteActualizado = new Cliente(
		                idCliente,
		                txtNombre.getText().isEmpty() ? null : txtNombre.getText(),
		                txtDireccion.getText().isEmpty() ? null : txtDireccion.getText(),
		                txtTelefono.getText().isEmpty() ? null : txtTelefono.getText(),
		                txtDni.getText().isEmpty() ? null : txtDni.getText()
		            );
		            
		            try {
		            	boolean exito = ClienteQuery.actualizarCliente(conn, clienteActualizado);
		            	if (exito) {
			                JOptionPane.showMessageDialog(frame, "Datos actualizados correctamente");
			                frame.dispose();
			            } else {
			                JOptionPane.showMessageDialog(frame, "Error al actualizar los datos");
			            }
		    		} catch (SQLIntegrityConstraintViolationException er) {
		    			System.out.println("Error: El número de teléfono y/o DNI ya han sido registrados");
		    		} catch (MysqlDataTruncation er) {
		    			System.out.println("Error: Ocurrió un problema en el ingreso de datos. Inténtelo nuevamente o contacte al soporte técnico");
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
	
	private static void insertarVehiculo(Scanner sc, Connection conn) {
		int anio;
		String tipo;
		String matricula;
		System.out.print("Ingrese la marca del vehículo: ");
		String marca = sc.nextLine();
		 
		System.out.print("Ingrese el modelo del vehículo: ");
		String modelo = sc.nextLine();
		
		while (true) {
			try {
				System.out.print("Ingrese el año de fabricación del vehículo: ");
				anio = sc.nextInt();
				sc.nextLine();
					if (anio > 2014) {
						break;
					} else {
						System.out.println("Error: Solo se admiten vehículos fabricados a partir de 2015");
					}
			} catch (InputMismatchException e) {
				System.out.println("Error: Este campo solo admite números");
				sc.nextLine();
			}
		}
		
		while (true) {
			System.out.print("Ingrese la mátricula del vehículo: ");
			matricula = sc.nextLine();
			if (validarMatricula(matricula)) {
				break;
			} else {
				System.out.println("Error: Formato de matrícula no válido");
			}
		}
		
		while (true) {
			System.out.println("Seleccione el tipo de vehículo");
			System.out.println("[1] Moto");
			System.out.println("[2] Auto");
			System.out.println("[3] Camioneta");
			byte opcion = selectorOpcion(sc);
			
			switch (opcion) {
				case 1:
					tipo = "Moto";
					break;
				case 2:
					tipo = "Auto";
					break;
				case 3:
					tipo = "Camioneta";
					break;
				default:
					System.out.println("Error: Opción no válida");
					continue;
			}
			break;
		}
		
		Vehiculo vehiculo = new Vehiculo(marca, modelo, anio, matricula, tipo);
		try {
			boolean exito = VehiculoQuery.insertarVehiculo(conn, vehiculo);
			if (exito) {
				System.out.println("Vehículo registrado correctamente");
			} else {
				System.out.println("Error: No se ha podido registrar el vehículo");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("Error: La matrícula ingresada ya ha sido registrada");
		} catch (MysqlDataTruncation e) {
			System.out.println("Error: Ocurrió un problema en el ingreso de datos. Inténtelo nuevamente o contacte al soporte técnico");
		}
		
	}

	private static void eliminarVehiculo(Scanner sc, Connection conn) {
		while (true) {
			String matricula;
			int id = 0;
			Vehiculo vehiculo;
			System.out.println("[1] Eliminar por ID");
			System.out.println("[2] Eliminar por matrícula");
			System.out.println("[3] Regresar al menú principal");
			byte opcion = selectorOpcion(sc);
			
			switch (opcion) {
				case 1:
					try {
						System.out.print("Ingrese el ID del vehículo: ");
						id = sc.nextInt();
					} catch (InputMismatchException e) {
						System.out.println("Error: Este campo solo admite números");
						sc.nextLine();
					}
					
					vehiculo = new VehiculoQuery().mostrarVehiculoPorId(conn, id);
					if (vehiculo != null) {
						VehiculoQuery.borrarVehiculoPorId(conn, id);
						System.out.println("Vehículo eliminado");
					} else {
						System.out.println("Vehículo no encontrado");
						continue;
					}
					break;
				case 2:
					while (true) {
						System.out.print("Ingrese la matrícula del vehículo (formatos válidos: AB123CD - ABC123): ");
						matricula = sc.nextLine();
						if (validarMatricula(matricula)) {
							break;
						} else {
							System.out.println("Error: Formato de matrícula no válido");
						}
					}
					
					vehiculo = new VehiculoQuery().mostrarVehiculoPorMatricula(conn, matricula);
					if (vehiculo != null) {
						VehiculoQuery.borrarVehiculoPorMatricula(conn, matricula);
						System.out.println("Vehículo eliminado");
					} else {
						System.out.println("Vehículo no encontrado");
						continue;
					}
					break;
				case 3:
					break;
				default:
					System.out.println("Error: Opción no válida");
					continue;
			}
			break;
		}
	}

	private static void buscarVehiculo(Scanner sc, Connection conn) {
		while (true) {
			ArrayList<Vehiculo> vehiculosPorMarca = new ArrayList<>();
			ArrayList<Vehiculo> vehiculosPorTipo = new ArrayList<>();
			ArrayList<Vehiculo> vehiculosPorEstado = new ArrayList<>();
			System.out.println("[1] Buscar por marca");
			System.out.println("[2] Buscar por tipo");
			System.out.println("[3] Buscar por estado");
			System.out.println("[4] Regresar al menú principal");
			byte opcion = selectorOpcion(sc);
			
			switch (opcion) {
			case 1:
				System.out.print("Ingrese la marca a buscar: ");
				String marca = sc.nextLine();
				vehiculosPorMarca = new VehiculoQuery().mostrarVehiculosPorMarca(conn, marca);
				if (!vehiculosPorMarca.isEmpty()) {
					for (Vehiculo v : vehiculosPorMarca) {
						v.mostrarDatos();
					}
				} else {
					System.out.println("No se han encontrado vehículos de la marca ingresada");
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
							vehiculosPorTipo = new VehiculoQuery().mostrarVehiculosPorTipo(conn, "Auto");
							if (!vehiculosPorTipo.isEmpty()) {
								for (Vehiculo v : vehiculosPorTipo) {
									v.mostrarDatos();
								}
							} else {
								System.out.println("No hay vehículos disponibles para la categoría 'Auto'");
							}
							break;
						case 2:
							vehiculosPorTipo = new VehiculoQuery().mostrarVehiculosPorTipo(conn, "Camioneta");
							if (!vehiculosPorTipo.isEmpty()) {
								for (Vehiculo v : vehiculosPorTipo) {
									v.mostrarDatos();
								}
							} else {
								System.out.println("No hay vehículos disponibles para la categoría 'Camioneta'");
							}
							break;
						case 3:
							vehiculosPorTipo = new VehiculoQuery().mostrarVehiculosPorTipo(conn, "Moto");
							if (!vehiculosPorTipo.isEmpty()) {
								for (Vehiculo v : vehiculosPorTipo) {
									v.mostrarDatos();
								}
							} else {
								System.out.println("No hay vehículos disponibles para la categoría 'Moto'");
							}
							break;
						default:
							continue;
					}
					break;
				}
				break;
			case 3:
				System.out.println("[1] Mostrar vehículos disponibles");
				System.out.println("[2] Mostrar vehículos alquilados");
				System.out.println("[3] Regresar al menú de opciones");
				byte opcion_estado = selectorOpcion(sc);
				
				switch (opcion_estado) {
					case 1:
						vehiculosPorEstado = new VehiculoQuery().mostrarVehiculosPorEstado(conn, "disp");
						if (!vehiculosPorEstado.isEmpty()) {
							for (Vehiculo v : vehiculosPorEstado) {
								v.mostrarDatos();
							}
						} else {
							System.out.println("No hay vehículos disponibles");
						}
						break;
					case 2:
						vehiculosPorEstado = new VehiculoQuery().mostrarVehiculosPorEstado(conn, "alq");
						if (!vehiculosPorEstado.isEmpty()) {
							for (Vehiculo v : vehiculosPorEstado) {
								v.mostrarDatos();
							}
						} else {
							System.out.println("No hay vehículos alquilados");
						}
						break;
					case 3:
						break;
					default:
						System.out.println("Advertencia: Opción no válida");
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

	private static void modificarVehiculo(Scanner sc, Connection conn) {
		String matricula = null;
		while (true) {
			System.out.print("Ingrese la matrícula del vehículo a modificar: ");
			matricula = sc.nextLine();
			if (validarMatricula(matricula)) {
				break;
			} else {
				System.out.println("Error: Formato de matrícula no válido");
			}
		}
		Vehiculo vehiculo = new VehiculoQuery().mostrarVehiculoPorMatricula(conn, matricula);
		
		if (vehiculo != null) {
			System.out.print("Ingrese la marca del vehículo: ");
			vehiculo.setMarca(sc.nextLine());
			
			System.out.print("Ingrese el modelo del vehículo: ");
			vehiculo.setModelo(sc.nextLine());
			
			while (true) {
				try {
					System.out.print("Ingrese el año de fabricación del vehículo: ");
					vehiculo.setAnio(sc.nextInt());
					sc.nextLine();
					if (vehiculo.getAnio() > 2014) {
						break;
					} else {
						System.out.println("Error: No se admiten vehículos fabricados antes de 2015");
						continue;
					}
				} catch (InputMismatchException e) {
					System.out.println("Error: Este campo solo admite números");
					sc.nextLine();
				}
			}
			
			System.out.print("Ingrese el tipo de vehículo (Moto/Auto/SUV): ");
			vehiculo.setTipo(sc.nextLine());
			
			try {
				boolean exito = VehiculoQuery.actualizarVehiculo(conn, vehiculo, matricula);
				if (exito) {
					System.out.println("Registro modificado con éxito");
				} else {
					System.out.println("Error: No se han podido realizar las modificaciones en este registro");
				}
			} catch (SQLIntegrityConstraintViolationException e) {
				System.out.println("Error: La matrícula ingresada ya ha sido registrada");
			} catch (MysqlDataTruncation e) {
				System.out.println("Error: Ocurrió un problema en el ingreso de datos. Inténtelo nuevamente o contacte al soporte técnico");
			}
		} else {
			System.out.println("Vehículo no encontrado");
		}
	}

	private static void iniciarAlquiler(Scanner sc, Connection conn) {
		Cliente cliente = null;
		String dni = null;
		int idCliente = 0;
		while (true) {
			if (VehiculoQuery.hayVehiculosDisponibles(conn)) {
				System.out.println("[1] Registrar alquiler por Nº de cliente");
				System.out.println("[2] Registrar alquiler por DNI/Licencia");
				System.out.println("[3] Regresar al menú principal");
				byte opcion = selectorOpcion(sc);
	
				switch(opcion) {
					case 1:
						System.out.println("[1] Ingresar Nº de cliente");
						System.out.println("[2] Mostrar lista de clientes existentes");
						byte opcion_cliente = selectorOpcion(sc);
						
						switch (opcion_cliente) {
							case 1:
								try {
									System.out.print("Ingrese el Nº de cliente: ");
									idCliente = sc.nextInt();
									sc.nextLine();
								} catch (InputMismatchException e) {
									System.out.println("Error: Este campo solo admite números");
									sc.nextLine();
								}
								cliente = new ClienteQuery().mostrarClientePorId(conn, idCliente);
								break;
							case 2:
								cliente = new ClienteQuery().selectorClientePorId(conn, sc);
								break;
							default:
								System.out.println("Advertencia: Opción no válida");
								break;
						}
						
						if (cliente != null) {
							if (ClienteQuery.tieneAlquileresActivos(conn, cliente.getId())) {
								System.out.println("Error: El cliente ingresado tiene un alquiler activo");
							} else {
								procesoAlquiler(conn, sc, cliente);
							}
						} else {
							if (manejoClienteNoExiste(sc, conn)) {
								continue;
							} else {
								continue;
							}
						}
						break;
					case 2:
						while (true) {
							System.out.print("Ingrese el DNI del cliente a eliminar: ");
							dni = sc.nextLine();
							if (validarDNI(dni)) {
								break;
							} else {
								System.out.println("El DNI debe contener solo números y entre 7 a 9 caracteres. Intentelo nuevamente");
							}
						}
						
						cliente = new ClienteQuery().mostrarClientePorDni(conn, dni);
						if (cliente != null) {
							procesoAlquiler(conn, sc, cliente);
						} else {
							if (manejoClienteNoExiste(sc, conn)) {
								continue;
							} else {
								continue;
							}
						}
						break;
					case 3:
						break;
					default:
						System.out.println("Advertencia: Opción no válida");
						continue;
				}
				break;
			} else {
				System.out.println("Error: No hay vehículos disponibles");
			}
			break;
		}
	}
	
	private static void procesoAlquiler(Connection conn, Scanner sc, Cliente cliente) {
		LocalDate fecha_alquiler = null;
		LocalDate fecha_devolucion = null;
		Vehiculo vehiculo = null;
		
		while (true) {
			System.out.println("Seleccione el tipo de alquiler");
			System.out.println("[1] Alquiler rápido");
			System.out.println("[2] Alquiler programado");
			byte opcion_tipo = selectorOpcion(sc);
			
			switch(opcion_tipo) {
				case 1:
					fecha_alquiler = LocalDate.now();
					System.out.println("El alquiler comenzará hoy (" + fecha_alquiler + ")");
					break;
				case 2:
					while (true) {
						System.out.print("Ingrese la fecha de comienzo del alquiler (formato: aaaa-MM-dd): ");
						String inicio_programado = sc.nextLine();
						
						try {
							fecha_alquiler = LocalDate.parse(inicio_programado);
							
							if (fecha_alquiler.isBefore(LocalDate.now())) {
								System.out.println("Error: La fecha de inicio del alquiler no puede ser previa a hoy.");
								continue;
							} else {
								System.out.println("El alquiler comenzará el día " + inicio_programado);
								break;
							}
						} catch (DateTimeParseException e) {
							System.out.println("Error: Formato de fecha inválido (formato esperado: aaaa-MM-dd)");
							continue;
						}
					}
					break;
				default:
					System.out.println("Advertencia: Opción no válida");
					continue;
			}
			break;
		}
		
		while (true) {
			System.out.println("Seleccione el tipo de vehículo deseado");
			System.out.println("[1] Auto");
			System.out.println("[2] Camioneta");
			System.out.println("[3] Moto");
			byte opcion_vehiculo = selectorOpcion(sc);
			
			vehiculo = (opcion_vehiculo == 1) ? new VehiculoQuery().selectorVehiculoDisponiblePorTipo(conn, "Auto", sc) :
				       (opcion_vehiculo == 2) ? new VehiculoQuery().selectorVehiculoDisponiblePorTipo(conn, "Camioneta", sc) :
			    	   (opcion_vehiculo == 3) ? new VehiculoQuery().selectorVehiculoDisponiblePorTipo(conn, "Moto", sc) :
		    		   null;
			
			if (vehiculo == null) {
				System.out.println("Advertencia: Opción no válida");
				continue;
			}
			break;
		}
		
		while (true) {
			System.out.println("Seleccione fecha de devolución");
			System.out.println("[1] Devolución en un día");
			System.out.println("[2] Devolución en tres días");
			System.out.println("[3] Devolución en siete días");
			System.out.println("[4] Establecer fecha de devolución personalizada");
			byte opcion_devolucion = selectorOpcion(sc);
			
			switch (opcion_devolucion) {
				case 1:
					fecha_devolucion = fecha_alquiler.plusDays(1);
					break;
				case 2:
					fecha_devolucion = fecha_alquiler.plusDays(3);
					break;
				case 3:
					fecha_devolucion = fecha_alquiler.plusDays(7);
					break;
				case 4:
					while (true) {
						System.out.print("Ingrese la fecha de devolución (formato: aaaa-MM-dd): ");
						String fecha_devolucion_str = sc.nextLine();
						
						try {
							fecha_devolucion = LocalDate.parse(fecha_devolucion_str);
							LocalDate fechaLimite = fecha_alquiler.plusDays(90);
							
							if (fecha_devolucion.isBefore(fecha_alquiler)) {
								System.out.println("Error: La fecha de finalización no puede ser anterior a la fecha de inicio");
								continue;
							}
			
							if (fecha_devolucion.isAfter(fechaLimite)) {
								System.out.println("Error: La fecha de finalización del alquiler no debe ser superior a 90 días");
								continue;
							}
							break;
						} catch (DateTimeParseException e) {
							System.out.println("Error: Formato de fecha inválido (formato esperado: aaaa-MM-dd)");
							continue;
						}
					}
					break;
				default:
					System.out.println("Advertencia: Opción no válida");
					continue;
			}
			break;
		}
		System.out.println("Fecha de devolución seleccionada: " + fecha_devolucion);
		
		System.out.println("¿Desea confirmar el alquiler?");
		System.out.println("[1] Confirmar alquiler");
		System.out.println("[2] Cancelar alquiler");
		byte opcion_confirmar = selectorOpcion(sc);
		
		switch (opcion_confirmar) {
			case 1:
				Alquiler alquiler = new Alquiler(cliente, vehiculo, fecha_alquiler, fecha_devolucion);
				new AlquilerQuery().insertarAlquiler(conn, alquiler);
				boolean exito = VehiculoQuery.modificarEstadoVehiculo(conn, "alq", alquiler.getVehiculo().getId_vehiculo());
				if (!exito) {
					System.out.println("Error: El estado del vehículo no ha podido ser modificado");
				}
				break;
			case 2:
				System.out.println("Alquiler cancelado");
				break;
			default:
				System.out.println("Advertencia: Opción no válida");
				break;
		}
	}

	private static void mostrarAlquileres(Connection conn) {
		JFrame frame = new JFrame("Listado de alquileres histórico");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1100, 300);
		
		DefaultTableModel model = new DefaultTableModel();
		JTable table = new JTable(model);
		
		model.addColumn("ID de alquiler");
		model.addColumn("Nº de Cliente");
		model.addColumn("Nombre del cliente");
		model.addColumn("ID del vehículo");
		model.addColumn("Descripción del vehículo");
		model.addColumn("Fecha de inicio del alquiler");
		model.addColumn("Fecha de finalización del alquiler");
		
		ArrayList<Alquiler> alquileres = new AlquilerQuery().mostrarAlquileresActivos(conn);
		
		for (Alquiler a : alquileres) {
			model.addRow(new Object[] {
					a.getId_alquiler(),
					a.getCliente().getId(),
					a.getCliente().getNombre(),
					a.getVehiculo().getId_vehiculo(),
					a.getVehiculo().getMarcaModelo(),
					a.getFecha_alquiler(),
					a.getFecha_devolucion()
			});
		}
		
		frame.add(new JScrollPane(table));
		frame.setVisible(true);
	}
	
	private static void mostrarAlquileresPorUsuario(Connection conn, Scanner sc) {
		while (true) {
			Cliente cliente = null;
			int idCliente = 0;
			System.out.println("[1] Ingresar Nº de Cliente");
			System.out.println("[2] Mostrar lista de clientes existentes");
			System.out.println("[3] Regresar al menú principal");
			byte opcion = selectorOpcion(sc);
			
			switch (opcion) {
				case 1:
					try {
						System.out.print("Ingrese el Nº de Cliente: ");
						idCliente = sc.nextInt();
					} catch (InputMismatchException e) {
						System.out.println("Error: Este campo solo admite números");
						sc.nextLine();
					}
					cliente = new ClienteQuery().mostrarClientePorId(conn, idCliente);
					break;
				case 2:
					cliente = new ClienteQuery().selectorClientePorId(conn, sc);
					break;
				case 3:
					break;
				default:
					System.out.println("Advertencia: Opción no válida");
					continue;
			}
			
			if (cliente != null) {
				JFrame frame = new JFrame("Listado de alquileres de " + cliente.getNombre());
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setSize(1100, 300);
				
				DefaultTableModel model = new DefaultTableModel();
				JTable table = new JTable(model);
				
				model.addColumn("ID de alquiler");
				model.addColumn("ID del vehículo");
				model.addColumn("Descripción del vehículo");
				model.addColumn("Fecha de inicio del alquiler");
				model.addColumn("Fecha de finalización del alquiler");
				model.addColumn("Estado");
				
				ArrayList<Alquiler> listaAlquileresPorUsuario = new AlquilerQuery().mostrarAlquileresPorUsuario(conn, cliente.getId());
				
				for (Alquiler a : listaAlquileresPorUsuario) {
					model.addRow(new Object[] {
							a.getId_alquiler(),
							a.getVehiculo().getId_vehiculo(),
							a.getVehiculo().getMarcaModelo(),
							a.getFecha_alquiler(),
							a.getFecha_devolucion(),
							a.getEstado()
					});
				}
				
				frame.add(new JScrollPane(table));
				frame.setVisible(true);
			} else {
				if (manejoClienteNoExiste(sc, conn)) {
					continue;
				} else {
					continue;
				}
			}
			break;
		}
	}
	
	private static void finalizarAlquiler(Connection conn, Scanner sc) {
		while (true) {
			Alquiler alquiler = null;
			System.out.println("[1] Mostrar lista de alquileres activos");
			System.out.println("[2] Regresar al menú principal");
			byte opcion = selectorOpcion(sc);
			
			switch (opcion) {
				case 1:
					alquiler = new AlquilerQuery().selectorAlquilerActivo(conn, sc);
					if (alquiler != null) {
						AlquilerQuery.finalizarAlquiler(conn, alquiler.getId_alquiler());
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
			System.out.print("Ingrese una opción: ");
			opcion = sc.nextByte();
			sc.nextLine();
		} catch (InputMismatchException e) {
			System.out.println("Error: El selector de opciones solo admite números");
			sc.nextLine();
		}
		return opcion;
	}
	
	private static boolean manejoClienteNoExiste(Scanner sc, Connection conn) {
		System.out.println("Error: El cliente no existe");
		System.out.println("[1] Registrar nuevo cliente");
		System.out.println("[2] Regresar a opciones");
		byte opcion_error = selectorOpcion(sc);
		
		switch (opcion_error) {
			case 1:
				insertarCliente(sc, conn);
				return true;
			case 2:
				return false;
			default:
				System.out.println("Advertencia: Opción no válida");
				return false;
		}
	}
	
	private static boolean validarDNI(String dni) {
		// Verifica que solo tenga números entre 6 y 9 caracteres
		return dni.matches("\\d{6,9}");
	}
	
	private static boolean validarNombre(String nombre) {
		// Verifica que solo tenga letras (sin importar acentos) y que tenga como mínimo 2 palabras separadas
		return nombre.matches("^[\\p{L}]+(\\s[\\p{L}]+)+$");
	}
	
	private static boolean validarDireccion(String direccion) {
		// Verifica que no contenga solo números, que tenga como mínimo 2 palabras separadas
	    return direccion.matches(".*[\\p{L}]+.*") && direccion.matches("^[\\p{L}\\d\\s,./]+$") && direccion.matches(".*\\b\\p{L}+\\b.*\\s+\\b\\p{L}+\\b.*");
	}
	
	private static boolean validarTelefono(String telefono) {
		// Verifica solo números entre 7 y 15 caracteres
		return telefono.matches("^\\d{7,15}$");
	}
	
	private static boolean validarMatricula(String matricula) {
		// Verifica los formatos AB123CD o ABC123
		return matricula.matches("^[A-Z]{2}\\d{3}[A-Z]{2}$|^[A-Z]{3}\\d{3}$");
	}
}
