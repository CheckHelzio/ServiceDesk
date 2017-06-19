package checkhelzio.ccv.servicedeskcucsh;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import checkhelzio.ccv.servicedeskcucsh.HTextView.ScaleTextView;

import static checkhelzio.ccv.servicedeskcucsh.ActivityMenuPrincipal.listaIncidenteCompleta;


public class ActivityFormularioTablet extends AppCompatActivity {

    ScaleTextView mysubTextView;
    Typeface bold, light;

    private EditText et_codigo;
    private EditText et_nombre;
    private EditText et_dependencia;
    private EditText et_ubicacion;
    private EditText et_telefono;
    private EditText et_correo;
    private TextInputLayout input_codigo;
    private TextInputLayout input_nombre;
    private TextInputLayout input_dependencia;
    private TextInputLayout input_ubicacion;
    private TextInputLayout input_telefono;
    private TextInputLayout input_correo;
    private SharedPreferences prefs;

    private Button bt_guardar;
    private TextView tv_label_area;
    private TextView tv_label_incidente;
    private TextView tv_label_prioridad;
    private Spinner sp_areas;
    private Spinner sp_incidente;
    private RadioButton radio_baja;
    private RadioButton radio_media;
    private RadioButton radio_alta;
    private TextInputLayout input_descripcion_del_problema;
    private EditText et_descripcion_del_problema;
    private LinearLayout conte_incidente;
    private String st_lista_incidentes = "";
    private String data = "";
    private boolean registroCorrecto;

    private boolean wifiConnected = false;
    private boolean mobileConnected = false;
    private String st_update;
    private boolean isEditMode = false;

    private ArrayList<Incidente> listaIncidenteConsultasBackup = new ArrayList<>();
    private Incidente incidente;


