package Lexico;

/* La clase Token es la encargada de representar los tokens del lenguaje
 * en conjunto con todas las propiedades necesarias para posteriores fases del compilador
 */

public class Token {
    private String tipoToken;
    private String lexema;
    private int fila;
    private int columna;

    public String obtenerToken(){
        return this.tipoToken;
    }

    public void establecerToken(String tipoToken){
        this.tipoToken = tipoToken;
    }

    public String obtenerLexema(){
        return this.lexema;
    }

    public void establecerLexema(String lexema){
        this.lexema = lexema;
    }

    public int obtenerFila(){
        return this.fila;
    }

    public int obtenerColumna(){
        return this.columna;
    }

}
