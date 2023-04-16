package Semantico.Atributo;

import Semantico.Tipo;

public class Atributo {
    private String nombre;
    private Tipo tipo;
    private int posicion;

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
