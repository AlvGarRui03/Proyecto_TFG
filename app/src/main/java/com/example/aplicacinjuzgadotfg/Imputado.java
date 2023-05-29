package com.example.aplicacinjuzgadotfg;

public class Imputado {
    private String idImputado;
    private String nombreCompleto;
    private String dni;

    public Imputado(String idImputado, String nombreCompleto, String dni) {
        this.idImputado = idImputado;
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
    }

    public String getIdImputado() {
        return idImputado;
    }

    public void setIdImputado(String idImputado) {
        this.idImputado = idImputado;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
