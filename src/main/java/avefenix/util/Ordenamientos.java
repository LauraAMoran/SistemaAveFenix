
package avefenix.util;
import avefenix.dominio.Producto;
import java.util.*;
public class Ordenamientos {
    public static void porNombre(java.util.List<Producto> l){ l.sort(Comparator.comparing(Producto::getNombre, String::compareToIgnoreCase)); }
    public static void porPrecio(java.util.List<Producto> l){ l.sort(Comparator.comparingDouble(Producto::calcularPrecioFinal)); }
    public static void porSku(java.util.List<Producto> l){ l.sort(Comparator.comparing(Producto::getSku)); }
}
