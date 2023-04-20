package Semantico.Funcion;

import Semantico.Tipo.Tipo;

public class Metodo extends Funcion{
    private String nombre;
    private boolean esEstatico;
    private Tipo tipoRetorno;
    private int posicion;

    public Metodo(String nombre){
        this.nombre = nombre;
    }

    public String obtenerNombre(){
        return this.nombre;
    }

    public boolean obtenerEsEstatico(){
        return this.esEstatico;
    }

    public Tipo obtenerTipoRetorno(){
        return this.tipoRetorno;
    }

    public int obtenerPosicion(){
        return this.posicion;
    }
}
