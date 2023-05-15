package Semantico.Nodo;

import Lexico.Token;

public class NodoVariable extends NodoExpresion{

    Token token;

    public NodoVariable(Nodo padre, Token token){
        this.token = token;
        super.establecerPadre(padre);
    }
}
