package Semantico.Funcion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

import Semantico.Variable.Parametro;
import Semantico.Variable.Variable;

public class Funcion {

    private HashMap<String, Parametro> parametros = new LinkedHashMap<String, Parametro>();
    private HashMap<String, Variable> variables = new HashMap<String, Variable>();
    protected String nombre;
        // Guardamos cuanto espacio ocupa en memoria este metodo para generar su
    // registro de activacion
    private int tamMemoria;

    public Parametro obtenerParametroPorNombre(String nombre) {
        return this.parametros.get(nombre);
    }

    public HashMap<String, Parametro> obtenerParametros() {
        return this.parametros;
    }

    public HashMap<String, Variable> obtenerVariables() {
        return this.variables;
    }

    public String obtenerNombre() {
        return this.nombre;
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

    public ArrayList<Variable> obtenerVarsOrdenadas(){
        ArrayList<Variable> ordenados = new ArrayList<Variable>();
        ArrayList<Integer> aux = new ArrayList<Integer>();
        // Insertamos las posiciones en un arreglo auxiliar para luego ordenarlas
        for(HashMap.Entry<String,Variable> variable : this.variables.entrySet()){
            aux.add(variable.getValue().obtenerPosicion());
        }
        Collections.sort(aux);
        // Insertamos los parametros ordenados por posicion en un LinkedHashMap
        for (int num : aux){
            for (HashMap.Entry<String, Variable> variable : this.variables.entrySet()){
                if(variable.getValue().obtenerPosicion() == num){
                    ordenados.add(variable.getValue());
                }
            }
        }
        return ordenados;
    }

        // Calculamos cuanto espacio en memoria se requiere para almacenar el RA de este
    // metodo
    public void calcularMemoria() {
        // Calculamos el espacio de las variables locales segun su tipo
        int memVariables = 0;
        for (HashMap.Entry<String, Variable> variable : obtenerVariables().entrySet()) {
            switch (variable.getValue().obtenerTipo().obtenerTipo()) {
                // Sumamos en bytes, todos los tipos
                case "Str":
                    // Esto esta sujeto a cambio segun cuanto espacio les demos para los strings
                    memVariables = memVariables + 4;
                    break;
                case "Array":
                    // Aca vamos a tener que implementar el calculo de tamaño dentro del tipo
                    // arreglo en funcion de
                    // la cantidad de elementos y su tipo
                    break;
                default:
                    memVariables = memVariables + 4;
                    break;
            }
        }

        // Sumamos los valores fijos del registro de activacion
        // Direccion al puntero self = 4 bytes
        // Direccion al RA del llamador = 4 bytes
        // Direccion al puntero de retorno = 4 bytes
        int memTotal = 12 + this.memoriaParametros() + memVariables;
        this.tamMemoria = memTotal;
    }

    // Este metodo retorna la memoria que ocupan los parametros en el RA
    public int memoriaParametros() {
        int memParametros = 0;
        for (HashMap.Entry<String, Parametro> parametro : obtenerParametros().entrySet()) {
            switch (parametro.getValue().obtenerTipo().obtenerTipo()) {
                // Sumamos en bytes, todos los tipos
                case "Str":
                    // Esto esta sujeto a cambio segun cuanto espacio les demos para los strings
                    memParametros = memParametros + 4;
                    break;
                case "Array":
                    // Aca vamos a tener que implementar el calculo de tamaño dentro del tipo
                    // arreglo en funcion de
                    // la cantidad de elementos y su tipo
                    break;
                default:
                    memParametros = memParametros + 4;
                    break;
            }
        }
        return memParametros;
    }

    public int obtenerTamMemoria() {
        if (this.tamMemoria == 0) {
            calcularMemoria();
        }
        return this.tamMemoria;
    }

    // Este metodo calcula el offset de un parametro dentro del RA del metodo por su
    // nombre
    public int offsetParametro(int posicionParametro) {
        int offset = 4;
        ArrayList<Parametro> paramOrdenados = this.obtenerParamsOrdenados();
        for (int i = 0; i < posicionParametro; i++) {
            Parametro var = paramOrdenados.get(i);
            switch (var.obtenerTipo().obtenerTipo()) {
                // Sumamos en bytes, todos los tipos
                case "Str":
                    // Esto esta sujeto a cambio segun cuanto espacio les demos para los strings
                    offset += 4;
                    break;
                case "Array":
                    // Aca vamos a tener que implementar el calculo de tamaño dentro del tipo
                    // arreglo en funcion de
                    // la cantidad de elementos y su tipo
                    break;
                default:
                    offset += 4;
                    break;
            }
        }
        return offset;
    }

    // Este metodo calcula el offset de una variable dentro del RA del metodo por su
    // nombre
    public int offsetVariable(String nombreVariable) {
        Variable variable = this.obtenerVariablePorNombre(nombreVariable);
        // Las variables se encuentran luego de los parametros del metodo y el retorno
        // dentro del RA
        int offset = 4 + this.memoriaParametros();
        // Desplazamos la memoria tanto como el tipo y posicion de la variable lo
        // requiera
        ArrayList<Variable> varOrdenadas = this.obtenerVarsOrdenadas();
        for (int i = 0; i < variable.obtenerPosicion(); i++) {
            Variable var = varOrdenadas.get(i);
            switch (var.obtenerTipo().obtenerTipo()) {
                // Sumamos en bytes, todos los tipos
                case "Str":
                    // Esto esta sujeto a cambio segun cuanto espacio les demos para los strings
                    offset += 4;
                    break;
                case "Array":
                    // Aca vamos a tener que implementar el calculo de tamaño dentro del tipo
                    // arreglo en funcion de
                    // la cantidad de elementos y su tipo
                    break;
                default:
                    offset += 4;
                    break;
            }
        }
        return offset;
    }
}

