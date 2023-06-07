package com.example.aplicacinjuzgadotfg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PeopleSelectionActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private ArrayList<String> nombresJueces;
    private ArrayList<String> nombresImputados;
    private ArrayList<String> nombresAbogados;
    private String juezSeleccionado;
    private String imputadoSeleccionado;
    private String abogadoSeleccionado;
    private String idJuicio;
    Spinner listaJueces;
    Spinner listaImputados;
    Spinner listaAbogados;
    ArrayAdapter<String> adapterJueces;
    ArrayAdapter<String> adapterImputados;
    ArrayAdapter<String> adapterAbogados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_selection);
        listaJueces = (Spinner) findViewById(R.id.Spinner_Jueces);
        listaImputados = (Spinner) findViewById(R.id.Spinner_Imputados);
        listaAbogados = (Spinner) findViewById(R.id.Spinner_Abogado);
        idJuicio = getIntent().getStringExtra("idJuicio");
        Log.e("TAG", "onCreate: " + idJuicio + "ESTA?");
        nombresJueces = new ArrayList<>();
        nombresImputados = new ArrayList<>();
        nombresAbogados = new ArrayList<>();
        buscarDatos();
        adapterJueces = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nombresJueces);
        listaJueces.setAdapter(adapterJueces);
        adapterImputados = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nombresImputados);
        listaImputados.setAdapter(adapterImputados);
        adapterAbogados = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nombresAbogados);
        listaAbogados.setAdapter(adapterAbogados);
    }

    public void buscarDatos() {
        db = FirebaseFirestore.getInstance();
        buscarJueces();
        buscarImputados();
        buscarAbogados();
    }

    public void buscarJueces() {
        db.collection("/Juez").orderBy("Nombre").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("TAG", "onSuccess:");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            Log.e("TAG", "onSuccess: " + queryDocumentSnapshots.size());
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Log.e("TAG", "onSuccess: " + d.getId() + " => " + d.getData());
                                System.out.println(d.getString("Nombre"));
                                Juez juez = new Juez(d.getString("idJuez"), d.getString("Nombre"), d.getString("DNI"));
                                System.out.println("ID: " + juez.getIdJuez() + " Nombre: " + juez.getNombreCompleto() + " DNI: " + juez.getDni());
                                nombresJueces.add(juez.getIdJuez() +": "+juez.getNombreCompleto());
                            }
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Log.e("TAG", "onSuccess: LIST EMPTY");
                            System.out.println("No data found in Database");
                        }
                        listaJueces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                juezSeleccionado = parent.getItemAtPosition(position).toString();
                                Log.e("TAG", "onItemSelected: " + parent.getItemAtPosition(position).toString());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                                Log.e("TAG", "onNothingSelected: ");
                            }

                        });
                        adapterJueces.notifyDataSetChanged();
                    }

                });
    }

    public void buscarImputados() {
        db.collection("/Imputado").orderBy("Nombre").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("TAG", "onSuccess:");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            Log.e("TAG", "onSuccess: " + queryDocumentSnapshots.size());
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Log.e("TAG", "onSuccess: " + d.getId() + " => " + d.getData());
                                System.out.println(d.getString("Nombre"));
                                Imputado imputado = new Imputado(d.getString("IdImputado"), d.getString("Nombre"), d.getString("DNI"));
                                System.out.println("ID: " + imputado.getIdImputado() + " Nombre: " + imputado.getNombreCompleto() + " DNI: " + imputado.getDni());
                                nombresImputados.add(imputado.getIdImputado() + ": " + imputado.getNombreCompleto());


                            }
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Log.e("TAG", "onSuccess: LIST EMPTY");
                            System.out.println("No data found in Database");
                        }
                        listaImputados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                imputadoSeleccionado = parent.getItemAtPosition(position).toString();
                                Log.e("TAG", "onItemSelected: " + parent.getItemAtPosition(position).toString());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Log.e("TAG", "onNothingSelected: ");
                            }

                        });
                        adapterImputados.notifyDataSetChanged();
                    }
                });
    }

    public void buscarAbogados() {
        db.collection("/Abogado").orderBy("Nombre").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("TAG", "onSuccess:");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            Log.e("TAG", "onSuccess: " + queryDocumentSnapshots.size());
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Log.e("TAG", "onSuccess: " + d.getId() + " => " + d.getData());
                                System.out.println(d.getString("Nombre"));
                                Abogado abogado = new Abogado(d.getString("IdAbogado"), d.getString("Nombre"), d.getString("Tipo"), d.getString("DNI"));
                                System.out.println("ID: " + abogado.getIdAbogado() + " Nombre: " + abogado.getNombre() + " DNI: " + abogado.getDni() + " Tipo: " + abogado.getTipo());
                                nombresAbogados.add(abogado.getIdAbogado() + ": " + abogado.getNombre());
                            }
                        } else {
                            Log.e("TAG", "onSuccess: LIST EMPTY");
                            System.out.println("No data found in Database");
                        }
                        listaAbogados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                abogadoSeleccionado = parent.getItemAtPosition(position).toString();
                                Log.e("TAG", "onItemSelected: " + parent.getItemAtPosition(position).toString());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Log.e("TAG", "onNothingSelected: ");
                            }

                        });
                        adapterAbogados.notifyDataSetChanged();
                    }
                });
    }

    public void crearJuez(View view) {
        Intent intent = new Intent(this, JuezActivity.class);
        startActivity(intent);
    }

    public void crearImputado(View view) {
        Intent intent = new Intent(this, ImputadoActivity.class);
        startActivity(intent);
    }

    public void crearAbogado(View view) {
        Intent intent = new Intent(this, AbogadoActivity.class);
        startActivity(intent);
    }

    public void crearConjuntoPersonas(View view) {
        System.out.println("Juez: " + juezSeleccionado + " Imputado: " + imputadoSeleccionado + " Abogado: " + abogadoSeleccionado);
        Intent intent = new Intent(this, SeleccionPruebasActivity.class);
        intent.putExtra("Juez", juezSeleccionado);
        intent.putExtra("Imputado", imputadoSeleccionado);
        intent.putExtra("Abogado", abogadoSeleccionado);
        intent.putExtra("idJuicio",idJuicio);
        startActivity(intent);
    }
}


