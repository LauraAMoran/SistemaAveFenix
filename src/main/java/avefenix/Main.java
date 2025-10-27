
package avefenix;

import avefenix.dominio.*;
import avefenix.excepciones.*;
import avefenix.repositorio.*;
import avefenix.servicio.*;
import avefenix.util.*;

import java.util.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        ProductoRepository repoProd = buildProductoRepo();
        ClienteRepository repoCli = buildClienteRepo();
        InventarioService inv = new InventarioService(repoProd);
        VentaService ventas = new VentaService(repoProd, repoCli);

        seed(inv, repoCli);

        boolean salir = false;
        while (!salir) {
            ConsoleUtils.clear();
            System.out.println("=== Sistema de Gestión Ave Fénix ===");
            System.out.println("1) Registrar producto");
            System.out.println("2) Listar productos");
            System.out.println("3) Modificar stock");
            System.out.println("4) Eliminar producto (Deshacer disponible)");
            System.out.println("5) Deshacer última baja");
            System.out.println("6) Registrar cliente");
            System.out.println("7) Listar clientes");
            System.out.println("8) Registrar venta");
            System.out.println("9) Ordenar productos (nombre/precio)");
            System.out.println("10) Buscar producto por SKU (binaria)");
            System.out.println("11) Mostrar alertas de bajo stock");
            System.out.println("0) Salir");
            System.out.print("Opción: ");
            String op = sc.nextLine().trim();
            try {
                switch (op) {
                    case "1" -> altaProducto(inv);
                    case "2" -> listarProductos(inv);
                    case "3" -> modificarStock(inv);
                    case "4" -> eliminarProducto(inv);
                    case "5" -> inv.deshacerUltimaBaja();
                    case "6" -> registrarCliente(repoCli);
                    case "7" -> listarClientes(repoCli);
                    case "8" -> registrarVenta(ventas);
                    case "9" -> ordenarProductos(inv);
                    case "10" -> buscarSKU(inv);
                    case "11" -> alertas(inv);
                    case "0" -> salir = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
            if (!salir) {
                System.out.println("\nEnter para continuar...");
                sc.nextLine();
            }
        }
        System.out.println("¡Hasta luego!");
    }

    private static ProductoRepository buildProductoRepo() {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");
        if (url != null && user != null && pass != null) {
            return new MySQLProductoRepository(url,user,pass);
        }
        return new InMemoryProductoRepository();
    }
    private static ClienteRepository buildClienteRepo() {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");
        if (url != null && user != null && pass != null) {
            return new MySQLClienteRepository(url,user,pass);
        }
        return new InMemoryClienteRepository();
    }

    private static void seed(InventarioService inv, ClienteRepository cliRepo) {
        if (inv.listarProductos().isEmpty()) {
            inv.altaProducto(new ProductoConsumo("SKU-AF-001","Gel limpiador", "Skincare", 4200, 40, 5));
            inv.altaProducto(new ProductoPremium("SKU-AF-010","Sérum Vitamina C", "Skincare", 9800, 15, 5, 0.10));
            inv.altaProducto(new ProductoConsumo("SKU-AF-020","Crema humectante", "Skincare", 6500, 25, 5));
        }
        if (cliRepo.listar().isEmpty()) {
            cliRepo.insertar(new Cliente(null,"00000000","Consumidor Final","@demo"));
        }
    }

    private static void altaProducto(InventarioService inv) {
        System.out.print("SKU: "); String sku = sc.nextLine().trim();
        System.out.print("Nombre: "); String nombre = sc.nextLine().trim();
        System.out.print("Categoria: "); String cat = sc.nextLine().trim();
        System.out.print("Tipo (1=Consumo,2=Premium): "); String t = sc.nextLine().trim();
        System.out.print("Precio base: "); double precio = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Stock inicial: "); int stock = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Umbral bajo stock: "); int umbral = Integer.parseInt(sc.nextLine().trim());
        if ("2".equals(t)) {
            System.out.print("Descuento (0..1): "); double d = Double.parseDouble(sc.nextLine().trim());
            inv.altaProducto(new ProductoPremium(sku,nombre,cat,precio,stock,umbral,d));
        } else {
            inv.altaProducto(new ProductoConsumo(sku,nombre,cat,precio,stock,umbral));
        }
        System.out.println("Producto registrado.");
    }
    private static void listarProductos(InventarioService inv) {
        System.out.println("\nSKU\tNombre\tTipo\tPrecioFinal\tStock\tUmbral\tAlerta");
        for (Producto p: inv.listarProductos()) {
            System.out.printf("%s\t%s\t%s\t$%.2f\t\t%d\t%d\t%s%n",
              p.getSku(), p.getNombre(), p.getTipo(), p.calcularPrecioFinal(), p.getStock(), p.getUmbralBajo(), 
              (p.getStock()<p.getUmbralBajo() ? "Poco stock" : "-"));
        }
    }
    private static void modificarStock(InventarioService inv) {
        System.out.print("SKU: "); String sku = sc.nextLine().trim();
        System.out.print("Nuevo stock: "); int s = Integer.parseInt(sc.nextLine().trim());
        inv.modificarStock(sku, s);
        System.out.println("Stock actualizado.");
    }
    private static void eliminarProducto(InventarioService inv) {
        System.out.print("SKU a eliminar: "); String sku = sc.nextLine().trim();
        inv.eliminarProducto(sku);
        System.out.println("Producto eliminado.");
    }
    private static void registrarCliente(ClienteRepository repoCli) {
        System.out.print("DNI: "); String dni = sc.nextLine().trim();
        System.out.print("Nombre: "); String nom = sc.nextLine().trim();
        System.out.print("Contacto: "); String cont = sc.nextLine().trim();
        repoCli.insertar(new Cliente(null,dni,nom,cont));
        System.out.println("Cliente registrado.");
    }
    private static void listarClientes(ClienteRepository repoCli) {
        System.out.println("\nID\tDNI\tNombre\tContacto");
        for (Cliente c: repoCli.listar()) {
            System.out.printf("%d\t%s\t%s\t%s%n", c.getId()==null?0:c.getId(), c.getDni(), c.getNombre(), c.getContacto());
        }
    }
    private static void registrarVenta(VentaService ventas) {
        System.out.print("SKU: "); String sku = sc.nextLine().trim();
        System.out.print("Cliente (ID o 0 para CF): "); int id = Integer.parseInt(sc.nextLine().trim());
        Integer clienteId = (id==0? null : id);
        System.out.print("Cantidad: "); int cant = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Canal (MOSTRADOR/ONLINE): "); String canal = sc.nextLine().trim().toUpperCase();
        double total = ventas.registrarVenta(sku, clienteId, cant, canal);
        System.out.println("Venta registrada. Total: $" + String.format("%.2f", total));
    }
    private static void ordenarProductos(InventarioService inv) {
        System.out.print("Ordenar por (1=Nombre,2=Precio): "); String t = sc.nextLine().trim();
        var lista = inv.listarProductos();
        if ("2".equals(t)) Ordenamientos.porPrecio(lista);
        else Ordenamientos.porNombre(lista);
        lista.forEach(p -> System.out.printf("%s - %s - $%.2f%n", p.getSku(), p.getNombre(), p.calcularPrecioFinal()));
    }
    private static void buscarSKU(InventarioService inv) {
        System.out.print("SKU: "); String sku = sc.nextLine().trim();
        var lista = inv.listarProductos();
        Ordenamientos.porSku(lista);
        int idx = Busquedas.binariaPorSku(lista, sku);
        if (idx>=0) System.out.println("Encontrado: " + lista.get(idx).getNombre());
        else System.out.println("No encontrado.");
    }
    private static void alertas(InventarioService inv) {
        System.out.println("Productos en bajo stock:");
        inv.listarProductos().stream().filter(p -> p.getStock() < p.getUmbralBajo())
            .forEach(p -> System.out.printf("- %s (%s) stock=%d umbral=%d%n", p.getNombre(), p.getSku(), p.getStock(), p.getUmbralBajo()));
    }
}
