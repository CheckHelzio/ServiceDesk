package checkhelzio.ccv.servicedeskcucsh;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;

import static checkhelzio.ccv.servicedeskcucsh.ActivityMenuPrincipal.listaIncidenteConsulta;
import static checkhelzio.ccv.servicedeskcucsh.ActivityMenuPrincipal.listaIndex;

public class FragmentListaIncidentes extends Fragment implements IncidenteAdapter.ListItemClickListener {

    private static final int CONSTANTE_NUEVO_INCIDENTE = 33;
    private static final int CONSTANTE_ELIMINAR_INCIDENTE = 34;
    private RecyclerView mRecyclerView;
    private IncidenteAdapter mIncidenteAdapter;
    private FloatingActionButton fab_mas;
    private FragmentDetalleIncidente fragmentDetalleIncidente;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_lista_incidentes, container, false);
        iniciarObjetos();
        return view;
    }

    private void iniciarObjetos() {

        // Iniciar objetos
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_incidentes);
        fab_mas = (FloatingActionButton) view.findViewById(R.id.fab_mas);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation());
        mDividerItemDecoration.setDrawable(getActivity().getResources().getDrawable(R.drawable.divisor, null));
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
    }

    private void checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            boolean wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            boolean mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifiConnected) {
                Intent i = new Intent(getActivity(), ActivityFormularioTablet.class);
                startActivityForResult(i, CONSTANTE_NUEVO_INCIDENTE);
            } else if (mobileConnected) {
                Snackbar.make(view.findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet.", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(view.findViewById(R.id.coordinador), "Hay un problema con la conexión a la base de datos. Verifica tu conexión a internet.", Snackbar.LENGTH_LONG).show();
        }
    }

    public void agregarIncidenteaLista(int x) {
        if (x == 0){
            mRecyclerView.scrollToPosition(0);
        }
        mIncidenteAdapter.notifyItemInserted(x);
    }

    @Override
    public void onListItemClick(int clickedItemIndex, View view) {
        if (listaIndex != clickedItemIndex){
            ActivityMenuPrincipal.listaIndex = clickedItemIndex;
            fragmentDetalleIncidente.reveal();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mIncidenteAdapter = new IncidenteAdapter(getActivity(), listaIncidenteConsulta, this);
        mRecyclerView.setAdapter(mIncidenteAdapter);

        if (ActivityMenuPrincipal.tecnico.getNivel() == 1) {
            fab_mas.setVisibility(View.GONE);
        } else {
            fab_mas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkNetworkConnection();
                }
            });
        }
    }

    public void notoficarCambios() {

        // MODIFICAR VISIBILIDAD DEL MONITO

        if (listaIncidenteConsulta.size() > 5) {
            try {
                view.findViewById(R.id.iv_trabajador).setVisibility(View.GONE);
                view.findViewById(R.id.iv_trabajador2).setVisibility(View.GONE);
            } catch (Exception ignored) {

            }
        } else {
            if (listaIncidenteConsulta.size() == 5) {
                try {
                    view.findViewById(R.id.iv_trabajador).setVisibility(View.GONE);
                    view.findViewById(R.id.iv_trabajador2).setVisibility(View.VISIBLE);
                } catch (Exception ignored) {

                }
            } else {
                try {
                    view.findViewById(R.id.iv_trabajador).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.iv_trabajador2).setVisibility(View.GONE);
                } catch (Exception ignored) {

                }
            }
        }

        if (ActivityMenuPrincipal.listaIndex == -1 && listaIncidenteConsulta.size() > 0) {
            fragmentDetalleIncidente = (FragmentDetalleIncidente) getActivity().getSupportFragmentManager().findFragmentById(R.id.detalleFragment);
            listaIndex = 0;
            fragmentDetalleIncidente.llenarDatos();
        } else if (listaIncidenteConsulta.size() != 0) {
            try {
                Log.v("FRAGMENTLISTAINCIDENTES", "COMPROBAR EXISTENCIA");
                fragmentDetalleIncidente.comprobarExistencia();
            } catch (Exception ignored) {
                fragmentDetalleIncidente = (FragmentDetalleIncidente) getActivity().getSupportFragmentManager().findFragmentById(R.id.detalleFragment);
                listaIndex = 0;
                fragmentDetalleIncidente.llenarDatos();
            }
        }
    }

    public void buscarItemParaEliminar(Integer folio) {
        mIncidenteAdapter.eliminar(folio);
    }
}
