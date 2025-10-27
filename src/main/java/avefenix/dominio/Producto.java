
package avefenix.dominio;

public abstract class Producto extends BaseEntity {
    private final String sku;
    private final String nombre;
    private final String categoria;
    private final double precioBase;
    private int stock;
    private int umbralBajo;

    public Producto(String sku, String nombre, String categoria, double precioBase, int stock, int umbralBajo) {
        if (sku==null || sku.isBlank()) throw new IllegalArgumentException("SKU obligatorio");
        if (nombre==null || nombre.isBlank()) throw new IllegalArgumentException("Nombre obligatorio");
        if (categoria==null || categoria.isBlank()) throw new IllegalArgumentException("Categoría obligatoria");
        if (precioBase < 0) throw new IllegalArgumentException("Precio inválido");
        if (stock < 0) throw new IllegalArgumentException("Stock inválido");
        if (umbralBajo < 0) throw new IllegalArgumentException("Umbral inválido");
        this.sku=sku; this.nombre=nombre; this.categoria=categoria;
        this.precioBase=precioBase; this.stock=stock; this.umbralBajo=umbralBajo;
    }

    public String getSku(){return sku;}
    public String getNombre(){return nombre;}
    public String getCategoria(){return categoria;}
    public double getPrecioBase(){return precioBase;}
    public int getStock(){return stock;}
    public void setStock(int s){ this.stock=s; }
    public int getUmbralBajo(){return umbralBajo;}
    public void setUmbralBajo(int u){ this.umbralBajo=u; }

    public abstract String getTipo();
    public abstract double calcularPrecioFinal();
}
