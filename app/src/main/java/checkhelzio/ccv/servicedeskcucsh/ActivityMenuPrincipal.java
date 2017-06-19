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
import android.support.v7.widget.Toolbar;
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


public class ActivityMenuPrincipal extends AppCompatActivity {

    private ArrayList<Incidente> listaIncidenteExtras;
    private ArrayList<Incidente> listaIncidenteEliminados;
    public static ArrayList<Incidente> listaIncidenteConsulta;
    public static ArrayList<Incidente> listaIncidenteCompleta;
    private Animation fadeIn, fadeOut;
    private FloatingActionButton fab_mas;
    private FragmentListaIncidentes fragmentListaIncidentes;
    private Handler handler;
    private int nivel_usuario = 0;
    private boolean isFirstCharge = true;
    public static int numeroFolioSiguiente;
    public static int listaIndex = -1;
    private final int CONSTANTE_NUEVO_INCIDENTE = 33;
    private ImageView iv_empty;
    private SharedPreferences prefs;
    private String st_update;
    private String st_lista_incidentes;
    private String st_lista_incidentes_original;
    public static Tecnico tecnico;
    public boolean isFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        iniciarObjetos();
    }

    private void iniciarObjetos() {

        // SET TOOLBAR
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_main));
        getSupportActionBar().setTitle("");

        // DATOS CARGADOS DESDE PREFERENCIAS
        prefs = getSharedPreferences("SERVICE_DESK_CUCSH_PREFERENCES", Context.MODE_PRIVATE);
        tecnico = new Tecnico(prefs.getString("TECNICO", ""));
        st_update = prefs.getString("UPDATE", "");
        st_lista_incidentes = prefs.getString("LISTA_DE_INCIDENTES", "");
        st_lista_incidentes_original = st_lista_incidentes;

        // INICIAR OBJETOS
        handler = new Handler();
        listaIncidenteConsulta = new ArrayList<>();
        listaIncidenteCompleta = new ArrayList<>();
        listaIncidenteEliminados = new ArrayList<>();
        listaIncidenteExtras = new ArrayList<>();
        iv_empty = (ImageView) findViewById(R.id.iv_empty_lista);
        fadeIn = AnimationUtils.loadAnimation(ActivityMenuPrincipal.this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(ActivityMenuPrincipal.this, R.anim.fade_out);
        fab_mas = (FloatingActionButton) findViewById(R.id.fab_mas_p);

        nivel_usuario = tecnico.getNivel();
        fragmentListaIncidentes = (FragmentListaIncidentes) getSupportFragmentManager().findFragmentById(R.id.listFragment);

        // SET LISTENNERS
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

        if (nivel_usuario == 1) {
            fab_mas.setVisibility(View.GONE);
        }

        fab_mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetworkConnection();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        isFinish = false;
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
                Intent i = new Intent(ActivityMenuPrincipal.this, ActivityFormularioTablet.class);
                startActivityForResult(i, CONSTANTE_NUEVO_INCIDENTE);
            } else if (mobileConnected) {
                Snackbar.make(findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet.", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet.", Snackbar.LENGTH_LONG).show();
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

    @Override
    protected void onStop() {
        super.onStop();
        isFinish = true;
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
                    // SI LA BASE DE DATOS QUE DESCARGARMOS NO ES IGUAL A LA QUE YA TENEMOS LA SOBREESCRIBIMOS Y DESPUES LLEMANOS NUEVAMENTE LA LISTA DE EVENTOS
                    new DescargarBD().execute("http://148.202.6.72/aplicacion/incidentes.txt");
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

            if (!st_lista_incidentes.isEmpty() && !st_lista_incidentes.contains("Redirection Error")) {
                for (String s : st_lista_incidentes.split("¦")) {

                    int numeroFolio = Integer.parseInt(s.split("::")[12].replace("F", ""));
                    if (numeroFolio >= numeroFolioSiguiente) {
                        numeroFolioSiguiente = numeroFolio + 1;
                    }

                    if (isFirstCharge) {

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
                                listaIncidenteCompleta.add(0, i);

                            } else {
                                // SI EL NUMERO DE FOLIO YA EXITE LO BUSCAMOS PARA EDITARLO.
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
                                    listaIncidenteExtras.add(0, i);
                                } else {
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

            st_lista_incidentes_original = st_lista_incidentes;

            for (Incidente i : listaIncidenteEliminados) {
                fragmentListaIncidentes.buscarItemParaEliminar(i.getFolioDelReporte());
            }


            for (Incidente incidenteNuevo : listaIncidenteExtras) {
                if (listaIncidenteConsulta.size() > 0) {
                    int x = 0;
                    for (Incidente incidenteViejo : listaIncidenteConsulta) {
                        if (incidenteNuevo.getFechaYHoraDelReporte() > incidenteViejo.getFechaYHoraDelReporte()) {
                            listaIncidenteConsulta.add(x, incidenteNuevo);
                            fragmentListaIncidentes.agregarIncidenteaLista(x);
                            break;
                        } else {
                            if (x == listaIncidenteConsulta.size() - 1) {
                                listaIncidenteConsulta.add(incidenteNuevo);
                                fragmentListaIncidentes.agregarIncidenteaLista(listaIncidenteConsulta.size() - 1);
                                break;
                            }
                        }
                        x++;
                    }
                } else {
                    listaIncidenteConsulta.add(0, incidenteNuevo);
                    fragmentListaIncidentes.agregarIncidenteaLista(0);
                }

            }
            fragmentListaIncidentes.notoficarCambios();
            actualizarEmptySate();

        }
    }

    private boolean isThereNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            boolean wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            if (wifiConnected) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            /*case R.id.menu_agenda:
                intent = new Intent(ActivityListaIncidentes.this, ActivityDirectorio.class);
                startActivity(intent);
                break;*/
            case R.id.menu_cerrar_sesion:
                intent = new Intent(ActivityMenuPrincipal.this, Loggin.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void actualizarEmptySate() {
        if (listaIncidenteConsulta.size() == 0) {
            if (nivel_usuario > 1) {
                fab_mas.show();
            }
            if (((View) iv_empty.getParent()).getVisibility() == View.GONE) {
                ((View) iv_empty.getParent()).startAnimation(fadeIn);
            }
        } else {
            fab_mas.hide();
            findViewById(R.id.conte_fragments).setVisibility(View.VISIBLE);
            if (((View) iv_empty.getParent()).getVisibility() == View.VISIBLE) {
                ((View) iv_empty.getParent()).startAnimation(fadeOut);
            }
        }
    }
}
