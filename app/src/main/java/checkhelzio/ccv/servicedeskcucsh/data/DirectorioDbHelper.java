package checkhelzio.ccv.servicedeskcucsh.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import checkhelzio.ccv.servicedeskcucsh.data.DirectorioContract.DirectorioEntry;

/**
 * Created by check on 15/05/2017.
 */

public class DirectorioDbHelper extends SQLiteOpenHelper {
    public static final String DATABANSE_NAME = "directorio.db";
    public static final int DATABANSE_VERSION = 1;
    private final String SQL_CREATE_ENTRIES = "CREATE TABLE " + DirectorioEntry.TABLE_NAME + " (" +
            DirectorioEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DirectorioEntry.COLUMN_DEPENDENCIA + " INTEGER NOT NULL, " +
            DirectorioEntry.COLUMN_SUBDEPENDENCIA + " INTEGER NOT NULL, " +
            DirectorioEntry.COLUMN_AREA + " TEXT NOT NULL, " +
            DirectorioEntry.COLUMN_TITULO + " TEXT, " +
            DirectorioEntry.COLUMN_NOMBRE + " TEXT NOT NULL, " +
            DirectorioEntry.COLUMN_APELLIDOS + " TEXT NOT NULL, " +
            DirectorioEntry.COLUMN_PUESTO + " TEXT NOT NULL, " +
            DirectorioEntry.COLUMN_DIRECCION + " TEXT NOT NULL, " +
            DirectorioEntry.COLUMN_TELEFONO + " TEXT NOT NULL, " +
            DirectorioEntry.COLUMN_FAX + " TEXT, " +
            DirectorioEntry.COLUMN_CORREO + " TEXT"
            + ");";

