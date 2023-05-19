package Semantico.Nodo;

import java.util.ArrayList;

import Semantico.Clase;
import Semantico.Funcion.Funcion;
import Semantico.Funcion.Metodo;

public class NodoBloque extends Nodo {
    private ArrayList<NodoSentencia> sentencias = new ArrayList<>();;

    // Las sentencias son un tipo polimorfico que admite if, while, asignacion, etc


    public NodoBloque(Funcion metodoContenedor, Clase claseContenedora){
        super(metodoContenedor,claseContenedora);
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

    public NodoSentencia agregarSentencia() {
        NodoSentencia hijo = new NodoSentencia(this.metodoContenedor,this.claseContenedora);
        hijo.establecerPadre(this);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.sentencias.add(hijo);
        return hijo;
    }

    public NodoAsignacion agregarAsignacion() {
        NodoAsignacion hijo = new NodoAsignacion(this.metodoContenedor,this.claseContenedora);
        hijo.establecerPadre(this);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.sentencias.add(hijo);
        return hijo;
    }

    public NodoWhile agregarWhile() {
        NodoWhile hijo = new NodoWhile(this.metodoContenedor,this.claseContenedora);
        hijo.establecerPadre(this);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.sentencias.add(hijo);
        return hijo;
    }

    public NodoIf agregarIf() {
        NodoIf hijo = new NodoIf(this.metodoContenedor,this.claseContenedora);
        hijo.establecerPadre(this);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.sentencias.add(hijo);
        return hijo;
    }

    public NodoReturn agregarReturn() {
        NodoReturn hijo = new NodoReturn(this.metodoContenedor,this.claseContenedora);
        hijo.establecerPadre(this);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.sentencias.add(hijo);
        return hijo;
    }

    public void agregarReturn(NodoReturn hijo) {
        this.sentencias.add(hijo);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
        hijo.establecerPadre(this);
    }

    public NodoExpresion agregarExpresion() {
        NodoExpresion hijo = new NodoExpresion(this.metodoContenedor,this.claseContenedora);
        hijo.establecerPadre(this);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.sentencias.add(hijo);
        return hijo;
    }

    // Sobrecarga para poder insertar expresiones de cualquier tipo
    public void agregarExpresion(NodoExpresion hijo) {
        hijo.establecerPadre(this);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.sentencias.add(hijo);
    }

    @Override
    public void checkeoTipos() {
        // Cada subclase de sentencia debe implementar su propio checkeo de tipos
        sentencias.forEach((sentencia) -> sentencia.checkeoTipos());
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("\"Sentencias\":[").append(System.lineSeparator());
        sb.append("{").append(System.lineSeparator());
        int s = sentencias.size()-1;
        if (s>0) {
            for (int i = 0; i < s; i++) {
                sb.append(sentencias.get(i).toJson() + ",").append(System.lineSeparator());
            }
            sb.append(sentencias.get(s).toJson()).append(System.lineSeparator());
        } else {
            if (s==0) {
                sb.append(sentencias.get(s).toJson()).append(System.lineSeparator());
            }
        }
        sb.append("}").append(System.lineSeparator());
        sb.append("]").append(System.lineSeparator());
        return sb.toString();
    }
}
