
package avefenix.dominio;

public class Cliente extends BaseEntity {
    private String dni;
    private String nombre;
    private String contacto;
    public Cliente(Integer id, String dni, String nombre, String contacto){
        setId(id);
        this.dni=dni; this.nombre=nombre; this.contacto=contacto;
        if (nombre==null || nombre.isBlank()) throw new IllegalArgumentException("Nombre de cliente obligatorio");
    }
    public String getDni(){return dni;}
    public String getNombre(){return nombre;}
    public String getContacto(){return contacto;}
}
