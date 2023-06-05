package com.example.aplicacinjuzgadotfg;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbogadoActivity extends AppCompatActivity {
    private ArrayList<String> tiposAbogado;
    Spinner listaTiposAbogado;
    private FirebaseFirestore db;
    ArrayAdapter<String> adapterTipos;
    private String tipoAbogadoSeleccionado = "Abogado de Derecho ambiental";
    private EditText idAbogadoText;
    private EditText nombreAbogadoText;
    private EditText dniAbogadoText;
    private String idAbogado;
    private String nombreAbogado;
    private String dniAbogado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abogado);
        idAbogadoText = findViewById(R.id.TxT_IdAbogado);
        nombreAbogadoText = findViewById(R.id.TxT_NombreAbogado);
        dniAbogadoText = findViewById(R.id.TxT_DniAbogado);
        tiposAbogado = new ArrayList<>();
        listaTiposAbogado = findViewById(R.id.SpinnerTiposAbogado);
        buscarTipos();
        adapterTipos = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tiposAbogado);
        listaTiposAbogado.setAdapter(adapterTipos);
    }
    public void buscarTipos() {
        db = FirebaseFirestore.getInstance();
        db.collection("/Tipos").orderBy("Tipo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("TAG", "onSuccess:");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            Log.e("TAG", "onSuccess: " + queryDocumentSnapshots.size());
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Log.e("TAG", "onSuccess: " + d.getId() + " => " + d.getData());
                                System.out.println(d.getString("Tipo"));
                                tiposAbogado.add(d.getString("Tipo"));
                            }
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Log.e("TAG", "onSuccess: LIST EMPTY");
                            System.out.println("No data found in Database");
                        }
                        adapterTipos.notifyDataSetChanged();
                        listaTiposAbogado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                System.out.println(parent.getSelectedItem().toString()+" seleccionado");
                                tipoAbogadoSeleccionado = parent.getSelectedItem().toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Log.e("Error", "No se ha seleccionado nada");
                            }
                        });
                    }
                });
    }
    public void creacionAbogado(View view){
        idAbogado = idAbogadoText.getText().toString();
        nombreAbogado = nombreAbogadoText.getText().toString();
        dniAbogado = dniAbogadoText.getText().toString();
        Map<String, Object> abogado = new HashMap<>();
        abogado.put("IdAbogado", idAbogado);
        abogado.put("Nombre", nombreAbogado);
        abogado.put("DNI", dniAbogado);
        abogado.put("Tipo", tipoAbogadoSeleccionado);


        db.collection("/Abogado")
                .add(abogado)
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
