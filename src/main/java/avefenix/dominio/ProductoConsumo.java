
package avefenix.dominio;

public class ProductoConsumo extends Producto {
    public ProductoConsumo(String sku, String nombre, String categoria, double precioBase, int stock, int umbral) {
        super(sku,nombre,categoria,precioBase,stock,umbral);
    }
    @Override public String getTipo(){ return "CONSUMO"; }
    @Override public double calcularPrecioFinal(){ return getPrecioBase() * 1.21; }
}
