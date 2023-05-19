package Semantico.Nodo;

import Lexico.Token;
import Semantico.Clase;
import Semantico.TablaDeSimbolos;
import Semantico.Funcion.Funcion;
import Semantico.Funcion.Metodo;
import Semantico.Tipo.Tipo;

// Esta clase es la clase padre de todos los nodos
public class Nodo {

    // Esta variable guarda la información del padre de un nodo
    private Nodo padre;
    // Esta variable la utilizamos para comunicar información hacia arriba en los nodos
    public Token aux;
    // Los nodos tienen un tipo que se utilizara en el checkeo de tipos
    protected Tipo tipo;

    protected Funcion metodoContenedor;
    protected Clase claseContenedora;

    public Nodo(Funcion metodoContenedor, Clase claseContenedora){
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

    public void establecerPadre(Nodo padre){
        this.padre = padre;
    }

    public Nodo obtenerPadre(){
        return this.padre;
    }

    // Este metodo sera el encargado de realizar el checkeo de tipos en todos los nodos del AST
    public void checkeoTipos(){
        // La implementacion es responsabilidad de cada uno de los nodos
    }

    
    // Todo nodo debe resolver a algun tipo, este metodo debe implementar la forma de obtenerlo
    public Tipo obtenerTipo(){
        return this.tipo;
    }

    public void establecerTipo(Tipo tipo){
        this.tipo = tipo;
    }

    public void establecerMetodoContenedor(Metodo metodo){
        this.metodoContenedor = metodo;
    }

    public void establecerClaseContenedora(Clase clase){
        this.claseContenedora = clase;
    }

}