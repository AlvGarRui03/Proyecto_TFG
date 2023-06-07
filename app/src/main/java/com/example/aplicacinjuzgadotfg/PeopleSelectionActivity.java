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

    /**
     * Metodo encargado de comenzar la busqueda de datos de la base de datos
     */
    public void buscarDatos() {
        db = FirebaseFirestore.getInstance();
        buscarJueces();
        buscarImputados();
        buscarAbogados();
    }
    /**
     * Metodo encargado de buscar los jueces en la base de datos
     */
    public void buscarJueces() {
        //Se ordena por orden alfabetico de nombre
        db.collection("/Juez").orderBy("Nombre").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("TAG", "onSuccess:");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            Log.e("TAG", "onSuccess: " + queryDocumentSnapshots.size());
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Juez juez = new Juez(d.getString("idJuez"), d.getString("Nombre"), d.getString("DNI"));
                                nombresJueces.add(juez.getIdJuez() +": "+juez.getNombreCompleto());
                            }
                        } else {
                            //Si esta vacio se muestra un mensaje de lista vacia
                            Log.e("TAG", "onSuccess: LIST EMPTY");
                            System.out.println("No data found in Database");
                        }
                        //Listener para cuando se selecciona un juez
                        listaJueces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                juezSeleccionado = parent.getItemAtPosition(position).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Log.e("TAG", "onNothingSelected: ");
                            }

                        });
                        //Se notifica al adapter que se han añadido nuevos datos
                        adapterJueces.notifyDataSetChanged();
                    }

                });
    }
    /**
     * Metodo encargado de buscar los Imputados en la base de datos
     */
    public void buscarImputados() {
        db.collection("/Imputado").orderBy("Nombre").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("TAG", "onSuccess:");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Imputado imputado = new Imputado(d.getString("IdImputado"), d.getString("Nombre"), d.getString("DNI"));
                                nombresImputados.add(imputado.getIdImputado() + ": " + imputado.getNombreCompleto());


                            }
                        } else {
                            // Si esta vacio se muestra un mensaje de lista vacia
                            Log.e("TAG", "onSuccess: LIST EMPTY");
                            System.out.println("No data found in Database");
                        }
                        //Listener para cuando se selecciona un imputado
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
    /**
     * Metodo encargado de buscar los abogados en la base de datos
     */
    public void buscarAbogados() {
        //Se ordena por orden alfabetico de nombre
        db.collection("/Abogado").orderBy("Nombre").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("TAG", "onSuccess:");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Abogado abogado = new Abogado(d.getString("IdAbogado"), d.getString("Nombre"), d.getString("Tipo"), d.getString("DNI"));
                                nombresAbogados.add(abogado.getIdAbogado() + ": " + abogado.getNombre());
                            }
                        } else {
                            //Si esta vacio se muestra un mensaje de lista vacia
                            Log.e("TAG", "onSuccess: LIST EMPTY");
                            System.out.println("No data found in Database");
                        }
                        //Listener para cuando se selecciona un abogado
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
    /**
     * Metodo encargado de enviar a la actividad de creacion de juez
     * @param view
     */
    public void crearJuez(View view) {
        Intent intent = new Intent(this, JuezActivity.class);
        startActivity(intent);
    }
    /**
     * Metodo encargado de enviar a la actividad de creacion de imputado
     * @param view
     */
    public void crearImputado(View view) {
        Intent intent = new Intent(this, ImputadoActivity.class);
        startActivity(intent);
    }
    /**
     * Metodo encargado de enviar a la actividad de creacion de abogado
     * @param view
     */
    public void crearAbogado(View view) {
        Intent intent = new Intent(this, AbogadoActivity.class);
        startActivity(intent);
    }

    /**
     * Metodo encargado de guardar los datos del conjunto de personas y enviar a la actividad de seleccion de pruebas
     * @param view
     */
    public void crearConjuntoPersonas(View view) {
        Intent intent = new Intent(this, SeleccionPruebasActivity.class);
        intent.putExtra("Juez", juezSeleccionado);
        intent.putExtra("Imputado", imputadoSeleccionado);
        intent.putExtra("Abogado", abogadoSeleccionado);
        intent.putExtra("idJuicio",idJuicio);
        startActivity(intent);
    }
}


