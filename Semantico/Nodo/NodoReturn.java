package Semantico.Nodo;

import Semantico.Clase;
import Semantico.Funcion.Funcion;
import Semantico.Funcion.Metodo;

public class NodoReturn extends NodoSentencia {
    private NodoExpresion retorno;

    public NodoReturn(Funcion metodoContenedor, Clase claseContenedora){
        super(metodoContenedor,claseContenedora);
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

    public NodoExpresion agregarExpresion(){
        NodoExpresion hijo = new NodoExpresion(this.metodoContenedor,this.claseContenedora);
        hijo.establecerPadre(this);
        this.retorno = hijo;
        return hijo;
    }

    public void agregarExpresion(NodoExpresion retorno){
        this.retorno = retorno;
        retorno.establecerPadre(this);
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("\"Nodo\":").append("\"NodoRetorno\",").append(System.lineSeparator());
        sb.append("\"hijos\":[").append(System.lineSeparator());
        sb.append("{").append(System.lineSeparator());
        sb.append(retorno.toJson()).append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        sb.append("]").append(System.lineSeparator());
        return sb.toString();
    }
}
