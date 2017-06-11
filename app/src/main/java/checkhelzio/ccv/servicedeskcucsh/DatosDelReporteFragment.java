package checkhelzio.ccv.servicedeskcucsh;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
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
import java.util.List;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import static checkhelzio.ccv.servicedeskcucsh.ActivityListaIncidentes.listaIncidente;
import static checkhelzio.ccv.servicedeskcucsh.R.id.et_codigo;
import static checkhelzio.ccv.servicedeskcucsh.R.id.tv_descripcion;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DatosDelReporteFragment.CambiarPaginaListenner} interface
 * to handle interaction events.
 * Use the {@link DatosDelReporteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatosDelReporteFragment extends Fragment {

    private Typeface bold, light;
    private CambiarPaginaListenner mListener;
    private RegistroListoListenner mRegistroListoListener;
    private EditarIncidenteListoListenner mEditarIncidenteListoListenner;
    private Button bt_atras;
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
    private String codigo, nombre, dependencia, ubicacion, telefono, correo;
    private String st_lista_incidentes = "";
    private String data = "";
    private boolean registroCorrecto;

    private boolean wifiConnected = false;
    private boolean mobileConnected = false;
    private String st_update;
    private View v;
    private boolean isEditMode = false;

    private ArrayList<Incidente> listaIncidentesBackup = new ArrayList<>();
    private Incidente incidente;


    public DatosDelReporteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DatosDelReporteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DatosDelReporteFragment newInstance() {
        return new DatosDelReporteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_datos_del_reporte, container, false);
        iniciarObjetos(v);
        ponerTiposDeFuente();
        return v;
    }

    private void iniciarObjetos(View layout) {

        bold = Typeface.createFromAsset(getActivity().getAssets(), "opensans_bold.ttf");
        light = Typeface.createFromAsset(getActivity().getAssets(), "opensans_light.ttf");

        bt_atras = (Button) layout.findViewById(R.id.bt_atras);
        bt_guardar = (Button) layout.findViewById(R.id.bt_guardar_reporte);
        bt_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cambiarPagina(0);
            }
        });
        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarItems()) {
                    bt_guardar.setEnabled(false);
                    checkNetworkConnection();
                }
            }
        });

        conte_incidente = (LinearLayout) layout.findViewById(R.id.conte_incidente);
        tv_label_area = (TextView) layout.findViewById(R.id.tv_label_areas);
        tv_label_incidente = (TextView) layout.findViewById(R.id.tv_label_incidente);
        tv_label_prioridad = (TextView) layout.findViewById(R.id.tv_label_prioridad);
        sp_areas = (Spinner) layout.findViewById(R.id.sp_areas);
        sp_incidente = (Spinner) layout.findViewById(R.id.sp_incidente);
        radio_baja = (RadioButton) layout.findViewById(R.id.radio_baja);
        radio_media = (RadioButton) layout.findViewById(R.id.radio_media);
        radio_alta = (RadioButton) layout.findViewById(R.id.radio_alta);
        input_descripcion_del_problema = (TextInputLayout) layout.findViewById(R.id.input_descripcion_del_problema);
        et_descripcion_del_problema = (EditText) layout.findViewById(R.id.et_descripcion_del_problema);
        et_descripcion_del_problema.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et_descripcion_del_problema.setRawInputType(InputType.TYPE_CLASS_TEXT);

        final String[] areas = {"Redes y Telefonía", "Taller de cómputo", "Multimedia", "Administrativa"};
        final List<String> listaProblemas = new ArrayList<String>();

        ArrayAdapter font_name_Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, areas) {
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

        final ArrayAdapter tipoDePorblemaAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listaProblemas) {
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

        incidente = getActivity().getIntent().getParcelableExtra("INCIDENTE");
        if (incidente != null) {
            isEditMode = true;
            et_descripcion_del_problema.setText(incidente.getDescripcionDelReporte());
            sp_areas.setSelection(incidente.getAreaDelServicio());
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

        listaIncidentesBackup.addAll(listaIncidente);
    }

    public void limpiarCampos() {
        et_descripcion_del_problema.setText("");
        sp_areas.setSelection(0);
        sp_incidente.setSelection(0);
        radio_baja.setChecked(true);
    }

    class GuardarEvento extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.v("GUARDAR EVENTO", "PRE EXECUTE - TAMAÑO LISTA: " + listaIncidente.size());

            Incidente i = new Incidente(
                    // CODIGO DEL CLIENTE
                    codigo,
                    // NOMBRE DEL CLIENTE
                    nombre,
                    // DEPENDENCIA DEL CLIENTE
                    dependencia,
                    // UBICACION DEL CLIENTE
                    ubicacion,
                    // TELEFONO DEL CLIENTE
                    telefono,
                    // CORREO ELECTRONICO DEL CLIENTE
                    correo,
                    // AREA A LA QUE PERTENECE EL INCIDENTE
                    sp_areas.getSelectedItemPosition(),
                    // TIPO DE INCIDENTE
                    sp_incidente.getSelectedItemPosition(),
                    // PRIRIDAD DEL INCIDENTE
                    getPrioridad(),
                    // DESCRIPCION DEL INCIDENTE
                    et_descripcion_del_problema.getText().toString(),
                    // HORA Y FECHA DEL LEVANTAMIENTO DEL INCIDENTE
                    Calendar.getInstance().getTimeInMillis(),
                    // STATUS DE TERMINACION DEL REPORTE
                    0, // ASIGNADO
                    // FOLIO DEL INCIDENTE
                    isEditMode ? incidente.getFolioDelReporte() : ActivityListaIncidentes.numeroFolioSiguiente,
                    // CODIGO DEL TECNICO ASIGNADO PARA LA SOLUCION DEL INSIDENTE
                    isEditMode ? incidente.getCodigoDelTecnicoAsignado() : asignarTecnico(),
                    // CODIGO DEL TECNICO QUE LEVANTO EL INSIDENTE
                    ActivityListaIncidentes.tecnico.getCodigoDelTecnico(),
                    // STATUS DE MODIFICACION DEL INSIDENTE, REGISTRADO (0), EDITADO (1)
                    isEditMode ? 1 : 0,
                    // TAG PARA GUARDAR EN LINEA
                    "",
                    "Sin comentarios adicionales" // COMENTARIO DEL TECNICO
            );
            i.setTagDelReporte(i.aTag());

            if (isEditMode) {
                listaIncidente.set(getActivity().getIntent().getIntExtra("INDEX", 0), i);
            } else {
                listaIncidente.add(i);
            }

            st_lista_incidentes = "";
            for (Incidente item : listaIncidente) {
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
                Snackbar.make(v.findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet e intentalo nuevamente.", Snackbar.LENGTH_LONG).show();
                bt_guardar.setEnabled(true);
                listaIncidente.clear();
                listaIncidente.addAll(listaIncidentesBackup);
            } else {
                SharedPreferences prefs = getActivity().getSharedPreferences("SERVICE_DESK_CUCSH_PREFERENCES", Context.MODE_PRIVATE);
                prefs.edit().putString("LISTA_DE_INCIDENTES", st_lista_incidentes).apply();

                new GuardarUpdate().execute();

            }
        }
    }

    private String asignarTecnico() {
        Calendar calendar = Calendar.getInstance();
        switch (sp_areas.getSelectedItemPosition()){
            case 0:
                if (calendar.get(Calendar.HOUR_OF_DAY) < 9){
                    return new Tecnico("2957917").getCodigoDelTecnico();
                }else if (calendar.get(Calendar.HOUR_OF_DAY) < 13){
                    int random = randomInt(0,1);
                    switch (random){
                        case 0:
                            return new Tecnico("2957917").getCodigoDelTecnico();
                        case 1:
                            return new Tecnico("2520761").getCodigoDelTecnico();
                    }
                }else if (calendar.get(Calendar.HOUR_OF_DAY) < 16){
                    int random = randomInt(0,2);
                    switch (random){
                        case 0:
                            return new Tecnico("2957917").getCodigoDelTecnico();
                        case 1:
                            return new Tecnico("2520761").getCodigoDelTecnico();
                        case 2:
                            return new Tecnico("2954064").getCodigoDelTecnico();
                    }
                }else if (calendar.get(Calendar.HOUR_OF_DAY) < 18){
                    int random = randomInt(1,2);
                    switch (random){
                        case 1:
                            return new Tecnico("2520761").getCodigoDelTecnico();
                        case 2:
                            return new Tecnico("2954064").getCodigoDelTecnico();
                    }
                }else if (calendar.get(Calendar.HOUR_OF_DAY) < 19){
                    return new Tecnico("2954064").getCodigoDelTecnico();
                }else {
                    return new Tecnico("2957917").getCodigoDelTecnico();
                }
                break;
            case 1:
                if (calendar.get(Calendar.HOUR_OF_DAY) < 10){
                    return new Tecnico("9715371").getCodigoDelTecnico();
                }else if (calendar.get(Calendar.HOUR_OF_DAY) < 14){
                    int random = randomInt(0,1);
                    switch (random){
                        case 0:
                            return new Tecnico("9715371").getCodigoDelTecnico();
                        case 1:
                            return new Tecnico("1234").getCodigoDelTecnico();
                    }
                }else if (calendar.get(Calendar.HOUR_OF_DAY) < 16){
                    int random = randomInt(0,2);
                    switch (random){
                        case 0:
                            return new Tecnico("9715371").getCodigoDelTecnico();
                        case 1:
                            return new Tecnico("1234").getCodigoDelTecnico();
                        case 2:
                            return new Tecnico("09537805").getCodigoDelTecnico();
                    }
                }else if (calendar.get(Calendar.HOUR_OF_DAY) < 18){
                    int random = randomInt(1,2);
                    switch (random){
                        case 1:
                            return new Tecnico("1234").getCodigoDelTecnico();
                        case 2:
                            return new Tecnico("09537805").getCodigoDelTecnico();
                    }
                }else if (calendar.get(Calendar.HOUR_OF_DAY) < 19){
                    return new Tecnico("09537805").getCodigoDelTecnico();
                }else {
                    return new Tecnico("9715371").getCodigoDelTecnico();
                }
                break;
            case 2:
                switch (sp_incidente.getSelectedItemPosition()){
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
                        if (calendar.get(Calendar.HOUR_OF_DAY) < 16){
                            return new Tecnico("2946467").getCodigoDelTecnico(); // LALO
                        }else {
                            return new Tecnico("2952470").getCodigoDelTecnico(); // EMMANUEL
                        }
                    case 6:
                        if (calendar.get(Calendar.HOUR_OF_DAY) < 16){
                            return new Tecnico("2946467").getCodigoDelTecnico(); // LALO
                        }else {
                            return new Tecnico("2952470").getCodigoDelTecnico(); // EMMANUEL
                        }
                    case 7:
                        if (calendar.get(Calendar.HOUR_OF_DAY) < 16){
                            return new Tecnico("2946467").getCodigoDelTecnico(); // LALO
                        }else {
                            return new Tecnico("2952470").getCodigoDelTecnico(); // EMMANUEL
                        }
                }
                break;
            case 3:
                switch (sp_incidente.getSelectedItemPosition()){
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

    private int getPrioridad() {
        int i = 1;
        if (radio_media.isChecked()) {
            i = 2;
        } else if (radio_alta.isChecked()) {
            i = 3;
        }
        return i;
    }

    private void ponerTiposDeFuente() {

        tv_label_area.setTypeface(light);
        tv_label_incidente.setTypeface(light);
        tv_label_prioridad.setTypeface(light);

        radio_baja.setTypeface(bold);
        radio_media.setTypeface(bold);
        radio_alta.setTypeface(bold);
        input_descripcion_del_problema.setTypeface(bold);
        et_descripcion_del_problema.setTypeface(bold);
    }

    public void pasarDatosPersonales(String codigo, String nombre, String dependencia, String ubicacion, String telefono, String correo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.dependencia = dependencia;
        this.ubicacion = ubicacion;
        this.telefono = telefono;
        this.correo = correo;
    }

    public interface CambiarPaginaListenner {
        public void cambiarPagina(int position);
    }

    public interface RegistroListoListenner {
        public void registroListo();
    }

    public interface EditarIncidenteListoListenner {
        public void editarIncidenteListo();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CambiarPaginaListenner) {
            mListener = (CambiarPaginaListenner) context;
            mRegistroListoListener = (RegistroListoListenner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        if (context instanceof EditarIncidenteListoListenner) {
            mEditarIncidenteListoListenner = (EditarIncidenteListoListenner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public boolean validarItems() {
        boolean isValid = true;
        if (et_descripcion_del_problema.length() == 0) {
            input_descripcion_del_problema.setError("Introduce la descripción del incidente.");
            isValid = false;
        } else {
            input_descripcion_del_problema.setError(null);
        }
        return isValid;
    }

    private void checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifiConnected) {
                new GuardarEvento().execute();
            } else if (mobileConnected) {
                new GuardarEvento().execute();
            }
        } else {
            Log.v("SIN INTERNET", "NO INTERNET");
            Snackbar.make(v.findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet.", Snackbar.LENGTH_LONG).show();
            bt_guardar.setEnabled(true);
        }
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
                Snackbar.make(v.findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet e intentalo nuevamente.", Snackbar.LENGTH_LONG).show();
                bt_guardar.setEnabled(true);
                listaIncidente.clear();
                listaIncidente.addAll(listaIncidentesBackup);
            } else {
                if (isEditMode) {
                    mEditarIncidenteListoListenner.editarIncidenteListo();
                } else {
                    mRegistroListoListener.registroListo();
                }
            }
        }
    }
}
