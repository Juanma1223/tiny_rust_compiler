package Semantico.Nodo;

import Lexico.Token;

public class NodoExpBinaria extends NodoExpresion {
    private Nodo ladoIzq;
    private Nodo ladoDer;
    private Token operador;

    public void establecerLadoIzq(Nodo ladoIzq){
        ladoIzq.establecerPadre(this);
        this.ladoIzq = ladoIzq;
    }

    public void establecerLadoDer(Nodo ladoDer){
        ladoDer.establecerPadre(this);
        this.ladoDer = ladoDer;
    }

    public void establecerOp(Token operador){
        this.operador = operador;
    }
}
