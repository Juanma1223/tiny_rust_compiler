package Semantico.Nodo;

import Lexico.Token;
import Semantico.Clase;
import Semantico.Funcion.Funcion;
import Semantico.Tipo.Tipo;

public class NodoVariable extends NodoExpresion {

    Token token;
    Tipo tipo;

    public NodoVariable(Funcion metodoContenedor, Clase claseContenedora, Token token) {
        super(metodoContenedor, claseContenedora);
        this.token = token;
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("\"Nodo\":").append("\"NodoVariable\",").append(System.lineSeparator());
        sb.append("\"valor\":").append("\""+token.obtenerLexema()+"\"").append(System.lineSeparator());
        return sb.toString();
    }

}
