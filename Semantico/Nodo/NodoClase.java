package Semantico.Nodo;

import java.util.ArrayList;

public class NodoClase extends Nodo {
    private ArrayList<NodoMetodo> metodos;

    public NodoMetodo agregarMetodo(){
        NodoMetodo hijo = new NodoMetodo();
        this.metodos.add(hijo);
        hijo.establecerPadre(this);
        return hijo;
    }
}
