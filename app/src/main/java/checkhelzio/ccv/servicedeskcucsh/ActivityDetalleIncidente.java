package checkhelzio.ccv.servicedeskcucsh;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import checkhelzio.ccv.servicedeskcucsh.HTextView.FadeTextView;

import static checkhelzio.ccv.servicedeskcucsh.ActivityListaIncidentes.listaIncidenteCompleta;
import static checkhelzio.ccv.servicedeskcucsh.R.id.et_descripcion_del_problema;
import static checkhelzio.ccv.servicedeskcucsh.R.id.input_descripcion_del_problema;

public class ActivityDetalleIncidente extends AppCompatActivity {

    private Incidente incidente;
    private boolean actualizandoStatus;
    private TextView tv_folio;
    private FadeTextView tv_tecnico, tvNombre, tvDependencia, tvUbicacion, tvTelefono, tvCorreo, tvTipoIncidente, tvDescripcion, tvProgreso, tvMensajeTecnico;
    private String st_lista_incidentes;
    private String data;
    private boolean registroCorrecto;
    private boolean isEditanto = false;
    private String st_update;
    private int index;
    private final int EDITAR_EVENTO = 35;
    private String tecnico;
    private int nivel_usuario;
    private int nivel_progreso;
    private String mensaje_tecnico = "Sin datos adicionales";

