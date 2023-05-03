package com.example.aplicacinjuzgadotfg;

public class Juez {
    private String idJuez;
    private String nombreCompleto;
    private String dni;

    public Juez(String idJuez, String nombreCompleto, String dni) {
        this.idJuez = idJuez;
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
    }

    public String getIdJuez() {
        return idJuez;
    }

    public void setIdJuez(String idJuez) {
        this.idJuez = idJuez;
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
