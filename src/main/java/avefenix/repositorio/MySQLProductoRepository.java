
package avefenix.repositorio;

import avefenix.dominio.*;
import avefenix.excepciones.*;
import java.sql.*;
import java.util.*;

public class MySQLProductoRepository implements ProductoRepository {
    private final String url,user,pass;
    public MySQLProductoRepository(String url,String user,String pass){
        this.url=url; this.user=user; this.pass=pass;
    }
    private Connection getConn() throws SQLException { return DriverManager.getConnection(url,user,pass); }

    @Override public void insertar(Producto p){
        String sql = "INSERT INTO productos(sku,nombre,categoria,tipo,precio_base,stock,umbral_bajo) VALUES(?,?,?,?,?,?,?)";
        try(Connection c=getConn(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,p.getSku()); ps.setString(2,p.getNombre()); ps.setString(3,p.getCategoria());
            ps.setString(4,p.getTipo()); ps.setDouble(5,p.getPrecioBase()); ps.setInt(6,p.getStock()); ps.setInt(7,p.getUmbralBajo());
            ps.executeUpdate();
        } catch(SQLException e){ throw new RepositorioException("Error insertando", e); }
    }
    @Override public void actualizar(Producto p){
        String sql = "UPDATE productos SET nombre=?, categoria=?, tipo=?, precio_base=?, stock=?, umbral_bajo=? WHERE sku=?";
        try(Connection c=getConn(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,p.getNombre()); ps.setString(2,p.getCategoria()); ps.setString(3,p.getTipo());
            ps.setDouble(4,p.getPrecioBase()); ps.setInt(5,p.getStock()); ps.setInt(6,p.getUmbralBajo());
            ps.setString(7,p.getSku());
            if (ps.executeUpdate()==0) throw new NoEncontradoException("SKU no encontrado");
        } catch(SQLException e){ throw new RepositorioException("Error actualizando", e); }
    }
    @Override public void eliminar(String sku){
        String sql = "DELETE FROM productos WHERE sku=?";
        try(Connection c=getConn(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,sku);
            if (ps.executeUpdate()==0) throw new NoEncontradoException("SKU no encontrado");
        } catch(SQLException e){ throw new RepositorioException("Error eliminando", e); }
    }
    @Override public Optional<Producto> buscar(String sku){
        String sql = "SELECT sku,nombre,categoria,tipo,precio_base,stock,umbral_bajo FROM productos WHERE sku=?";
        try(Connection c=getConn(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,sku);
            try(ResultSet rs=ps.executeQuery()){
                if (rs.next()) return Optional.of(map(rs));
                return Optional.empty();
            }
        } catch(SQLException e){ throw new RepositorioException("Error buscando", e); }
    }
    @Override public List<Producto> listar(){
        String sql = "SELECT sku,nombre,categoria,tipo,precio_base,stock,umbral_bajo FROM productos";
        List<Producto> out=new ArrayList<>();
        try(Connection c=getConn(); PreparedStatement ps=c.prepareStatement(sql); ResultSet rs=ps.executeQuery()){
            while(rs.next()) out.add(map(rs));
            return out;
        } catch(SQLException e){ throw new RepositorioException("Error listando", e); }
    }
    private Producto map(ResultSet rs) throws SQLException {
        String sku=rs.getString(1), nombre=rs.getString(2), categoria=rs.getString(3), tipo=rs.getString(4);
        double precio=rs.getDouble(5); int stock=rs.getInt(6), umbral=rs.getInt(7);
        if ("PREMIUM".equalsIgnoreCase(tipo)) return new ProductoPremium(sku,nombre,categoria,precio,stock,umbral,0.10);
        return new ProductoConsumo(sku,nombre,categoria,precio,stock,umbral);
    }
}
