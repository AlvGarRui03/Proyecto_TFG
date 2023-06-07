package com.example.aplicacinjuzgadotfg;

import java.util.Calendar;

public class Juicio {
    private String idJuicio;
    private String nombreJuez;
    private String codigoJuez;
    private String nombreImputado;
    private String codigoImputado;
    private String nombreAbogado;
    private String codigoAbogado;
    private String sentencia;
    private String pruebas;
    private String fecha;
    public Juicio(String idJuicio, String nombreJuez, String codigoJuez, String nombreImputado, String codigoImputado, String nombreAbogado, String codigoAbogado, String sentencia, String pruebas, String fecha) {
        this.idJuicio = idJuicio;
        this.nombreJuez = nombreJuez;
        this.codigoJuez = codigoJuez;
        this.nombreImputado = nombreImputado;
        this.codigoImputado = codigoImputado;
        this.nombreAbogado = nombreAbogado;
        this.codigoAbogado = codigoAbogado;
        this.sentencia = sentencia;
        this.pruebas = pruebas;
        this.fecha = fecha;
    }

    public Juicio(){
        fecha = Calendar.getInstance().getTime().toString();
    }
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

    public String getNombreJuez() {
        return nombreJuez;
    }

    public void setNombreJuez(String nombreJuez) {
        this.nombreJuez = nombreJuez;
    }

    public String getCodigoJuez() {
        return codigoJuez;
    }

    public void setCodigoJuez(String codigoJuez) {
        this.codigoJuez = codigoJuez;
    }

    public String getNombreImputado() {
        return nombreImputado;
    }

    public void setNombreImputado(String nombreImputado) {
        this.nombreImputado = nombreImputado;
    }

    public String getCodigoImputado() {
        return codigoImputado;
    }

    public void setCodigoImputado(String codigoImputado) {
        this.codigoImputado = codigoImputado;
    }

    public String getNombreAbogado() {
        return nombreAbogado;
    }

    public void setNombreAbogado(String nombreAbogado) {
        this.nombreAbogado = nombreAbogado;
    }

    public String getCodigoAbogado() {
        return codigoAbogado;
    }

    public void setCodigoAbogado(String codigoAbogado) {
        this.codigoAbogado = codigoAbogado;
    }

    public String getSentencia() {
        return sentencia;
    }

    public void setSentencia(String sentencia) {
        this.sentencia = sentencia;
    }
}
