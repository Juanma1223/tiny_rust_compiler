package Semantico.Funcion;

import java.util.HashMap;

import Semantico.Tipo.Tipo;
import Semantico.Variable.Parametro;
import Semantico.Variable.Variable;

public class Metodo extends Funcion {
    private boolean esEstatico;
    private Tipo tipoRetorno;
    private int posicion;
    private int fila, columna;
    // Guardamos cuanto espacio ocupa en memoria este metodo para generar su
    // registro de activacion
    private int tamMemoria;

    public Metodo(String nombre, boolean esEstatico) {
        this.nombre = nombre;
        this.esEstatico = esEstatico;
    }

    public Metodo(String nombre, boolean esEstatico, Tipo tipoRetorno) {
        this.nombre = nombre;
        this.esEstatico = esEstatico;
        this.tipoRetorno = tipoRetorno;
    }

    public Metodo(String nombre, boolean esEstatico, int fila, int columna) {
        this.nombre = nombre;
        this.esEstatico = esEstatico;
        this.fila = fila;
        this.columna = columna;
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(System.lineSeparator());
        sb.append("\"nombre\":").append("\"" + nombre + "\",").append(System.lineSeparator());
        sb.append("\"static\":").append("\"" + esEstatico + "\",").append(System.lineSeparator());
        sb.append("\"retorno\":").append("\"" + tipoRetorno.obtenerTipo() + "\",").append(System.lineSeparator());
        sb.append("\"posicion\":").append("\"" + posicion + "\",").append(System.lineSeparator());
        sb.append("\"paramF\":[").append(System.lineSeparator());
        int i = 1;
        for (HashMap.Entry<String, Parametro> parametro : super.obtenerParametros().entrySet()) {
            if (i < super.obtenerParametros().size()) {
                sb.append(parametro.getValue().toJson() + ",").append(System.lineSeparator());
            } else {
                sb.append(parametro.getValue().toJson()).append(System.lineSeparator());
            }
            i++;
        }
        sb.append("]").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        return sb.toString();
    }

    public boolean obtenerEsEstatico() {
        return this.esEstatico;
    }

    public Tipo obtenerTipoRetorno() {
        return this.tipoRetorno;
    }

    public void establecerTipoRetorno(Tipo tipoRetorno) {
        this.tipoRetorno = tipoRetorno;
    }

    public int obtenerPosicion() {
        return this.posicion;
    }

    public void establecerPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int obtenerFila() {
        return this.fila;
    }

    public int obtenerColumna() {
        return this.columna;
    }

    // Calculamos cuanto espacio en memoria se requiere para almacenar el RA de este
    // metodo
    public void calcularMemoria() {
        // Calculamos el espacio de las variables locales segun su tipo
        int memVariables = 0;
        for (HashMap.Entry<String, Variable> variable : super.obtenerVariables().entrySet()) {
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
        int memParametros = 0;
        for (HashMap.Entry<String, Parametro> parametro : super.obtenerParametros().entrySet()) {
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
        // Sumamos los valores fijos del registro de activacion
        // Direccion de retornos = 4 bytes
        // Direccion al puntero self = 4 bytes
        // Direccion al RA del llamador = 4 bytes
        // Direccion al puntero de retorno = 4 bytes
        int memTotal = 16 + memParametros + memVariables;
        this.tamMemoria = memTotal;
    }
    
    public int obtenerTamMemoria(){
        if(this.tamMemoria == 0){
            calcularMemoria();
        }
        return this.tamMemoria;
    }
}
