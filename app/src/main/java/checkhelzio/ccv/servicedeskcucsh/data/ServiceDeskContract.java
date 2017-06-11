package checkhelzio.ccv.servicedeskcucsh.data;

import android.provider.BaseColumns;

/**
 * Created by check on 22/04/2017.
 */

public final class ServiceDeskContract {

    public final class ClienteEntry implements BaseColumns {

        public static final String TABLE_NAME = "clientes";

        public static final String COLUMN_CLIENTE_CODIGO = "codigo";
        public static final String COLUMN_CLIENTE_NOMBRE = "nombre";
        public static final String COLUMN_CLIENTE_DEPENDENCIA = "dependencia";
        public static final String COLUMN_CLIENTE_UBICACION = "ubicacion";
        public static final String COLUMN_CLIENTE_TELEFONO= "telefono";
        public static final String COLUMN_CLIENTE_CORREO = "correo";
    }

    public final class IncidenteEntry implements BaseColumns {

        public static final String TABLE_NAME = "incidentes";

        public static final String COLUMN_INCIDENTE_FOLIO = "folio";
        public static final String COLUMN_INCIDENTE_AREA_DEL_SERVICIO = "area_del_servicio";
        public static final String COLUMN_INCIDENTE_TIPO_DE_SERVICIO = "tipo_de_servicio";
        public static final String COLUMN_INCIDENTE_PRIORIDAD = "prioridad";
        public static final String COLUMN_INCIDENTE_DESCRIPCION = "descripcion";
        public static final String COLUMN_INCIDENTE_FECHA = "fecha";
        public static final String COLUMN_INCIDENTE_STATUS_DE_TERMINACION = "status_de_terminacion";
    }
}
