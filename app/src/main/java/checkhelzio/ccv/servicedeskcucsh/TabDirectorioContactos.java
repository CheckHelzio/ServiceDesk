package checkhelzio.ccv.servicedeskcucsh;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import checkhelzio.ccv.servicedeskcucsh.data.DirectorioContract;
import checkhelzio.ccv.servicedeskcucsh.data.DirectorioContract.DirectorioEntry;


public class TabDirectorioContactos extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, DirectorioAdapter.ListItemClickListener{

    private String mDependencia;
    private int mPosition;
    private DirectorioAdapter mDirectorioAdapter;
    private RecyclerView mRecyclerView;
    private ImageView iv_empty;

    public TabDirectorioContactos() {
        // Required empty public constructor
    }



    public static TabDirectorioContactos newInstance(int position, String dependencia) {
        TabDirectorioContactos fragment =  new TabDirectorioContactos();
        Bundle args = new Bundle();
        args.putInt("POSICION", position);
        args.putString("DEPENDENCIA", dependencia);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDependencia = getArguments().getString("DEPENDENCIA");
            mPosition = getArguments().getInt("POSICION");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_directorio_contactos, container, false);

        iv_empty = (ImageView) v.findViewById(R.id.empty_directorio);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_directorio);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mDirectorioAdapter = new DirectorioAdapter(getContext(), this);
        mRecyclerView.setAdapter(mDirectorioAdapter);
        Log.v("LOADER", "posicion 1: " + mPosition);
        getActivity().getSupportLoaderManager().initLoader(mPosition, null, this);
        actualizarEmptySate();
        return v;
    }


    private void actualizarEmptySate() {

        if (mRecyclerView.getAdapter().getItemCount() == 0) {
            iv_empty.setVisibility(View.VISIBLE);
        } else {
            iv_empty.setVisibility(View.GONE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String seleccion = null;
        String seleccionArgs [] = new String[0];

        switch (mDependencia) {
            case "Rectoría":
                switch (mPosition){
                    case 0:
                        seleccion = DirectorioEntry.COLUMN_DEPENDENCIA + "=?";
                        seleccionArgs = new String[] {String.valueOf(DirectorioEntry.DEPENDENCIA_RECTORIA)};
                        break;
                    case 1:
                        seleccion = DirectorioEntry.COLUMN_DEPENDENCIA + "=?" + " AND " + DirectorioEntry.COLUMN_SUBDEPENDENCIA + "=?";
                        seleccionArgs = new String[] {String.valueOf(DirectorioEntry.DEPENDENCIA_RECTORIA), String.valueOf(DirectorioEntry.SUBDEPENDENCIA_RECTORIA)};
                        break;
                    case 2:
                        seleccion = DirectorioEntry.COLUMN_DEPENDENCIA + "=?" + " AND " + DirectorioEntry.COLUMN_SUBDEPENDENCIA + "=?";
                        seleccionArgs = new String[] {String.valueOf(DirectorioEntry.DEPENDENCIA_RECTORIA), String.valueOf(DirectorioEntry.SUBDEPENDENCIA_SECRETARIA_ACADEMICA)};
                        break;
                    case 3:
                        seleccion = DirectorioEntry.COLUMN_DEPENDENCIA + "=?" + " AND " + DirectorioEntry.COLUMN_SUBDEPENDENCIA + "=?";
                        seleccionArgs = new String[] {String.valueOf(DirectorioEntry.DEPENDENCIA_RECTORIA), String.valueOf(DirectorioEntry.SUBDEPENDENCIA_SECRETARIA_ADMINISTRATIVA)};
                        break;
                    case 4:
                        seleccion = DirectorioEntry.COLUMN_DEPENDENCIA + "=?" + " AND " + DirectorioEntry.COLUMN_SUBDEPENDENCIA + "=?";
                        seleccionArgs = new String[] {String.valueOf(DirectorioEntry.DEPENDENCIA_RECTORIA), String.valueOf(DirectorioEntry.SUBDEPENDENCIA_CONTRALORIA)};
                        break;
                }
                break;
            case "División de Estudios Históricos y Humanos":
                break;
            case "División de Estudios de la Cultura":
                break;
            case "División de Estudios Jurídicos":
                break;
            case "Cátedras":
                break;
            case "División de Estudios Políticos y Sociales":
                break;
            case "División de Estado Sociedad":
                break;

        }

        String[] proyeccion = {
                DirectorioContract.DirectorioEntry._ID,
                DirectorioContract.DirectorioEntry.COLUMN_DEPENDENCIA,
                DirectorioContract.DirectorioEntry.COLUMN_TITULO,
                DirectorioContract.DirectorioEntry.COLUMN_NOMBRE,
                DirectorioContract.DirectorioEntry.COLUMN_APELLIDOS,
                DirectorioContract.DirectorioEntry.COLUMN_PUESTO,
                DirectorioContract.DirectorioEntry.COLUMN_TELEFONO,
                DirectorioContract.DirectorioEntry.COLUMN_CORREO
        };

        return new CursorLoader(
                getActivity(),
                DirectorioContract.DirectorioEntry.CONTENT_URI,
                proyeccion,
                seleccion,
                seleccionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.v("LOADER", "LOADER FINISH");

        if (mDirectorioAdapter != null) {
            Log.v("LOADER", "SWAP CURSOR");
            mDirectorioAdapter.swapCursor(data);
        }
        actualizarEmptySate();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onListItemClick(int clickedItemIndex, View view) {
        switch (view.getId()){
            case R.id.conte_correo:
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {((TextView)((RelativeLayout)view).getChildAt(2)).getText().toString().trim()});
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    getActivity().startActivity(intent);
                }
                break;
        }
    }
}
