package main.java.com.empresa.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.com.empresa.model.Producto;

public class JSONUtil {

    /**
     * Exporta una lista de productos a un archivo JSON usando Gson.
     * Si el directorio de destino no existe (por ejemplo, "log/export"), se crea automáticamente.
     *
     * @param productos lista de productos
     * @param filePath ruta completa del archivo destino, por ejemplo "log/export/productos.json"
     */
    public static void exportarProductos(List<Producto> productos, String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            // Crear carpeta si no existe
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
                LogUtil.info("Directorio creado: " + parentDir.getAbsolutePath());
            }

            // Escribir el archivo JSON
            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(productos, writer);
                LogUtil.info("Productos exportados correctamente a " + filePath);
            }

        } catch (IOException e) {
            LogUtil.error("Error exportando productos a JSON: " + e.getMessage());
        }
    }
}
