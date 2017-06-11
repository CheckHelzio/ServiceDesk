package checkhelzio.ccv.servicedeskcucsh;

/**
 * Created by check on 06/04/2017.
 */

public class DatosPersonales {
    private String codigo;
    private String nombre;
    private String dependencia;
    private String ubicacion;
    private String telefono;
    private String correo;

    public DatosPersonales(String codigo, String nombre, String dependencia, String ubicacion, String telefono, String correo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.dependencia = dependencia;
        this.ubicacion = ubicacion;
        this.telefono = telefono;
        this.correo = correo;
    }

    public String getCodigo() {
        return codigo;
    }


    public String getNombre() {
        return nombre;
    }


    public String getDependencia() {
        return dependencia;
    }


    public String getUbicacion() {
        return ubicacion;
    }


    public String getTelefono() {
        return telefono;
    }


    public String getCorreo() {
        return correo;
    }


    public String aTag() {
        String t = "";

        t += this.codigo + "::";
        t += this.nombre + "::";
        t += this.dependencia + "::";
        t += this.ubicacion + "::";
        t += this.telefono + "::";
        t += this.correo;

        return t;
    }
}
