package checkhelzio.ccv.servicedeskcucsh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ActivityDirectorio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directorio);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar_directorio));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDirectorio.this, DirectorioDetalle.class);
                switch (v.getId()){
                    case R.id.card_buscar:
                        intent.putExtra("DEPENDENCIA", "Buscar");
                        break;
                    case R.id.card_rectoria:
                        intent.putExtra("DEPENDENCIA", "Rectoría");
                        break;
                    case R.id.card_historicos_humanos:
                        intent.putExtra("DEPENDENCIA", "División de Estudios Históricos y Humanos");
                        break;
                    case R.id.card_estudios_cultura:
                        intent.putExtra("DEPENDENCIA", "División de Estudios de la Cultura");
                        break;
                    case R.id.card_estudios_juridicos:
                        intent.putExtra("DEPENDENCIA", "División de Estudios Jurídicos");
                        break;
                    case R.id.card_catedras:
                        intent.putExtra("DEPENDENCIA", "Cátedras");
                        break;
                    case R.id.card_politicos_sociales:
                        intent.putExtra("DEPENDENCIA", "División de Estudios Políticos y Sociales");
                        break;
                    case R.id.card_estado_sociedad:
                        intent.putExtra("DEPENDENCIA", "División de Estado Sociedad");
                        break;

                }
                startActivity(intent);
            }
        };

        findViewById(R.id.card_buscar).setOnClickListener(onClickListener);
        findViewById(R.id.card_rectoria).setOnClickListener(onClickListener);
        findViewById(R.id.card_historicos_humanos).setOnClickListener(onClickListener);
        findViewById(R.id.card_estudios_cultura).setOnClickListener(onClickListener);
        findViewById(R.id.card_estudios_juridicos).setOnClickListener(onClickListener);
        findViewById(R.id.card_catedras).setOnClickListener(onClickListener);
        findViewById(R.id.card_politicos_sociales).setOnClickListener(onClickListener);
        findViewById(R.id.card_estado_sociedad).setOnClickListener(onClickListener);
    }
}
