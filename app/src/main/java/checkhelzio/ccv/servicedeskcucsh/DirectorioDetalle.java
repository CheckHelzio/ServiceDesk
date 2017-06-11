package checkhelzio.ccv.servicedeskcucsh;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class DirectorioDetalle extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager pager;
    private TextView tv_titulo;
    private ImageView iv_logo;
    private DirectorioPagerAdapter directorioPagerAdapter;
    private int numeroDePaginas;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_directorio, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directorio_detalle);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_directorio_detalle));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pager = (ViewPager) findViewById(R.id.pager_directorio);
        tabLayout = (TabLayout) findViewById(R.id.tablayout_directorio);
        tv_titulo = (TextView) findViewById(R.id.tv_titulo);
        iv_logo = (ImageView) findViewById(R.id.logo);

        switch (getIntent().getStringExtra("DEPENDENCIA")) {
            case "Buscar":
                tabLayout.addTab(tabLayout.newTab().setText("Buscar"));
                tv_titulo.setText("Buscar en el directorio");
                tv_titulo.setGravity(Gravity.CENTER_VERTICAL);
                tabLayout.setVisibility(View.GONE);
                iv_logo.setVisibility(View.GONE);
                break;
            case "Rectoría":
                numeroDePaginas = 4;
                tv_titulo.setText("Rectoría");
                tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRectoria));
                iv_logo.setImageResource(R.drawable.ic_rectoria);
                break;
            case "División de Estudios Históricos y Humanos":
                numeroDePaginas = 5;
                tv_titulo.setText("División de Estudios Históricos y Humanos");
                tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorHistoricosHumanos));
                iv_logo.setImageResource(R.drawable.ic_historicos_humanos);
                break;
            case "División de Estudios de la Cultura":
                numeroDePaginas = 4;
                tv_titulo.setText("División de Estudios de la Cultura");
                tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorEstudiosCultura));
                iv_logo.setImageResource(R.drawable.ic_estudios_cultura);
                break;
            case "División de Estudios Jurídicos":
                numeroDePaginas = 4;
                tv_titulo.setText("División de Estudios Jurídicos");
                tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorEstudiosJuridicos));
                iv_logo.setImageResource(R.drawable.ic_estudios_juridicos);
                tv_titulo.setPadding(16, 0, 0, 0);
                break;
            case "Cátedras":
                tabLayout.setTabMode(TabLayout.MODE_FIXED);
                numeroDePaginas = 3;
                tv_titulo.setText("Cátedras");
                tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorCatedras));
                iv_logo.setImageResource(R.drawable.ic_catedras);
                break;
            case "División de Estudios Políticos y Sociales":
                numeroDePaginas = 5;
                tv_titulo.setText("División de Estudios Políticos y Sociales");
                tv_titulo.setPadding(16, 0, 0, 0);
                tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPoliticosSociales));
                iv_logo.setImageResource(R.drawable.ic_estudios_politicos);
                break;
            case "División de Estado Sociedad":
                numeroDePaginas = 5;
                tv_titulo.setText("División de Estado Sociedad");
                tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorEstadoSociedad));
                iv_logo.setImageResource(R.drawable.ic_estado_sociedad);
                break;
        }

        directorioPagerAdapter = new DirectorioPagerAdapter(getSupportFragmentManager(), numeroDePaginas + 1, getIntent().getStringExtra("DEPENDENCIA"));
        pager.setAdapter(directorioPagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }
}
