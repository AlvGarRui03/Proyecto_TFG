package com.example.aplicacinjuzgadotfg;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.ContentValues.TAG;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
public class FinalActivity extends AppCompatActivity {
    private String encabezadoPDF = "Documento oficial emitido por el juzgado de Granada";
    private String cuerpoPDF="Granada, a "+ ( new SimpleDateFormat( "dd/MM/yyyy")).format(Calendar.getInstance().getTime()) + " se emite la siguiente sentencia: ";
    private File file;
    private String idJuicio=" ";
    private String Juez=" ";
    private String Imputado=" ";
    private String Abogado=" ";
    private String imagen=" ";
    private String numeroSentencia=" ";
    private String [] divisionJuez = new String[1];
    private String [] divisionImputado = new String[1];
    private String [] divisionAbogado = new String[1];
    private String nombreJuez=" ";
    private String nombreImputado=" ";
    private String nombreAbogado=" ";
    private String codigoJuez=" ";
    private String codigoImputado=" ";
    private String codigoAbogado=" ";
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        idJuicio = getIntent().getStringExtra("idJuicio");
            Juez = getIntent().getStringExtra("Juez");
            Imputado = getIntent().getStringExtra("Imputado");
            Abogado = getIntent().getStringExtra("Abogado");
            imagen = getIntent().getStringExtra("imagen");
            numeroSentencia = getIntent().getStringExtra("numeroSentencia");
            divisionJuez = Juez.split(":");
            divisionImputado = Imputado.split(":");
            divisionAbogado = Abogado.split(":");
            codigoJuez = divisionJuez[0].trim();
            codigoImputado = divisionImputado[0].trim();
            codigoAbogado = divisionAbogado[0].trim();
            nombreJuez = divisionJuez[1].trim();
            nombreImputado = divisionImputado[1].trim();
            nombreAbogado = divisionAbogado[1].trim();
            crearJuicio();
            if (comprobarPermisos()) {
        } else {
            pedirPermisos();
        }
    }

    private boolean comprobarPermisos() {
        int permisoEscritura = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int permisoLectura = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        return permisoEscritura == PackageManager.PERMISSION_GRANTED && permisoLectura == PackageManager.PERMISSION_GRANTED;
    }

    private void pedirPermisos() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 0);
    }

    public void resultadoDePedirPermisos(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0) {
                boolean permisoEscritura = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean permisoLectura = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (permisoEscritura && permisoLectura) {
                    Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void crearPDF(View view) {
        PdfDocument documentoPDF = new PdfDocument();
        Paint paint = new Paint();
        TextPaint titulo = new TextPaint();
        TextPaint cuerpo = new TextPaint();
        Bitmap bitmap, bitmapEscala;
        PdfDocument.PageInfo paginaInformacion = new PdfDocument.PageInfo.Builder(816, 1054, 1).create();
        PdfDocument.Page pagina = documentoPDF.startPage(paginaInformacion);
        Canvas canvas = pagina.getCanvas();
        bitmap = Bitmap.createBitmap(816, 1054, Bitmap.Config.ARGB_8888);
        bitmapEscala = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
        canvas.drawBitmap(bitmapEscala, 368, 20, paint);

        titulo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titulo.setTextSize(21);
        canvas.drawText(encabezadoPDF, 175, 100, titulo);

        cuerpo.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        cuerpo.setTextSize(14);
        String [] cuerpoPDFArray = cuerpoPDF.split("\n");
        int y = 150;
        for(int i =0 ; i< cuerpoPDFArray.length;i++){
            canvas.drawText(cuerpoPDFArray[i], 10, y, cuerpo);
            y+=15;
        }
        documentoPDF.finishPage(pagina);
        File docsFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents");
        boolean isPresent = true;
        if (!docsFolder.exists()) {
            isPresent = docsFolder.mkdir();
        }
        if (isPresent) {
             file = new File(docsFolder.getAbsolutePath(),"Sentencia"+ idJuicio+".pdf");
        } else {
            Toast.makeText(this, "No se ha podido encontrar la carpeta documentos", Toast.LENGTH_SHORT).show();
        }
        try {
            documentoPDF.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF creado correctamente", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        documentoPDF.close();
    }
    public void crearJuicio(){
        db = FirebaseFirestore.getInstance();
        Map<String, Object> juicio = new HashMap<>();
        juicio.put("idJuicio", idJuicio);
        juicio.put("nombreJuez", nombreJuez);
        juicio.put("codigoJuez", codigoJuez);
        juicio.put("nombreImputado", nombreImputado);
        juicio.put("codigoImputado", codigoImputado);
        juicio.put("nombreAbogado", nombreAbogado);
        juicio.put("codigoAbogado", codigoAbogado);
        juicio.put("sentencia", numeroSentencia);
        juicio.put("pruebas", imagen);
        juicio.put("fecha", FieldValue.serverTimestamp());
        db.collection("/Juicios")
                .add(juicio)
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
