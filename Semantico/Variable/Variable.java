package Semantico.Variable;

import Semantico.Tipo.Tipo;

public class Variable {
    private String nombre;
    private Tipo tipo;
    private int posicion;

    public Variable(String nombre, Tipo tipo){
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String obtenerNombre(){
        return this.nombre;
    }

    public Tipo obtenerTipo(){
        return this.tipo;
    }

    public int obtenerPosicion(){
        return this.posicion;
    }
}
