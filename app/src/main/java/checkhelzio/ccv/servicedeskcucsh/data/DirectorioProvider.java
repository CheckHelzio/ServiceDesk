package checkhelzio.ccv.servicedeskcucsh.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by check on 18/05/2017.
 */

public class DirectorioProvider extends ContentProvider {

    /**
     * Database Helper Object
     */
    private DirectorioDbHelper mDbHelper;
    private static final int TODOS_CONTACTOS = 13;
    private static final int UN_CONTACTO = 14;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DirectorioContract.CONTENT_AUTHORITY, DirectorioContract.PATH_DIRECTORIO, TODOS_CONTACTOS);
        sUriMatcher.addURI(DirectorioContract.CONTENT_AUTHORITY, DirectorioContract.PATH_DIRECTORIO + "/#", UN_CONTACTO);
    }


    @Override
    public boolean onCreate() {
        mDbHelper = new DirectorioDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // OBTENEMOS LA BASE DE DATOS DE LECTURA
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // ESTE CURSOR TENDRA EL RESULTADO DE LA BUSQUEDA
        Cursor cursor;

        // COMPROBAMOS SI EL URI MATCHER CONTIENE LA URI PROPORCIONADA
        int match = sUriMatcher.match(uri);
        switch (match){
            case TODOS_CONTACTOS:
                cursor = db.query(DirectorioContract.DirectorioEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case UN_CONTACTO:
                selection = DirectorioContract.DirectorioEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(DirectorioContract.DirectorioEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("No se puede realizar la busqueda URI desconocido " + uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
