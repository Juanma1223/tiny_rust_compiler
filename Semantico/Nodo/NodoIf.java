package Semantico.Nodo;

import Semantico.Clase;
import Semantico.ErrorSemantico;
import Semantico.Funcion.Funcion;
import Semantico.Tipo.Tipo;

public class NodoIf extends NodoSentencia {
    private NodoExpresion condicion;
    private NodoBloque sentenciaThen;
    private NodoBloque sentenciaElse;

    public NodoIf(Funcion metodoContenedor, Clase claseContenedora){
        super(metodoContenedor,claseContenedora);
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

    public NodoExpresion agregarCondicion(){
        NodoExpresion hijo = new NodoExpresion(this.metodoContenedor,this.claseContenedora);
        hijo.establecerPadre(this);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.condicion = hijo;
        return hijo;
    }

    public void agregarCondicion(NodoExpresion hijo){
        hijo.establecerPadre(this);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.condicion = hijo;
    }

    public NodoBloque agregarSentenciaThen(){
        NodoBloque hijo = new NodoBloque(this.metodoContenedor,this.claseContenedora);
        hijo.establecerPadre(this);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.sentenciaThen = hijo;
        return hijo;
    }

    public NodoBloque agregarSentenciaElse(){
        NodoBloque hijo = new NodoBloque(this.metodoContenedor,this.claseContenedora);
        hijo.establecerPadre(this);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.sentenciaElse = hijo;
        return hijo;
    }

    @Override
    public void checkeoTipos(){
        condicion.checkeoTipos();
        Tipo tipo = condicion.obtenerTipo();
        if(!tipo.obtenerTipo().equals("Bool")){
            new ErrorSemantico(this.aux.obtenerFila(), this.aux.obtenerColumna(), "El tipo de la condicion if no es booleano!", true);
        }
        this.establecerTipo(tipo);
        sentenciaThen.checkeoTipos();
        sentenciaElse.checkeoTipos();
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("\"Nodo\":").append("\"NodoIf\",").append(System.lineSeparator());
        sb.append("\"condicion\":{").append(System.lineSeparator());
        sb.append(condicion.toJson()).append(System.lineSeparator());
        sb.append("},").append(System.lineSeparator());
        sb.append("\"hijos\":[").append(System.lineSeparator());
        sb.append("{").append(System.lineSeparator());
        if (sentenciaThen != null) {
            sb.append(sentenciaThen.toJson()).append(System.lineSeparator());
        }
        sb.append("}").append(System.lineSeparator());
        if (sentenciaElse != null) {
            sb.append(",").append(System.lineSeparator());
            sb.append("{").append(System.lineSeparator());
            sb.append(sentenciaElse.toJson()).append(System.lineSeparator());
            sb.append("}").append(System.lineSeparator());
        }
        sb.append("]").append(System.lineSeparator());
        return sb.toString();
    }

    @Override
    public String genCodigo() {
        StringBuilder sb = new StringBuilder();
        int numIf = tablaDeSimbolos.obtenerLabel();
        sb.append(condicion.genCodigo()).append(System.lineSeparator());
        sb.append("bne $a0, 1, else"+numIf).append(System.lineSeparator());
        sb.append(sentenciaThen.genCodigo()).append(System.lineSeparator());
        sb.append("j endif"+numIf).append(System.lineSeparator());
        sb.append("else"+numIf+":").append(System.lineSeparator());
        sb.append(sentenciaElse.genCodigo()).append(System.lineSeparator());
        sb.append("endif"+numIf+":").append(System.lineSeparator());
        return sb.toString();
    }
}
