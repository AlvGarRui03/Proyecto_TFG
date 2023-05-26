package com.example.aplicacinjuzgadotfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    /**
     * Clase principal de la aplicación, se encarga de cargar el layout de la pantalla principal
     * y de cargar las actividades de selección de código de juicio y de vista de juicios
     */
    private Context contexto = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int nightModeFlags = contexto.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }

    }

    /**
     * Método que se ejecuta al pulsar el botón de crear juicio, se encarga de cargar la actividad
     * de selección de código de juicio
     * @param view recibe como parámetro la vista
     */
    public void crearJuicio(View view){
        Intent intent = new Intent(this, CodeSelectionActivity.class);
        startActivity(intent);
    }
}