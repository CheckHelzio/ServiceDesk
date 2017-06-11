package checkhelzio.ccv.servicedeskcucsh;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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

import static checkhelzio.ccv.servicedeskcucsh.ActivityMenuPrincipal.listaIncidenteCompleta;
import static checkhelzio.ccv.servicedeskcucsh.ActivityMenuPrincipal.listaIncidenteConsulta;
import static checkhelzio.ccv.servicedeskcucsh.ActivityMenuPrincipal.listaIndex;
import static checkhelzio.ccv.servicedeskcucsh.R.id.et_descripcion_del_problema;
import static checkhelzio.ccv.servicedeskcucsh.R.id.input_descripcion_del_problema;

public class FragmentDetalleIncidente extends Fragment {

    private Incidente incidente;
    private TextView tv_folio;
    private FadeTextView tv_tecnico, tvNombre, tvDependencia, tvUbicacion, tvTelefono, tvCorreo, tvTipoIncidente, tvDescripcion, tvProgreso, tvMensajeTecnico;
    private String st_lista_incidentes;
    private String data;
    private boolean registroCorrecto;
    private boolean isEditanto = false;
    private String st_update;
    private final int EDITAR_EVENTO = 35;
    private String tecnico;
    private int nivel_progreso;
    private String mensaje_tecnico = "Sin datos adicionales";

    private ArrayList<Incidente> listaIncidenteConsultasBackup = new ArrayList<>();
    private boolean wifiConnected;

