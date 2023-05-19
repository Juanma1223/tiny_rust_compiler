package Semantico.Nodo;

public class NodoExpresion extends NodoSentencia {
    
    NodoExpresion encadenado;

    public void establecerEncadenado(NodoExpresion encadenado){
        encadenado.establecerPadre(this);
        this.encadenado = encadenado;
    }
}
