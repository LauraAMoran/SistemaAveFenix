
package avefenix.repositorio;

import avefenix.dominio.*;
import java.util.*;

public class InMemoryClienteRepository implements ClienteRepository {
    private final Map<Integer, Cliente> data = new HashMap<>();
    private int seq=1;
    @Override public void insertar(Cliente c){
        if (c.getId()==null) c.setId(seq++);
        data.put(c.getId(), c);
    }
    @Override public Optional<Cliente> buscarPorId(Integer id){ return Optional.ofNullable(data.get(id)); }
    @Override public List<Cliente> listar(){ return new ArrayList<>(data.values()); }
}
