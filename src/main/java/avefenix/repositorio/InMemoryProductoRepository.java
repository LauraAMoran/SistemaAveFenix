
package avefenix.repositorio;

import avefenix.dominio.*;
import avefenix.excepciones.*;
import java.util.*;

public class InMemoryProductoRepository implements ProductoRepository {
    private final Map<String, Producto> data = new HashMap<>();

    @Override public void insertar(Producto p){
        if (data.containsKey(p.getSku())) throw new ValidacionException("SKU existente");
        data.put(p.getSku(), p);
    }
    @Override public void actualizar(Producto p){
        if (!data.containsKey(p.getSku())) throw new NoEncontradoException("SKU no encontrado");
        data.put(p.getSku(), p);
    }
    @Override public void eliminar(String sku){
        if (data.remove(sku)==null) throw new NoEncontradoException("SKU no encontrado");
    }
    @Override public Optional<Producto> buscar(String sku){ return Optional.ofNullable(data.get(sku)); }
    @Override public List<Producto> listar(){ return new ArrayList<>(data.values()); }
}
