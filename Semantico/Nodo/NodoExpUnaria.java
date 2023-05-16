package Semantico.Nodo;

import Lexico.Token;

public class NodoExpUnaria extends NodoExpresion {
    private NodoExpresion ladoDer;
    private Token operador;

    public void establecerLadoDer(NodoExpresion ladoDer){
        this.ladoDer = ladoDer;
    }

    public void establecerOp(Token operador){
        this.operador = operador;
    }
}