package com.example.aplicacinjuzgadotfg;


public class Abogado {
    //abogado.getIdAbogado() + " Nombre: " + abogado.getNombre() + " DNI: " + abogado.getDNI() + " Tipo: " + abogado.getTipo()
    private String idAbogado;
    private String nombre;
    private String dni;
    private String tipo;
    public Abogado(String idAbogado , String nombre , String dni, String tipo){
       this.idAbogado = idAbogado;
       this.nombre = nombre;
       this.dni = dni;
       this.tipo = tipo;
    }
    public String getIdAbogado() {
        return idAbogado;
    }

    public void setIdAbogado(String idAbogado) {
        this.idAbogado = idAbogado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        tipo = tipo;
    }
}
