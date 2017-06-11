package checkhelzio.ccv.servicedeskcucsh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Text;

public class Loggin extends AppCompatActivity implements View.OnClickListener {

    private EditText et_usuario;
    private EditText et_contraseña;
    private TextInputLayout input_usuario;
    private TextInputLayout input_contraseña;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin2);

        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        prefs = getSharedPreferences("SERVICE_DESK_CUCSH_PREFERENCES", Context.MODE_PRIVATE);

        et_usuario = (EditText) findViewById(R.id.et_usuario);
        input_usuario = (TextInputLayout) findViewById(R.id.input_usuario);
        input_contraseña = (TextInputLayout) findViewById(R.id.input_contraseña);
        et_contraseña = (EditText) findViewById(R.id.et_contraseña);
        Button bt_iniciar_sesion = (Button) findViewById(R.id.bt_iniciar_sesion);

        bt_iniciar_sesion.setOnClickListener(this);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "opensans_light.ttf");
        Typeface myTypeface_bold = Typeface.createFromAsset(getAssets(), "opensans_bold.ttf");
        input_usuario.setTypeface(myTypeface);
        input_contraseña.setTypeface(myTypeface);
        et_contraseña.setTypeface(myTypeface_bold);
        et_usuario.setTypeface(myTypeface_bold);
        bt_iniciar_sesion.setTypeface(myTypeface_bold);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_iniciar_sesion:
                switch (et_usuario.getText().toString()){
                    case "207509057": // ADMIN
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("1313")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "005107962": // HIRAM PEÑA FRANCO
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("1960")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "207512147": // OSCAR MÉNDEZ HERNÁNDEZ
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("2911")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "01290713": // ZYANYA LÓPEZ DÍAZ
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("0000")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "210092965": // ARIANA ISABEL PÉREZ ALVAREZ
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("0000")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "2806533": // IDANIA GOMEZ COSIO
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("0000")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "09537805": // JUAN ALBERTO ESTRADA MANCILLA
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("0000")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "2528894": // Hector Aceves Shimizu Lopez
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("0000")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "2921073": // Eduardo Solano Guzmán
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("0000")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "2906163": // Jonathan Josue De Leon Barajas
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("0000")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "2417626": // Omar Alberto Andrade Muñoz
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("0000")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "2520761": // Octavio Cortazar Rodríguez
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("0000")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "2915286": // Jesus Enrique Vega Coronel
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("0000")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "2946467": // Eduardo Antonino García Salazar
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("1299")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "2952470": // Emanuel Gregorio Chacon Vera
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("2371")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "2954064": // Roberto Carlos Villaseñor Rodriguez
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("0000")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "2957917": // Jose Gerardo Carrillo Manriquez
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("0000")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "9102248": // Jorge Adán Plascencia Perez
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("0000")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "9103031": // Gladys Veronica Tabares Torres
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("0000")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "9319743": // Maria Clara Cuellar Espinosa
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("0000")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "9715371": // Ricardo Cortes De la O
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("0000")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    case "9811621": // Teresa De los Santos López
                        input_usuario.setError(null);
                        if (et_contraseña.getText().toString().equals("0000")){
                            logginCorrecto();
                        }else {
                            input_contraseña.setError("Contraseña inválida");
                        }
                        break;
                    default:
                        input_usuario.setError("Usuario inválido");
                        input_contraseña.setError("Contraseña inválida");
                        break;
                }
                break;
        }
    }

    private void logginCorrecto() {
        input_contraseña.setError(null);
        prefs.edit().putString("TECNICO", et_usuario.getText().toString()).apply();
        Intent i;

        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            i = new Intent(this, ActivityMenuPrincipal.class);
        }else {
            i = new Intent(this, ActivityListaIncidentes.class);
        }

        startActivity(i);
        finish();
    }
}
