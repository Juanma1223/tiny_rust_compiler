package Semantico.Funcion;

import Semantico.Tipo.Tipo;

public class Metodo extends Funcion{
    private String nombre;
    private boolean esEstatico;
    private Tipo tipoRetorno;
    private int posicion;

    public Metodo(String nombre, boolean esEstatico){
        this.nombre = nombre;
        this.esEstatico = esEstatico;
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

    public void establecerTipoRetorno(Tipo tipoRetorno){
        this.tipoRetorno = tipoRetorno;
    }

    public int obtenerPosicion(){
        return this.posicion;
    }
}
