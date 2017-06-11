package checkhelzio.ccv.servicedeskcucsh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;


public class IncidenteAdapter extends RecyclerView.Adapter<IncidenteAdapter.IncidenteViewHolder> {

    private ArrayList<Incidente> mListaIncidente;
    private Calendar c;
    final private ListItemClickListener mOnClcikListener;
    private Context mContext;

    public void eliminar(Integer folio) {
        Log.v("ActivityMenuPrincipal", "INCIDENTE ELIMINADO DETECTADO 4");
        int x = 0;
        for (Incidente i : mListaIncidente){
            if (i.getFolioDelReporte() == folio){
                mListaIncidente.remove(x);
                notifyItemRemoved(x);
                break;
            }
            x++;
        }
    }

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex, View view);
    }

    public IncidenteAdapter(Context context, ArrayList<Incidente> listaIncidente, ListItemClickListener listener) {
        mListaIncidente = listaIncidente;
        mOnClcikListener = listener;
        mContext = context;
    }

    @Override
    public IncidenteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.incidente_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new IncidenteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IncidenteViewHolder holder, int position) {
        holder.bind(position);
    }

    private String getHora() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(c.getTime());
    }

    private String getFecha(long fechayHora) {
        c = Calendar.getInstance();
        c.setTimeInMillis(fechayHora);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(c.getTime());
    }

    private int getFondoPrioridad(int prioridadDelServicio) {
        switch (prioridadDelServicio) {
            case 1:
                return R.drawable.prioridad_baja;
            case 2:
                return R.drawable.prioridad_media;
            case 3:
                return R.drawable.prioridad_alta;
        }
        return 0;
    }

    private String getStringPrioridad(int prioridadDelServicio) {
        switch (prioridadDelServicio) {
            case 1:
                return "B";
            case 2:
                return "M";
            case 3:
                return "A";
        }
        return null;
    }

    private String getStringStatus(int int_status) {
        switch (int_status) {
            case 0:
                return "Asignado";
            case 1:
                return "En progreso";
            case 2:
                return "En espera";
            case 3:
                return "Finalizado";
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mListaIncidente.size();
    }

    class IncidenteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_prioridad, tv_folio, tv_asignado, tv_estatus, tv_fecha, tv_hora;

        public IncidenteViewHolder(View itemView) {
            super(itemView);
            tv_prioridad = (TextView) itemView.findViewById(R.id.tv_prioridad);
            tv_folio = (TextView) itemView.findViewById(R.id.tv_folio);
            tv_asignado = (TextView) itemView.findViewById(R.id.tv_asignado);
            tv_estatus = (TextView) itemView.findViewById(R.id.tv_estatus);
            tv_fecha = (TextView) itemView.findViewById(R.id.tv_fecha);
            tv_hora = (TextView) itemView.findViewById(R.id.tv_hora);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            String folio = String.format(Locale.getDefault(), "%03d", mListaIncidente.get(position).getFolioDelReporte());
            tv_prioridad.setBackgroundResource(getFondoPrioridad(mListaIncidente.get(position).getPrioridadDelServicio()));
            tv_folio.setText(folio);
            tv_asignado.setText(new Tecnico(mListaIncidente.get(position).getCodigoDelTecnicoAsignado()).getNombreCompletoDelTecnico());
            tv_estatus.setText(getStringStatus(mListaIncidente.get(position).getStatusDeTerminacionDelReporte()));
            tv_fecha.setText(getFecha(mListaIncidente.get(position).getFechaYHoraDelReporte()));
            tv_hora.setText(getHora());
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClcikListener.onListItemClick(clickedPosition, view);

        }

        private int randomInt(int min, int max) {
            Random rand = new Random();
            return min + rand.nextInt((max - min) + 1);
        }

    }
}
