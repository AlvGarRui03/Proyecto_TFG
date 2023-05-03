package com.example.aplicacinjuzgadotfg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class PeopleSelectionActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private ArrayList<Juez> jueces;
    private Spinner listaJueces;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_selection);
        listaJueces = findViewById(R.id.Spinner_Jueces);
        db = FirebaseFirestore.getInstance();
        db.collection("/Juez").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                jueces = new ArrayList<>();
                for (int i = 0; i < task.getResult().size(); i++) {
                    jueces.add(new Juez(task.getResult().getDocuments().get(i).getId(), task.getResult().getDocuments().get(i).get("nombreCompleto").toString(), task.getResult().getDocuments().get(i).get("dni").toString()));
                }
            }
        });
        for(Juez juez : jueces){
            listaJueces.setAdapter((SpinnerAdapter) juez);
            System.out.println(juez.getNombreCompleto());

        }

    }
}
