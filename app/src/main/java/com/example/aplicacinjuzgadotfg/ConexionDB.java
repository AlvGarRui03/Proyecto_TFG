package com.example.aplicacinjuzgadotfg;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ConexionDB {
    public static void conectar() throws FileNotFoundException {
        // Conecta con la base de datos
         FirebaseFirestore db = FirebaseFirestore.getInstance();
    }
}
