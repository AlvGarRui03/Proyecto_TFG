package com.example.aplicacinjuzgadotfg;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JudgmentActivity extends AppCompatActivity {
    private Spinner tipoSentencia;
    private ArrayList<String> tipoSentenciaList = new ArrayList<String>();
    private ArrayAdapter<String> adapterTipoSentencia;
    private FirebaseFirestore db;
    private String tipoSentenciaSeleccionada;
    private String idJuicio;
    private String juez;
    private String imputado;
    private String abogado;
    private String imagen;
    private String descripcion;
    private boolean recurso;
    private int numeroSentencia;
    private TextInputEditText ET_descripcion;
    private CheckBox CB_recurso;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgment);
        tipoSentencia = (Spinner) findViewById(R.id.Spinner_TipoSentencia);
        CB_recurso = (CheckBox) findViewById(R.id.CB_Recurso);
        adapterTipoSentencia = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tipoSentenciaList);
        tipoSentencia.setAdapter(adapterTipoSentencia);
        ET_descripcion = (TextInputEditText) findViewById(R.id.TxT_Descripción);
        buscarTiposSentencias();

        idJuicio = getIntent().getStringExtra("idJuicio");
        juez = getIntent().getStringExtra("Juez");
        imputado = getIntent().getStringExtra("Imputado");
        abogado = getIntent().getStringExtra("Abogado");
        imagen = getIntent().getStringExtra("imagen");
    }

    public void ultimaActividad(View view) {
        //Comprobamos que el checkbox esta seleccionado o no
        recurso = CB_recurso.isChecked();
        descripcion = ET_descripcion.getText().toString();
        if (descripcion.trim().length() == 0 || descripcion.length() > 80) {
            Toast.makeText(this, "La descripcion debe ser mayor de 0 y menor de 80 caracteres", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, FinalActivity.class);
            creacionSentencia();
            intent.putExtra("imagen", imagen);
            intent.putExtra("Juez", juez);
            intent.putExtra("Imputado", imputado);
            intent.putExtra("Abogado", abogado);
            intent.putExtra("idJuicio", idJuicio);
            intent.putExtra("numeroSentencia", String.valueOf(numeroSentencia));
            intent.putExtra("descripcion", descripcion);
            intent.putExtra("recurso",recurso);
            intent.putExtra("tipoSentencia", tipoSentenciaSeleccionada);
            startActivity(intent);
        }

    }
    /**
     * Metodo que busca los tipos sentencia en la base de datos
     */
    public void buscarTiposSentencias() {
        db = FirebaseFirestore.getInstance();
        db.collection("/Tipo_Sentencia").orderBy("Tipo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("TAG", "onSuccess:");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                tipoSentenciaList.add(d.getString("Tipo"));
                            }
                        } else {
                            // Error si la lista esta vacia
                            Log.e("TAG", "onSuccess: LIST EMPTY");
                            System.out.println("No data found in Database");
                        }
                        //Obtenemos el tipo de sentencia seleccionado
                        tipoSentencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                tipoSentenciaSeleccionada = parent.getItemAtPosition(position).toString();
                                Log.e("TAG", "onItemSelected: " + parent.getItemAtPosition(position).toString());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Log.e("TAG", "onNothingSelected: ");
                            }

                        });
                        //Notificamos al adapter que se ha realizado un cambio en la lista
                        adapterTipoSentencia.notifyDataSetChanged();
                    }
                });
        //Incrementamos el id de la sentencia
        autoincrementSentencia();
    }
    /**
     * Metodo que obtiene el ultimo id de sentencia y lo incrementa en 1
     */
    public void autoincrementSentencia() {
        db.collection("/Sentencias").orderBy("idSentencia", Query.Direction.ASCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("TAG", "onSuccess:");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            Log.e("TAG", "onSuccess: " + queryDocumentSnapshots.size());
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                numeroSentencia = d.getLong("idSentencia").intValue() + 1;
                            }
                        } else {
                            // Error si la lista esta vacia
                            Log.e("TAG", "onSuccess: LIST EMPTY");
                            System.out.println("No data found in Database");
                        }

                    }
                });


    }
    /**
     * Metodo que crea la sentencia en la base de datos
     */
    public void creacionSentencia() {
        Map<String, Object> sentencia = new HashMap<>();
        sentencia.put("idSentencia", numeroSentencia);
        sentencia.put("Tipo", tipoSentenciaSeleccionada);
        sentencia.put("Descripcion", descripcion);
        sentencia.put("PosibilidadRecurso", recurso);

        db.collection("/Sentencias")
                .add(sentencia)
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
    }
}