package com.example.aplicacinjuzgadotfg;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{
    private List<Juicio> listadoJuicios;
    private Activity activity;
    public RecyclerAdapter(List<Juicio> listadoJuicios, Activity activity) {
        this.listadoJuicios = listadoJuicios;
        this.activity=activity;
    }


    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       //Indicamos el diseño del Holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.disenio_lista_juicios,parent, false);
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);
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
        holder.Id_Juicio.setText(juicio.getIdJuicio());
        holder.nombreJuez.setText(juicio.getNombreJuez());
        holder.nombreImputado.setText(juicio.getNombreImputado());
        holder.nombreAbogado.setText(juicio.getNombreAbogado());
        holder.fecha.setText(juicio.getFecha().toString());
    }

    /**
     * Metodo que obtiene el tamaño del List de mensajes
     * @return listadoMensajes.size()
     */

    @Override
    public int getItemCount() {
        return listadoJuicios.size();
    }

    /**
     * Obtenermos las vistas desde el layout
     */
    public class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView Id_Juicio;
        TextView nombreJuez;
        TextView nombreImputado;
        TextView nombreAbogado;
        TextView fecha;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);

            Id_Juicio = (TextView) itemView.findViewById(R.id.TxT_Juicio);
            nombreJuez = (TextView) itemView.findViewById(R.id.TxT_NombreJuez);
            nombreImputado = (TextView) itemView.findViewById(R.id.TxT_NombreImputado);
            nombreAbogado = (TextView) itemView.findViewById(R.id.TxT_Abogado);
            fecha = (TextView) itemView.findViewById(R.id.TxT_Fecha);
        }

}

}
