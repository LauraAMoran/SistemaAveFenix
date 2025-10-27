
package avefenix.util;
import avefenix.dominio.Producto;
import java.util.List;
public class Busquedas {
    public static int binariaPorSku(List<Producto> lista, String sku){
        int lo=0, hi=lista.size()-1;
        while (lo<=hi){
            int mid=(lo+hi)>>>1;
            int cmp = lista.get(mid).getSku().compareTo(sku);
            if (cmp==0) return mid;
            if (cmp<0) lo=mid+1; else hi=mid-1;
        }
        return -1;
    }
}
