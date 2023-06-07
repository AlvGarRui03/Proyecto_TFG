package com.example.aplicacinjuzgadotfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    RecyclerView listaJuicios;
    ArrayList<Juicio> datosLista;
    RecyclerAdapter recAdapter;
    FirebaseFirestore db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listaJuicios = findViewById(R.id.RV_listaJuicio);
        datosLista = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        listaJuicios.setLayoutManager(layoutManager);
        recAdapter = new RecyclerAdapter(devolverLista(), this);

        listaJuicios.setAdapter(recAdapter);
        buscarJuicios();

    }

    public List devolverLista() {
        return datosLista;
    }

    public void actualizarLista() {
        recAdapter.notifyDataSetChanged();
    }

    public void buscarJuicios() {
        db = FirebaseFirestore.getInstance();
        db.collection("/Juicios").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("TAG", "onSuccess:");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            Log.e("TAG", "onSuccess: " + queryDocumentSnapshots.size());
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Log.e("TAG", "onSuccess: " + d.getId() + " => " + d.getData());
                                Juicio juicio = new Juicio(d.getString("idJuicio"),d.getString("nombreJuez"),d.getString("codigoJuez"),d.getString("nombreImputado"),d.getString("codigoImputado"),d.getString("nombreAbogado"),d.getString("codigoAbogado"),d.getString("sentencia"),d.getString("pruebas"),String.valueOf(d.getDate("fecha")));
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
}