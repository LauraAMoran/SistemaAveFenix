
package avefenix.dominio;

public class ProductoPremium extends Producto {
    private final double descuento; // 0..1
    public ProductoPremium(String sku, String nombre, String categoria, double precioBase, int stock, int umbral, double descuento) {
        super(sku,nombre,categoria,precioBase,stock,umbral);
        if (descuento<0 || descuento>1) throw new IllegalArgumentException("Descuento inválido");
        this.descuento=descuento;
    }
    public double getDescuento(){return descuento;}
    @Override public String getTipo(){ return "PREMIUM"; }
    @Override public double calcularPrecioFinal(){
        double conIva = getPrecioBase() * 1.21;
        return conIva * (1 - descuento);
    }
}
