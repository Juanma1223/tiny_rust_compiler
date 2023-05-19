package Semantico.Nodo;

import Lexico.Token;
import Semantico.Clase;
import Semantico.Funcion.Funcion;
import Semantico.Funcion.Metodo;
import Semantico.Tipo.Tipo;

public class NodoVariable extends NodoExpresion {

    Token token;
    Tipo tipo;

    public NodoVariable(Funcion metodoContenedor, Clase claseContenedora, Nodo padre, Token token) {
        super(metodoContenedor, claseContenedora);
        this.token = token;
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

    public NodoVariable(Funcion metodoContenedor, Clase claseContenedora, Token token) {
        super(metodoContenedor, claseContenedora);
        this.token = token;
    }
}
