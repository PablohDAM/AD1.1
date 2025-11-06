package main.java.com.empresa;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import main.java.com.empresa.db.DBConnection;
import main.java.com.empresa.model.Categoria;
import main.java.com.empresa.model.Producto;
import main.java.com.empresa.service.CategoriaService;
import main.java.com.empresa.service.InventarioService;
import main.java.com.empresa.service.MovimientoService;
import main.java.com.empresa.util.JSONUtil;
import main.java.com.empresa.util.LogUtil;

public class Main {

	public static void main(String[] args) {
		System.out.println("Sistema de gestión de inventario iniciado.");

		InventarioService inventarioService = new InventarioService();
		CategoriaService categoriaService = new CategoriaService();
		MovimientoService movimientoService = new MovimientoService();
		
		// 1️ Importar productos desde CSV al inicio
		inventarioService.importarProductosDesdeCSV("docs/inventario.csv");
		LogUtil.info("Datos iniciales cargados desde inventario.csv");

		Scanner scanner = new Scanner(System.in);
		int opcion;

		do {
			System.out.println("\n===== MENÚ PRINCIPAL =====");
			System.out.println("1. Gestión Categorías");
			System.out.println("2. Gestión Productos");
			System.out.println("3. Gestionar movimientos de stock");
			System.out.println("4. Exportar productos con bajo stock a JSON");
			System.out.println("0. Salir");
			System.out.print("Elige una opción: ");


			try {
		        opcion = Integer.parseInt(scanner.nextLine());
		    } catch (NumberFormatException e) {
		        System.out.println("Entrada no válida. Por favor, introduce un número.");
		        opcion = -1; // Valor inválido para repetir el menú
		        continue; // Salta al inicio del ciclo
		    }

			switch (opcion) {
			case 1:
				menuCategorias(scanner, categoriaService);
				break;
			case 2:
				menuProductos(scanner, inventarioService);
				break;
			case 3:
				menuMovimientos(scanner, movimientoService);
				break;
			case 4:
				System.out.print("Introduce el stock mínimo: ");
				int umbral = Integer.parseInt(scanner.nextLine());
				List<Producto> productosBajos = inventarioService.obtenerProductosConStockMenorA(umbral);
				JSONUtil.exportarProductos(productosBajos, "productos_bajo_stock.json");
				System.out.println("Exportación realizada a productos_bajo_stock.json");
				break;
			case 0:
				System.out.println("Saliendo del sistema...");
				try {
					DBConnection.closeConnection();// cerrar conexión DDBB
					System.out.println("Base de datos desconectada con éxito");
				} catch (SQLException e) {
					System.out.println("Error al desconectar la base de datos");
					LogUtil.error(e.getMessage());
				} 
				break;
			default:
				System.out.println("Opción no válida. Inténtalo de nuevo.");
			}
		} while (opcion != 0);

		scanner.close();
	}

	// ===== SUBMENÚ CATEGORÍAS =====
	private static void menuCategorias(Scanner scanner, CategoriaService categoriaService) {
		int opcion;
		do {
			System.out.println("\n--- CRUD Categorías ---");
			System.out.println("1. Crear categoría");
			System.out.println("2. Listar categorías");
			System.out.println("3. Actualizar categoría");
			System.out.println("4. Eliminar categoría");
			System.out.println("0. Volver");
			System.out.print("Elige una opción: ");

			try {
		        opcion = Integer.parseInt(scanner.nextLine());
		    } catch (NumberFormatException e) {
		        System.out.println("Entrada no válida. Por favor, introduce un número.");
		        opcion = -1; // Valor inválido para repetir el menú
		        continue; // Salta al inicio del ciclo
		    }

			switch (opcion) {
			case 1:
				System.out.print("Nombre de la categoría: ");
				String nombre = scanner.nextLine();
				categoriaService.crearCategoria(new Categoria(0, nombre));
				break;
			case 2:
				List<Categoria> categorias = categoriaService.obtenerTodas();
				categorias.forEach(System.out::println);
				break;
			case 3:
				System.out.print("ID de la categoría a actualizar: ");
				int idUpd = Integer.parseInt(scanner.nextLine());
				System.out.print("Nuevo nombre: ");
				String nuevoNombre = scanner.nextLine();
				categoriaService.actualizarCategoria(new Categoria(idUpd, nuevoNombre));
				break;
			case 4:
				System.out.print("ID de la categoría a eliminar: ");
				int idDel = Integer.parseInt(scanner.nextLine());
				categoriaService.eliminarCategoria(idDel);
				break;
			}
		} while (opcion != 0);
	}

