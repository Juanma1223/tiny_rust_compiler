package Lexico.Automata;

import Lexico.Token;

/* Superclase de la cual derivan los automatas encargados de reconocer
cada uno de los Tokens de tinyrust+ */
public class Automata {
    // Llevamos un conteo de la fila y la columna en las que estamos analizando el token actual
    private int filaActual;
    private int columnaActual;

    /* Esta funcion recibe como argumento el codigo fuente que aun no se ha leido
     y devuelve el primer token que encuentra con todas sus propiedades, consumiendolo
     del codigo fuente, debe ser reimplementada segun el tipo de token que se quiera reconocer */
    public Token reconocerToken(String codigoFuente){
        return new Token();
    }

    public int obtenerFila(){
        return this.filaActual;
    }

    public int obtenerColumna(){
        return this.columnaActual;
    }
}
