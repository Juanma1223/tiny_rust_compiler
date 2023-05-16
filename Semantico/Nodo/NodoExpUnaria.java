package Semantico.Nodo;

import Lexico.Token;

public class NodoExpUnaria extends NodoExpresion {
    private NodoExpresion ladoDer;
    private Token operador;

    public void establecerLadoDer(NodoExpresion ladoDer){
        ladoDer.establecerPadre(this);
        this.ladoDer = ladoDer;
    }

    public void establecerOp(Token operador){
        this.operador = operador;
    }
}