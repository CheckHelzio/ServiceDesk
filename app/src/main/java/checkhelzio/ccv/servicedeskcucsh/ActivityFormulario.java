package checkhelzio.ccv.servicedeskcucsh;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pixelcan.inkpageindicator.InkPageIndicator;

import checkhelzio.ccv.servicedeskcucsh.HTextView.HTextView;
import checkhelzio.ccv.servicedeskcucsh.HTextView.ScaleTextView;

import static checkhelzio.ccv.servicedeskcucsh.R.id.indicator;

public class ActivityFormulario extends AppCompatActivity implements ViewPager.OnPageChangeListener, DatosPersonalesFragment.OnCambiarPaginaListenner, DatosDelReporteFragment.RegistroListoListenner, DatosDelReporteFragment.CambiarPaginaListenner, DatosDelReporteFragment.EditarIncidenteListoListenner {

    ViewPager pager;
    ScaleTextView mysubTextView;
    Typeface bold, light;
    private InkPageIndicator inkPageIndicator;
    private FormularioPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        iniciarObjetos();
        ponerTiposDeFuente();
        iniciarPager();
    }

    private void iniciarObjetos() {

        bold = Typeface.createFromAsset(getAssets(), "opensans_bold.ttf");
        light = Typeface.createFromAsset(getAssets(), "opensans_light.ttf");

        inkPageIndicator = (InkPageIndicator) findViewById(indicator);
        mysubTextView = (ScaleTextView) findViewById(R.id.tv_subtitulo);
        pager = (ViewPager) findViewById(R.id.pager);

        Log.v("INTENT", getCallingActivity().getClassName());
    }

    private void ponerTiposDeFuente() {
        mysubTextView.setTypeface(bold);
    }

    private void iniciarPager() {
        adapter = new FormularioPageAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(this);
        inkPageIndicator.setViewPager(pager);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            mysubTextView.animateText("DATOS PERSONALES");
        } else {
            mysubTextView.animateText("INCIDENTE");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCambiarPagina(int position, String codigo, String nombre, String dependencia, String ubicacion, String telefono, String correo) {
        Fragment fragment = adapter.getFragment(1);
        assert fragment != null;
        ((DatosDelReporteFragment) fragment).pasarDatosPersonales(codigo, nombre, dependencia, ubicacion, telefono, correo);
        pager.setCurrentItem(position, true);
    }

    @Override
    public void registroListo() {
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void cambiarPagina(int position) {
        pager.setCurrentItem(position, true);
    }

    @Override
    public void editarIncidenteListo() {
        finish();
    }
}
