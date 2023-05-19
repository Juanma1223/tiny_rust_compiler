package Semantico.Nodo;

import Semantico.Clase;
import Semantico.Funcion.Funcion;

public class NodoSentencia extends Nodo {
    
    public NodoSentencia(Funcion metodoContenedor, Clase claseContenedora){
        super(metodoContenedor,claseContenedora);
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();

        return sb.toString();
    }

}
