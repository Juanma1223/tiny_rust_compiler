package Semantico.Nodo;

import Lexico.Token;

public class NodoExpUnaria extends NodoExpresion {
    private NodoExpresion ladoDer;
    private Token operador;

    public NodoExpUnaria(NodoExpresion ladoDer, Token operador) {
        this.ladoDer = ladoDer;
        this.operador = operador;
    }
}