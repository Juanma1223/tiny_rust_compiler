package Semantico.Nodo;

import Lexico.Token;

public class NodoExpBinaria extends NodoExpresion {
    private Nodo ladoIzq;
    private Nodo ladoDer;
    private Token operador;

    public NodoExpBinaria(Nodo ladoIzq, Nodo ladoDer, Token operador){
        this.ladoIzq = ladoIzq;
        this.ladoDer = ladoDer;
        this.operador = operador;
    }

    public void establecerLadoIzq(Nodo ladoIzq){
        this.ladoIzq = ladoIzq;
    }

    public void establecerLadoDer(Nodo ladoDer){
        this.ladoDer = ladoDer;
    }

    public void establecerOp(Token operador){
        this.operador = operador;
    }
}
