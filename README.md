# Proyecto Inventario - Sistema de Gestión de Productos y Categorías

## 📦 Descripción

Este proyecto es una aplicación Java para la **gestión de un inventario de productos y categorías**, con soporte para:

- Importación desde CSV
- Exportación a JSON
- Registro de movimientos de stock
- Logging de eventos

---

## ⚙️ Configuración y Conexión a Base de Datos

**Base de datos:** MySQL  
**Host:** localhost  
**Puerto:** 3307  
**Base de datos:** `inventario_db`  
**Usuario:** `root`  
**Contraseña:** (vacía)

La conexión se configura en:

```java
package main.java.com.empresa.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3307/inventario_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("No se encontró el driver de MySQL", e);
            }
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null) {
            if (!connection.isClosed()) {
                connection.close();
                System.out.println("Conexión a la base de datos cerrada correctamente.");
            }
        }
    }
}
```

---

## 🗄️ Script SQL para Creación de Tablas

```sql
CREATE DATABASE IF NOT EXISTS inventario_db;
USE inventario_db;

CREATE TABLE categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    categoria_id INT NOT NULL,
    precio DOUBLE NOT NULL,
    stock INT NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

CREATE TABLE movimiento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    producto_id INT NOT NULL,
    tipo ENUM('ENTRADA', 'SALIDA') NOT NULL,
    cantidad INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);
```

---

## 📁 Paquetes y Clases Principales

- **model:** Clases modelo `Producto`, `Categoria`  
- **dao:** Clases para acceso a datos `ProductoDAO`, `CategoriaDAO`  
- **service:** Lógica de negocio y servicios `InventarioService`, `CategoriaService`, `MovimientoService`  
- **util:** Utilidades para CSV, JSON y Logging (`CSVUtil`, `JSONUtil`, `LogUtil`)  
- **db:** Clase de conexión a base de datos `DBConnection`  

---

## 💡 Uso y Ejemplos

### Crear y listar categorías
```java
CategoriaService categoriaService = new CategoriaService();

// Crear categoría
Categoria cat = new Categoria(0, "Electrónica");
categoriaService.crearCategoria(cat);

// Listar todas
List<Categoria> categorias = categoriaService.obtenerTodas();
categorias.forEach(System.out::println);
```

### Crear y listar productos
```java
InventarioService inventarioService = new InventarioService();

// Crear producto
Producto prod = new Producto();
prod.setNombre("Televisor");
prod.setCategoriaId(1);
prod.setPrecio(599.99);
prod.setStock(10);
inventarioService.crearProducto(prod);

// Listar todos
List<Producto> productos = inventarioService.obtenerTodos();
productos.forEach(System.out::println);
```

### Registrar movimientos de stock
```java
MovimientoService movimientoService = new MovimientoService();

// Registrar entrada de 5 unidades al producto 1
movimientoService.registrarEntrada(1, 5);

// Registrar salida de 3 unidades del producto 1
movimientoService.registrarSalida(1, 3);
```

---

## 📥 Importar productos desde CSV

```java
InventarioService inventarioService = new InventarioService();
inventarioService.importarProductosDesdeCSV("ruta/a/archivo.csv");
```

**Formato del CSV (separado por `;`):**

```
id_producto;nombre;categoria;precio;stock
1;Televisor;Electrónica;599.99;10
2;Laptop;Electrónica;999.99;5
```

---

## 📤 Exportar productos a JSON

```java
List<Producto> productos = inventarioService.obtenerTodos();
JSONUtil.exportarProductos(productos, "productos.json");
```

---

## 🧾 Logging

Los logs se guardan en el archivo:

```
log/app.log
```

Formato del log:

```
[2025-10-06T15:30:45.123] [INFO] Producto creado: Televisor
```

**Niveles de log:**

- `INFO`: Información general  
- `WARN`: Advertencias  
- `ERROR`: Errores  

---

## 🧩 Dependencias

- **MySQL Connector/J** – conexión con MySQL  
- **Gson** – exportación a JSON  

---

## 📝 Notas adicionales

- Ejecutar el script SQL antes de iniciar la aplicación.  
- Ajustar las credenciales de conexión en `DBConnection.java` si es necesario.  
- Se usa `try-with-resources` para manejo seguro de recursos JDBC.  
- La gestión de transacciones se aplica en movimientos de entrada y salida para asegurar consistencia.  

---

## 📬 Contacto

Para dudas o sugerencias, puedes contactarme.  
¡Gracias por usar este sistema de inventario! 💻
