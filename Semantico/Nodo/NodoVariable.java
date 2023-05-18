package Semantico.Nodo;

import Lexico.Token;
import Semantico.Tipo.Tipo;

public class NodoVariable extends NodoExpresion{

    Token token;
    Tipo tipo;

    public NodoVariable(Nodo padre, Token token){
        this.token = token;
    }

    public NodoVariable(Token token){
        this.token = token;
    }
}
