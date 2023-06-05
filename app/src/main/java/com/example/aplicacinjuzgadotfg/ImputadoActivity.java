package com.example.aplicacinjuzgadotfg;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ImputadoActivity extends AppCompatActivity {
    private Context contexto = this;
    private String idImputado;
    private String nombre;
    private String dni;
    private EditText idImputadoText;
    private EditText nombreText;
    private EditText dniText;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imputado);
        int nightModeFlags = contexto.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
        idImputadoText = findViewById(R.id.TxT_IdAbogado);
        nombreText = findViewById(R.id.TxT_NombreAbogado);
        dniText = findViewById(R.id.TxT_DniAbogado);

    }
    public void crearImputado(View view){
    idImputado = idImputadoText.getText().toString();
    nombre = nombreText.getText().toString();
    dni = dniText.getText().toString();
        Map<String, Object> imputado = new HashMap<>();
        imputado.put("IdImputado", idImputado);
        imputado.put("Nombre", nombre);
        imputado.put("DNI", dni);


        db.collection("/Imputado")
                .add(imputado)
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