package Semantico;

import java.util.HashMap;

import Semantico.Funcion.Funcion;

public class TablaDeSimbolos {
    private Clase claseActual;
    private Funcion metodoActual;
    private HashMap<String, Clase> clases = new HashMap<String, Clase>();

    public Clase obtenerClaseActual(){
        return this.claseActual;
    }

    public void establecerClaseActual(Clase claseActual){
        this.claseActual = claseActual;
    }

    public Funcion obtenerMetodoActual(){
        return this.metodoActual;
    }

    public void establecerMetodoActual(Funcion metodoActual){
        this.metodoActual = metodoActual;
    }

    public Clase obtenerClasePorNombre(String nombreClase){
        return clases.get("nombreClase");
    }

    public void insertarClase(Clase nuevaClase){
        this.clases.put(nuevaClase.obtenerNombre(), nuevaClase);
    }
}
