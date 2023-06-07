package com.example.aplicacinjuzgadotfg;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> implements Filterable, View.OnClickListener{
    private List<Juicio> listadoJuicios;
    private Activity activity;
    private List<Juicio> todosJuicios;
    private View.OnClickListener listener;
    public RecyclerAdapter(List<Juicio> listadoJuicios, Activity activity) {
        this.listadoJuicios = listadoJuicios;
        this.activity=activity;
        this.todosJuicios = listadoJuicios;

    }


    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       //Indicamos el diseño del Holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_juicios_design,parent, false);
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);
        view.setOnClickListener(this);
        return recyclerHolder;
    }

    /**
     * Metodo que nos ayuda marcar los datos que irán dentro del RecyclerView
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        Juicio juicio = listadoJuicios.get(position);
        holder.Id_Juicio.setText("ID: " + juicio.getIdJuicio());
        holder.nombreJuez.setText("Juez: " + juicio.getNombreJuez());
        holder.nombreImputado.setText("Imputado: " + juicio.getNombreImputado());
        holder.nombreAbogado.setText("Abogado: " + juicio.getNombreAbogado());
        holder.fecha.setText("Fecha: " + juicio.getFecha().toString());
    }
    @Override
    public int getItemCount() {
        return listadoJuicios.size();
    }

    @Override
    public Filter getFilter() {
        Filter filtro = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults resultado = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    resultado.count = todosJuicios.size();
                    resultado.values = todosJuicios;

                } else {
                    String busqueda = constraint.toString().toLowerCase();
                    List<Juicio> juicios = new ArrayList<>();
                    for(Juicio juicio : todosJuicios){
                        if(juicio.getIdJuicio().toLowerCase().contains(busqueda)){
                            juicios.add(juicio);
                        }
                    }
                    resultado.count = juicios.size();
                    resultado.values = juicios;
                    }

                return resultado;
            }




            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listadoJuicios = (List<Juicio>) results.values;
                notifyDataSetChanged();
            }
        };
        return filtro;
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
                listener.onClick(v);


        }
    }


    public class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView Id_Juicio;
        TextView nombreJuez;
        TextView nombreImputado;
        TextView nombreAbogado;
        TextView fecha;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);

            Id_Juicio = (TextView) itemView.findViewById(R.id.TxT_Juicio);
            nombreJuez = (TextView) itemView.findViewById(R.id.TxT_Juez);
            nombreImputado = (TextView) itemView.findViewById(R.id.TxT_Imputado);
            nombreAbogado = (TextView) itemView.findViewById(R.id.TxT_Abogado);
            fecha = (TextView) itemView.findViewById(R.id.TxT_Fecha);
        }

}

}