    public DirectorioDbHelper(Context context) {
        super(context, DATABANSE_NAME, null, DATABANSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

        ContentValues values = new ContentValues();

        values.put(DirectorioEntry.COLUMN_DEPENDENCIA, DirectorioEntry.DEPENDENCIA_RECTORIA);
        values.put(DirectorioEntry.COLUMN_SUBDEPENDENCIA, DirectorioEntry.SUBDEPENDENCIA_RECTORIA);
        values.put(DirectorioEntry.COLUMN_AREA, "Rectoría");
        values.put(DirectorioEntry.COLUMN_TITULO, "Doctor");
        values.put(DirectorioEntry.COLUMN_NOMBRE, "Héctor Raúl");
        values.put(DirectorioEntry.COLUMN_APELLIDOS, "Solís Gadea");
        values.put(DirectorioEntry.COLUMN_PUESTO, "Rector");
        values.put(DirectorioEntry.COLUMN_DIRECCION, "Guanajuato No. 1045. C.P. 44260. Col. Alcalde Barranquitas");
        values.put(DirectorioEntry.COLUMN_TELEFONO, "(01-33) 3819-3320, 3819-3300 ext. 23320 y 23437");
        values.put(DirectorioEntry.COLUMN_FAX, "3853-9092");
        values.put(DirectorioEntry.COLUMN_CORREO, "solish@redudg.udg.mx");
        db.insert(DirectorioEntry.TABLE_NAME, null, values);

        values.put(DirectorioEntry.COLUMN_DEPENDENCIA, DirectorioEntry.DEPENDENCIA_RECTORIA);
        values.put(DirectorioEntry.COLUMN_SUBDEPENDENCIA, DirectorioEntry.SUBDEPENDENCIA_SECRETARIA_ACADEMICA);
        values.put(DirectorioEntry.COLUMN_AREA, "Secretaría Académica");
        values.put(DirectorioEntry.COLUMN_TITULO, "Doctora");
        values.put(DirectorioEntry.COLUMN_NOMBRE, "María Guadalupe");
        values.put(DirectorioEntry.COLUMN_APELLIDOS, "Moreno González");
        values.put(DirectorioEntry.COLUMN_PUESTO, "Secretaria Académica");
        values.put(DirectorioEntry.COLUMN_DIRECCION, "Guanajuato No. 1045. C.P. 44260. Col. Alcalde Barranquitas");
        values.put(DirectorioEntry.COLUMN_TELEFONO, "(01-33) 3819-3300 ext. 23335, 23416");
        values.put(DirectorioEntry.COLUMN_FAX, "3853-9092");
        values.put(DirectorioEntry.COLUMN_CORREO, "guadalupe.moreno@csh.udg.mx");
        db.insert(DirectorioEntry.TABLE_NAME, null, values);

        values.put(DirectorioEntry.COLUMN_DEPENDENCIA, DirectorioEntry.DEPENDENCIA_RECTORIA);
        values.put(DirectorioEntry.COLUMN_SUBDEPENDENCIA, DirectorioEntry.SUBDEPENDENCIA_SECRETARIA_ACADEMICA);
        values.put(DirectorioEntry.COLUMN_AREA, "Coordinación de Docencia");
        values.put(DirectorioEntry.COLUMN_TITULO, "Maestra");
        values.put(DirectorioEntry.COLUMN_NOMBRE, "Ma. Raquel");
        values.put(DirectorioEntry.COLUMN_APELLIDOS, "Carvajal Silva");
        values.put(DirectorioEntry.COLUMN_PUESTO, "Coordinadora");
        values.put(DirectorioEntry.COLUMN_DIRECCION, "Guanajuato No. 1045. C.P. 44260. Col. Alcalde Barranquitas");
        values.put(DirectorioEntry.COLUMN_TELEFONO, "(01-33) 3819-3341, Ext. 23436, 23401");
        values.put(DirectorioEntry.COLUMN_FAX, "");
        values.put(DirectorioEntry.COLUMN_CORREO, "raquel.carvajal@csh.udg.mx");
        db.insert(DirectorioEntry.TABLE_NAME, null, values);

        values.put(DirectorioEntry.COLUMN_DEPENDENCIA, DirectorioEntry.DEPENDENCIA_RECTORIA);
        values.put(DirectorioEntry.COLUMN_SUBDEPENDENCIA, DirectorioEntry.SUBDEPENDENCIA_SECRETARIA_ACADEMICA);
        values.put(DirectorioEntry.COLUMN_AREA, "Unidad de seguimiento de los procesos de calidad de los programas educativos");
        values.put(DirectorioEntry.COLUMN_TITULO, "");
        values.put(DirectorioEntry.COLUMN_NOMBRE, "No");
        values.put(DirectorioEntry.COLUMN_APELLIDOS, "asignado");
        values.put(DirectorioEntry.COLUMN_PUESTO, "Jefe");
        values.put(DirectorioEntry.COLUMN_DIRECCION, "Guanajuato No. 1045. C.P. 44260. Col. Alcalde Barranquitas");
        values.put(DirectorioEntry.COLUMN_TELEFONO, "(01-33) 3819-3300 Ext. 23401");
        values.put(DirectorioEntry.COLUMN_FAX, "");
        values.put(DirectorioEntry.COLUMN_CORREO, "");
        db.insert(DirectorioEntry.TABLE_NAME, null, values);

        values.put(DirectorioEntry.COLUMN_DEPENDENCIA, DirectorioEntry.DEPENDENCIA_RECTORIA);
        values.put(DirectorioEntry.COLUMN_SUBDEPENDENCIA, DirectorioEntry.SUBDEPENDENCIA_SECRETARIA_ACADEMICA);
        values.put(DirectorioEntry.COLUMN_AREA, "Coordinación de Planeación");
        values.put(DirectorioEntry.COLUMN_TITULO, "Licenciada");
        values.put(DirectorioEntry.COLUMN_NOMBRE, "María del Rosario");
        values.put(DirectorioEntry.COLUMN_APELLIDOS, "Ortiz Hernández");
        values.put(DirectorioEntry.COLUMN_PUESTO, "Coordinadora");
        values.put(DirectorioEntry.COLUMN_DIRECCION, "Guanajuato No. 1045. C.P. 44260. Col. Alcalde Barranquitas");
        values.put(DirectorioEntry.COLUMN_TELEFONO, "(01-33) 3819-3300 ext. 23485, 23433");
        values.put(DirectorioEntry.COLUMN_FAX, "");
        values.put(DirectorioEntry.COLUMN_CORREO, "rosario.ortiz@csh.udg.mx");
        db.insert(DirectorioEntry.TABLE_NAME, null, values);

        values.put(DirectorioEntry.COLUMN_DEPENDENCIA, DirectorioEntry.DEPENDENCIA_RECTORIA);
        values.put(DirectorioEntry.COLUMN_SUBDEPENDENCIA, DirectorioEntry.SUBDEPENDENCIA_SECRETARIA_ACADEMICA);
        values.put(DirectorioEntry.COLUMN_AREA, "Unidad de Planeación");
        values.put(DirectorioEntry.COLUMN_TITULO, "Licenciada");
        values.put(DirectorioEntry.COLUMN_NOMBRE, "Josefina");
        values.put(DirectorioEntry.COLUMN_APELLIDOS, "Cervantes Sánchez");
        values.put(DirectorioEntry.COLUMN_PUESTO, "Jefa");
        values.put(DirectorioEntry.COLUMN_DIRECCION, "Guanajuato No. 1045. C.P. 44260. Col. Alcalde Barranquitas");
        values.put(DirectorioEntry.COLUMN_TELEFONO, "(01-33) 3819-3300 ext. 23485");
        values.put(DirectorioEntry.COLUMN_FAX, "");
        values.put(DirectorioEntry.COLUMN_CORREO, "josefina.cervantes@csh.udg.mx");
        db.insert(DirectorioEntry.TABLE_NAME, null, values);

        values.put(DirectorioEntry.COLUMN_DEPENDENCIA, DirectorioEntry.DEPENDENCIA_RECTORIA);
        values.put(DirectorioEntry.COLUMN_SUBDEPENDENCIA, DirectorioEntry.SUBDEPENDENCIA_SECRETARIA_ACADEMICA);
        values.put(DirectorioEntry.COLUMN_AREA, "Coordinación de Tecnologías para el Aprendizaje");
        values.put(DirectorioEntry.COLUMN_TITULO, "Ingeniero");
        values.put(DirectorioEntry.COLUMN_NOMBRE, "Héctor");
        values.put(DirectorioEntry.COLUMN_APELLIDOS, "Aceves Shimizu y López");
        values.put(DirectorioEntry.COLUMN_PUESTO, "Coordinador");
        values.put(DirectorioEntry.COLUMN_DIRECCION, "Av. Alcalde, esq. Guanajuato. Biblioteca \"Manuel Rodriguez Lapuente\" planta alta. C.P. 44260 Guadalajara, Jalisco, México. y Parres Arias #150, esquina periférico. Campus Los Belenes, edificio B, segundo piso.");
        values.put(DirectorioEntry.COLUMN_TELEFONO, "Campus La Normal (01-33) 3819-3303 ext 23382 y Campus Belenes 3819-3306 Atención a usuario: 23432");
        values.put(DirectorioEntry.COLUMN_FAX, "");
        values.put(DirectorioEntry.COLUMN_CORREO, "hector.shimizu@redudg.udg.mx");
        db.insert(DirectorioEntry.TABLE_NAME, null, values);

        values.put(DirectorioEntry.COLUMN_DEPENDENCIA, DirectorioEntry.DEPENDENCIA_RECTORIA);
        values.put(DirectorioEntry.COLUMN_SUBDEPENDENCIA, DirectorioEntry.SUBDEPENDENCIA_SECRETARIA_ACADEMICA);
        values.put(DirectorioEntry.COLUMN_AREA, "Unidad de Multimedia Instruccional");
        values.put(DirectorioEntry.COLUMN_TITULO, "Licenciada");
        values.put(DirectorioEntry.COLUMN_NOMBRE, "Idania");
        values.put(DirectorioEntry.COLUMN_APELLIDOS, "Gómez Cosio");
        values.put(DirectorioEntry.COLUMN_PUESTO, "Jefa");
        values.put(DirectorioEntry.COLUMN_DIRECCION, "Av. Alcalde, esq. Guanajuato. Biblioteca \"Manuel Rodriguez Lapuente\" planta alta. C.P. 44260 Guadalajara, Jalisco, México.");
        values.put(DirectorioEntry.COLUMN_TELEFONO, "(01-33) 3819-3330 3819-3300 exts. 23330, 23460");
        values.put(DirectorioEntry.COLUMN_FAX, "");
        values.put(DirectorioEntry.COLUMN_CORREO, "idania.gomez@csh.udg.mx");
        db.insert(DirectorioEntry.TABLE_NAME, null, values);

        values.put(DirectorioEntry.COLUMN_DEPENDENCIA, DirectorioEntry.DEPENDENCIA_RECTORIA);
        values.put(DirectorioEntry.COLUMN_SUBDEPENDENCIA, DirectorioEntry.SUBDEPENDENCIA_SECRETARIA_ACADEMICA);
        values.put(DirectorioEntry.COLUMN_AREA, "Unidad de Cómputo y Telecomunicaciones para el Aprendizaje");
        values.put(DirectorioEntry.COLUMN_TITULO, "Licenciado");
        values.put(DirectorioEntry.COLUMN_NOMBRE, "Eduardo");
        values.put(DirectorioEntry.COLUMN_APELLIDOS, "Solano Guzmán");
        values.put(DirectorioEntry.COLUMN_PUESTO, "Jefe");
        values.put(DirectorioEntry.COLUMN_DIRECCION, "Av. Alcalde, esq. Guanajuato. Biblioteca \"Manuel Rodriguez Lapuente\" planta alta. C.P. 44260 Guadalajara, Jalisco, México.");
        values.put(DirectorioEntry.COLUMN_TELEFONO, "(01-33) 3819-3300 Ext. 23303, 23397 Atención a usuario: 23432");
        values.put(DirectorioEntry.COLUMN_FAX, "");
        values.put(DirectorioEntry.COLUMN_CORREO, "eduardo.solano@redudg.udg.mx");
        db.insert(DirectorioEntry.TABLE_NAME, null, values);

        values.put(DirectorioEntry.COLUMN_DEPENDENCIA, DirectorioEntry.DEPENDENCIA_RECTORIA);
        values.put(DirectorioEntry.COLUMN_SUBDEPENDENCIA, DirectorioEntry.SUBDEPENDENCIA_SECRETARIA_ACADEMICA);
        values.put(DirectorioEntry.COLUMN_AREA, "Área de Seguimiento y Evaluación");
        values.put(DirectorioEntry.COLUMN_TITULO, "");
        values.put(DirectorioEntry.COLUMN_NOMBRE, "No");
        values.put(DirectorioEntry.COLUMN_APELLIDOS, "asignado");
        values.put(DirectorioEntry.COLUMN_PUESTO, "Encargado");
        values.put(DirectorioEntry.COLUMN_DIRECCION, "Guanajuato No. 1045. C.P. 44260. Col. Alcalde Barranquitas");
        values.put(DirectorioEntry.COLUMN_TELEFONO, "(01-33) 3819-3300 ext. 23485");
        values.put(DirectorioEntry.COLUMN_FAX, "");
        values.put(DirectorioEntry.COLUMN_CORREO, "");
        db.insert(DirectorioEntry.TABLE_NAME, null, values);

        values.put(DirectorioEntry.COLUMN_DEPENDENCIA, DirectorioEntry.DEPENDENCIA_RECTORIA);
        values.put(DirectorioEntry.COLUMN_SUBDEPENDENCIA, DirectorioEntry.SUBDEPENDENCIA_SECRETARIA_ADMINISTRATIVA);
        values.put(DirectorioEntry.COLUMN_AREA, "Secretaría Administrativa");
        values.put(DirectorioEntry.COLUMN_TITULO, "Mestra");
        values.put(DirectorioEntry.COLUMN_NOMBRE, "Karla Alejandrina");
        values.put(DirectorioEntry.COLUMN_APELLIDOS, "Planter Pérez");
        values.put(DirectorioEntry.COLUMN_PUESTO, "Secretaria Administrativa");
        values.put(DirectorioEntry.COLUMN_DIRECCION, "Guanajuato No. 1045. C.P. 44260. Col. Alcalde Barranquitas");
        values.put(DirectorioEntry.COLUMN_TELEFONO, "(01-33) 3819-3300 Ext. 23334, 23584 y 23359");
        values.put(DirectorioEntry.COLUMN_FAX, "3853-9092");
        values.put(DirectorioEntry.COLUMN_CORREO, "karla.planter@redudg.udg.mx");
        db.insert(DirectorioEntry.TABLE_NAME, null, values);

        values.put(DirectorioEntry.COLUMN_DEPENDENCIA, DirectorioEntry.DEPENDENCIA_RECTORIA);
        values.put(DirectorioEntry.COLUMN_SUBDEPENDENCIA, DirectorioEntry.SUBDEPENDENCIA_CONTRALORIA);
        values.put(DirectorioEntry.COLUMN_AREA, "Contraloría");
        values.put(DirectorioEntry.COLUMN_TITULO, "Mestro");
        values.put(DirectorioEntry.COLUMN_NOMBRE, "Luis Guillermo");
        values.put(DirectorioEntry.COLUMN_APELLIDOS, "Valdivia Meza");
        values.put(DirectorioEntry.COLUMN_PUESTO, "Contralor");
        values.put(DirectorioEntry.COLUMN_DIRECCION, "Guanajuato No. 1045. C.P. 44260. Col. Alcalde Barranquitas");
        values.put(DirectorioEntry.COLUMN_TELEFONO, "(01-33) 3819-3300, 3819-3312");
        values.put(DirectorioEntry.COLUMN_FAX, "");
        values.put(DirectorioEntry.COLUMN_CORREO, "guillermo.valdivia@redudg.udg.mx");
        db.insert(DirectorioEntry.TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
