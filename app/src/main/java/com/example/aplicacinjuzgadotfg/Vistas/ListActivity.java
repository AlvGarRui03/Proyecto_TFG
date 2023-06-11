package com.example.aplicacinjuzgadotfg.Vistas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import com.example.aplicacinjuzgadotfg.Modelos.Juicio;
import com.example.aplicacinjuzgadotfg.R;
import com.example.aplicacinjuzgadotfg.Controlador.RecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView listaJuicios;
    private ArrayList<Juicio> datosLista;
    private RecyclerAdapter recAdapter;
    private String juicioSeleccionado="";
    private String sentencia="";
    private SearchView searchView;
    private FirebaseFirestore db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listaJuicios = findViewById(R.id.RV_listaJuicio);
        searchView = findViewById(R.id.busquedaIdJuicio);
        //Añadimos el listener para cuando se escriba en el buscador
        searchView.setOnQueryTextListener(this);
        datosLista = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        listaJuicios.setLayoutManager(layoutManager);
        buscarJuicios();
        recAdapter = new RecyclerAdapter(devolverLista(), this);
        listaJuicios.setAdapter(recAdapter);
        //Añadimos el listener para cuando se pulse en un elemento de la lista
        recAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                juicioSeleccionado = datosLista.get(listaJuicios.getChildAdapterPosition(view)).getIdJuicio();
                sentencia = datosLista.get(listaJuicios.getChildAdapterPosition(view)).getSentencia();
                Intent intent = new Intent(ListActivity.this, DatosExtendidosActivity.class);
                intent.putExtra("idJuicio", juicioSeleccionado);
                intent.putExtra("Sentencia",sentencia);
                startActivity(intent);
            }
        });


    }

    /**
     * Método que devuelve la lista de juicios
     *
     * @return list
     */
    public List devolverLista() {
        return datosLista;
    }

    /**
     * Metodo que notifica al adaptador que se ha actualizado la lista
     */
    public void actualizarLista() {
        recAdapter.notifyDataSetChanged();
    }

    /**
     * Método que busca todos los juicios en la base de datos
     */
    public void buscarJuicios() {
        //Obtenemos la instancia de la base de datos
        db = FirebaseFirestore.getInstance();
        //Obtenemos todos los juicios de la base de datos
        db.collection("/Juicios").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("TAG", "onSuccess:");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            Log.e("TAG", "onSuccess: " + queryDocumentSnapshots.size());
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Juicio juicio = new Juicio(d.getString("idJuicio"),d.getString("nombreJuez"),d.getString("codigoJuez"),d.getString("nombreImputado"),d.getString("codigoImputado"),d.getString("nombreAbogado"),d.getString("codigoAbogado"),d.getString("sentencia"),d.getString("pruebas"),new SimpleDateFormat("dd-MM-yyyy HH:mm").format(d.getDate("fecha")));
                                datosLista.add(juicio);
                            }
                        } else {
                            Log.e("TAG", "onSuccess: LIST EMPTY");
                            System.out.println("No data found in Database");
                        }
                        actualizarLista();
                    }
                });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    /**
     * Método que filtra la lista de juicios según lo que se escriba en el buscador
     * @param newText
     * @return false
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        recAdapter.getFilter().filter(newText);
        return false;
    }
}