package checkhelzio.ccv.servicedeskcucsh;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import checkhelzio.ccv.servicedeskcucsh.data.DirectorioContract;


public class DirectorioAdapter extends RecyclerView.Adapter<DirectorioAdapter.DirectorioViewHolder> {

    private Cursor lista;
    private Context mContext;
    final private ListItemClickListener mOnClcikListener;

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex, View view);
    }

    public DirectorioAdapter(Context context, ListItemClickListener listItemClickListener) {
        mContext = context;
        mOnClcikListener = listItemClickListener;
    }

    @Override
    public DirectorioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.directorio_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new DirectorioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DirectorioViewHolder holder, int position) {
        Log.v("BIND", "POSITION: " + position);
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        if (lista != null) {
            return lista.getCount();
        }
        return 0;
    }

    public void swapCursor(Cursor nuevoCursor) {
        if (nuevoCursor != null) {
            lista = nuevoCursor;
            notifyDataSetChanged();
        }
    }

    class DirectorioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_nombre, tv_puesto, tv_telefono, tv_correo;

        public DirectorioViewHolder(View itemView) {
            super(itemView);
            tv_nombre = (TextView) itemView.findViewById(R.id.tv_nombre);
            tv_puesto = (TextView) itemView.findViewById(R.id.tv_puesto);
            tv_telefono = (TextView) itemView.findViewById(R.id.tv_telefono);
            tv_correo = (TextView) itemView.findViewById(R.id.tv_correo);

            itemView.findViewById(R.id.conte_correo).setOnClickListener(this);
        }

        public void bind(int position) {
            lista.moveToPosition(position);
            String s;

            s = lista.getString(lista.getColumnIndex(DirectorioContract.DirectorioEntry.COLUMN_TITULO)) + " " +
                    lista.getString(lista.getColumnIndex(DirectorioContract.DirectorioEntry.COLUMN_NOMBRE)) + " " +
                    lista.getString(lista.getColumnIndex(DirectorioContract.DirectorioEntry.COLUMN_APELLIDOS));
            tv_nombre.setText(s);

            s = lista.getString(lista.getColumnIndex(DirectorioContract.DirectorioEntry.COLUMN_PUESTO));
            tv_puesto.setText(s);

            s = lista.getString(lista.getColumnIndex(DirectorioContract.DirectorioEntry.COLUMN_TELEFONO));
            tv_telefono.setText(s);

            s = lista.getString(lista.getColumnIndex(DirectorioContract.DirectorioEntry.COLUMN_CORREO));
            tv_correo.setText(s);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClcikListener.onListItemClick(clickedPosition, v);
        }
    }
}