    private ArrayList<Incidente> listaIncidenteConsultasBackup = new ArrayList<>();
    private boolean wifiConnected;
    private Tecnico tec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_incidente2);
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        incidente = getIntent().getParcelableExtra("INCIDENTE");
        index = getIntent().getIntExtra("INDEX", 0);
        iniciarObjetos();
        llenarDatos();
    }

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

    private void iniciarObjetos() {
        tv_folio = (TextView) findViewById(R.id.tv_folio);
        tv_tecnico = (FadeTextView) findViewById(R.id.tv_tecnico);
        tvNombre = (FadeTextView) findViewById(R.id.tv_nombre);
        tvDependencia = (FadeTextView) findViewById(R.id.tv_dependencia);
        tvUbicacion = (FadeTextView) findViewById(R.id.tv_ubicacion);
        tvTelefono = (FadeTextView) findViewById(R.id.tv_telefono);
        tvCorreo = (FadeTextView) findViewById(R.id.tv_correo);
        tvTipoIncidente = (FadeTextView) findViewById(R.id.tv_incidente);
        tvDescripcion = (FadeTextView) findViewById(R.id.tv_descripcion);
        tvProgreso = (FadeTextView) findViewById(R.id.tv_progreso);
        tvMensajeTecnico = (FadeTextView) findViewById(R.id.tv_mensaje_tecnico);

        SharedPreferences sharedPreferences = getSharedPreferences("SERVICE_DESK_CUCSH_PREFERENCES", Context.MODE_PRIVATE);
        String tecnico = sharedPreferences.getString("TECNICO", "FALSE");
        tec = new Tecnico(tecnico);
        nivel_usuario = new Tecnico(tecnico).getNivel();
        if (nivel_usuario == 1) {
            findViewById(R.id.iv_delete).setVisibility(View.GONE);
        }
    }

    private void llenarDatos() {
        String folio = "Folio " + String.format(Locale.getDefault(), "%03d", incidente.getFolioDelReporte());
        tecnico = "Técnico: " + new Tecnico(incidente.getCodigoDelTecnicoAsignado()).getNombreCompletoDelTecnico();
        tv_folio.setText(folio);
        tv_tecnico.setTextoInicial(tecnico);

        findViewById(R.id.appbar_detalle).setBackgroundColor(Color.parseColor(getFondoPrioridad(incidente.getPrioridadDelServicio())));
        tvNombre.setTextoInicial(incidente.getNombreDelCliente());
        tvDependencia.setTextoInicial(incidente.getDependenciaDelCliente());
        tvUbicacion.setTextoInicial(incidente.getUbicacionDelCliente());
        tvTelefono.setTextoInicial(incidente.getTelefonoDelCliente());
        tvCorreo.setTextoInicial(incidente.getCorreoElectronicoDelCliente());
        tvTipoIncidente.setTextoInicial(crearTextoTipoIncidente(incidente));
        tvDescripcion.setTextoInicial(incidente.getDescripcionDelReporte()
                .replaceAll("Editado por:", "\n\nEditado por:")
                .replaceAll("~~", "\n"));
        tvProgreso.setTextoInicial(crearTextoProgreso(incidente.getStatusDeTerminacionDelReporte()));

        nivel_progreso = incidente.getStatusDeTerminacionDelReporte();

        ViewTreeObserver vto = tvMensajeTecnico.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (incidente.getComentarioTecnico().trim().length() == 0 || incidente.getStatusDeTerminacionDelReporte() == 0) {
                    tvMensajeTecnico.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    findViewById(R.id.conte_extra).setVisibility(View.GONE);
                } else {
                    cambiarLabelMensajeTecnico();
                    tvMensajeTecnico.setTextoInicial(incidente.getComentarioTecnico().trim());
                }
            }
        });

        if (incidente.getStatusDeTerminacionDelReporte() == 0 && incidente.getCodigoDelTecnicoAsignado().equals(tec.getCodigoDelTecnico())) {
            if (isThereNetworkConnection()) {
                actualizarStatusAsigando();
            }
        }
    }

    private void actualizarStatusAsigando() {
        new ActualizarEvento().execute();
        Log.v("ACTUALIZAR", "A:0");
    }

    private class ActualizarEvento extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v("ACTUALIZAR", "A:1");

            actualizandoStatus = true;
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy h:mm a", Locale.getDefault());

            Incidente incidenteNuevo = new Incidente(
                    incidente.getCodigoDelCliente(), // CODIGO DEL CLIENTE
                    incidente.getNombreDelCliente(), // NOMBRE DEL CLIENTE
                    incidente.getDependenciaDelCliente(), // DEPENDENCIA DEL CLIENTE
                    incidente.getUbicacionDelCliente(), // UBICACION DEL CLIENTE
                    incidente.getTelefonoDelCliente(), // TELEFONO DEL CLIENTE
                    incidente.getCorreoElectronicoDelCliente(), // CORREO DEL CLIENTE

                    incidente.getAreaDelServicio(), // AREA DEL SERVICIO
                    incidente.getTipoDeServicio(), // TIPO DEL SERVICIO
                    incidente.getPrioridadDelServicio(), // PRIPRIDAD DEL SERVICIO
                    incidente.getDescripcionDelReporte(), // DESCRIPCION DEL SERVICIO

                    incidente.getFechaYHoraDelReporte() + 1, // FECHA Y HORA DEL SERVICIO
                    1, // STATUS DE TERMINACION DEL SERVICIO
                    incidente.getFolioDelReporte(), // FOLIO DEL SERVICIO

                    incidente.getCodigoDelTecnicoAsignado(), // CODIGO DEL TECNICO ASIGNADO
                    tec.getCodigoDelTecnico(), // CODIGO DE QUIEN LEVANTO EL REPORTE
                    incidente.getStatusDeModificacionDelReporte(), // STATUS DE MODIFICACION DEL SERVICIO
                    "", // TAG DEL REPORTE
                    "Visto por el técnico - " + format.format(Calendar.getInstance().getTime())
            );
            incidenteNuevo.setTagDelReporte(incidenteNuevo.aTag());
            Log.v("ACTUALIZAR", "A: " + incidenteNuevo.aTag());
            Log.v("ACTUALIZAR", "B: " + incidente.aTag());

            listaIncidenteConsultasBackup.clear();
            listaIncidenteConsultasBackup.addAll(listaIncidenteCompleta);

            int x = 0;
            for (Incidente in : listaIncidenteConsultasBackup) {
                if (in.getFolioDelReporte() == incidente.getFolioDelReporte()) {
                    listaIncidenteConsultasBackup.remove(x);
                    break;
                }
                x++;
            }
            listaIncidenteConsultasBackup.add(0, incidenteNuevo);

            Collections.sort(listaIncidenteConsultasBackup, new Comparator<Incidente>() {
                @Override
                public int compare(Incidente o1, Incidente o2) {
                    return Long.valueOf(o1.getFechaYHoraDelReporte()).compareTo(o2.getFechaYHoraDelReporte());
                }
            });

            st_lista_incidentes = "";
            for (Incidente item : listaIncidenteConsultasBackup) {
                st_lista_incidentes += item.aTag() + "¦";
            }

            data = "";
            registroCorrecto = false;
        }

        @Override
        protected Void doInBackground(String... aa12) {
            try {
                URL url = new URL("http://148.202.6.72/aplicacion/incidentes.php");
                HttpURLConnection aaaaa = (HttpURLConnection) url.openConnection();
                aaaaa.setReadTimeout(0);
                aaaaa.setConnectTimeout(0);
                aaaaa.setRequestMethod("POST");
                aaaaa.setDoInput(true);
                aaaaa.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("incidentes", st_lista_incidentes);
                String query = builder.build().getEncodedQuery();

                OutputStream os = aaaaa.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                aaaaa.connect();

                int aaaaaaa = aaaaa.getResponseCode();
                if (aaaaaaa == HttpsURLConnection.HTTP_OK) {
                    registroCorrecto = true;
                    String aaaaaaaa;
                    BufferedReader br = new BufferedReader(new InputStreamReader(aaaaa.getInputStream(), "UTF-8"));
                    while ((aaaaaaaa = br.readLine()) != null) {
                        data += aaaaaaaa;
                    }
                } else {
                    data = "error code: " + aaaaaaa;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (data.contains("error code: ") || !registroCorrecto) {
                Toast.makeText(ActivityDetalleIncidente.this, "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet.", Toast.LENGTH_LONG).show();
            } else {
                SharedPreferences prefs = getSharedPreferences("SERVICE_DESK_CUCSH_PREFERENCES", Context.MODE_PRIVATE);
                prefs.edit().putString("LISTA_DE_INCIDENTES", st_lista_incidentes).apply();

                isEditanto = true;
                new GuardarUpdate().execute();
            }
        }
    }

    private void cambiarLabelMensajeTecnico() {
        switch (nivel_progreso) {
            case 1:
                ((TextView) findViewById(R.id.tv_label_mensaje_tecnico)).setText("Nota del técnico:");
                break;
            case 2:
                ((TextView) findViewById(R.id.tv_label_mensaje_tecnico)).setText("¿Por qué?");
                break;
            case 3:
                ((TextView) findViewById(R.id.tv_label_mensaje_tecnico)).setText("Comentarios finales:");
                break;
        }
    }

    private String crearTextoTipoIncidente(Incidente i) {
        StringBuilder tipoIncidente = new StringBuilder();
        switch (i.getAreaDelServicio()) {
            case 0:
                tipoIncidente.append("Redes y Telefonía - ");
                switch (i.getTipoDeServicio()) {
                    case 0:
                        tipoIncidente.append("Teléfono");
                        break;
                    case 1:
                        tipoIncidente.append("Internet");
                        break;
                    case 2:
                        tipoIncidente.append("WIFI");
                        break;
                    case 3:
                        tipoIncidente.append("Mantenimiento correctivo");
                        break;

                }
                break;
            case 1:
                tipoIncidente.append("Taller de cómputo - ");
                switch (i.getTipoDeServicio()) {
                    case 0:
                        tipoIncidente.append("Mantenimiento correctivo");
                        break;
                    case 1:
                        tipoIncidente.append("Impresoras");
                        break;
                    case 2:
                        tipoIncidente.append("Software");
                        break;
                    case 3:
                        tipoIncidente.append("Hardware");
                        break;
                    case 4:
                        tipoIncidente.append("Correo");
                        break;

                }
                break;
            case 2:
                tipoIncidente.append("Multimedia - ");
                switch (i.getTipoDeServicio()) {
                    case 0:
                        tipoIncidente.append("Grabación de eventos academicos");
                        break;
                    case 1:
                        tipoIncidente.append("Producción de videos");
                        break;
                    case 2:
                        tipoIncidente.append("Fotografia de eventos academicos");
                        break;
                    case 3:
                        tipoIncidente.append("Página web");
                        break;
                    case 4:
                        tipoIncidente.append("Diseño");
                        break;
                    case 5:
                        tipoIncidente.append("Videoconferencias");
                        break;
                    case 6:
                        tipoIncidente.append("Streaming");
                        break;
                    case 7:
                        tipoIncidente.append("Prestamo de equipo de audio y video");
                        break;

                }
                break;
            case 3:
                tipoIncidente.append("Administrativo");
                break;
        }
        return tipoIncidente.toString();
    }

    private String crearTextoProgreso(int statusDeTerminacionDelReporte) {
        switch (statusDeTerminacionDelReporte) {
            case 0:
                return "Asignado";
            case 1:
                return "En progreso";
            case 2:
                return "En espera";
            case 3:
                return "Finalizado";
        }
        return null;
    }

    private String getFondoPrioridad(int prioridadDelServicio) {
        switch (prioridadDelServicio) {
            case 1:
                return "#006A3D";
            case 2:
                return "#DB963F";
            case 3:
                return "#8E171A";
        }
        return "";
    }

    public void cerrar(View view) {
        finish();
    }

    public void eliminar(View view) {
        checkNetworkConnection();
    }

    private void checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            boolean wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            boolean mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifiConnected) {
                if (isEditanto) {
                    isEditanto = false;
                    if (nivel_usuario == 2) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityDetalleIncidente.this);
                        alertDialogBuilder.setMessage("\n" + "¿Deseas editar la información este incidente?");
                        alertDialogBuilder.setPositiveButton("ACEPTAR",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(ActivityDetalleIncidente.this, ActivityFormulario.class);
                                        intent.putExtra("INCIDENTE", incidente);
                                        intent.putExtra("INDEX", index);
                                        startActivityForResult(intent, EDITAR_EVENTO);
                                    }
                                });
                        alertDialogBuilder.setNegativeButton("CANCELAR",
                                null);

                        alertDialogBuilder.create().show();
                    } else {
                        mostrarDialog();
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityDetalleIncidente.this);
                    alertDialogBuilder.setMessage("\n" + "¿Deseas eliminar este incidente?");
                    alertDialogBuilder.setPositiveButton("ACEPTAR",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    new EliminarEvento().execute();
                                }
                            });
                    alertDialogBuilder.setNegativeButton("CANCELAR",
                            null);

                    alertDialogBuilder.create().show();
                }
            } else if (mobileConnected) {
                Snackbar.make(findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet.", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void mostrarDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityDetalleIncidente.this);
        View inflateView = getLayoutInflater().inflate(R.layout.layout_editar_tecnico, null);
        final TextInputLayout input_descripcion = (TextInputLayout) inflateView.findViewById(input_descripcion_del_problema);
        final EditText et_descripcion = (EditText) inflateView.findViewById(et_descripcion_del_problema);
        RadioButton radioEnProgreso = (RadioButton) inflateView.findViewById(R.id.radio_en_progreso);
        RadioButton radioEnEspera = (RadioButton) inflateView.findViewById(R.id.radio_en_espera);
        RadioButton radioFinalizado = (RadioButton) inflateView.findViewById(R.id.radio_finalizado);

        nivel_progreso = 1;

        radioEnProgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_descripcion.setHint("Comentario:");
                nivel_progreso = 1;

            }
        });

        radioEnEspera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_descripcion.setHint("¿Por qué?");
                nivel_progreso = 2;

            }
        });

        radioFinalizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_descripcion.setHint("¿Cómo te fue?");
                nivel_progreso = 3;
            }
        });

        alertDialogBuilder.setMessage("\n" + "¿Deseas editar la información este incidente?");
        alertDialogBuilder.setPositiveButton("ACEPTAR",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (validarItems(input_descripcion, et_descripcion)) {
                            isEditanto = true;
                            new EditarEvento().execute();
                        }
                    }

                    private boolean validarItems(TextInputLayout input_descripcion, EditText et_descripcion) {
                        boolean isValid = true;
                        input_descripcion.setError(null);
                        mensaje_tecnico = et_descripcion.getText().toString();
                        return isValid;
                    }
                });
        alertDialogBuilder.setNegativeButton("CANCELAR",
                null);

        alertDialogBuilder.setView(inflateView);
        alertDialogBuilder.setCancelable(false);
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                .setEnabled(false);
        input_descripcion.setError("Introduce un comentario para continuar");
        et_descripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    // Disable ok button
                    ((AlertDialog) dialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    // Something into edit text. Enable the button.
                    ((AlertDialog) dialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
    }

    public void confirmarEventoEliminado() {
        Toast.makeText(this, "Los cambios se han realizado con éxito", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void editar(View view) {
        isEditanto = true;
        if (nivel_usuario == 2) {
            checkNetworkConnection();
        } else {
            checkNetworkConnection();
        }
    }

    private class EliminarEvento extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listaIncidenteConsultasBackup.clear();
            listaIncidenteConsultasBackup.addAll(listaIncidenteCompleta);
            Collections.sort(listaIncidenteConsultasBackup, new Comparator<Incidente>() {
                @Override
                public int compare(Incidente o1, Incidente o2) {
                    return Long.valueOf(o1.getFechaYHoraDelReporte()).compareTo(o2.getFechaYHoraDelReporte());
                }
            });

            st_lista_incidentes = "";

            for (Incidente i : listaIncidenteConsultasBackup) {
                if (i.getFolioDelReporte() != incidente.getFolioDelReporte()) {
                    st_lista_incidentes += i.aTag() + "¦";
                }
            }

            data = "";
            registroCorrecto = false;
        }

        @Override
        protected Void doInBackground(String... aa12) {
            try {
                URL url = new URL("http://148.202.6.72/aplicacion/incidentes.php");
                HttpURLConnection aaaaa = (HttpURLConnection) url.openConnection();
                aaaaa.setReadTimeout(0);
                aaaaa.setConnectTimeout(0);
                aaaaa.setRequestMethod("POST");
                aaaaa.setDoInput(true);
                aaaaa.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("incidentes", st_lista_incidentes);
                String query = builder.build().getEncodedQuery();

                OutputStream os = aaaaa.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                aaaaa.connect();

                int aaaaaaa = aaaaa.getResponseCode();
                if (aaaaaaa == HttpsURLConnection.HTTP_OK) {
                    registroCorrecto = true;
                    String aaaaaaaa;
                    BufferedReader br = new BufferedReader(new InputStreamReader(aaaaa.getInputStream(), "UTF-8"));
                    while ((aaaaaaaa = br.readLine()) != null) {
                        data += aaaaaaaa;
                    }
                } else {
                    data = "error code: " + aaaaaaa;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (data.contains("error code: ") || !registroCorrecto) {
                Snackbar.make(findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet e intentalo nuevamente.", Snackbar.LENGTH_LONG).show();
            } else {
                SharedPreferences prefs = getSharedPreferences("SERVICE_DESK_CUCSH_PREFERENCES", Context.MODE_PRIVATE);
                prefs.edit().putString("LISTA_DE_INCIDENTES", st_lista_incidentes).apply();

                new GuardarUpdate().execute();

            }
        }
    }

    private class EditarEvento extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Incidente incidenteNuevo = new Incidente(
                    incidente.getCodigoDelCliente(), // CODIGO DEL CLIENTE
                    incidente.getNombreDelCliente(), // NOMBRE DEL CLIENTE
                    incidente.getDependenciaDelCliente(), // DEPENDENCIA DEL CLIENTE
                    incidente.getUbicacionDelCliente(), // UBICACION DEL CLIENTE
                    incidente.getTelefonoDelCliente(), // TELEFONO DEL CLIENTE
                    incidente.getCorreoElectronicoDelCliente(), // CORREO DEL CLIENTE

                    incidente.getAreaDelServicio(), // AREA DEL SERVICIO
                    incidente.getTipoDeServicio(), // TIPO DEL SERVICIO
                    incidente.getPrioridadDelServicio(), // PRIPRIDAD DEL SERVICIO
                    incidente.getDescripcionDelReporte(), // DESCRIPCION DEL SERVICIO

                    Calendar.getInstance().getTimeInMillis(), // FECHA Y HORA DEL SERVICIO
                    nivel_progreso, // STATUS DE TERMINACION DEL SERVICIO
                    incidente.getFolioDelReporte(), // FOLIO DEL SERVICIO

                    incidente.getCodigoDelTecnicoAsignado(), // CODIGO DEL TECNICO ASIGNADO
                    tec.getCodigoDelTecnico(), // CODIGO DE QUIEN LEVANTO EL REPORTE
                    1, // STATUS DE MODIFICACION DEL SERVICIO
                    "", // TAG DEL REPORTE
                    mensaje_tecnico
            );
            incidenteNuevo.setTagDelReporte(incidenteNuevo.aTag());

            listaIncidenteConsultasBackup.clear();
            listaIncidenteConsultasBackup.addAll(listaIncidenteCompleta);

            int x = 0;
            for (Incidente in : listaIncidenteConsultasBackup) {
                if (in.getFolioDelReporte() == incidente.getFolioDelReporte()) {
                    listaIncidenteConsultasBackup.remove(x);
                    break;
                }
                x++;
            }
            listaIncidenteConsultasBackup.add(0, incidenteNuevo);

            Collections.sort(listaIncidenteConsultasBackup, new Comparator<Incidente>() {
                @Override
                public int compare(Incidente o1, Incidente o2) {
                    return Long.valueOf(o1.getFechaYHoraDelReporte()).compareTo(o2.getFechaYHoraDelReporte());
                }
            });

            st_lista_incidentes = "";
            for (Incidente item : listaIncidenteConsultasBackup) {
                st_lista_incidentes += item.aTag() + "¦";
            }

            data = "";
            registroCorrecto = false;
        }

        @Override
        protected Void doInBackground(String... aa12) {
            try {
                URL url = new URL("http://148.202.6.72/aplicacion/incidentes.php");
                HttpURLConnection aaaaa = (HttpURLConnection) url.openConnection();
                aaaaa.setReadTimeout(0);
                aaaaa.setConnectTimeout(0);
                aaaaa.setRequestMethod("POST");
                aaaaa.setDoInput(true);
                aaaaa.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("incidentes", st_lista_incidentes);
                String query = builder.build().getEncodedQuery();

                OutputStream os = aaaaa.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                aaaaa.connect();

                int aaaaaaa = aaaaa.getResponseCode();
                if (aaaaaaa == HttpsURLConnection.HTTP_OK) {
                    registroCorrecto = true;
                    String aaaaaaaa;
                    BufferedReader br = new BufferedReader(new InputStreamReader(aaaaa.getInputStream(), "UTF-8"));
                    while ((aaaaaaaa = br.readLine()) != null) {
                        data += aaaaaaaa;
                    }
                } else {
                    data = "error code: " + aaaaaaa;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (data.contains("error code: ") || !registroCorrecto) {
                Snackbar.make(findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet e intentalo nuevamente.", Snackbar.LENGTH_LONG).show();
            } else {
                SharedPreferences prefs = getSharedPreferences("SERVICE_DESK_CUCSH_PREFERENCES", Context.MODE_PRIVATE);
                prefs.edit().putString("LISTA_DE_INCIDENTES", st_lista_incidentes).apply();

                isEditanto = true;
                new GuardarUpdate().execute();
            }
        }
    }

    private class GuardarUpdate extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            data = "";
            Calendar c = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yy HH:mm:ss", Locale.getDefault());
            st_update = format.format(c.getTime());
        }

        @Override
        protected Void doInBackground(String... aa12) {
            if (st_update.length() == 19) {
                try {
                    URL url = new URL("http://148.202.6.72/aplicacion/update_incidentes.php");
                    HttpURLConnection aaaaa = (HttpURLConnection) url.openConnection();
                    aaaaa.setReadTimeout(0);
                    aaaaa.setConnectTimeout(0);
                    aaaaa.setRequestMethod("POST");
                    aaaaa.setDoInput(true);
                    aaaaa.setDoOutput(true);

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("incidentes", st_update);
                    String query = builder.build().getEncodedQuery();

                    OutputStream os = aaaaa.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();

                    aaaaa.connect();

                    int aaaaaaa = aaaaa.getResponseCode();
                    if (aaaaaaa == HttpsURLConnection.HTTP_OK) {
                        registroCorrecto = true;
                        String aaaaaaaa;
                        BufferedReader br = new BufferedReader(new InputStreamReader(aaaaa.getInputStream(), "UTF-8"));
                        while ((aaaaaaaa = br.readLine()) != null) {
                            data += aaaaaaaa;
                        }
                    } else {
                        data = "error code: " + aaaaaaa;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (data.contains("error code: ")) {
                Snackbar.make(findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet e intentalo nuevamente.", Snackbar.LENGTH_LONG).show();
                Log.v("Error", "ERROR PASO 2");
            } else {
                if (isEditanto) {
                    cambiarLabelMensajeTecnico();
                    tvProgreso.animateText(crearTextoProgreso(nivel_progreso));
                    tvMensajeTecnico.setVisibility(View.VISIBLE);
                    tvMensajeTecnico.animateText(mensaje_tecnico);
                } else {
                    confirmarEventoEliminado();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDITAR_EVENTO && resultCode == RESULT_OK) {
            index = data.getIntExtra("INDEX", 0);
            final Incidente incidenteEditado = data.getParcelableExtra("INCI");
            Log.v("RESULT 1", incidenteEditado.aTag());
            Log.v("RESULT 2", incidente.aTag());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (incidenteEditado.getPrioridadDelServicio() != incidente.getPrioridadDelServicio()) {
                        final View reveal = findViewById(R.id.reveal);
                        reveal.setBackgroundColor(Color.parseColor(getFondoPrioridad(incidenteEditado.getPrioridadDelServicio())));

                        // get the center for the clipping circle
                        int cx = (reveal.getLeft() + reveal.getRight()) / 2;
                        int cy = (reveal.getTop() + reveal.getBottom()) / 2;

                        // get the final radius for the clipping circle
                        int finalRadius = Math.max(reveal.getWidth(), reveal.getHeight());

                        // create the animator for this view (the start radius is zero)
                        Animator anim =
                                ViewAnimationUtils.createCircularReveal(reveal, cx, cy, 0, finalRadius);

                        // make the view visible and start the animation
                        reveal.setVisibility(View.VISIBLE);
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                findViewById(R.id.appbar_detalle).setBackgroundColor(Color.parseColor(getFondoPrioridad(incidenteEditado.getPrioridadDelServicio())));
                                reveal.setVisibility(View.INVISIBLE);
                            }
                        });
                        anim.start();
                    }

                    if (!incidenteEditado.getCodigoDelTecnicoAsignado().equals(incidente.getCodigoDelTecnicoAsignado())) {
                        tecnico = "Técnico: " + new Tecnico(incidenteEditado.getCodigoDelTecnicoAsignado()).getNombreCompletoDelTecnico();
                        tv_tecnico.animateText(tecnico);
                    }

                    if (!incidenteEditado.getNombreDelCliente().equals(incidente.getNombreDelCliente())) {
                        tvNombre.animateText(incidenteEditado.getNombreDelCliente());
                    }

                    if (!incidenteEditado.getDependenciaDelCliente().equals(incidente.getDependenciaDelCliente())) {
                        tvDependencia.animateText(incidenteEditado.getDependenciaDelCliente());
                    }

                    if (!incidenteEditado.getUbicacionDelCliente().equals(incidente.getUbicacionDelCliente())) {
                        tvUbicacion.animateText(incidenteEditado.getUbicacionDelCliente());
                    }

                    if (!incidenteEditado.getTelefonoDelCliente().equals(incidente.getTelefonoDelCliente())) {
                        tvTelefono.animateText(incidenteEditado.getTelefonoDelCliente());
                    }

                    if (!incidenteEditado.getCorreoElectronicoDelCliente().equals(incidente.getCorreoElectronicoDelCliente())) {
                        tvCorreo.animateText(incidenteEditado.getCorreoElectronicoDelCliente());
                    }

                    if (incidenteEditado.getAreaDelServicio() != incidente.getAreaDelServicio() ||
                            incidenteEditado.getTipoDeServicio() != incidente.getTipoDeServicio()) {
                        tvTipoIncidente.animateText(crearTextoTipoIncidente(incidenteEditado));
                    }

                    if (!incidenteEditado.getDescripcionDelReporte().equals(incidente.getDescripcionDelReporte())) {
                        tvDescripcion.animateText(incidenteEditado.getDescripcionDelReporte().replaceAll("Editado por:", "\n\nEditado por:")
                                .replaceAll("~~", "\n"));
                    }

                    incidente = incidenteEditado;
                    Intent intent = getIntent();
                    intent.putExtra("INDEX", index);
                    intent.putExtra("EDITANDO", true);
                    setResult(RESULT_OK, intent);
                }
            }, 500);

        }
    }
}