    private ArrayList<DatosPersonales> listaDatosPersonales = new ArrayList<>();
    private ScaleTextView mysubTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        iniciarObjetos();
        ponerTiposDeFuente();
    }

    private void iniciarObjetos() {

        // PREFERENCIAS
        prefs = getSharedPreferences("SERVICE_DESK_CUCSH_PREFERENCES", Context.MODE_PRIVATE);
        String st_lista_datos_personales = prefs.getString("LISTA_DATOS_PERSONALES", "");

        // LISTA DE DATOS PERSONALES PARA LLENADO RAPIDO
        if (!st_lista_datos_personales.equals("")) {
            llenarArray(st_lista_datos_personales);
        }

        // INICIAR TIPOS DE LETRA
        bold = Typeface.createFromAsset(getAssets(), "opensans_bold.ttf");
        light = Typeface.createFromAsset(getAssets(), "opensans_light.ttf");

        // INICIAR OBJETOS
        et_codigo = (EditText) findViewById(R.id.et_codigo);
        et_codigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                for (DatosPersonales d : listaDatosPersonales) {
                    if (d.getCodigo().equals(s.toString())) {
                        et_nombre.setText(d.getNombre());
                        et_dependencia.setText(d.getDependencia());
                        et_ubicacion.setText(d.getUbicacion());
                        et_telefono.setText(d.getTelefono());
                        et_correo.setText(d.getCorreo());
                    }
                }
            }
        });

        et_nombre = (EditText) findViewById(R.id.et_nombre);
        et_dependencia = (EditText) findViewById(R.id.et_dependencia);
        et_ubicacion = (EditText) findViewById(R.id.et_ubicacion);
        et_telefono = (EditText) findViewById(R.id.et_telefono);
        et_correo = (EditText) findViewById(R.id.et_correo);

        input_codigo = (TextInputLayout) findViewById(R.id.input_codigo);
        input_nombre = (TextInputLayout) findViewById(R.id.input_nombre);
        input_dependencia = (TextInputLayout) findViewById(R.id.input_dependencia);
        input_ubicacion = (TextInputLayout) findViewById(R.id.input_ubicacion);
        input_telefono = (TextInputLayout) findViewById(R.id.input_telefono);
        input_correo = (TextInputLayout) findViewById(R.id.input_correo);

        incidente = getIntent().getParcelableExtra("INCIDENTE");

        mysubTextView = (ScaleTextView) findViewById(R.id.tv_subtitulo);
        mysubTextView2 = (ScaleTextView) findViewById(R.id.tv_subtitulo2);

        bt_guardar = (Button) findViewById(R.id.bt_guardar_reporte);
        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarItems()) {

                    DatosPersonales dd = new DatosPersonales(
                            // codigo
                            et_codigo.getText().toString(),
                            // nombre
                            et_nombre.getText().toString(),
                            // dependencia
                            et_dependencia.getText().toString(),
                            // ubicacion
                            et_ubicacion.getText().toString(),
                            // telefono
                            et_telefono.getText().toString(),
                            // correo
                            et_correo.getText().toString());

                    for (DatosPersonales d : listaDatosPersonales) {
                        if (d.getCodigo().equals(dd.getCodigo())) {
                            listaDatosPersonales.remove(d);
                            break;
                        }
                    }

                    listaDatosPersonales.add(dd);

                    String nueva_lista = "";
                    for (DatosPersonales d : listaDatosPersonales) {
                        nueva_lista += d.aTag() + "¦";
                    }

                    prefs.edit().putString("LISTA_DATOS_PERSONALES", nueva_lista).apply();

                    bt_guardar.setEnabled(false);
                    checkNetworkConnection();
                }
            }
        });

        conte_incidente = (LinearLayout) findViewById(R.id.conte_incidente);
        tv_label_area = (TextView) findViewById(R.id.tv_label_areas);
        tv_label_incidente = (TextView) findViewById(R.id.tv_label_incidente);
        tv_label_prioridad = (TextView) findViewById(R.id.tv_label_prioridad);
        sp_areas = (Spinner) findViewById(R.id.sp_areas);
        sp_incidente = (Spinner) findViewById(R.id.sp_incidente);
        radio_baja = (RadioButton) findViewById(R.id.radio_baja);
        radio_media = (RadioButton) findViewById(R.id.radio_media);
        radio_alta = (RadioButton) findViewById(R.id.radio_alta);
        input_descripcion_del_problema = (TextInputLayout) findViewById(R.id.input_descripcion_del_problema);
        et_descripcion_del_problema = (EditText) findViewById(R.id.et_descripcion_del_problema);
        et_descripcion_del_problema.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et_descripcion_del_problema.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        final String[] areas = {"Redes y Telefonía", "Taller de cómputo", "Multimedia", "Administrativa"};
        final List<String> listaProblemas = new ArrayList<>();

        ArrayAdapter font_name_Adapter = new ArrayAdapter<String>(ActivityFormularioTablet.this, android.R.layout.simple_spinner_item, areas) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                v.setPadding(0, v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
                ((TextView) v).setTypeface(bold);
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                return v;
            }


            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(bold);
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                return v;
            }
        };

        final ArrayAdapter tipoDePorblemaAdapter = new ArrayAdapter<String>(ActivityFormularioTablet.this, android.R.layout.simple_spinner_item, listaProblemas) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                v.setPadding(0, v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
                ((TextView) v).setTypeface(bold);
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                return v;
            }


            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(bold);
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                return v;
            }
        };

        font_name_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoDePorblemaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_areas.setAdapter(font_name_Adapter);
        sp_areas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        tv_label_incidente.setText("Tipo de incidente:");
                        listaProblemas.clear();
                        listaProblemas.add("Teléfono");
                        listaProblemas.add("Internet");
                        listaProblemas.add("WIFI");
                        listaProblemas.add("Mantenimiento correctivo");
                        tipoDePorblemaAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        tv_label_incidente.setText("Tipo de incidente:");
                        listaProblemas.clear();
                        listaProblemas.add("Mantenimiento correctivo");
                        listaProblemas.add("Impresoras");
                        listaProblemas.add("Software");
                        listaProblemas.add("Hardware");
                        listaProblemas.add("Correo");
                        tipoDePorblemaAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        tv_label_incidente.setText("Tipo de incidente:");
                        listaProblemas.clear();
                        listaProblemas.add("Grabación de eventos academicos");
                        listaProblemas.add("Producción de videos");
                        listaProblemas.add("Fotografia de eventos academicos");
                        listaProblemas.add("Página web");
                        listaProblemas.add("Diseño");
                        listaProblemas.add("Videoconferencias");
                        listaProblemas.add("Streaming");
                        listaProblemas.add("Préstamo de equipo de audio y video");
                        tipoDePorblemaAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        tv_label_incidente.setText("Asignado a:");
                        listaProblemas.clear();
                        listaProblemas.add("Héctor Aceves Shimizu y López");
                        listaProblemas.add("Idania Gómez Cosio");
                        listaProblemas.add("Eduardo Solano Guzmán");
                        tipoDePorblemaAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_incidente.setAdapter(tipoDePorblemaAdapter);

        if (incidente != null) {
            isEditMode = true;
            et_codigo.setText(incidente.getCodigoDelCliente());
            et_nombre.setText(incidente.getNombreDelCliente());
            et_dependencia.setText(incidente.getDependenciaDelCliente());
            et_ubicacion.setText(incidente.getUbicacionDelCliente());
            et_telefono.setText(incidente.getTelefonoDelCliente());
            et_correo.setText(incidente.getCorreoElectronicoDelCliente());

            sp_areas.setSelection(incidente.getAreaDelServicio());
            sp_areas.setEnabled(false);
            sp_incidente.setSelection(incidente.getTipoDeServicio());
            switch (incidente.getPrioridadDelServicio()) {
                case 1:
                    radio_baja.setChecked(true);
                    break;
                case 2:
                    radio_media.setChecked(true);
                    break;
                case 3:
                    radio_alta.setChecked(true);
                    break;
            }
        }
    }

    private void checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifiConnected) {
                new GuardarEvento().execute();
            }
        } else {
            Log.v("SIN INTERNET", "NO INTERNET");
            Snackbar.make(findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet.", Snackbar.LENGTH_LONG).show();
            bt_guardar.setEnabled(true);
        }
    }

    class GuardarEvento extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            listaIncidenteConsultasBackup.clear();
            listaIncidenteConsultasBackup.addAll(listaIncidenteCompleta);

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            SimpleDateFormat formatHora = new SimpleDateFormat("h:mm a", Locale.getDefault());

            Incidente i = new Incidente(
                    // CODIGO DEL CLIENTE
                    et_codigo.getText().toString().trim(),
                    // NOMBRE DEL CLIENTE
                    et_nombre.getText().toString().trim(),
                    // DEPENDENCIA DEL CLIENTE
                    et_dependencia.getText().toString().trim(),
                    // UBICACION DEL CLIENTE
                    et_ubicacion.getText().toString().trim(),
                    // TELEFONO DEL CLIENTE
                    et_telefono.getText().toString().trim(),
                    // CORREO ELECTRONICO DEL CLIENTE
                    et_correo.getText().toString().trim(),
                    // AREA A LA QUE PERTENECE EL INCIDENTE
                    sp_areas.getSelectedItemPosition(),
                    // TIPO DE INCIDENTE
                    sp_incidente.getSelectedItemPosition(),
                    // PRIRIDAD DEL INCIDENTE
                    getPrioridad(),
                    // DESCRIPCION DEL INCIDENTE
                    isEditMode ?
                            incidente.getDescripcionDelReporte() +
                                    "Editado por: " +
                                    ActivityMenuPrincipal.tecnico.getNombreDelTecnico() +
                                    "  -  " + format.format(Calendar.getInstance().getTime()) +
                                    " a las " + formatHora.format(Calendar.getInstance().getTime())
                                    + "~~" + et_descripcion_del_problema.getText().toString()
                            : et_descripcion_del_problema.getText().toString(),
                    // HORA Y FECHA DEL LEVANTAMIENTO DEL INCIDENTE
                    Calendar.getInstance().getTimeInMillis(),
                    // STATUS DE TERMINACION DEL REPORTE
                    isEditMode ? incidente.getStatusDeTerminacionDelReporte() : 0, // ASIGNADO
                    // FOLIO DEL INCIDENTE
                    isEditMode ? incidente.getFolioDelReporte() : ActivityMenuPrincipal.numeroFolioSiguiente,
                    // CODIGO DEL TECNICO ASIGNADO PARA LA SOLUCION DEL INSIDENTE
                    isEditMode ? incidente.getCodigoDelTecnicoAsignado() : asignarTecnico(),
                    // CODIGO DEL TECNICO QUE LEVANTO EL INSIDENTE
                    ActivityMenuPrincipal.tecnico.getCodigoDelTecnico(),
                    // STATUS DE MODIFICACION DEL INSIDENTE, REGISTRADO (0), EDITADO (1)
                    isEditMode ? 1 : 0,
                    // TAG PARA GUARDAR EN LINEA
                    "",
                    isEditMode ? incidente.getComentarioTecnico() : "Sin comentarios adicionales" // COMENTARIO DEL TECNICO
            );
            i.setTagDelReporte(i.aTag());

            if (isEditMode) {
                int x = 0;
                for (Incidente in : listaIncidenteConsultasBackup) {
                    if (in.getFolioDelReporte() == incidente.getFolioDelReporte()) {
                        listaIncidenteConsultasBackup.remove(x);
                        break;
                    }
                    x++;
                }
            }
            listaIncidenteConsultasBackup.add(0, i);

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
                bt_guardar.setEnabled(true);
            } else {
                SharedPreferences prefs = getSharedPreferences("SERVICE_DESK_CUCSH_PREFERENCES", Context.MODE_PRIVATE);
                prefs.edit().putString("LISTA_DE_INCIDENTES", st_lista_incidentes).apply();
                new GuardarUpdate().execute();
            }
        }
    }

    public boolean validarItems() {
        boolean isValid = true;

        if (et_descripcion_del_problema.length() == 0) {
            input_descripcion_del_problema.setError(" ");
            isValid = false;
        } else {
            input_descripcion_del_problema.setError(null);
        }

        if (et_codigo.getText().toString().length() == 0) {
            input_codigo.setError(" ");
            isValid = false;
        } else {
            input_codigo.setError(null);
        }

        if (et_nombre.getText().toString().length() == 0) {
            input_nombre.setError(" ");
            isValid = false;
        } else {
            input_nombre.setError(null);
        }

        if (et_dependencia.getText().toString().length() == 0) {
            input_dependencia.setError(" ");
            isValid = false;
        } else {
            input_dependencia.setError(null);
        }

        if (et_ubicacion.getText().toString().length() == 0) {
            input_ubicacion.setError(" ");
            isValid = false;
        } else {
            input_ubicacion.setError(null);
        }

        if (et_telefono.getText().toString().length() == 0) {
            input_telefono.setError(" ");
            isValid = false;
        } else {
            input_telefono.setError(null);
        }

        if (et_correo.getText().toString().length() == 0) {
            input_correo.setError(" ");
            isValid = false;
        } else {
            input_correo.setError(null);
        }
        return isValid;
    }

    private void llenarArray(String st_lista_datos_personales) {
        for (String s : st_lista_datos_personales.split("¦")) {
            listaDatosPersonales.add(new DatosPersonales(
                    s.split("::")[0],
                    s.split("::")[1],
                    s.split("::")[2],
                    s.split("::")[3],
                    s.split("::")[4],
                    s.split("::")[5]
            ));
        }
    }

    private void ponerTiposDeFuente() {
        mysubTextView.setTypeface(bold);
        mysubTextView2.setTypeface(bold);
        et_codigo.setTypeface(bold);
        et_nombre.setTypeface(bold);
        et_dependencia.setTypeface(bold);
        et_ubicacion.setTypeface(bold);
        et_telefono.setTypeface(bold);
        et_correo.setTypeface(bold);

        input_nombre.setTypeface(light);
        input_codigo.setTypeface(light);
        input_dependencia.setTypeface(light);
        input_ubicacion.setTypeface(light);
        input_telefono.setTypeface(light);
        input_correo.setTypeface(light);

        tv_label_area.setTypeface(light);
        tv_label_incidente.setTypeface(light);
        tv_label_prioridad.setTypeface(light);

        radio_baja.setTypeface(bold);
        radio_media.setTypeface(bold);
        radio_alta.setTypeface(bold);
        input_descripcion_del_problema.setTypeface(light);
        et_descripcion_del_problema.setTypeface(bold);
    }

    class GuardarUpdate extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            data = "";
            Calendar c = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yy HH:mm:ss");
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
                bt_guardar.setEnabled(true);
            } else {
                Toast.makeText(ActivityFormularioTablet.this, "Los cambios se han realizado con éxito (Editar)", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private int getPrioridad() {
        int i = 1;
        if (radio_media.isChecked()) {
            i = 2;
        } else if (radio_alta.isChecked()) {
            i = 3;
        }
        return i;
    }

    private String asignarTecnico() {
        Calendar calendar = Calendar.getInstance();
        switch (sp_areas.getSelectedItemPosition()) {
            case 0:
                if (calendar.get(Calendar.HOUR_OF_DAY) < 9) {
                    return new Tecnico("2957917").getCodigoDelTecnico();
                } else if (calendar.get(Calendar.HOUR_OF_DAY) < 13) {
                    int random = randomInt(0, 1);
                    switch (random) {
                        case 0:
                            return new Tecnico("2957917").getCodigoDelTecnico();
                        case 1:
                            return new Tecnico("2520761").getCodigoDelTecnico();
                    }
                } else if (calendar.get(Calendar.HOUR_OF_DAY) < 16) {
                    int random = randomInt(0, 2);
                    switch (random) {
                        case 0:
                            return new Tecnico("2957917").getCodigoDelTecnico();
                        case 1:
                            return new Tecnico("2520761").getCodigoDelTecnico();
                        case 2:
                            return new Tecnico("2954064").getCodigoDelTecnico();
                    }
                } else if (calendar.get(Calendar.HOUR_OF_DAY) < 18) {
                    int random = randomInt(1, 2);
                    switch (random) {
                        case 1:
                            return new Tecnico("2520761").getCodigoDelTecnico();
                        case 2:
                            return new Tecnico("2954064").getCodigoDelTecnico();
                    }
                } else if (calendar.get(Calendar.HOUR_OF_DAY) < 19) {
                    return new Tecnico("2954064").getCodigoDelTecnico();
                } else {
                    return new Tecnico("2957917").getCodigoDelTecnico();
                }
                break;
            case 1:
                if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
                    return new Tecnico("9715371").getCodigoDelTecnico();
                } else if (calendar.get(Calendar.HOUR_OF_DAY) < 14) {
                    int random = randomInt(0, 1);
                    switch (random) {
                        case 0:
                            return new Tecnico("9715371").getCodigoDelTecnico();
                        case 1:
                            return new Tecnico("1234").getCodigoDelTecnico();
                    }
                } else if (calendar.get(Calendar.HOUR_OF_DAY) < 16) {
                    int random = randomInt(0, 2);
                    switch (random) {
                        case 0:
                            return new Tecnico("9715371").getCodigoDelTecnico();
                        case 1:
                            return new Tecnico("1234").getCodigoDelTecnico();
                        case 2:
                            return new Tecnico("09537805").getCodigoDelTecnico();
                    }
                } else if (calendar.get(Calendar.HOUR_OF_DAY) < 18) {
                    int random = randomInt(1, 2);
                    switch (random) {
                        case 1:
                            return new Tecnico("1234").getCodigoDelTecnico();
                        case 2:
                            return new Tecnico("09537805").getCodigoDelTecnico();
                    }
                } else if (calendar.get(Calendar.HOUR_OF_DAY) < 19) {
                    return new Tecnico("09537805").getCodigoDelTecnico();
                } else {
                    return new Tecnico("9715371").getCodigoDelTecnico();
                }
                break;
            case 2:
                switch (sp_incidente.getSelectedItemPosition()) {
                    case 0:
                        return new Tecnico("01290713").getCodigoDelTecnico(); // ZYANYA
                    case 1:
                        return new Tecnico("01290713").getCodigoDelTecnico(); // ZYANYA
                    case 2:
                        return new Tecnico("01290713").getCodigoDelTecnico(); // ZYANYA
                    case 3:
                        return new Tecnico("2946467").getCodigoDelTecnico(); // LALO
                    case 4:
                        return new Tecnico("005107962").getCodigoDelTecnico(); // HIRAM
                    case 5:
                        if (calendar.get(Calendar.HOUR_OF_DAY) < 16) {
                            return new Tecnico("2946467").getCodigoDelTecnico(); // LALO
                        } else {
                            return new Tecnico("2952470").getCodigoDelTecnico(); // EMMANUEL
                        }
                    case 6:
                        if (calendar.get(Calendar.HOUR_OF_DAY) < 16) {
                            return new Tecnico("2946467").getCodigoDelTecnico(); // LALO
                        } else {
                            return new Tecnico("2952470").getCodigoDelTecnico(); // EMMANUEL
                        }
                    case 7:
                        if (calendar.get(Calendar.HOUR_OF_DAY) < 16) {
                            return new Tecnico("2946467").getCodigoDelTecnico(); // LALO
                        } else {
                            return new Tecnico("2952470").getCodigoDelTecnico(); // EMMANUEL
                        }
                }
                break;
            case 3:
                switch (sp_incidente.getSelectedItemPosition()) {
                    case 0:
                        return new Tecnico("2528894").getCodigoDelTecnico(); // HECTOR
                    case 1:
                        return new Tecnico("2806533").getCodigoDelTecnico(); // IDANIA
                    case 2:
                        return new Tecnico("2921073").getCodigoDelTecnico(); // EDUARDO
                }
                break;
        }
        return null;
    }

    private int randomInt(int min, int max) {
        Random rand = new Random();
        return min + rand.nextInt((max - min) + 1);
    }

}
