package com.example.aplicacinjuzgadotfg;

public class Juicio {
    private String idJuicio;
    private Juez juez;
    private Imputado imputado;
    private Abogado abogado;
    private Sentencia sentencia;
    private String pruebas;
    private String fecha;

    public String getIdJuicio() {
        return idJuicio;
    }

    public void setIdJuicio(String idJuicio) {
        this.idJuicio = idJuicio;
    }

    public String getPruebas() {
        return pruebas;
    }

    public void setPruebas(String pruebas) {
        this.pruebas = pruebas;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
