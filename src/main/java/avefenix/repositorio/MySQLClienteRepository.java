
package avefenix.repositorio;

import avefenix.dominio.*;
import avefenix.excepciones.*;
import java.sql.*;
import java.util.*;

public class MySQLClienteRepository implements ClienteRepository {
    private final String url,user,pass;
    public MySQLClienteRepository(String url,String user,String pass){ this.url=url; this.user=user; this.pass=pass; }
    private Connection getConn() throws SQLException { return DriverManager.getConnection(url,user,pass); }

    @Override public void insertar(Cliente c){
        String sql = "INSERT INTO clientes(dni,nombre,contacto) VALUES(?,?,?)";
        try(Connection con=getConn(); PreparedStatement ps=con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,c.getDni()); ps.setString(2,c.getNombre()); ps.setString(3,c.getContacto());
            ps.executeUpdate();
            try(ResultSet keys = ps.getGeneratedKeys()){
                if (keys.next()) c.setId(keys.getInt(1));
            }
        } catch(SQLException e){ throw new RepositorioException("Error insertando cliente", e); }
    }
    @Override public Optional<Cliente> buscarPorId(Integer id){
        String sql = "SELECT id,dni,nombre,contacto FROM clientes WHERE id=?";
        try(Connection con=getConn(); PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1,id);
            try(ResultSet rs=ps.executeQuery()){
                if (rs.next()) return Optional.of(new Cliente(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                return Optional.empty();
            }
        } catch(SQLException e){ throw new RepositorioException("Error buscando cliente", e); }
    }
    @Override public List<Cliente> listar(){
        String sql = "SELECT id,dni,nombre,contacto FROM clientes";
        List<Cliente> out=new ArrayList<>();
        try(Connection con=getConn(); PreparedStatement ps=con.prepareStatement(sql); ResultSet rs=ps.executeQuery()){
            while(rs.next()) out.add(new Cliente(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            return out;
        } catch(SQLException e){ throw new RepositorioException("Error listando clientes", e); }
    }
}
