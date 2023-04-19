package Semantico.Funcion;

import Semantico.Tipo;

public class Metodo extends Funcion{
    private String nombre;
    private boolean esEstatico;
    private boolean esPublico;
    private Tipo tipoRetorno;
    private int posicion;


    public String obtenerNombre(){
        return this.nombre;
    }

    public boolean obtenerEsEstatico(){
        return this.esEstatico;
    }

    public boolean obtenerEsPublico(){
        return this.esPublico;
    }

    public Tipo obtenerTipoRetorno(){
        return this.tipoRetorno;
    }

    public int obtenerPosicion(){
        return this.posicion;
    }
}