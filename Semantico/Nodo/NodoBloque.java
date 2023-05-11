package Semantico.Nodo;

import java.util.ArrayList;

public class NodoBloque extends Nodo {
    private ArrayList<NodoSentencia> sentencias;

    public NodoSentencia agregarSentencia(){
        NodoSentencia hijo = new NodoSentencia();
        hijo.establecerPadre(this);
        this.sentencias.add(hijo);
        return agregarSentencia();
    }
}
