package checkhelzio.ccv.servicedeskcucsh.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by check on 22/04/2017.
 */

public final class DirectorioContract {

    public static final String CONTENT_AUTHORITY = "checkhelzio.ccv.servicedeskcucsh";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_DIRECTORIO = "directorio";

    private DirectorioContract(){}

    public static final class DirectorioEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DIRECTORIO);
        public static final String TABLE_NAME = "directorio";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_DEPENDENCIA = "dependencia";
        public static final String COLUMN_SUBDEPENDENCIA = "subdependencia";
        public static final String COLUMN_AREA = "area";
        public static final String COLUMN_TITULO = "titulo";
        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_APELLIDOS= "apellidos";
        public static final String COLUMN_PUESTO = "puesto";
        public static final String COLUMN_DIRECCION = "direccion";
        public static final String COLUMN_TELEFONO = "telefono";
        public static final String COLUMN_FAX = "fax";
        public static final String COLUMN_CORREO = "correo";

        public static final int DEPENDENCIA_RECTORIA = 0;
        public static final int DEPENDENCIA_DIVISION_DE_ESTUDIOS_HISTORICOS = 1;
        public static final int DEPENDENCIA_DIVISION_DE_ESTUDIOS_JURIDICOS = 2;
        public static final int DEPENDENCIA_DIVISION_DE_ESTUDIOS_DE_LA_CULTURA = 3;
        public static final int DEPENDENCIA_DIVISION_DE_ESTUDIOS_POLITICOS = 4;
        public static final int DEPENDENCIA_DIVISION_DE_ESTUDIOS_DEL_ESTADO = 5;
        public static final int DEPENDENCIA_CATEDRAS = 6;

        public static final int SUBDEPENDENCIA_RECTORIA = 1;
        public static final int SUBDEPENDENCIA_SECRETARIA_ACADEMICA = 2;
        public static final int SUBDEPENDENCIA_SECRETARIA_ADMINISTRATIVA = 3;
        public static final int SUBDEPENDENCIA_CONTRALORIA = 4;
    }

}
