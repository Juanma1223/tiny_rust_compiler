package Semantico.Nodo;

import Lexico.Token;
import Semantico.ErrorSemantico;
import Semantico.Tipo.Tipo;
import Semantico.Tipo.TipoPrimitivo;

public class NodoExpBinaria extends NodoExpresion {
    private NodoExpresion ladoIzq;
    private NodoExpresion ladoDer;
    private Token operador;

    public void establecerLadoIzq(NodoExpresion ladoIzq) {
        ladoIzq.establecerPadre(this);
        this.ladoIzq = ladoIzq;
    }

    public void establecerLadoDer(NodoExpresion ladoDer) {
        ladoDer.establecerPadre(this);
        this.ladoDer = ladoDer;
    }

    public void establecerOp(Token operador) {
        this.operador = operador;
    }

    // Necesitamos resolver el tipo de la expresion binaria en funcion de cada uno
    // de los lados
    @Override
    public Tipo obtenerTipo() {
        checkeoTipos();
        // Una vez hecho el checkeo de tipos ya podemos resolver el tipo de esta
        // expresion binaria
        return this.tipo;
    }

    @Override
    public void checkeoTipos() {
        Tipo tipoIzq = ladoIzq.obtenerTipo();
        Tipo tipoDer = ladoDer.obtenerTipo();
        if (!tipoIzq.obtenerTipo().equals(tipoDer.obtenerTipo())) {
            new ErrorSemantico(operador.obtenerFila(), operador.obtenerColumna(),
                    "Los tipos de la expresion binaria no coinciden!",true);
        }
        Tipo tExp = this.checkeoOperadorValido(tipoIzq, operador);
        this.establecerTipo(tExp);
    }

    // Esta funcion revisa si la operacion que estamos ejecutando es compatible con
    // el tipo de dato con el que se esta trabajando
    public Tipo checkeoOperadorValido(Tipo tipoIzq, Token operador) {
        String tipo = tipoIzq.obtenerTipo();
        String op = operador.obtenerLexema();
        if (op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") || op.equals("%")) {
            if (tipo.equals("I32")) {
                // Operador y tipo correctos
                return new TipoPrimitivo("I32");
            }
        } else {
            if (op.equals("<") || op.equals("<=") || op.equals(">") || op.equals(">=")) {
                if (tipo.equals("I32")) {
                    // Operador y tipo correctos
                    return new TipoPrimitivo("Bool");
                }
            }
            else {
                if (op.equals("&&") || op.equals("||")) {
                    if (tipo.equals("Bool")) {
                        // Operador y tipo correctos
                        return new TipoPrimitivo("Bool");
                    }
                }
                else {
                    return tipoIzq;
                }
            }
        }
        new ErrorSemantico(operador.obtenerFila(), operador.obtenerColumna(),
                "No se puede aplicar la operacion " + op + " en el tipo de dato " + tipo, true);
        return null;
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("\"Nodo\":").append("\"NodoExpBinaria\",").append(System.lineSeparator());
        sb.append("\"operador\":").append("\""+operador.obtenerLexema()+"\",").append(System.lineSeparator());
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
