package Semantico.Nodo;

import Lexico.Token;

public class NodoVariable extends Nodo{

    Token token;

    public NodoVariable(Nodo padre, Token token){
        this.token = token;
        super.establecerPadre(padre);
    }
}
