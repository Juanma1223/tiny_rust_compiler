package Semantico.Funcion;

import java.util.HashMap;

import Semantico.Variable.Parametro;

public class Funcion {

    private HashMap<String,Parametro> parametros = new HashMap<String,Parametro>();

    public Parametro obtenerParametroPorNombre(String nombre){
        return this.parametros.get(nombre);
    }

    public void insertarParametro(Parametro parametro){
        this.parametros.put(parametro.obtenerNombre(), parametro);
    }
    
}
