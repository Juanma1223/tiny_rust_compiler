package Semantico.Nodo;

import java.util.ArrayList;

public class NodoBloque extends Nodo {
    private ArrayList<NodoSentencia> sentencias = new ArrayList<>();;

    // Las sentencias son un tipo polimorfico que admite if, while, asignacion, etc

    public NodoSentencia agregarSentencia(){
        NodoSentencia hijo = new NodoSentencia();
        hijo.establecerPadre(this);
        this.sentencias.add(hijo);
        return hijo;
    }

    public NodoAsignacion agregarAsignacion(){
        NodoAsignacion hijo = new NodoAsignacion();
        hijo.establecerPadre(this);
        this.sentencias.add(hijo);
        return hijo;
    }

    public NodoWhile agregarWhile(){
        NodoWhile hijo = new NodoWhile();
        hijo.establecerPadre(this);
        this.sentencias.add(hijo);
        return hijo;
    }

    public NodoIf agregarIf(){
        NodoIf hijo = new NodoIf();
        hijo.establecerPadre(this);
        this.sentencias.add(hijo);
        return hijo;
    }

    public NodoReturn agregarReturn(){
        NodoReturn hijo = new NodoReturn();
        hijo.establecerPadre(this);
        this.sentencias.add(hijo);
        return hijo;
    }

    public NodoExpresion agregarExpresion(){
        NodoExpresion hijo = new NodoExpresion();
        hijo.establecerPadre(this);
        this.sentencias.add(hijo);
        return hijo;
    }
}
