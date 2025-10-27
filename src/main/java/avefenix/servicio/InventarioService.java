
package avefenix.servicio;

import avefenix.dominio.*;
import avefenix.excepciones.*;
import avefenix.repositorio.ProductoRepository;
import java.util.*;

public class InventarioService {
    private final ProductoRepository repo;
    private final Deque<Producto> pilaBajas = new ArrayDeque<>();

    public InventarioService(ProductoRepository repo){ this.repo=repo; }

    public void altaProducto(Producto p){ repo.insertar(p); }
    public java.util.List<Producto> listarProductos(){ return repo.listar(); }

    public void modificarStock(String sku, int nuevo){
        Producto p = repo.buscar(sku).orElseThrow(() -> new NoEncontradoException("SKU inexistente"));
        p.setStock(nuevo);
        repo.actualizar(p);
    }
    public void eliminarProducto(String sku){
        Producto p = repo.buscar(sku).orElseThrow(() -> new NoEncontradoException("SKU inexistente"));
        repo.eliminar(sku);
        pilaBajas.push(p);
    }
    public void deshacerUltimaBaja(){
        if (pilaBajas.isEmpty()) throw new ValidacionException("No hay bajas para deshacer");
        repo.insertar(pilaBajas.pop());
    }
}
