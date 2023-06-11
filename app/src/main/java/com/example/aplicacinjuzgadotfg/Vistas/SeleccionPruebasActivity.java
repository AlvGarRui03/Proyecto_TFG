package com.example.aplicacinjuzgadotfg.Vistas;



import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aplicacinjuzgadotfg.R;
import com.example.aplicacinjuzgadotfg.Vistas.JudgmentActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



public class SeleccionPruebasActivity<result> extends AppCompatActivity {
    private ImageView imagenPrueba;
    private StorageReference storageRef;
    private String imagen;
    private String idJuicio=" ";
    private String juez;
    private String imputado;
    private String abogado;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_pruebas);
        imagenPrueba = (ImageView) findViewById(R.id.IV_imagenPruebas);
        storageRef = FirebaseStorage.getInstance().getReference();
        idJuicio = getIntent().getStringExtra("idJuicio");
        Log.e("idJuicio Pruebas", idJuicio);
        juez = getIntent().getStringExtra("Juez");
        imputado = getIntent().getStringExtra("Imputado");
        abogado = getIntent().getStringExtra("Abogado");


    }

    /**
     * Metodo que obtiene la seleccion del usuario y la muestra en el imageView
     *
     * @param view
     */
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        //Obtenemos el resultado de la seleccion
                        Intent data = result.getData();
                        //Obtenemos la uri de la imagen seleccionada
                         uri = data.getData();
                        //Mostramos la imagen en el imageView
                         imagenPrueba.setImageURI(uri);


                    }
                }
            }
    );

    /**
     * Metodo que abre el chooser para que el usuario seleccione una imagen
     *
     * @param view the view
     */
    public void abrirChooser(View view){
        Intent intentChooser = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        //Limitamos la seleccion a imagenes
        intentChooser.setType("image/*");
        //Creamos el chooser
        intentChooser.createChooser(intentChooser, "Selecciona un archivo");
        someActivityResultLauncher.launch(intentChooser);
    }

    /**
     * Metodo que sube la imagen seleccionada al almacenamiento de Firebase y carga la siguiente actividad
     *
     * @param view the view
     */
    public void subirPrueba(View view){
        //Comprobamos que el usuario ha seleccionado una imagen
        if(uri!=null) {
            //Subimos la imagen al almacenamiento de Firebase
            storageRef.child("pruebas").child(uri.getLastPathSegment()).putFile(uri);
            //Obtenemos el nombre de la imagen
            imagen = uri.getLastPathSegment();
            Intent intent = new Intent(this, JudgmentActivity.class);
            intent.putExtra("imagen", imagen);
            intent.putExtra("Juez", juez);
            intent.putExtra("Imputado", imputado);
            intent.putExtra("Abogado", abogado);
            intent.putExtra("idJuicio",idJuicio);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Debe seleccionar una prueba", Toast.LENGTH_SHORT).show();
        }
    }
}