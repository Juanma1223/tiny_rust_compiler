package Semantico.Nodo;

import Semantico.Clase;
import Semantico.ErrorSemantico;
import Semantico.Funcion.Funcion;
import Semantico.Tipo.Tipo;

public class NodoWhile extends NodoSentencia {
    private NodoExpresion condicion;
    private NodoBloque bloqueW;
    

    public NodoWhile(Funcion metodoContenedor, Clase claseContenedora){
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

    public void agregarCondicion(NodoExpresion condicion){
        this.condicion = condicion;
        condicion.establecerPadre(this);
    }
    
    public NodoBloque agregarBloqueW(){
        NodoBloque hijo = new NodoBloque(this.metodoContenedor,this.claseContenedora);
        hijo.establecerPadre(this);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.bloqueW = hijo;
        return hijo;
    }

    @Override
    public void checkeoTipos(){
        Tipo tipo = condicion.obtenerTipo();
        if(!tipo.obtenerTipo().equals("Bool")){
            new ErrorSemantico(this.aux.obtenerFila(), this.aux.obtenerColumna(), "El tipo de la condicion while no es booleano!", true);
        }
        this.establecerTipo(tipo);
        bloqueW.checkeoTipos();
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("\"Nodo\":").append("\"NodoWhile\",").append(System.lineSeparator());
        sb.append("\"condicion\":{").append(System.lineSeparator());
        sb.append(condicion.toJson()).append(System.lineSeparator());
        sb.append("},").append(System.lineSeparator());
        sb.append("\"hijos\":[").append(System.lineSeparator());
        sb.append("{").append(System.lineSeparator());
        if (bloqueW != null) {
            sb.append(bloqueW.toJson()).append(System.lineSeparator());
        }
        sb.append("}").append(System.lineSeparator());
        sb.append("]").append(System.lineSeparator());
        return sb.toString();
    }
}
