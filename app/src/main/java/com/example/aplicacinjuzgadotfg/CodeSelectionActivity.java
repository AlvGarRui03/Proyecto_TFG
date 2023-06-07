package com.example.aplicacinjuzgadotfg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CodeSelectionActivity extends AppCompatActivity {
    private Button botonPersonas;
    private ArrayList<String> listaidJueces;
    private FirebaseFirestore db;
    private EditText codigoJuicio;
    private String idJuicio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_selection);
        listaidJueces = new ArrayList<>();
        botonPersonas = findViewById(R.id.BT_ContinuarPersonas);
        codigoJuicio = findViewById(R.id.TxT_CodJuicio);
        botonPersonas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionPersonas();
            }
        });
        buscaridJuicios();

    }

    /**
     * Método que se ejecuta al pulsar el botón de continuar, se encarga de cargar la actividad
     * de selección de personas
     *
     *
     */
    public void seleccionPersonas() {
        Intent intent = new Intent(this, PeopleSelectionActivity.class);
        idJuicio= codigoJuicio.getText().toString();
        if (idJuicio.trim().length() > 0) {
            if(listaidJueces.contains(idJuicio.trim().toLowerCase(Locale.ROOT))){
                Toast.makeText(this, "El código de juicio ya existe, elija otro código", Toast.LENGTH_SHORT).show();
            }else{
                intent.putExtra("idJuicio", idJuicio);
                startActivity(intent);
            }

            } else {
                Toast.makeText(this, "Introduzca un código de juicio", Toast.LENGTH_SHORT).show();
            }


    }

    public void buscaridJuicios() {
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
                                listaidJueces.add(d.getString("idJuicio").trim().toLowerCase(Locale.ROOT));
                            }
                        } else {
                            Log.e("TAG", "onSuccess: LIST EMPTY");
                            System.out.println("No data found in Database");
                        }
                    }
                });
                botonPersonas.setClickable(true);
    }

}