package Semantico.Nodo;

import java.util.ArrayList;

public class NodoWhile extends NodoSentencia {
    private NodoExpresion condicion;
    private ArrayList<NodoSentencia> sentencias;

    public NodoExpresion agregarExpresion(){
        NodoExpresion hijo = new NodoExpresion();
        hijo.establecerPadre(this);
        this.sentencias.add(hijo);
        return hijo;
    }
}
