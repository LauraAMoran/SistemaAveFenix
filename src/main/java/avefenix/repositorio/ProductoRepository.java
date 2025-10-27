
package avefenix.repositorio;

import avefenix.dominio.Producto;
import java.util.*;

public interface ProductoRepository {
    void insertar(Producto p);
    void actualizar(Producto p);
    void eliminar(String sku);
    Optional<Producto> buscar(String sku);
    java.util.List<Producto> listar();
}
