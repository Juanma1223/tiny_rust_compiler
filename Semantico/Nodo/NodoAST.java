package Semantico.Nodo;

import java.util.ArrayList;

// Esta clase es la raíz de nuestra estructura de árbol
// Es quien mantiene los subarboles de cada clase
public class NodoAST extends Nodo {
    
    private ArrayList<NodoClase> clases = new ArrayList<>();

    // Esta función crea un Nodo Clase hijo, lo agrega al árbol y lo retorna
    public NodoClase agregarHijo(){
        NodoClase hijo = new NodoClase();
        hijo.establecerPadre(this);
        this.clases.add(hijo);
        return hijo;
    }
}
