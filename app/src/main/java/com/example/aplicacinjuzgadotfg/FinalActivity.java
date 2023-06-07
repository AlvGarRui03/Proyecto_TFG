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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
public class FinalActivity extends AppCompatActivity {
    private String encabezadoPDF = "Documento oficial emitido por el juzgado de Granada";
    private String cuerpoPDF="Granada, a "+ ( new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", new Locale("es","ES")).format(Calendar.getInstance().getTime()) + ".\n");
    private String piePDF="";
    private String firmante="";
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
    private String descripcion=" ";
    private String tipoSentencia=" ";
    private String posibilidadRecurso=" ";
    private boolean recurso=false;
    private FirebaseFirestore db;
    private URI uri;
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
            recurso = getIntent().getBooleanExtra("recurso", false);
            descripcion = getIntent().getStringExtra("descripcion");
            tipoSentencia = getIntent().getStringExtra("tipoSentencia");
            uri = getIntent().getParcelableExtra("uri");
            //Obtenemos los datos de delante y detras de un caracter
            divisionJuez = Juez.split(":");
            divisionImputado = Imputado.split(":");
            divisionAbogado = Abogado.split(":");
            //Obtenemos los datos de delante
            codigoJuez = divisionJuez[0].trim();
            codigoImputado = divisionImputado[0].trim();
            codigoAbogado = divisionAbogado[0].trim();
            //Obtenemos los datos de detras
            nombreJuez = divisionJuez[1].trim();
            nombreImputado = divisionImputado[1].trim();
            nombreAbogado = divisionAbogado[1].trim();
            firmante = "Juez decano de Granada:";
            crearJuicio();
            //Comprobamos si la sentencia es recurrible
            if(recurso){
                posibilidadRecurso = "Pudiendo ser recurrida la sentencia";
            }else{
                posibilidadRecurso = "No pudiendo ser recurrida la sentencia";
            }
            //Añadimos el cuerpo del PDF
            cuerpoPDF = cuerpoPDF+"El juez " + nombreJuez + ", ha dictaminado para el imputado "+ nombreImputado +", el cual es representado por el abogado "+ nombreAbogado +"\n"
                    +"que la sentencia sobre el caso de " + descripcion +". \nSiendo la misma  de tipo " + tipoSentencia + " a la cual se le ha asignado el número de sentencia: " + numeroSentencia + ".\n"
            + posibilidadRecurso + ".\n";
            //Añadimos el pie del PDF
            piePDF = "Este documento podrá ser encontrado bajo el código de juicio " + idJuicio + " en la aplicación móvil del juzgado de Granada.\n"
                    +"Atentamente, el juzgado de Granada.";
            //Comprobamos si tenemos permisos
            if (comprobarPermisos()) {
        } else {
            pedirPermisos();
        }
    }
    /**
     * Comprueba si tenemos permisos de escritura y lectura
     */
    private boolean comprobarPermisos() {
        int permisoEscritura = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int permisoLectura = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        return permisoEscritura == PackageManager.PERMISSION_GRANTED && permisoLectura == PackageManager.PERMISSION_GRANTED;
    }
    /**
     * Pide permisos de escritura y lectura
     */
    private void pedirPermisos() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 0);
    }
/*
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
    }*/

    /**
     * Crea el PDF
     * @param view
     */
    public void crearPDF(View view) {
        //Creamos el documento
        PdfDocument documentoPDF = new PdfDocument();
        //Creamos el paint
        Paint paint = new Paint();
        //Creamos los textPaint
        TextPaint titulo = new TextPaint();
        TextPaint cuerpo = new TextPaint();
        TextPaint pie = new TextPaint();
        TextPaint firma = new TextPaint();
        //Obtenermos las imagenes
        Bitmap imagenEmblema = BitmapFactory.decodeResource(getResources(), R.drawable.iconojuzgado);
        Bitmap imagenPDF = BitmapFactory.decodeResource(getResources(), R.drawable.firma);
        Bitmap selloPDF = BitmapFactory.decodeResource(getResources(), R.drawable.sello2);
        Bitmap bitmap, bitmapEscala;
        //Creamos la informacion de la pagina
        PdfDocument.PageInfo paginaInformacion = new PdfDocument.PageInfo.Builder(816, 1054, 1).create();
        //Iniciamos la pagina
        PdfDocument.Page pagina = documentoPDF.startPage(paginaInformacion);
        //Creamos el canvas
        Canvas canvas = pagina.getCanvas();
        bitmap = Bitmap.createBitmap(816, 1054, Bitmap.Config.ARGB_8888);
        //Creamos la escala
        bitmapEscala = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
        canvas.drawBitmap(bitmapEscala, 368, 20, paint);
        //Creamos el titulo
        titulo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titulo.setTextSize(21);
        canvas.drawText(encabezadoPDF, 180, 100, titulo);
        //Creamos el firmante
        firma.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        firma.setTextSize(14);
        canvas.drawText(firmante, 500, 780, firma);
        //Ponemos la imagenes
        canvas.drawBitmap(imagenPDF, 530, 800, paint);
        canvas.drawBitmap(imagenEmblema, 5, 5, paint);
        canvas.drawBitmap(selloPDF, 200, 350, paint);
        //Creamos el cuerpo
        cuerpo.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        cuerpo.setTextSize(14);
        //Separamos el cuerpo en lineas
        String [] cuerpoPDFArray = cuerpoPDF.split("\n");
        int y = 200;
        //Si el cuerpo es muy largo, saltamos a la siguiente linea
        for(int i =0 ; i< cuerpoPDFArray.length;i++){
            canvas.drawText(cuerpoPDFArray[i], 25, y, cuerpo);
            y+=30;
        }
        //Creamos el pie
        pie.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        pie.setTextSize(10);
        canvas.drawText(piePDF, 10, 1000, pie);
        //Terminamos la pagina
        documentoPDF.finishPage(pagina);
        //Creamos el fichero en la carpeta Documents
        File docsFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents");
        boolean isPresent = true;
        //Comprobamos si existe la carpeta
        if (!docsFolder.exists()) {
            //Si no existe la creamos
            isPresent = docsFolder.mkdir();
        }
        //Si existe la carpeta, creamos el fichero
        if (isPresent) {
            //Creamos el fichero teniendo como nombre el id del juicio
             file = new File(docsFolder.getAbsolutePath(),"Sentencia"+ idJuicio+".pdf");
        } else {
            Toast.makeText(this, "No se ha podido encontrar la carpeta documentos", Toast.LENGTH_SHORT).show();
        }
        try {
            //Escribimos el fichero en el storage
            documentoPDF.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF creado correctamente", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Cerramos el documento
        documentoPDF.close();
    }
    /**
     * Creamos el juicio en la base de datos
     *
     */
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
    /**
     * Vuelve al inicio
     * @param view
     */
    public void volverInicio(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