	// ===== SUBMENÚ PRODUCTOS =====
	private static void menuProductos(Scanner scanner, InventarioService inventarioService) {
		int opcion;
		do {
			System.out.println("\n--- CRUD Productos ---");
			System.out.println("1. Crear producto");
			System.out.println("2. Listar productos");
			System.out.println("3. Actualizar producto");
			System.out.println("4. Eliminar producto");
			System.out.println("0. Volver");
			System.out.print("Elige una opción: ");

			try {
		        opcion = Integer.parseInt(scanner.nextLine());
		    } catch (NumberFormatException e) {
		        System.out.println("Entrada no válida. Por favor, introduce un número.");
		        opcion = -1; // Valor inválido para repetir el menú
		        continue; // Salta al inicio del ciclo
		    }

			switch (opcion) {
			case 1:
				System.out.print("Nombre: ");
				String nombre = scanner.nextLine();
				System.out.print("Categoría ID: ");
				int catId = Integer.parseInt(scanner.nextLine());
				System.out.print("Precio: ");
				double precio = scanner.nextDouble();
				scanner.nextLine();
				System.out.print("Stock: ");
				int stock = Integer.parseInt(scanner.nextLine());
				inventarioService.crearProducto(new Producto(0, nombre, catId, precio, stock));
				break;
			case 2:
				List<Producto> productos = inventarioService.obtenerTodos();
				productos.forEach(System.out::println);
				break;
			case 3:
				System.out.print("ID del producto a actualizar: ");
				int idUpd = Integer.parseInt(scanner.nextLine());
				System.out.print("Nuevo nombre: ");
				String nuevoNombre = scanner.nextLine();
				System.out.print("Nueva categoría ID: ");
				int nuevaCat = Integer.parseInt(scanner.nextLine());
				System.out.print("Nuevo precio: ");
				double nuevoPrecio = scanner.nextDouble();
				scanner.nextLine();
				System.out.print("Nuevo stock: ");
				int nuevoStock = Integer.parseInt(scanner.nextLine());
				inventarioService
						.actualizarProducto(new Producto(idUpd, nuevoNombre, nuevaCat, nuevoPrecio, nuevoStock));
				break;
			case 4:
				System.out.print("ID del producto a eliminar: ");
				int idDel = Integer.parseInt(scanner.nextLine());
				inventarioService.eliminarProducto(idDel);
				break;
			}
		} while (opcion != 0);
	}

	// ===== SUBMENÚ MOVIMIENTOS =====
	private static void menuMovimientos(Scanner scanner, MovimientoService movimientoService) {
		int opcion;
		do {
			System.out.println("\n--- Gestión de movimientos de stock ---");
			System.out.println("1. Registrar entrada de stock");
			System.out.println("2. Registrar salida de stock");
			System.out.println("0. Volver");
			System.out.print("Elige una opción: ");

			try {
		        opcion = Integer.parseInt(scanner.nextLine());
		    } catch (NumberFormatException e) {
		        System.out.println("Entrada no válida. Por favor, introduce un número.");
		        opcion = -1; // Valor inválido para repetir el menú
		        continue; // Salta al inicio del ciclo
		    }

			switch (opcion) {
			case 1:
				System.out.print("ID del producto: ");
				int idEntrada = Integer.parseInt(scanner.nextLine());
				System.out.print("Cantidad: ");
				int cantEntrada = Integer.parseInt(scanner.nextLine());
				movimientoService.registrarEntrada(idEntrada, cantEntrada);
				break;
			case 2:
				System.out.print("ID del producto: ");
				int idSalida = Integer.parseInt(scanner.nextLine());
				System.out.print("Cantidad: ");
				int cantSalida = Integer.parseInt(scanner.nextLine());
				movimientoService.registrarSalida(idSalida, cantSalida);
				break;
			}
		} while (opcion != 0);
	}
}
