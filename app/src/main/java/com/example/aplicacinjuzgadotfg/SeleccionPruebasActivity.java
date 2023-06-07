package com.example.aplicacinjuzgadotfg;



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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



public class SeleccionPruebasActivity<result> extends AppCompatActivity {
    private TextView nombrePrueba;
    private ImageView imagenPrueba;
    private StorageReference storageRef;
    private String imagen;
    private String idJuicio;
    private String juez;
    private String imputado;
    private String abogado;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_pruebas);
        nombrePrueba = (TextView) findViewById(R.id.TxT_ArchivoPrueba);
        imagenPrueba = (ImageView) findViewById(R.id.IV_imagenPruebas);
        storageRef = FirebaseStorage.getInstance().getReference();
        idJuicio = getIntent().getStringExtra("idJuicio");
        juez = getIntent().getStringExtra("Juez");
        imputado = getIntent().getStringExtra("Imputado");
        abogado = getIntent().getStringExtra("Abogado");
        Log.e("idJuicio", idJuicio + "ESTA?");
        Log.e("juez", juez + "ESTA?");
        Log.e("imputado", imputado + "ESTA?");
        Log.e("abogado", abogado + "ESTA?");

    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                         uri = data.getData();
                        imagenPrueba.setImageURI(uri);


                    }
                }
            }
    );

    public void abrirChooser(View view){
        Intent intentChooser = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intentChooser.setType("image/*");
        intentChooser.createChooser(intentChooser, "Selecciona un archivo");
        someActivityResultLauncher.launch(intentChooser);
    }
    public void subirPrueba(View view){

        if(uri!=null) {
            storageRef.child("pruebas").child(uri.getLastPathSegment()).putFile(uri);
            imagen = uri.getLastPathSegment();
            Log.e("imagen", imagen);
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