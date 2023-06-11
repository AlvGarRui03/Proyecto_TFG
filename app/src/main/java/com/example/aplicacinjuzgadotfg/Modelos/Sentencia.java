package com.example.aplicacinjuzgadotfg.Modelos;

public class Sentencia {
    private String idSentencia;
    private String tipo;
    private String descripcion;



    public Sentencia(String idSentencia, String tipo, String descripcion) {
        this.idSentencia = idSentencia;
        this.tipo = tipo;
        this.descripcion = descripcion;

    }


    public String getIdSentencia() {
        return idSentencia;
    }


    public void setIdSentencia(String idSentencia) {
        this.idSentencia = idSentencia;
    }


    public String getTipo() {
        return tipo;
    }


    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public String getDescripcion() {
        return descripcion;
    }


    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
