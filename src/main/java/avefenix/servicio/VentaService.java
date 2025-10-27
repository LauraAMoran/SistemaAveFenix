
package avefenix.servicio;

import avefenix.dominio.*;
import avefenix.excepciones.*;
import avefenix.repositorio.*;

public class VentaService {
    private final ProductoRepository repoProd;
    private final ClienteRepository repoCli;

    public VentaService(ProductoRepository p, ClienteRepository c){
        this.repoProd=p; this.repoCli=c;
    }

    public double registrarVenta(String sku, Integer clienteId, int cantidad, String canal){
        if (cantidad<=0) throw new ValidacionException("Cantidad inválida");
        Producto p = repoProd.buscar(sku).orElseThrow(() -> new NoEncontradoException("SKU inexistente"));
        if (p.getStock() < cantidad) throw new ValidacionException("Stock insuficiente");
        // (Persistencia de ventas en DB se puede agregar luego; aquí simulamos cálculo)
        double total = p.calcularPrecioFinal() * cantidad;
        p.setStock(p.getStock()-cantidad);
        repoProd.actualizar(p);
        return total;
    }
}
