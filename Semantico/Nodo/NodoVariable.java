package Semantico.Nodo;

import Lexico.Token;
import Semantico.Tipo.Tipo;

public class NodoVariable extends NodoExpresion{

    Token token;
    Tipo tipo;

    public NodoVariable(Token token){
        this.token = token;
    }

}