    private View view;
    private View coordinador;
    private Animation fadeIn;
    private Animation fadeOut;
    private View appBar;
    private FadeTextView tv_codigo;
    private Button bt_eliminar;
    private Button bt_editar;
    private boolean actualizandoStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detalle_incidente, container, false);
        iniciarObjetos();
        return view;
    }

    private boolean isThereNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

        bt_eliminar = (Button) view.findViewById(R.id.bt_eliminar);
        bt_editar = (Button) view.findViewById(R.id.bt_editar);
        fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
        tv_folio = (TextView) view.findViewById(R.id.tv_folio);
        tv_codigo = (FadeTextView) view.findViewById(R.id.tv_codigo);
        tv_tecnico = (FadeTextView) view.findViewById(R.id.tv_tecnico);
        tvNombre = (FadeTextView) view.findViewById(R.id.tv_nombre);
        tvDependencia = (FadeTextView) view.findViewById(R.id.tv_dependencia);
        tvUbicacion = (FadeTextView) view.findViewById(R.id.tv_ubicacion);
        tvTelefono = (FadeTextView) view.findViewById(R.id.tv_telefono);
        tvCorreo = (FadeTextView) view.findViewById(R.id.tv_correo);
        tvTipoIncidente = (FadeTextView) view.findViewById(R.id.tv_incidente);
        tvDescripcion = (FadeTextView) view.findViewById(R.id.tv_descripcion);
        tvProgreso = (FadeTextView) view.findViewById(R.id.tv_progreso);
        tvMensajeTecnico = (FadeTextView) view.findViewById(R.id.tv_mensaje_tecnico);
        coordinador = view.findViewById(R.id.nestedScrollView);
        appBar = view.findViewById(R.id.appbar_detalle);

        // SET LISTENNERS
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                coordinador.setVisibility(View.VISIBLE);
                appBar.setVisibility(View.VISIBLE);
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
                coordinador.setVisibility(View.GONE);
                appBar.setVisibility(View.GONE);
                llenarDatos();
                coordinador.startAnimation(fadeIn);
                appBar.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        bt_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar();
            }
        });

        bt_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityMenuPrincipal.tecnico.getNivel() == 1) {
            view.findViewById(R.id.iv_delete).setVisibility(View.GONE);
            bt_eliminar.setVisibility(View.GONE);
        }
    }

    public void reveal(){
        coordinador.startAnimation(fadeOut);
        appBar.startAnimation(fadeOut);
    }

    public void llenarDatos() {

        incidente = ActivityMenuPrincipal.listaIncidenteConsulta.get(ActivityMenuPrincipal.listaIndex);

        String folio = "Folio " + String.format(Locale.getDefault(), "%03d", incidente.getFolioDelReporte());
        tecnico = "Técnico: " + new Tecnico(incidente.getCodigoDelTecnicoAsignado()).getNombreCompletoDelTecnico();
        tv_folio.setText(folio);
        tv_tecnico.setTextoInicial(tecnico);

        view.findViewById(R.id.appbar_detalle).setBackgroundColor(Color.parseColor(getFondoPrioridad(incidente.getPrioridadDelServicio())));
        tvNombre.setTextoInicial(incidente.getNombreDelCliente());
        tv_codigo.setTextoInicial(incidente.getCodigoDelCliente());
        tvDependencia.setTextoInicial(incidente.getDependenciaDelCliente());
        tvUbicacion.setTextoInicial(incidente.getUbicacionDelCliente());
        tvTelefono.setTextoInicial(incidente.getTelefonoDelCliente());
        tvCorreo.setTextoInicial(incidente.getCorreoElectronicoDelCliente());
        tvTipoIncidente.setTextoInicial(crearTextoTipoIncidente(incidente));
        tvDescripcion.setTextoInicial(incidente.getDescripcionDelReporte());
        tvProgreso.setTextoInicial(crearTextoProgreso(incidente.getStatusDeTerminacionDelReporte()));

        nivel_progreso = incidente.getStatusDeTerminacionDelReporte();

        ViewTreeObserver vto = tvMensajeTecnico.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (incidente.getComentarioTecnico().trim().length() == 0 || incidente.getStatusDeTerminacionDelReporte() == 0) {
                    tvMensajeTecnico.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    view.findViewById(R.id.conte_extra).setVisibility(View.GONE);
                } else {
                    cambiarLabelMensajeTecnico();
                    tvMensajeTecnico.setTextoInicial(incidente.getComentarioTecnico().trim());
                }
            }
        });

        try {
            if (incidente.getComentarioTecnico().trim().length() == 0 || incidente.getStatusDeTerminacionDelReporte() == 0) {
                view.findViewById(R.id.conte_extra).setVisibility(View.GONE);
            } else {
                cambiarLabelMensajeTecnico();
                tvMensajeTecnico.setTextoInicial(incidente.getComentarioTecnico().trim());
            }
        }catch (Exception ignored){}
        
        if (incidente.getStatusDeTerminacionDelReporte() == 0 && incidente.getCodigoDelTecnicoAsignado().equals(ActivityMenuPrincipal.tecnico.getCodigoDelTecnico())){
            if (isThereNetworkConnection()){
                actualizarStatusAsigando();
            }
        }

    }

    private void actualizarStatusAsigando() {
        new ActualizarEvento().execute();
    }

    private void cambiarLabelMensajeTecnico() {
        switch (nivel_progreso) {
            case 1:
                ((TextView) view.findViewById(R.id.tv_label_mensaje_tecnico)).setText("Nota del técnico:");
                break;
            case 2:
                ((TextView) view.findViewById(R.id.tv_label_mensaje_tecnico)).setText("¿Por qué?");
                break;
            case 3:
                ((TextView) view.findViewById(R.id.tv_label_mensaje_tecnico)).setText("Comentarios finales:");
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

    public void eliminar() {
        checkNetworkConnection();
    }

    private void checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            boolean wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            boolean mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifiConnected) {
                if (isEditanto) {
                    isEditanto = false;
                    if (ActivityMenuPrincipal.tecnico.getNivel() == 2) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setMessage("\n" + "¿Deseas editar la información este incidente?");
                        alertDialogBuilder.setPositiveButton("ACEPTAR",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(getActivity(), ActivityFormularioTablet.class);
                                        intent.putExtra("INCIDENTE", incidente);
                                        intent.putExtra("INDEX", ActivityMenuPrincipal.listaIndex);
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
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
                Snackbar.make(view.findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet.", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(view.findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void mostrarDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View inflateView = getActivity().getLayoutInflater().inflate(R.layout.layout_editar_tecnico, null);
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
                            new EditarEvento().execute();
                        }
                    }

                    private boolean validarItems(TextInputLayout input_descripcion, EditText et_descripcion) {
                        boolean isValid = true;
                        if (et_descripcion.length() == 0) {
                            input_descripcion.setError("Introduce la descripción del incidente.");
                            isValid = false;
                        } else {
                            input_descripcion.setError(null);
                            mensaje_tecnico = et_descripcion.getText().toString();
                        }
                        return isValid;
                    }
                });
        alertDialogBuilder.setNegativeButton("CANCELAR",
                null);

        alertDialogBuilder.setView(inflateView);
        alertDialogBuilder.setCancelable(false);
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    public void editar() {
        isEditanto = true;
        if (ActivityMenuPrincipal.tecnico.getNivel() == 2) {
            checkNetworkConnection();
        } else {
            checkNetworkConnection();
        }
    }

    public void comprobarExistencia() {
        int indice = 0;
        boolean estaEnLaLista = false;
        for (Incidente i : listaIncidenteConsulta){
            if (i.getFolioDelReporte() == incidente.getFolioDelReporte()){
                listaIndex = indice;
                estaEnLaLista = true;
                break;
            }
            indice++;
        }

        if (estaEnLaLista){
            actualizar(indice);
        }else {
            listaIndex = 0;
            reveal();
        }

    }

    private void actualizar(int indice) {
        final Incidente incidenteEditado = listaIncidenteConsulta.get(indice);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (incidenteEditado.getPrioridadDelServicio() != incidente.getPrioridadDelServicio()) {
                    final View reveal = view.findViewById(R.id.reveal);
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
                            view.findViewById(R.id.appbar_detalle).setBackgroundColor(Color.parseColor(getFondoPrioridad(incidenteEditado.getPrioridadDelServicio())));
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

                Log.v("CODIGO 1", "CODIGO 1: " + incidente.getCodigoDelCliente());
                Log.v("CODIGO 2", "CODIGO 2: " + incidenteEditado.getCodigoDelCliente());

                if (!incidenteEditado.getCodigoDelCliente().equals(incidente.getCodigoDelCliente())) {
                    tv_codigo.animateText(incidenteEditado.getCodigoDelCliente());
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
                    tvDescripcion.animateText(incidenteEditado.getDescripcionDelReporte());
                }

                incidente = incidenteEditado;
            }
        }, 500);

    }

    private class EliminarEvento extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v("ELIMINAR", "ELIMINAR PRE");
            listaIncidenteConsultasBackup.clear();
            listaIncidenteConsultasBackup.addAll(listaIncidenteCompleta);
            Collections.sort(listaIncidenteConsultasBackup, new Comparator<Incidente>() {
                @Override
                public int compare(Incidente o1, Incidente o2) {
                    return Long.valueOf(o1.getFechaYHoraDelReporte()).compareTo(o2.getFechaYHoraDelReporte());
                }
            });

            st_lista_incidentes = "";

            for (Incidente i : listaIncidenteConsultasBackup){
                if (i.getFolioDelReporte() != incidente.getFolioDelReporte()){
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
                Snackbar.make(view.findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet e intentalo nuevamente.", Snackbar.LENGTH_LONG).show();
                listaIncidenteConsultasBackup.clear();
            } else {
                SharedPreferences prefs = getActivity().getSharedPreferences("SERVICE_DESK_CUCSH_PREFERENCES", Context.MODE_PRIVATE);
                prefs.edit().putString("LISTA_DE_INCIDENTES", st_lista_incidentes).apply();

                new GuardarUpdate().execute();

            }
        }
    }

    private class ActualizarEvento extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            actualizandoStatus = true;

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
                    incidente.getCodigoDeQuienLevandoElReporte(), // CODIGO DE QUIEN LEVANTO EL REPORTE
                    incidente.getStatusDeModificacionDelReporte(), // STATUS DE MODIFICACION DEL SERVICIO
                    "", // TAG DEL REPORTE
                    incidente.getComentarioTecnico()
            );
            incidenteNuevo.setTagDelReporte(incidenteNuevo.aTag());

            listaIncidenteConsultasBackup.clear();
            listaIncidenteConsultasBackup.addAll(listaIncidenteCompleta);

            int x = 0;
            for (Incidente in : listaIncidenteConsultasBackup){
                if (in.getFolioDelReporte() == incidente.getFolioDelReporte()){
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
                Snackbar.make(view.findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet e intentalo nuevamente.", Snackbar.LENGTH_LONG).show();
                listaIncidenteConsulta.clear();
                listaIncidenteConsulta.addAll(listaIncidenteConsultasBackup);
            } else {
                SharedPreferences prefs = getActivity().getSharedPreferences("SERVICE_DESK_CUCSH_PREFERENCES", Context.MODE_PRIVATE);
                prefs.edit().putString("LISTA_DE_INCIDENTES", st_lista_incidentes).apply();

                isEditanto = true;
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
                    incidente.getCodigoDeQuienLevandoElReporte(), // CODIGO DE QUIEN LEVANTO EL REPORTE
                    1, // STATUS DE MODIFICACION DEL SERVICIO
                    "", // TAG DEL REPORTE
                    mensaje_tecnico
            );
            incidenteNuevo.setTagDelReporte(incidenteNuevo.aTag());

            listaIncidenteConsultasBackup.clear();
            listaIncidenteConsultasBackup.addAll(listaIncidenteCompleta);

            int x = 0;
            for (Incidente in : listaIncidenteConsultasBackup){
                if (in.getFolioDelReporte() == incidente.getFolioDelReporte()){
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
                Snackbar.make(view.findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet e intentalo nuevamente.", Snackbar.LENGTH_LONG).show();
                listaIncidenteConsulta.clear();
                listaIncidenteConsulta.addAll(listaIncidenteConsultasBackup);
            } else {
                SharedPreferences prefs = getActivity().getSharedPreferences("SERVICE_DESK_CUCSH_PREFERENCES", Context.MODE_PRIVATE);
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
                Snackbar.make(view.findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet e intentalo nuevamente.", Snackbar.LENGTH_LONG).show();
                Log.v("Error", "ERROR PASO 2");
            } else {
                if (!actualizandoStatus){
                    Toast.makeText(getActivity(), "Los cambios se han realizado con éxito", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
