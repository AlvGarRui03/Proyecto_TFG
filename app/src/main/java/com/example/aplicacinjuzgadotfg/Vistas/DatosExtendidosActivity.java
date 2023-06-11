package com.example.aplicacinjuzgadotfg.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicacinjuzgadotfg.Modelos.Imputado;
import com.example.aplicacinjuzgadotfg.Modelos.Juez;
import com.example.aplicacinjuzgadotfg.Modelos.Juicio;
import com.example.aplicacinjuzgadotfg.Modelos.Abogado;
import com.example.aplicacinjuzgadotfg.R;
import com.example.aplicacinjuzgadotfg.Modelos.Sentencia;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class DatosExtendidosActivity extends AppCompatActivity {
    private String idJuicio = " ";
    private TextView Titulo;
    private TextView datos;
    private ImageView imageView;
    private FirebaseFirestore db;
    private Juicio juicio;
    private String codigoJuez;
    private String fecha;
    private String idsentencia;
    private Juez juez;
    private Imputado imputado;
    private Abogado abogado;
    private Sentencia sentencia;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_extendidos);
        Titulo = findViewById(R.id.TV_Datos);
        datos = findViewById(R.id.TxT_DatosJuicio);
        imageView = findViewById(R.id.IV_Pruebas);
        storageRef = FirebaseStorage.getInstance().getReference();
        idJuicio = getIntent().getStringExtra("idJuicio");
        //Ponemos el codigo del juicio en el titulo
        Titulo.setText("Datos del juicio " + idJuicio);
        busquedaDatos();
        buscarPruebas();


    }

    /**
     * Metodo que busca los datos asociados al juicio
     */
    public void busquedaDatos() {
        db = FirebaseFirestore.getInstance();
        buscarJuicio();
        buscarJuez();
        buscarImputado();
        buscarAbogado();
        buscarSentencia();
        ponerDatos();
    }

    /**
     * Metodo que busca el juicio asociado al código seleccionado
     */
    public void buscarJuicio() {
        //Buscamos el juicio que tiene ese código
        db.collection("/Juicios").whereEqualTo("idJuicio", idJuicio).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("TAG", "onSuccess:");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                juicio = new Juicio(d.getString("idJuicio"), d.getString("nombreJuez"), d.getString("codigoJuez"), d.getString("nombreImputado"), d.getString("codigoImputado"), d.getString("nombreAbogado"), d.getString("codigoAbogado"), d.getString("sentencia"), d.getString("pruebas"), new SimpleDateFormat("dd-MM-yyyy HH:mm").format(d.getDate("fecha")));
                                codigoJuez = d.getString("codigoJuez");
                                fecha = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", new Locale("es", "ES")).format(d.getDate("fecha"));
                            }
                        } else {
                            //Si no hay juicio con ese código, mostramos un mensaje de error
                            Log.e("TAG", "onSuccess: LIST EMPTY");
                            System.out.println("No data found in Database");
                        }
                    }
                });
    }

    /**
     * Metodo que busca el juez asociado al juicio
     */
    public void buscarJuez() {
        //Esperamos a que se haya encontrado el juicio
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                {
                    //Buscamos el juez que tiene ese código
                    db.collection("/Juez").whereEqualTo("idJuez", codigoJuez).get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    Log.e("TAG", "onSuccess:");
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                        for (DocumentSnapshot d : list) {
                                            juez = new Juez(d.getString("idJuez"), d.getString("Nombre"), d.getString("DNI"));
                                        }
                                    } else {
                                        //Si no hay juez con ese código, mostramos un mensaje de error
                                        Log.e("TAG", "onSuccess: LIST EMPTY");
                                        System.out.println("No data found in Database");
                                    }
                                }
                            });
                }
            }
            //Esperamos 2 segundos para que se haya encontrado el juicio
        }, 2000);

    }

    /**
     * Metodo que busca el imputado asociado al juicio
     */
    public void buscarImputado() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                db.collection("/Imputado").whereEqualTo("IdImputado", juicio.getCodigoImputado()).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                Log.e("TAG", "onSuccess:");
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot d : list) {
                                        imputado = new Imputado(d.getString("IdImputado"), d.getString("Nombre"), d.getString("DNI"));
                                    }
                                } else {
                                    //Si no hay imputado con ese código, mostramos un mensaje de error
                                    Log.e("TAG", "onSuccess: LIST EMPTY");
                                    System.out.println("No data found in Database");
                                }
                            }
                        });
            }
        }, 2000);

    }

    /**
     * Metodo que busca el abogado asociado al juicio
     */
    public void buscarAbogado() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                db.collection("/Abogado").whereEqualTo("IdAbogado", juicio.getCodigoAbogado()).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                Log.e("TAG", "onSuccess:");
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot d : list) {
                                        abogado = new Abogado(d.getString("IdAbogado"), d.getString("Nombre"), d.getString("DNI"), d.getString("Tipo"));
                                    }
                                } else {
                                    Log.e("TAG", "onSuccess: LIST EMPTY");
                                    System.out.println("No data found in Database");
                                }
                            }
                        });
            }
        }, 2000);

    }

    /**
     * Metodo pone los datos del juicio
     */
    public void ponerDatos() {
        //Esperamos a que se hayan encontrado todos los datos
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                datos.setText("El imputado " + imputado.getNombreCompleto() + " con DNI " + imputado.getDni() + " el cual esta siendo representado por " +
                        abogado.getNombre() + " con DNI " + abogado.getDni() + " fue juzgado por el juez "
                        + juez.getNombreCompleto() + " con DNI " + juez.getDni() + " el dia " + fecha + "\n \n" +
                        "Siendo la sentencia por " + sentencia.getDescripcion() + " de tipo " + sentencia.getTipo());
            }
        }, 6000);
    }

    /**
     * Metodo que busca la prueba asociada al juicio y la añade a la ImageView
     */
    public void buscarPruebas() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Obtenemos instancia de FirebaseStorage
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    //Obtenemos la ruta con la URL
                                    StorageReference routeReference = storage.getReferenceFromUrl("gs://proyectotfg-9f261.appspot.com");
                                    //Obtenemos la referencia a la imagen
                                    StorageReference photoReference = routeReference.child("pruebas/" + juicio.getPruebas());
                                    //Obtenemos la imagen del Storage
                                    final long ONE_MEGABYTE = 1024 * 1024 * 1024;
                                    photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            //Obtenemos la imagen y la añadimos a la ImageView
                                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            imageView.setImageBitmap(bmp);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
                                        }
                                    });


                                }
                            }
                , 4000);

    }

    /**
     * Metodo que busca la sentencia asociada al juicio
     */
    public void buscarSentencia() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    db.collection("/Sentencias").whereEqualTo("idSentencia", Integer.parseInt(juicio.getSentencia())).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    Log.e("TAG", "onSuccess:");
                                                    if (!queryDocumentSnapshots.isEmpty()) {
                                                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                        for (DocumentSnapshot d : list) {
                                                            sentencia = new Sentencia(String.valueOf(d.getLong("idSentencia")), d.getString("Tipo"), d.getString("Descripcion"));
                                                        }
                                                    } else {
                                                        //Si no hay sentencia con ese código, mostramos un mensaje de error
                                                        Log.e("TAG", "onSuccess: LIST EMPTY");
                                                        System.out.println("No data found in Database");
                                                    }
                                                }
                                            });
                                }
                            }

                , 2000);
    }

    /**
     * Metodo que vuelve a la actividad anterior
     *
     * @param view the view
     */
    public void volverActividadAnterior(View view){
        onBackPressed();
    }
}