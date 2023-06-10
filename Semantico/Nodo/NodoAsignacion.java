package Semantico.Nodo;

import Lexico.Token;
import Semantico.Clase;
import Semantico.ErrorSemantico;
import Semantico.Funcion.Funcion;
import Semantico.Funcion.Metodo;
import Semantico.Tipo.Tipo;
import Semantico.Tipo.TipoArreglo;
import Semantico.Tipo.TipoPrimitivo;
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
        if(tipoIzq instanceof TipoPrimitivo) {
            if(tipoDer instanceof TipoPrimitivo) {
                if (!tipoIzq.obtenerTipo().equals(tipoDer.obtenerTipo())) {
                    new ErrorSemantico(operador.obtenerFila(), operador.obtenerColumna(),
                            "Los tipos de la asignacion no coinciden!", true);
                }
            } else {
                new ErrorSemantico(operador.obtenerFila(), operador.obtenerColumna(),
                            "Los tipos de la asignacion no coinciden!", true);
            }
        } else if(tipoIzq instanceof TipoReferencia) {
            if(tipoDer instanceof TipoReferencia) {
                Clase infoClaseDer = tablaDeSimbolos.obtenerClasePorNombre(tipoDer.obtenerTipo());
                if (!infoClaseDer.esSubclaseDe(tipoIzq.obtenerTipo())) {
                    if (!tipoIzq.obtenerTipo().equals(tipoDer.obtenerTipo())) {
                        new ErrorSemantico(operador.obtenerFila(), operador.obtenerColumna(),
                            "Los tipos de la asignacion no coinciden!", true);
                    }
                }
            } else {
                new ErrorSemantico(operador.obtenerFila(), operador.obtenerColumna(),
                            "Los tipos de la asignacion no coinciden!", true);
            }
        } else if(tipoIzq instanceof TipoArreglo) {
            if(tipoDer instanceof TipoArreglo) {
                if(!tipoIzq.obtenerTipo().equals(tipoDer.obtenerTipo())){
                    new ErrorSemantico(operador.obtenerFila(), operador.obtenerColumna(),
                            "Los tipos de la asignacion no coinciden!", true);
                }
            } else {
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

    @Override
    public String genCodigo() {
        StringBuilder sb = new StringBuilder();
        // Obtenemos la variable del lado izquierdo para saber a donde apuntar en el RA o el CIR
        NodoVariable ladoIzqNodoVariable = ladoIzq.obtenerNodoVariable();
        String nombreVariable = ladoIzqNodoVariable.token.obtenerLexema();
        Metodo infoMetodo = claseContenedora.obtenerMetodoPorNombre(metodoContenedor.obtenerNombre());
        // sb.append(ladoIzq.genCodigo()).append(System.lineSeparator());
        sb.append("sw $a0, 0($sp) # Comienzo asignacion").append(System.lineSeparator());
        sb.append("subu $sp, $sp, 4").append(System.lineSeparator());
        sb.append(ladoDer.genCodigo()).append(System.lineSeparator());
        // Asignamos a la posicion de memoria de la variable el valor del lado derecho
        sb.append("sw $a0,-"+ infoMetodo.offsetVariable(nombreVariable)+"($fp)").append(System.lineSeparator());
        sb.append("lw $a0, 4($sp)").append(System.lineSeparator());
        sb.append("addiu $sp, $sp, 4 # Fin asignacion").append(System.lineSeparator());
        return sb.toString();
    }

}
