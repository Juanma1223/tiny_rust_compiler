package Lexico.Automata;

import java.io.BufferedReader;
import java.io.File;

import Lexico.Token;

/* Superclase de la cual derivan los automatas encargados de reconocer
cada uno de los Tokens de tinyrust+ */
public class Automata {
    // Llevamos un conteo de la fila y la columna en las que estamos analizando el token actual
    private int filaActual;
    private int columnaActual;

    public Automata(int filaActual, int columnaActual){
        this.filaActual = filaActual;
        this.columnaActual = columnaActual;
    }

    /* Esta funcion recibe como parametro el archivo con el codigo fuente y devuelve el token
     * que se encuentra en la fila y columna actualmente siendo analizada
    */
    public Token reconocerToken(BufferedReader lector){
        return new Token();
    }

    public int obtenerFila(){
        return this.filaActual;
    }

    public int obtenerColumna(){
        return this.columnaActual;
    }
}
