package com.example.aplicacinjuzgadotfg.Vistas;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AbogadoActivity extends AppCompatActivity {
    private ArrayList<String> tiposAbogado;
    private ArrayList<String> codigosAbogados = new ArrayList<String>();
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
        buscarAbogados();
        adapterTipos = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tiposAbogado);
        listaTiposAbogado.setAdapter(adapterTipos);
    }

    /**
     * Metodo que busca los tipos de abogado en la base de datos para añadirlos al spinner
     */
    public void buscarTipos() {
        db = FirebaseFirestore.getInstance();
        db.collection("/Tipos").orderBy("Tipo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("TAG", "onSuccess:");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                tiposAbogado.add(d.getString("Tipo"));
                            }
                        } else {
                            //Si la consulta devuelve una lista vacia se marca un error
                            Log.e("TAG", "onSuccess: LIST EMPTY");
                            System.out.println("No data found in Database");
                        }
                        //Se notifica al spinner que se ha cambiado el array de tipos de abogado
                        adapterTipos.notifyDataSetChanged();
                        listaTiposAbogado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
    /**
     * Metodo que crea un abogado en la base de datos
     * @param view
     */
    public void creacionAbogado(View view){
            idAbogado = idAbogadoText.getText().toString();
            nombreAbogado = nombreAbogadoText.getText().toString();
            dniAbogado = dniAbogadoText.getText().toString();
            if(codigosAbogados.contains(idAbogado.trim().toLowerCase(Locale.ROOT)) || idAbogado.trim().length()==0) {
                Toast.makeText(this, "El código de abogado ya existe o esta vacío", Toast.LENGTH_SHORT).show();
            }else{
                if(dniAbogado.length()!=9){
                    Toast.makeText(this, "El DNI debe tener 9 caracteres", Toast.LENGTH_SHORT).show();
                }else {
                    if(nombreAbogado.trim().length()!=0){
                        //Se crea un mapa con los datos del abogado
                    Map<String, Object> abogado = new HashMap<>();
                    abogado.put("IdAbogado", idAbogado);
                    abogado.put("Nombre", nombreAbogado);
                    abogado.put("DNI", dniAbogado);
                    abogado.put("Tipo", tipoAbogadoSeleccionado);

                    //Se añade el abogado a la base de datos
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
                    }else{
                        Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    }
    /**
     * Metodo que busca los abogados en la base de datos para añadirlos al array de codigos de abogados
     */
    public void buscarAbogados(){

        db.collection("/Abogado").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("TAG", "onSuccess:");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            Log.e("TAG", "onSuccess: " + queryDocumentSnapshots.size());
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Log.e("TAG", "onSuccess: " + d.getId() + " => " + d.getData());
                                codigosAbogados.add(d.getString("IdAbogado").toLowerCase(Locale.ROOT).trim());
                            }
                        } else {
                            //Si la consulta devuelve una lista vacia se marca un error
                            Log.e("TAG", "onSuccess: LIST EMPTY");
                            System.out.println("No data found in Database");
                        }
                    }
                });



}
    }