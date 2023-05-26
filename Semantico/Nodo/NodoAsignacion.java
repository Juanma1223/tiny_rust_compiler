package Semantico.Nodo;

import Lexico.Token;
import Semantico.Clase;
import Semantico.ErrorSemantico;
import Semantico.Funcion.Funcion;
import Semantico.Tipo.Tipo;
import Semantico.Tipo.TipoArreglo;
import Semantico.Tipo.TipoReferencia;

public class NodoAsignacion extends NodoExpresion {
    private NodoExpresion ladoIzq;
    private NodoExpresion ladoDer;
    private Token operador;

    public NodoAsignacion(Funcion metodoContenedor, Clase claseContenedora) {
        super(metodoContenedor, claseContenedora);
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

    public void establecerLadoIzq(NodoExpresion ladoIzq) {
        ladoIzq.establecerPadre(this);
        ladoIzq.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.ladoIzq = ladoIzq;
    }

    public void establecerLadoDer(NodoExpresion ladoDer) {
        ladoDer.establecerPadre(this);
        ladoDer.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.ladoDer = ladoDer;
    }

    public void establecerOp(Token operador) {
        this.operador = operador;
    }

    @Override
    public void checkeoTipos() {
        ladoIzq.checkeoTipos();
        ladoDer.checkeoTipos();
        Tipo tipoIzq = ladoIzq.obtenerTipo();
        Tipo tipoDer = ladoDer.obtenerTipo();
        if (ladoDer instanceof NodoArreglo) {
            if (!(tipoIzq instanceof TipoArreglo)) {
                new ErrorSemantico(operador.obtenerFila(), operador.obtenerColumna(),
                        "Los tipos de la asignacion no coinciden!", true);
            }
        }
        // Verificamos el caso de polimorfismo
        if (tipoIzq instanceof TipoReferencia && tipoDer instanceof TipoReferencia) {
            Clase infoClaseDer = tablaDeSimbolos.obtenerClasePorNombre(tipoDer.obtenerTipo());
            if (!infoClaseDer.esSubclaseDe(tipoIzq.obtenerTipo())) {
                new ErrorSemantico(operador.obtenerFila(), operador.obtenerColumna(),
                        "Los tipos de la asignacion no coinciden!", true);
            }
        }else{
            if (!tipoIzq.obtenerTipo().equals(tipoDer.obtenerTipo())) {
                new ErrorSemantico(operador.obtenerFila(), operador.obtenerColumna(),
                        "Los tipos de la asignacion no coinciden!", true);
            }
        }
        this.establecerTipo(tipoDer);
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("\"Nodo\":").append("\"NodoAsignacion\",").append(System.lineSeparator());
        sb.append("\"operador\":").append("\"" + operador.obtenerLexema() + "\",").append(System.lineSeparator());
        sb.append("\"hijos\":[").append(System.lineSeparator());
        sb.append("{").append(System.lineSeparator());
        sb.append(ladoIzq.toJson()).append(System.lineSeparator());
        sb.append("},").append(System.lineSeparator());
        sb.append("{").append(System.lineSeparator());
        sb.append(ladoDer.toJson()).append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        sb.append("]").append(System.lineSeparator());
        return sb.toString();
    }

}
