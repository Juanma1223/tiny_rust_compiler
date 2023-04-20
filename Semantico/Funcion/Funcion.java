package Semantico.Funcion;

import java.util.HashMap;

import Semantico.Variable.Parametro;
import Semantico.Variable.Variable;

public class Funcion {

    private HashMap<String,Parametro> parametros = new HashMap<String,Parametro>();
    private HashMap<String,Variable> variables = new HashMap<String,Variable>();

    public Parametro obtenerParametroPorNombre(String nombre){
        return this.parametros.get(nombre);
    }

    public void insertarParametro(Parametro parametro){
        this.parametros.put(parametro.obtenerNombre(), parametro);
    }

    public Variable obtenerVariablePorNombre(String nombre){
        return this.variables.get(nombre);
    }

    public void insertarVariable(Variable variable){
        this.variables.put(variable.obtenerNombre(), variable);
    }
    
}
