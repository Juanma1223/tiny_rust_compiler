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
        if(this.encadenado != null) {
            sb.append("\"valor\":").append("\""+token.obtenerLexema()+"\",").append(System.lineSeparator());
            sb.append("\"encadenado\":{").append(System.lineSeparator());
            sb.append(this.encadenado.toJson()).append(System.lineSeparator());
            sb.append("}").append(System.lineSeparator());
        } else {
            sb.append("\"valor\":").append("\""+token.obtenerLexema()+"\"").append(System.lineSeparator());
        }
        return sb.toString();
    }

}
