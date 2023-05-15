package Semantico.Nodo;

import Lexico.Token;

public class NodoLiteral extends NodoExpresion{

    private Token token;

    public NodoLiteral(Token token){
        this.token = token;
    }
    
}
