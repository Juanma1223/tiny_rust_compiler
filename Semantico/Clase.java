package Semantico;

import java.util.HashMap;

import Semantico.Variable.Atributo;
import Semantico.Funcion.Metodo;
import Semantico.Funcion.Constructor;

public class Clase {
    private String nombre;
    private String heredaDe;
    private HashMap<String, Atributo> atributos = new HashMap<String, Atributo>();
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

    public Atributo obtenerAtributoPorNombre(String nombre){
        return this.atributos.get(nombre);
    }

    public Metodo obtenerMetodoPorNombre(String nombre){
        return this.metodos.get(nombre);
    }

    public void insertarMetodo(Metodo metodo){
        this.metodos.put(metodo.obtenerNombre(), metodo);
    }

    public void insertarAtributo(Atributo atributo){
        this.atributos.put(atributo.obtenerNombre(), atributo);
    }

}
