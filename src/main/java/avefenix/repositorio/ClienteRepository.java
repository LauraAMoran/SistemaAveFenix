
package avefenix.repositorio;

import avefenix.dominio.Cliente;
import java.util.*;

public interface ClienteRepository {
    void insertar(Cliente c);
    Optional<Cliente> buscarPorId(Integer id);
    java.util.List<Cliente> listar();
}
