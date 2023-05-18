package Semantico.Nodo;

import Lexico.Token;
import Semantico.ErrorSemantico;
import Semantico.Tipo.Tipo;

public class NodoExpBinaria extends NodoExpresion {
    private Nodo ladoIzq;
    private Nodo ladoDer;
    private Token operador;

    public void establecerLadoIzq(Nodo ladoIzq) {
        ladoIzq.establecerPadre(this);
        this.ladoIzq = ladoIzq;
    }

    public void establecerLadoDer(Nodo ladoDer) {
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
            new ErrorSemantico(0, 0,
                    "Los tipos de la expresion binaria no coinciden! (falta agregar filas y columnas)");
        }
        this.checkeoOperadorValido(tipoIzq.obtenerTipo(), operador);
        this.establecerTipo(tipoDer);
    }

    // Esta funcion revisa si la operacion que estamos ejecutando es compatible con
    // el tipo de dato con el que se esta trabajando
    public void checkeoOperadorValido(String tipo, Token operador) {
        String op = operador.obtenerLexema();
        if (tipo.equals("I32")) {
            if (op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/")) {
                // Operador y tipo correctos
                return;
            }
        } else {
            if (op.equals("+") && (tipo.equals("Char") || tipo.equals("Str"))) {
                // Operador y tipo correctos
                return;
            } else {
                if (tipo == "Bool") {
                    if (op.equals("&&") || op.equals("||")) {
                        return;
                    } else {
                        new ErrorSemantico(0, 0,
                                "No se puede aplicar la operacion " + op + " en el tipo de dato " + tipo);
                    }
                }
            }
        }
    }
}
