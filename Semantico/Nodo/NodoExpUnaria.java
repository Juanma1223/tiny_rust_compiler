package Semantico.Nodo;

import Lexico.Token;
import Semantico.Clase;
import Semantico.ErrorSemantico;
import Semantico.Funcion.Funcion;
import Semantico.Tipo.Tipo;

public class NodoExpUnaria extends NodoExpresion {
    private NodoExpresion ladoDer;
    private Token operador;

    public NodoExpUnaria(Funcion metodoContenedor, Clase claseContenedora){
        super(metodoContenedor,claseContenedora);
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

    public void establecerLadoDer(NodoExpresion ladoDer){
        ladoDer.establecerPadre(this);
        ladoDer.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.ladoDer = ladoDer;
    }

    public void establecerOp(Token operador){
        this.operador = operador;
    }

    // Necesitamos resolver el tipo de la expresion unaria en funcion del
    // lado derecho
    @Override
    public Tipo obtenerTipo() {
        checkeoTipos();
        // Una vez hecho el checkeo de tipos ya podemos resolver el tipo de esta
        // expresion unaria
        return this.tipo;
    }

    @Override
    public void checkeoTipos() {
        Tipo tipoDer = ladoDer.obtenerTipo();
        this.checkeoOperadorValido(tipoDer, operador);
        this.establecerTipo(tipoDer);
    }

    // Esta funcion revisa si la operacion que estamos ejecutando es compatible con
    // el tipo de dato con el que se esta trabajando
    public void checkeoOperadorValido(Tipo tipoDer, Token operador) {
        String tipo = tipoDer.obtenerTipo();
        String op = operador.obtenerLexema();
        if (op.equals("+") || op.equals("-")) {
            if (tipo.equals("I32")) {
                // Operador y tipo correctos
                return;
            }
        } else {
            if (op.equals("!")) {
                if (tipo.equals("Bool")) {
                    // Operador y tipo correctos
                    return;
                }
            }
        }
        new ErrorSemantico(operador.obtenerFila(), operador.obtenerColumna(),
                "No se puede aplicar la operacion " + op + " en el tipo de dato " + tipo, true);
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("\"Nodo\":").append("\"NodoExpUnaria\",").append(System.lineSeparator());
        sb.append("\"operador\":").append("\""+operador.obtenerLexema()+"\",").append(System.lineSeparator());
        sb.append("\"hijos\":[").append(System.lineSeparator());
        sb.append("{").append(System.lineSeparator());
        sb.append(ladoDer.toJson()).append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        sb.append("]").append(System.lineSeparator());
        return sb.toString();
    }
}