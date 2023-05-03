package com.example.aplicacinjuzgadotfg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CodeSelectionActivity extends AppCompatActivity {
    private Button botonPersonas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_selection);
        botonPersonas = findViewById(R.id.BT_ContinuarPersonas);
    }
    public void seleccionPersonas(View view){
        Intent intent = new Intent(this, PeopleSelectionActivity.class);
        if(botonPersonas.getText().toString().trim().length()>0){
        startActivity(intent);
        }
    }
}