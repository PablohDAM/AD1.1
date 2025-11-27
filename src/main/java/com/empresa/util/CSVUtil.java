package main.java.com.empresa.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import main.java.com.empresa.dao.CategoriaDAO;
import main.java.com.empresa.model.Producto;

public class CSVUtil {

	public static List<Producto> leerProductos(String rutaCSV) {
		List<Producto> productos = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(rutaCSV))) {
			CategoriaDAO categoriaDAO = new CategoriaDAO();
			String linea;
			br.readLine(); // Saltar encabezado

			while ((linea = br.readLine()) != null) {
				String[] campos = linea.split(";");
				String nombre = campos[1].trim();
				String nombreCategoria = campos[2].trim();
				double precio = Double.parseDouble(campos[3].trim());
				int stock = Integer.parseInt(campos[4].trim());

				// Obtener o crear categoria_id
				int categoriaId = categoriaDAO.obtenerOCrearCategoria(nombreCategoria);

				Producto p = new Producto();
				p.setNombre(nombre);
				p.setCategoriaId(categoriaId);
				p.setPrecio(precio);
				p.setStock(stock);

				productos.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productos;
	}
}
