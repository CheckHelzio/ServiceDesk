package checkhelzio.ccv.servicedeskcucsh;

import android.os.Parcel;
import android.os.Parcelable;

public class Incidente implements Parcelable {

    private String codigoDelCliente;
    private String nombreDelCliente;
    private String dependenciaDelCliente;
    private String ubicacionDelCliente;
    private String telefonoDelCliente;
    private String correoElectronicoDelCliente;

    private int areaDelServicio;
    private int tipoDeServicio;
    private int prioridadDelServicio;
    private String descripcionDelReporte;

    private long fechaYHoraDelReporte;
    private int statusDeTerminacionDelReporte;
    private int folioDelReporte;

    private String codigoDelTecnicoAsignado;
    private String codigoDeQuienLevandoElReporte;
    private int statusDeModificacionDelReporte;
    private String tagDelReporte;
    private String comentarioTecnico;

    protected String aTag() {
        String t = "";

        t += this.codigoDelCliente + "::";
        t += this.nombreDelCliente + "::";
        t += this.dependenciaDelCliente + "::";
        t += this.ubicacionDelCliente + "::";
        t += this.telefonoDelCliente + "::";
        t += this.correoElectronicoDelCliente + "::";
        t += this.areaDelServicio + "::";
        t += this.tipoDeServicio + "::";
        t += this.prioridadDelServicio + "::";
        t += this.descripcionDelReporte + "::";
        t += fechaYHoraDelReporte + "::";
        t += this.statusDeTerminacionDelReporte + "::";
        t += "F" + this.folioDelReporte + "::";
        t += this.codigoDelTecnicoAsignado + "::";
        t += this.codigoDeQuienLevandoElReporte + "::";
        t += this.statusDeModificacionDelReporte + "::";
        t += this.comentarioTecnico;

        return t;
    }

    public Incidente(String codigoDelCliente, String nombreDelCliente, String dependenciaDelCliente, String ubicacionDelCliente, String telefonoDelCliente, String correoElectronicoDelCliente, int areaDelServicio, int tipoDeServicio, int prioridadDelServicio, String descripcionDelReporte, long fechaYHoraDelReporte, int statusDeTerminacionDelReporte, int folioDelReporte, String codigoDelTecnicoAsignado, String codigoDeQuienLevandoElReporte, int statusDeModificacionDelReporte, String tagDelReporte, String comentarioTecnico) {
        this.codigoDelCliente = codigoDelCliente;
        this.nombreDelCliente = nombreDelCliente;
        this.dependenciaDelCliente = dependenciaDelCliente;
        this.ubicacionDelCliente = ubicacionDelCliente;
        this.telefonoDelCliente = telefonoDelCliente;
        this.correoElectronicoDelCliente = correoElectronicoDelCliente;
        this.areaDelServicio = areaDelServicio;
        this.tipoDeServicio = tipoDeServicio;
        this.prioridadDelServicio = prioridadDelServicio;
        this.descripcionDelReporte = descripcionDelReporte;
        this.fechaYHoraDelReporte = fechaYHoraDelReporte;
        this.statusDeTerminacionDelReporte = statusDeTerminacionDelReporte;
        this.folioDelReporte = folioDelReporte;
        this.codigoDelTecnicoAsignado = codigoDelTecnicoAsignado;
        this.codigoDeQuienLevandoElReporte = codigoDeQuienLevandoElReporte;
        this.statusDeModificacionDelReporte = statusDeModificacionDelReporte;
        this.tagDelReporte = tagDelReporte;
        this.comentarioTecnico = comentarioTecnico;
    }

    public void setFechaYHoraDelReporte(long fechaYHoraDelReporte) {
        this.fechaYHoraDelReporte = fechaYHoraDelReporte;
    }

    public String getCodigoDelCliente() {
        return codigoDelCliente;
    }

    public String getNombreDelCliente() {
        return nombreDelCliente;
    }

    public String getDependenciaDelCliente() {
        return dependenciaDelCliente;
    }

    public String getUbicacionDelCliente() {
        return ubicacionDelCliente;
    }

