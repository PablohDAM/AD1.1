package main.java.com.empresa.model;

public class Producto {
    private int id_producto;
    private String nombre;
    private int categoriaId;
    private double precio;
    private int stock;

    // Campo auxiliar para cuando se lee desde CSV
    private String categoriaNombre;

    public Producto(int id_producto, String nombre, int categoriaId, double precio, int stock) {
        this.id_producto = id_producto;
        this.nombre = nombre;
        this.categoriaId = categoriaId;
        this.precio = precio;
        this.stock = stock;
    }

    public Producto() {
	}

	// Getters y setters
    public int getId_producto() { return id_producto; }
    public void setId_producto(int id_producto) { this.id_producto = id_producto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCategoriaId() { return categoriaId; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }

    @Override
    public String toString() {
        return "Producto: id_producto = " + id_producto + ", nombre = " + nombre +
               ", categoriaId = " + categoriaId + " (" + categoriaNombre + ")" +
               ", precio = " + precio + ", stock = " + stock;
    }
}
