package checkhelzio.ccv.servicedeskcucsh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class Portada extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("ActivityPortada", "ON CREATE");

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_portada);

        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences("SERVICE_DESK_CUCSH_PREFERENCES", Context.MODE_PRIVATE);
            String tecnico = sharedPreferences.getString("TECNICO", "FALSE");
            Intent i = null;
            if (tecnico.equals("FALSE")){
                i = new Intent(Portada.this, Loggin.class);
            }else {
                if ((getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK) ==
                        Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                    i = new Intent(Portada.this, ActivityMenuPrincipal.class);
                }else {
                    i = new Intent(Portada.this, ActivityListaIncidentes.class);
                }
            }
            startActivity(i);
            finish();
        }, 2000);
    }
}