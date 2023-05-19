package Semantico.Funcion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import Semantico.Variable.Parametro;
import Semantico.Variable.Variable;

public class Funcion {

    private HashMap<String, Parametro> parametros = new LinkedHashMap<String, Parametro>();
    private HashMap<String, Variable> variables = new HashMap<String, Variable>();

    public Parametro obtenerParametroPorNombre(String nombre) {
        return this.parametros.get(nombre);
    }

    public HashMap<String, Parametro> obtenerParametros() {
        return this.parametros;
    }

    public HashMap<String, Variable> obtenerVariables() {
        return this.variables;
    }

    public void insertarParametro(Parametro parametro) {
        // Al insertar el parametro tambien queremos guardar su posicion en la lista de
        // parametros
        int posicion = parametros.size();
        // Calculamos la posicion en base a la cantidad de parametros que hay guardados
        // actualmente
        parametro.establecerPosicion(posicion);
        this.parametros.put(parametro.obtenerNombre(), parametro);
    }

    public Variable obtenerVariablePorNombre(String nombre) {
        return this.variables.get(nombre);
    }

    public void insertarVariable(Variable variable) {
        // Al insertar la variable tambien queremos guardar su posicion en la lista de
        // variables del bloque
        int posicion = variables.size();
        // Calculamos la posicion en base a la cantidad de variables que hay guardados
        // actualmente en este metodo
        variable.establecerPosicion(posicion);
        this.variables.put(variable.obtenerNombre(), variable);
    }

    // Devuelve true si el atributo ya ha sido declarado en esta clase
    public Boolean variableYaDeclarada(String nombre) {
        Variable atrib = this.obtenerVariablePorNombre(nombre);
        if (atrib == null) {
            return false;
        } else {
            return true;
        }
    }

    // Devuelve true si el parametro ya ha sido declarado en esta clase
    public Boolean parametroYaDeclarado(String nombre) {
        Variable atrib = this.obtenerParametroPorNombre(nombre);
        if (atrib == null) {
            return false;
        } else {
            return true;
        }
    }

    // Retorna los parametros en el orden de insercion
    public ArrayList<Parametro> obtenerParamsOrdenados(){
        ArrayList<Parametro> ordenados = new ArrayList<Parametro>();
        ArrayList<Integer> aux = new ArrayList<Integer>();
        // Insertamos las posiciones en un arreglo auxiliar para luego ordenarlas
        for(HashMap.Entry<String,Parametro> parametro : this.parametros.entrySet()){
            aux.add(parametro.getValue().obtenerPosicion());
        }
        Collections.sort(aux);
        // Insertamos los parametros ordenados por posicion en un LinkedHashMap
        for (int num : aux){
            for (HashMap.Entry<String, Parametro> parametro : this.parametros.entrySet()){
                if(parametro.getValue().obtenerPosicion() == num){
                    ordenados.add(parametro.getValue());
                }
            }
        }
        return ordenados;
    }

}
