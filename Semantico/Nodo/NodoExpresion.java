package Semantico.Nodo;

import Semantico.Clase;
import Semantico.Funcion.Funcion;

public class NodoExpresion extends NodoSentencia {
    
    NodoExpresion encadenado;

    public void establecerEncadenado(NodoExpresion encadenado){
        encadenado.establecerPadre(this);
        this.encadenado = encadenado;
    }
    
    public NodoExpresion(Funcion metodoContenedor, Clase claseContenedora){
        super(metodoContenedor,claseContenedora);
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

}
