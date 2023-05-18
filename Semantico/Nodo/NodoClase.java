package Semantico.Nodo;

import java.util.ArrayList;

// Esta clase mantiene los subarboles de cada método
// dentro de un Nodo Clase
public class NodoClase extends Nodo {
    
    private ArrayList<NodoMetodo> metodos = new ArrayList<>();

    public NodoMetodo agregarMetodo(){
        NodoMetodo hijo = new NodoMetodo();
        this.metodos.add(hijo);
        hijo.establecerPadre(this);
        return hijo;
    }

    @Override
    public void checkeoTipos(){
        metodos.forEach((metodo) -> metodo.checkeoTipos());
    }
}
