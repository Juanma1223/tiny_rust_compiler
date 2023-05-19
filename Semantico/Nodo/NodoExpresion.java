package Semantico.Nodo;

import Semantico.Clase;
import Semantico.Funcion.Funcion;
import Semantico.Funcion.Metodo;

public class NodoExpresion extends NodoSentencia {

    public NodoExpresion(Funcion metodoContenedor, Clase claseContenedora){
        super(metodoContenedor,claseContenedora);
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

}
