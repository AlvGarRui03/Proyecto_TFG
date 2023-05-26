package com.example.aplicacinjuzgadotfg;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.List;

public class PeopleSelectionActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private ArrayList<Juez> jueces;
    private Spinner listaJueces;
    private Thread hiloObtencionDatos = null;
    private ArrayList<String> nombresJueces;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_selection);
        listaJueces = findViewById(R.id.Spinner_Jueces);
        jueces = new ArrayList<>();
        nombresJueces = new ArrayList<>();
        buscarDatos();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nombresJueces);
        listaJueces.setAdapter(adapter);


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
        Log.e("TAG", "buscarDatos:" );
        db.collection("/Juez").get()
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
                                jueces.add(juez);
                                System.out.println("Tamaño de la lista: " + jueces.size());
                                nombresJueces.add(juez.getNombreCompleto());

                                //Juez j = d.toObject(Juez.class);
                               // Log.e("TAG", "onSuccess: " + j.getNombreCompleto() );
                                /*System.out.println(j.getNombreCompleto());
                                System.out.println(j.getDni());
                                System.out.println(j.getIdJuez());*/
                            }
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Log.e("TAG", "onSuccess: LIST EMPTY" );
                            System.out.println("No data found in Database");
                        }
                    }
                });
        /*db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });*/


    }



    }