    public String getTelefonoDelCliente() {
        return telefonoDelCliente;
    }

    public String getCorreoElectronicoDelCliente() {
        return correoElectronicoDelCliente;
    }

    public int getAreaDelServicio() {
        return areaDelServicio;
    }

    public int getTipoDeServicio() {
        return tipoDeServicio;
    }

    public int getPrioridadDelServicio() {
        return prioridadDelServicio;
    }

    public String getDescripcionDelReporte() {
        return descripcionDelReporte;
    }

    public long getFechaYHoraDelReporte() {
        return fechaYHoraDelReporte;
    }

    public int getStatusDeTerminacionDelReporte() {
        return statusDeTerminacionDelReporte;
    }

    public int getFolioDelReporte() {
        return folioDelReporte;
    }

    public String getCodigoDelTecnicoAsignado() {
        return codigoDelTecnicoAsignado;
    }

    public String getCodigoDeQuienLevandoElReporte() {
        return codigoDeQuienLevandoElReporte;
    }

    public int getStatusDeModificacionDelReporte() {
        return statusDeModificacionDelReporte;
    }

    public String getTagDelReporte() {
        return tagDelReporte;
    }

    public String getComentarioTecnico() {
        return comentarioTecnico;
    }

    public void setStatusDeTerminacionDelReporte(int statusDeTerminacionDelReporte) {
        this.statusDeTerminacionDelReporte = statusDeTerminacionDelReporte;
    }

    public void setStatusDeModificacionDelReporte(int statusDeModificacionDelReporte) {
        this.statusDeModificacionDelReporte = statusDeModificacionDelReporte;
    }

    public void setComentarioTecnico(String comentarioTecnico) {
        this.comentarioTecnico = comentarioTecnico;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.codigoDelCliente);
        dest.writeString(this.nombreDelCliente);
        dest.writeString(this.dependenciaDelCliente);
        dest.writeString(this.ubicacionDelCliente);
        dest.writeString(this.telefonoDelCliente);
        dest.writeString(this.correoElectronicoDelCliente);
        dest.writeInt(this.areaDelServicio);
        dest.writeInt(this.tipoDeServicio);
        dest.writeInt(this.prioridadDelServicio);
        dest.writeString(this.descripcionDelReporte);
        dest.writeLong(this.fechaYHoraDelReporte);
        dest.writeInt(this.statusDeTerminacionDelReporte);
        dest.writeInt(this.folioDelReporte);
        dest.writeString(this.codigoDelTecnicoAsignado);
        dest.writeString(this.codigoDeQuienLevandoElReporte);
        dest.writeInt(this.statusDeModificacionDelReporte);
        dest.writeString(this.tagDelReporte);
        dest.writeString(this.comentarioTecnico);
    }

    protected Incidente(Parcel in) {
        this.codigoDelCliente = in.readString();
        this.nombreDelCliente = in.readString();
        this.dependenciaDelCliente = in.readString();
        this.ubicacionDelCliente = in.readString();
        this.telefonoDelCliente = in.readString();
        this.correoElectronicoDelCliente = in.readString();
        this.areaDelServicio = in.readInt();
        this.tipoDeServicio = in.readInt();
        this.prioridadDelServicio = in.readInt();
        this.descripcionDelReporte = in.readString();
        this.fechaYHoraDelReporte = in.readLong();
        this.statusDeTerminacionDelReporte = in.readInt();
        this.folioDelReporte = in.readInt();
        this.codigoDelTecnicoAsignado = in.readString();
        this.codigoDeQuienLevandoElReporte = in.readString();
        this.statusDeModificacionDelReporte = in.readInt();
        this.tagDelReporte = in.readString();
        this.comentarioTecnico = in.readString();
    }

    public static final Parcelable.Creator<Incidente> CREATOR = new Parcelable.Creator<Incidente>() {
        @Override
        public Incidente createFromParcel(Parcel source) {
            return new Incidente(source);
        }

        @Override
        public Incidente[] newArray(int size) {
            return new Incidente[size];
        }
    };

    public void setTagDelReporte(String tagDelReporte) {
        this.tagDelReporte = tagDelReporte;
    }
}
