package com.example.aplicacinjuzgadotfg.Vistas;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

public class ImputadoActivity extends AppCompatActivity {
    private Context contexto = this;
    private String idImputado;
    private ArrayList<String> codigosImputado = new ArrayList<String>();
    private String nombre;
    private String dni ="";
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
        buscarImputado();

    }
    /**
     * Método que añade los imputados a la base de datos y vuelve a la anterior actividad
     */
    public void crearImputado(View view){
    idImputado = idImputadoText.getText().toString();
    nombre = nombreText.getText().toString();
    dni = dniText.getText().toString();
        if(codigosImputado.contains(idImputado.trim().toLowerCase(Locale.ROOT)) || idImputado.trim().length()==0){
            Toast.makeText(this, "El id del imputado ya existe o no se ha introducido un id", Toast.LENGTH_SHORT).show();
        }else {
            if(dni.length()==9){
                if(nombre.trim().length()==0){
                    Toast.makeText(this, "No se ha introducido un nombre", Toast.LENGTH_SHORT).show();
                }else {
                    //Creamos una colección con los datos del imputado para subirlo a la base de datos
                    Map<String, Object> imputado = new HashMap<>();
                    imputado.put("IdImputado", idImputado);
                    imputado.put("Nombre", nombre);
                    imputado.put("DNI", dni);

                    // Añadimos los datos a la base de datos
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
            }else {
                Toast.makeText(this, "El dni introducido no es correcto", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * Método que busca los imputados en la base de datos
     */
    public void buscarImputado(){
        db.collection("/Imputado").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            codigosImputado.add(documentSnapshot.getString("IdImputado").trim().toLowerCase(Locale.ROOT));
                        }
                    }
                });
    }

}