package com.example.aplicacinjuzgadotfg.Vistas;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplicacinjuzgadotfg.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class JuezActivity extends AppCompatActivity {
    private String idJuez;
    private String nombre;
    private String dni;
    private ArrayList<String> codigosJueces = new ArrayList<String>();
    private EditText idJuezText;
    private EditText nombreText;
    private EditText dniText;
    private String idJuicio ="";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juez);
        idJuicio = getIntent().getStringExtra("idJuicio");
        idJuezText = findViewById(R.id.TxT_IdAbogado);
        nombreText = findViewById(R.id.TxT_NombreAbogado);
        dniText = findViewById(R.id.TxT_DniAbogado);
        buscarJuez();
    }

    /**
     * Método que crea un nuevo juez si se cumplen las condiciones y pasa a la anterior actividad
     *
     * @param view the view
     */
    public void creacionJuez(View view){

        idJuez = idJuezText.getText().toString();
        nombre = nombreText.getText().toString();
        dni = dniText.getText().toString();
        if(codigosJueces.contains(idJuez.trim().toLowerCase(Locale.ROOT)) || idJuez.trim().length()==0){
            Toast.makeText(this, "Ese código de juez esta en uso o vacío", Toast.LENGTH_SHORT).show();
       }else {
            if(dni.length()!=9){
                Toast.makeText(this, "El DNI debe tener 9 caracteres", Toast.LENGTH_SHORT).show();
            }else {
                if(nombre.trim().length()==0){
                    Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                }else{
                    //Se crea una Map con la clave y el objeto a añadir
                Map<String, Object> juez = new HashMap<>();
                juez.put("idJuez", idJuez);
                juez.put("Nombre", nombre);
                juez.put("DNI", dni);

                //Se añade la coleccion a la base de datos
                db.collection("/Juez")
                        .add(juez)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
                Intent intent = new Intent(this, PeopleSelectionActivity.class);
                intent.putExtra("idJuicio", idJuicio);
                startActivity(intent);
            }
            }
        }
    }

    /**
     * Método que busca los jueces en la base de datos
     */
    public void buscarJuez(){
        db.collection("/Juez").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                codigosJueces.add(documentSnapshot.getString("idJuez").trim().toLowerCase(Locale.ROOT));
                            }
                    }
                });
    }

}