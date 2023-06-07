package com.example.aplicacinjuzgadotfg.Controlador;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicacinjuzgadotfg.Modelos.Juicio;
import com.example.aplicacinjuzgadotfg.R;

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
    /**
     * Constructor del Holder
     */
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       //Indicamos el diseño del Holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_juicios_design,parent, false);
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);
        //Añadimos el listener para cuando se pulse en un elemento de la lista
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

    /**
     * Metodo que nos devuelve el listado de juicios
     * @return listadoJuicios.size()
     */
    @Override
    public int getItemCount() {
        return listadoJuicios.size();
    }

    /**
     * Filtro de busqueda de juicios
     * @return resultado
     */
    @Override
    public Filter getFilter() {
        //Creamos un filtro para la busqueda de juicios
        Filter filtro = new Filter() {
            @Override
            //Metodo que nos devuelve los resultados de la busqueda
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults resultado = new FilterResults();
                //Si no hay nada escrito en el buscador, se muestran todos los juicios
                if (constraint == null || constraint.length() == 0) {
                    resultado.count = todosJuicios.size();
                    resultado.values = todosJuicios;
                } else {
                    //Si hay algo escrito en el buscador, se muestran los juicios que contengan lo escrito
                    String busqueda = constraint.toString().toLowerCase();
                    List<Juicio> juicios = new ArrayList<>();
                    for(Juicio juicio : todosJuicios){
                        if(juicio.getIdJuicio().toLowerCase().contains(busqueda)){
                            juicios.add(juicio);
                        }
                    }
                    //Se devuelven los resultados de la busqueda
                    resultado.count = juicios.size();
                    resultado.values = juicios;
                    }

                return resultado;
            }




            @Override
            //Metodo que nos devuelve los resultados de la busqueda
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listadoJuicios = (List<Juicio>) results.values;
                //Se notifica al adaptador que se han producido cambios en los datos
                notifyDataSetChanged();
            }
        };
        return filtro;
    }
    //Metodo que nos permite asignar un listener al adaptador
    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    //Listener de onClick
    public void onClick(View v) {
        if(listener!=null){
                listener.onClick(v);


        }
    }

    /**
     * Clase que nos permite crear el Holder
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
            nombreJuez = (TextView) itemView.findViewById(R.id.TxT_Juez);
            nombreImputado = (TextView) itemView.findViewById(R.id.TxT_Imputado);
            nombreAbogado = (TextView) itemView.findViewById(R.id.TxT_Abogado);
            fecha = (TextView) itemView.findViewById(R.id.TxT_Fecha);
        }

}

}
