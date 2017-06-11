package checkhelzio.ccv.servicedeskcucsh;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DatosPersonalesFragment.OnCambiarPaginaListenner} interface
 * to handle interaction events.
 * Use the {@link DatosPersonalesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatosPersonalesFragment extends Fragment {

    private Typeface bold, light;

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
    private ArrayList<DatosPersonales> listaDatosPersonales = new ArrayList<>();

    private OnCambiarPaginaListenner mListener;
    private SharedPreferences prefs;

    public DatosPersonalesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DatosPersonalesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DatosPersonalesFragment newInstance() {
        return new DatosPersonalesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getActivity().getSharedPreferences("SERVICE_DESK_CUCSH_PREFERENCES", Context.MODE_PRIVATE);
        String st_lista_datos_personales = prefs.getString("LISTA_DATOS_PERSONALES", "");
        Log.v("DATOSPERSONALES", "Nepe: " + st_lista_datos_personales);
        if (!st_lista_datos_personales.equals("")) {
            llenarArray(st_lista_datos_personales);
        }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_datos_personales, container, false);
        iniciarObjetos(v);
        ponerTiposDeFuente();
        return v;
    }


    interface OnCambiarPaginaListenner {
        void onCambiarPagina(int position, String codigo, String nombre, String dependencia, String ubicacion, String telefono, String correo);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCambiarPaginaListenner) {
            mListener = (OnCambiarPaginaListenner) context;
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

    private void ponerTiposDeFuente() {
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
    }

    private void iniciarObjetos(View layout) {

        bold = Typeface.createFromAsset(getActivity().getAssets(), "opensans_bold.ttf");
        light = Typeface.createFromAsset(getActivity().getAssets(), "opensans_light.ttf");

        Button bt_validar = (Button) layout.findViewById(R.id.bt_guardar_reporte);
        bt_validar.setOnClickListener(new View.OnClickListener() {
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


                    mListener.onCambiarPagina(1, et_codigo.getText().toString(), et_nombre.getText().toString(),
                            et_dependencia.getText().toString(), et_ubicacion.getText().toString(), et_telefono.getText().toString(),
                            et_correo.getText().toString());
                }
            }
        });

        et_codigo = (EditText) layout.findViewById(R.id.et_codigo);
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

        et_nombre = (EditText) layout.findViewById(R.id.et_nombre);
        et_dependencia = (EditText) layout.findViewById(R.id.et_dependencia);
        et_ubicacion = (EditText) layout.findViewById(R.id.et_ubicacion);
        et_telefono = (EditText) layout.findViewById(R.id.et_telefono);
        et_correo = (EditText) layout.findViewById(R.id.et_correo);

        input_codigo = (TextInputLayout) layout.findViewById(R.id.input_codigo);
        input_nombre = (TextInputLayout) layout.findViewById(R.id.input_nombre);
        input_dependencia = (TextInputLayout) layout.findViewById(R.id.input_dependencia);
        input_ubicacion = (TextInputLayout) layout.findViewById(R.id.input_ubicacion);
        input_telefono = (TextInputLayout) layout.findViewById(R.id.input_telefono);
        input_correo = (TextInputLayout) layout.findViewById(R.id.input_correo);

        Incidente incidente = getActivity().getIntent().getParcelableExtra("INCIDENTE");
        if (incidente != null) {
            et_codigo.setText(incidente.getCodigoDelCliente());
        }
    }

    public boolean validarItems() {
        boolean isValid = true;
        if (et_codigo.getText().toString().length() == 0) {
            input_codigo.setError("Ingresa el código de la personal que solicita el servicio.");
            isValid = false;
        } else {
            input_codigo.setError(null);
        }

        if (et_nombre.getText().toString().length() == 0) {
            input_nombre.setError("Ingresa el nombre de la persona que solicita el servicio.");
            isValid = false;
        } else {
            input_nombre.setError(null);
        }

        if (et_dependencia.getText().toString().length() == 0) {
            input_dependencia.setError("Ingresa el nombre de la dependencia que solicita el servicio.");
            isValid = false;
        } else {
            input_dependencia.setError(null);
        }

        if (et_ubicacion.getText().toString().length() == 0) {
            input_ubicacion.setError("Ingresa la ubicación de la dependencia que solicita el servicio.");
            isValid = false;
        } else {
            input_ubicacion.setError(null);
        }

        if (et_telefono.getText().toString().length() == 0) {
            input_telefono.setError("Ingresa la extensión teléfonica de la persona que solicita el servicio.");
            isValid = false;
        } else {
            input_telefono.setError(null);
        }

        if (et_correo.getText().toString().length() == 0) {
            input_correo.setError("Ingresa el correo electronico de la persona que solicita el servicio.");
            isValid = false;
        } else {
            input_correo.setError(null);
        }
        return isValid;
    }


}
