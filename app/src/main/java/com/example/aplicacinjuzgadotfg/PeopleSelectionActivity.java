package com.example.aplicacinjuzgadotfg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PeopleSelectionActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private ArrayList<String> nombresJueces;
    private ArrayList<String> nombresImputados;
    private ArrayList<String> nombresAbogados;
    private Spinner listaJueces;
    private Spinner listaImputados;
    private Spinner listaAbogados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_selection);
        listaJueces = findViewById(R.id.Spinner_Jueces);
        listaImputados = findViewById(R.id.Spinner_Imputados);
        listaAbogados = findViewById(R.id.Spinner_Abogado);
        nombresJueces = new ArrayList<>();
        nombresImputados = new ArrayList<>();
        nombresAbogados = new ArrayList<>();
        buscarDatos();
        ArrayAdapter<String> adapterJueces = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nombresJueces);
        listaJueces.setAdapter(adapterJueces);
        ArrayAdapter<String> adapterImputados = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nombresImputados);
        listaImputados.setAdapter(adapterImputados);
        ArrayAdapter<String> adapterAbogados = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nombresAbogados);
        listaAbogados.setAdapter(adapterAbogados);

        /*hiloObtencionDatos = new Thread(new Runnable() {
            @Override
            public void run() {
                db = FirebaseFirestore.getInstance();
                db.collection("/Juez").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        jueces = new ArrayList<>();
                        for (int i = 0; i < task.getResult().size(); i++) {
                            jueces.add(new Juez(task.getResult().getDocuments().get(i).getId(), task.getResult().getDocuments().get(i).get("nombreCompleto").toString(), task.getResult().getDocuments().get(i).get("dni").toString()));
                        }
                    }
                });
                for (Juez juez : jueces) {
                    System.out.println(juez.getNombreCompleto());
                }
            }
        });
        hiloObtencionDatos.start();*/


    }
    public void buscarDatos(){
        db = FirebaseFirestore.getInstance();
        buscarJueces();
        buscarInputados();
        buscarAbogados();
    }
    public void buscarJueces(){
        db.collection("/Juez").orderBy("Nombre").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("TAG", "onSuccess:" );
                        if (!queryDocumentSnapshots.isEmpty()) {
                            Log.e("TAG", "onSuccess: " + queryDocumentSnapshots.size() );
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Log.e("TAG","onSuccess: " + d.getId() + " => " + d.getData());
                                System.out.println(d.getString("Nombre"));
                                Juez juez = new Juez(d.getId(), d.getString("Nombre"), d.getString("DNI"));
                                System.out.println("ID: " + juez.getIdJuez() + " Nombre: " + juez.getNombreCompleto() + " DNI: " + juez.getDni());
                                nombresJueces.add(juez.getNombreCompleto());
                            }
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Log.e("TAG", "onSuccess: LIST EMPTY" );
                            System.out.println("No data found in Database");
                        }
                    }
                });
    }
    public void buscarInputados(){
        db.collection("/Imputado").orderBy("Nombre").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("TAG", "onSuccess:" );
                        if (!queryDocumentSnapshots.isEmpty()) {
                            Log.e("TAG", "onSuccess: " + queryDocumentSnapshots.size() );
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Log.e("TAG","onSuccess: " + d.getId() + " => " + d.getData());
                                System.out.println(d.getString("Nombre"));
                                Imputado imputado = new Imputado (d.getId(), d.getString("Nombre"), d.getString("DNI"));
                                System.out.println("ID: " + imputado.getIdImputado() + " Nombre: " + imputado.getNombreCompleto() + " DNI: " + imputado.getDni());
                                nombresImputados.add(imputado.getNombreCompleto());
                            }
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Log.e("TAG", "onSuccess: LIST EMPTY" );
                            System.out.println("No data found in Database");
                        }
                    }
                });
    }
    public void buscarAbogados(){
        db.collection("/Abogado").orderBy("Nombre").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("TAG", "onSuccess:" );
                        if (!queryDocumentSnapshots.isEmpty()) {
                            Log.e("TAG", "onSuccess: " + queryDocumentSnapshots.size() );
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Log.e("TAG","onSuccess: " + d.getId() + " => " + d.getData());
                                System.out.println(d.getString("Nombre"));
                                Abogado abogado = new Abogado (d.getId(), d.getString("Nombre"), d.getString("Tipo"),d.getString("DNI"));
                                System.out.println("ID: " + abogado.getIdAbogado() + " Nombre: " + abogado.getNombre() + " DNI: " + abogado.getDni() + " Tipo: " + abogado.getTipo());
                                nombresAbogados.add(abogado.getNombre());
                            }
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Log.e("TAG", "onSuccess: LIST EMPTY" );
                            System.out.println("No data found in Database");
                        }
                    }
                });
    }
    public void crearJuez(View view){
        Intent intent = new Intent(this, JuezActivity.class);
        startActivity(intent);
    }
    public void crearImputado(View view){
        Intent intent = new Intent(this, ImputadoActivity.class);
        startActivity(intent);
    }
    public void crearAbogado(View view){
        Intent intent = new Intent(this, AbogadoActivity.class);
        startActivity(intent);
    }
    }


