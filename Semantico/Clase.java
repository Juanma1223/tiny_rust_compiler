package Semantico;

import java.util.HashMap;

import Semantico.Atributo.Variable;
import Semantico.Funcion.Metodo;
import Semantico.Funcion.Constructor;

public class Clase {
    private String nombre;
    private String heredaDe;
    private HashMap<String, Variable> variables = new HashMap<String, Variable>();
    private HashMap<String, Metodo> metodos = new HashMap<String, Metodo>();
    private Constructor constructor;

    public Clase(String nombre){
        this.nombre = nombre;
    }
    
    public String obtenerNombre() {
        return this.nombre;
    }

    public void establecerHerencia(String heredaDe){
        this.heredaDe = heredaDe;
    }

    public Constructor obtenerConstructor(){
        return this.constructor;
    }

    public void establecerConstructor(Constructor constructor){
        this.constructor = constructor;
    }

    public Variable obtenerVariablePorNombre(String nombre){
        return this.variables.get(nombre);
    }

    public Metodo obtenerMetodoPorNombre(String nombre){
        return this.metodos.get(nombre);
    }

    public void insertarMetodo(Metodo metodo){
        this.metodos.put(metodo.obtenerNombre(), metodo);
    }

    public void insertarVariable(Variable variable){
        this.variables.put(variable.obtenerNombre(), variable);
    }


}
