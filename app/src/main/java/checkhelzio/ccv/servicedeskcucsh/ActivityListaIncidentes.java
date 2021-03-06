package checkhelzio.ccv.servicedeskcucsh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ActivityListaIncidentes extends AppCompatActivity implements IncidenteAdapter.ListItemClickListener {

    public static Tecnico tecnico;
    public static ArrayList<Incidente> listaIncidenteConsulta;
    public static ArrayList<Incidente> listaIncidenteCompleta;
    private ArrayList<Incidente> listaIncidenteExtras;
    private ArrayList<Incidente> listaIncidenteEliminados;

    private static final int CONSTANTE_NUEVO_INCIDENTE = 33;
    private static final int CONSTANTE_ELIMINAR_INCIDENTE = 34;
    private RecyclerView mRecyclerView;
    private ImageView iv_empty;
    private IncidenteAdapter mIncidenteAdapter;
    private FloatingActionButton fab_mas;
    private String st_update = "";
    public static boolean isAdministrador = true;
    private SharedPreferences prefs;
    public static int numeroFolioSiguiente = 0;

    private int nivel_usuario = 0;
    private boolean wifiConnected;
    private Handler handler;
    private String st_lista_incidentes;
    private Animation fadeIn;
    private Animation fadeOut;
    private boolean isFirstCharge = true;
    private String st_lista_incidentes_original;
    private final String TAG = "ActivityListaIncidentes";
    private boolean isFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_incidentes);
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("SERVICE_DESK_CUCSH_PREFERENCES", Context.MODE_PRIVATE);
        String tecnico = sharedPreferences.getString("TECNICO", "FALSE");
        nivel_usuario = new Tecnico(tecnico).getNivel();
        iniciarObjetos();
        comprobarActualizaciones();
    }

    private void iniciarObjetos() {

        Log.v("ActivityListIncidentes", "INICIAR OBJETOS");
        handler = new Handler();
        prefs = getSharedPreferences("SERVICE_DESK_CUCSH_PREFERENCES", Context.MODE_PRIVATE);
        tecnico = new Tecnico(prefs.getString("TECNICO", ""));
        st_update = prefs.getString("UPDATE", "");
        st_lista_incidentes = prefs.getString("LISTA_DE_INCIDENTES", "");
        st_lista_incidentes_original = st_lista_incidentes;

        listaIncidenteCompleta = new ArrayList<>();
        listaIncidenteConsulta = new ArrayList<>();
        listaIncidenteExtras = new ArrayList<>();
        listaIncidenteEliminados = new ArrayList<>();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_main));
        getSupportActionBar().setTitle("");

        fadeIn = AnimationUtils.loadAnimation(ActivityListaIncidentes.this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(ActivityListaIncidentes.this, R.anim.fade_out);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ((View) iv_empty.getParent()).setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ((View) iv_empty.getParent()).setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        iv_empty = (ImageView) findViewById(R.id.iv_empty_lista);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_incidentes);
        fab_mas = (FloatingActionButton) findViewById(R.id.fab_mas);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mIncidenteAdapter = new IncidenteAdapter(getApplicationContext(), listaIncidenteConsulta, this);
        mRecyclerView.setAdapter(mIncidenteAdapter);

        if (nivel_usuario == 1) {
            fab_mas.setVisibility(View.GONE);
        } else {
            fab_mas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkNetworkConnection();
                }
            });
        }
    }

    private void comprobarActualizaciones() {
        if (isFinish) {
            return;
        }
        if (isThereNetworkConnection()) {
            new DescargarUD().execute("http://148.202.6.72/aplicacion/update_incidentes.txt");
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    comprobarActualizaciones();
                }
            }, 1000);
        }
    }

    private class LlenarLista extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listaIncidenteExtras.clear();
            listaIncidenteEliminados.clear();
            numeroFolioSiguiente = 0;
        }

        @Override
        protected Void doInBackground(String... aa12) {

            Log.v(TAG, "S: " + st_lista_incidentes);

            if (!st_lista_incidentes.isEmpty() && !st_lista_incidentes.contains("Redirection Error")) {
                for (String s : st_lista_incidentes.split("¦")) {

                    int numeroFolio = Integer.parseInt(s.split("::")[12].replace("F", ""));
                    if (numeroFolio >= numeroFolioSiguiente) {
                        numeroFolioSiguiente = numeroFolio + 1;
                    }

                    if (isFirstCharge) {
                        Log.v("ActivityListIncidentes", "PRIMERA CARGA");

                        Incidente i = new Incidente(
                                s.split("::")[0], // CODIGO DEL CLIENTE
                                s.split("::")[1], // NOMBRE DEL CLIENTE
                                s.split("::")[2], // DEPENDENCIA DEL CLIENTE
                                s.split("::")[3], // UBICACION DEL CLIENTE
                                s.split("::")[4], // TELEFONO DEL CLIENTE
                                s.split("::")[5], // CORREO DEL CLIENTE

                                Integer.parseInt(s.split("::")[6]), // AREA DEL SERVICIO
                                Integer.parseInt(s.split("::")[7]), // TIPO DEL SERVICIO
                                Integer.parseInt(s.split("::")[8]), // PRIPRIDAD DEL SERVICIO
                                s.split("::")[9], // DESCRIPCION DEL SERVICIO


                                Long.parseLong(s.split("::")[10]), // FECHA Y HORA DEL SERVICIO
                                Integer.parseInt(s.split("::")[11]), // STATUS DE TERMINACION DEL SERVICIO
                                Integer.parseInt(s.split("::")[12].replace("F", "")), // FOLIO DEL SERVICIO

                                s.split("::")[13], // CODIGO DEL TECNICO ASIGNADO
                                s.split("::")[14], // CODIGO DE QUIEN LEVANTO EL REPORTE
                                Integer.parseInt(s.split("::")[15]), // STATUS DE MODIFICACION DEL SERVICIO
                                s, // TAG DEL REPORTE
                                s.split("::")[16]
                        );

                        listaIncidenteCompleta.add(i);
                        if (nivel_usuario != 1 || tecnico.getCodigoDelTecnico().equals(s.split("::")[13])) {
                            listaIncidenteExtras.add(i);
                        }
                    } else {
                        Log.v("ActivityListIncidentes", "SEGUNDA CARGA");

                        // MODIFICAMOS PRIMERO LA LISTA COMPLETA
                        // SI LA HORA DE MODIFICACION DEL INCIDENTE NO ESTA EN LA LISTA ANTERIOR QUIERE DECIR QUE ES UNO NUEVO O UNO MODIFICADO
                        if (!st_lista_incidentes_original.contains(s.split("::")[10])) {

                            // CREAMOS EL NUEVO INCIDENTE
                            Incidente i = new Incidente(
                                    s.split("::")[0], // CODIGO DEL CLIENTE
                                    s.split("::")[1], // NOMBRE DEL CLIENTE
                                    s.split("::")[2], // DEPENDENCIA DEL CLIENTE
                                    s.split("::")[3], // UBICACION DEL CLIENTE
                                    s.split("::")[4], // TELEFONO DEL CLIENTE
                                    s.split("::")[5], // CORREO DEL CLIENTE

                                    Integer.parseInt(s.split("::")[6]), // AREA DEL SERVICIO
                                    Integer.parseInt(s.split("::")[7]), // TIPO DEL SERVICIO
                                    Integer.parseInt(s.split("::")[8]), // PRIPRIDAD DEL SERVICIO
                                    s.split("::")[9], // DESCRIPCION DEL SERVICIO


                                    Long.parseLong(s.split("::")[10]), // FECHA Y HORA DEL SERVICIO
                                    Integer.parseInt(s.split("::")[11]), // STATUS DE TERMINACION DEL SERVICIO
                                    Integer.parseInt(s.split("::")[12].replace("F", "")), // FOLIO DEL SERVICIO

                                    s.split("::")[13], // CODIGO DEL TECNICO ASIGNADO
                                    s.split("::")[14], // CODIGO DE QUIEN LEVANTO EL REPORTE
                                    Integer.parseInt(s.split("::")[15]), // STATUS DE MODIFICACION DEL SERVICIO
                                    s, // TAG DEL REPORTE
                                    s.split("::")[16]
                            );

                            // SI EL FOLIO DEL INCIDENTE NO APARECE EN LA LISTA VIEJA QUIERE DECIR QUE ES UN NUEVO INCIDENTE Y LO AGREGAMOS DIRECTAMENTE
                            if (!st_lista_incidentes_original.contains("::" + s.split("::")[12] + "::")) {
                                Log.v("ActivityListIncidentes", "FOLIO NUEVO");
                                listaIncidenteCompleta.add(0, i);

                            } else {
                                // SI EL NUMERO DE FOLIO YA EXITE LO BUSCAMOS PARA EDITARLO.
                                Log.v("ActivityListIncidentes", "FOLIO YA EXISTE");
                                int x = 0;
                                for (Incidente incidente : listaIncidenteCompleta) {
                                    if (incidente.aTag().contains("::" + s.split("::")[12] + "::")) {
                                        // LO ELIMINAMOS DE SU LUGAR Y LO MOVEMOS AL INICIO
                                        listaIncidenteCompleta.remove(x);
                                        listaIncidenteCompleta.add(0, i);
                                        break;
                                    }
                                    x++;
                                }
                            }
                        }

                        // MODIFICAMOS LA LISTA INDIVIDUAL
                        // COMPROBAMOS SI LA CUENTA ES ADMINISTRADOR O SI EL INCIDENTE LE PERTENECE AL TECNICO
                        if (nivel_usuario != 1 || tecnico.getCodigoDelTecnico().equals(s.split("::")[13])) {

                            // SI LA HORA DE MODIFICACION DEL INCIDENTE NO ESTA EN LA LISTA ANTERIOR QUIERE DECIR QUE ES UNO NUEVO O UNO MODIFICADO
                            if (!st_lista_incidentes_original.contains(s.split("::")[10])) {

                                Log.v("ActivityListIncidentes", "INCIDENTE NUEVO O MODIFICADO");
                                // CREAMOS EL NUEVO INCIDENTE
                                Incidente i = new Incidente(
                                        s.split("::")[0], // CODIGO DEL CLIENTE
                                        s.split("::")[1], // NOMBRE DEL CLIENTE
                                        s.split("::")[2], // DEPENDENCIA DEL CLIENTE
                                        s.split("::")[3], // UBICACION DEL CLIENTE
                                        s.split("::")[4], // TELEFONO DEL CLIENTE
                                        s.split("::")[5], // CORREO DEL CLIENTE

                                        Integer.parseInt(s.split("::")[6]), // AREA DEL SERVICIO
                                        Integer.parseInt(s.split("::")[7]), // TIPO DEL SERVICIO
                                        Integer.parseInt(s.split("::")[8]), // PRIPRIDAD DEL SERVICIO
                                        s.split("::")[9], // DESCRIPCION DEL SERVICIO


                                        Long.parseLong(s.split("::")[10]), // FECHA Y HORA DEL SERVICIO
                                        Integer.parseInt(s.split("::")[11]), // STATUS DE TERMINACION DEL SERVICIO
                                        Integer.parseInt(s.split("::")[12].replace("F", "")), // FOLIO DEL SERVICIO

                                        s.split("::")[13], // CODIGO DEL TECNICO ASIGNADO
                                        s.split("::")[14], // CODIGO DE QUIEN LEVANTO EL REPORTE
                                        Integer.parseInt(s.split("::")[15]), // STATUS DE MODIFICACION DEL SERVICIO
                                        s, // TAG DEL REPORTE
                                        s.split("::")[16]
                                );

                                // SI EL FOLIO DEL INCIDENTE NO APARECE EN LA LISTA VIEJA QUIERE DECIR QUE ES UN NUEVO INCIDENTE Y LO AGREGAMOS DIRECTAMENTE
                                if (!st_lista_incidentes_original.contains("::" + s.split("::")[12] + "::")) {
                                    Log.v("ActivityListIncidentes", "FOLIO NUEVO");
                                    listaIncidenteExtras.add(0, i);
                                } else {
                                    Log.v("ActivityListIncidentes", "ESTE FOLIO YA ESXISTE");
                                    Log.v("ActivityListIncidentes", i.aTag());
                                    //EDITAR EVENTO
                                    listaIncidenteExtras.add(0, i);
                                }
                            }
                        }
                    }
                }


                for (Incidente i : listaIncidenteCompleta) {
                    if (!st_lista_incidentes.contains("" + i.getFechaYHoraDelReporte())) {
                        listaIncidenteEliminados.add(i);
                    }
                }
                listaIncidenteCompleta.removeAll(listaIncidenteEliminados);
                listaIncidenteEliminados.clear();

                for (Incidente i : listaIncidenteConsulta) {
                    if (!st_lista_incidentes.contains("" + i.getFechaYHoraDelReporte())) {

                        Log.v("ST LISTA", "ESTE EVENTO YA NO EXISTE: " + i.aTag());
                        listaIncidenteEliminados.add(i);
                    }
                }

                if (isFirstCharge) {
                    isFirstCharge = false;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.v("ActivityListIncidentes", "LLENAR LISTA - POST EXECUTE");
            st_lista_incidentes_original = st_lista_incidentes;

            for (Incidente i : listaIncidenteEliminados) {
                buscarItemParaEliminar(i.getFolioDelReporte());
            }

            Log.v("LISTA", "TAMAÑO: " + listaIncidenteExtras.size());

            for (Incidente incidenteNuevo : listaIncidenteExtras) {
                if (listaIncidenteConsulta.size() > 0) {
                    int x = 0;
                    for (Incidente incidenteViejo : listaIncidenteConsulta) {
                        if (incidenteNuevo.getFechaYHoraDelReporte() > incidenteViejo.getFechaYHoraDelReporte()) {
                            listaIncidenteConsulta.add(x, incidenteNuevo);
                            agregarIncidenteaLista(x);
                            break;
                        } else {
                            if (x == listaIncidenteConsulta.size() - 1) {
                                listaIncidenteConsulta.add(incidenteNuevo);
                                agregarIncidenteaLista(listaIncidenteConsulta.size() - 1);
                                break;
                            }
                        }
                        x++;
                    }
                } else {
                    listaIncidenteConsulta.add(0, incidenteNuevo);
                    agregarIncidenteaLista(0);
                }

            }
            actualizarEmptySate();

            for (Incidente i : listaIncidenteConsulta) {
                Log.v("ST LISTA", "C: " + i.aTag());
            }
        }
    }

    private class DescargarUD extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... params) {
            try {
                return loadFromNetwork(params[0].toString());
            } catch (IOException e) {
                return "Error de conexión";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.equals("Error de conexión")) {

                if (s.contains("</form>")) {
                    // MUCHAS VECES LA BASE DE DATOS ES DESCARGADA CON CODIGO HTML QUE NO NECESITOS, POR ESO AQUI LO REEMPLAZAMOS
                    s = s.split("</form>")[1].trim();
                }

                if (!s.trim().equals(st_update.trim())) {
                    st_update = s.trim();
                    prefs.edit().putString("UPDATE", st_update).apply();
                    Log.v("DESCARGAR BD", "DESCARGARBD");
                    // SI LA BASE DE DATOS QUE DESCARGARMOS NO ES IGUAL A LA QUE YA TENEMOS LA SOBREESCRIBIMOS Y DESPUES LLEMANOS NUEVAMENTE LA LISTA DE EVENTOS
                    new DescargarBD().execute("http://148.202.6.72/aplicacion/incidentes.txt", ActivityListaIncidentes.this);
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            comprobarActualizaciones();
                        }
                    }, 1000);
                }
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        comprobarActualizaciones();
                    }
                }, 1000);
            }
        }
    }

    private class DescargarBD extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... objects) {
            try {
                return loadFromNetwork(objects[0].toString());
            } catch (IOException e) {
                return "Error de conexión";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.equals("Error de conexión")) {
                if (s.contains("</form>")) {
                    // MUCHAS VECES LA BASE DE DATOS ES DESCARGADA CON CODIGO HTML QUE NO NECESITOS, POR ESO AQUI LO REEMPLAZAMOS
                    s = s.split("</form>")[1].trim();
                }

                st_lista_incidentes = s;
                prefs.edit().putString("LISTA_DE_INCIDENTES", st_lista_incidentes).apply();
                new LlenarLista().execute();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        comprobarActualizaciones();
                    }
                }, 1000);
            } else {
                st_update = "";
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        comprobarActualizaciones();
                    }
                }, 1000);
            }
        }
    }

    public void buscarItemParaEliminar(Integer folio) {
        mIncidenteAdapter.eliminar(folio);
    }

    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream;
        String str = "";
        try {
            stream = downloadUrl(urlString);
            str = readIt(stream);
            assert stream != null;
            stream.close();
        } catch (Exception ignored) {
        }
        return str;
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(0);
        conn.setConnectTimeout(0);
        return conn.getInputStream();
    }

    private String readIt(InputStream stream) throws IOException {
        String a = "";
        String linea;
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        while ((linea = br.readLine()) != null) {
            a += linea;
        }
        return a;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isFinish = false;
        Log.v("ActivityListIncidentes", "ON START");
        new LlenarLista().execute();
        comprobarActualizaciones();
    }

    private void checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            boolean wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            boolean mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifiConnected) {
                Intent i = new Intent(ActivityListaIncidentes.this, ActivityFormulario.class);
                startActivityForResult(i, CONSTANTE_NUEVO_INCIDENTE);
            } else if (mobileConnected) {
                Snackbar.make(findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet.", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet.", Snackbar.LENGTH_LONG).show();
        }
    }

    public void agregarIncidenteaLista(int x) {
        if (x == 0){
            mRecyclerView.scrollToPosition(0);
        }
        mIncidenteAdapter.notifyItemInserted(x);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public void onListItemClick(int clickedItemIndex, View view) {
        Intent intent = new Intent(ActivityListaIncidentes.this, ActivityDetalleIncidente.class);
        intent.putExtra("INCIDENTE", listaIncidenteConsulta.get(clickedItemIndex));
        intent.putExtra("INDEX", clickedItemIndex);
        startActivityForResult(intent, CONSTANTE_ELIMINAR_INCIDENTE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isFinish = true;
    }

    public void actualizarEmptySate() {
        if (listaIncidenteConsulta.size() == 0) {
            if (nivel_usuario > 1){
                fab_mas.show();
            }
            if (((View) iv_empty.getParent()).getVisibility() == View.GONE) {
                ((View) iv_empty.getParent()).startAnimation(fadeIn);
            }
        } else {
            if (nivel_usuario == 1){
                fab_mas.hide();
            }
            if (((View) iv_empty.getParent()).getVisibility() == View.VISIBLE) {
                ((View) iv_empty.getParent()).startAnimation(fadeOut);
            }
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("ActivityListIncidentes", "a - TAMAÑO LISTA: " + listaIncidente.size());

        if (requestCode == CONSTANTE_NUEVO_INCIDENTE && resultCode == RESULT_OK) {
            Log.v("LISTA INCIDENTES", "AGREGAR");
            agregarIncidenteaLista(listaIncidente.size() - 1);
            Log.v("LISTA INCIDENTES", "TAMAÑO LISTA: " + listaIncidente.size());
        } else if (requestCode == CONSTANTE_ELIMINAR_INCIDENTE && resultCode == RESULT_OK) {
            boolean editando = data.getBooleanExtra("EDITANDO", false);

            if (!editando) {
                if (listaIncidente.size() == 0) {
                    eliminarIncidenteaLista(data.getIntExtra("INDEX", 0));
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            eliminarIncidenteaLista(data.getIntExtra("INDEX", 0));
                        }
                    }, 500);
                }
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        editarIncidenteaLista(data.getIntExtra("INDEX", 0));
                    }
                }, 500);
            }
        }
    }*/

    private boolean isThereNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            if (wifiConnected) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    private void editarIncidenteaLista(int index) {
        mIncidenteAdapter.notifyItemChanged(index);
    }

    /*private void eliminarIncidenteaLista(int index) {

        Log.v("ActivityListIncidentes", "ELIMINAR INCIDENTE DE LA LISTA - TAMAÑO LISTA: " + listaIncidente.size());
        actualizarEmptySate();
        mIncidenteAdapter.notifyItemRemoved(index);
        mIncidenteAdapter.notifyItemRangeChanged(index, listaIncidente.size());
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            /*case R.id.menu_agenda:
                intent = new Intent(ActivityListaIncidentes.this, ActivityDirectorio.class);
                startActivity(intent);
                break;*/
            case R.id.menu_cerrar_sesion:
                intent = new Intent(ActivityListaIncidentes.this, Loggin.class);
                startActivity(intent);
                finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }


}
