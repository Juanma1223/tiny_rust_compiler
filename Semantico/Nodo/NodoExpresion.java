package Semantico.Nodo;

import Semantico.Clase;
import Semantico.Funcion.Funcion;

public class NodoExpresion extends NodoSentencia {
    
    NodoExpresion encadenado;

    public void establecerEncadenado(NodoExpresion encadenado){
        encadenado.establecerPadre(this);
        encadenado.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.encadenado = encadenado;
    }
    
    public NodoExpresion(Funcion metodoContenedor, Clase claseContenedora){
        super(metodoContenedor,claseContenedora);
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

    @Override
    public void checkeoTipos(){
        if(encadenado != null){
            this.encadenado.checkeoTipos();
        }
    }

    // @Override
    // public String genCodigo() {
    //     StringBuilder sb = new StringBuilder();
    //     if(encadenado != null){
    //         sb.append(this.encadenado.genCodigo()).append(System.lineSeparator());
    //     }
    //     return sb.toString();
    // }

}
