package com.example.aplicacinjuzgadotfg;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class JuezActivity extends AppCompatActivity {
    private String idJuez;
    private String nombre;
    private String dni;
    private EditText idJuezText;
    private EditText nombreText;
    private EditText dniText;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juez);
        idJuezText = findViewById(R.id.TxT_IdAbogado);
        nombreText = findViewById(R.id.TxT_NombreAbogado);
        dniText = findViewById(R.id.TxT_DniAbogado);
    }
    public void creacionJuez(View view){
        idJuez = idJuezText.getText().toString();
        nombre = nombreText.getText().toString();
        dni = dniText.getText().toString();
        Map<String, Object> juez = new HashMap<>();
        juez.put("idJuez", idJuez);
        juez.put("Nombre", nombre);
        juez.put("DNI", dni);


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
        startActivity(intent);
    }
}