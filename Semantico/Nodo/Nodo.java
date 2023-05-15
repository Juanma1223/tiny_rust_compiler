package Semantico.Nodo;

import java.util.ArrayList;

import Lexico.Token;

// Esta clase es la raiz de nuestra estructura de arbol
// Es quien mantiene los subarboles de cada clase
public class Nodo {

    private Nodo padre;
    private ArrayList<NodoClase> clases = new ArrayList<>();
    // Esta variable la utilizamos para comunicar informacion hacia arriba en los nodos
    public Token aux;

    public void establecerPadre(Nodo padre){
        this.padre = padre;
    }

    public Nodo obtenerPadre(){
        return this.padre;
    }

    // Esta clase crea un hijo y lo retorna
    public NodoClase agregarHijo(){
        NodoClase hijo = new NodoClase();
        hijo.establecerPadre(this);
        this.clases.add(hijo);
        return hijo;
    }

}