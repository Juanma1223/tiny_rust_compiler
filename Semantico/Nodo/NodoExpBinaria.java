package Semantico.Nodo;

import Lexico.Token;
import Semantico.Clase;
import Semantico.ErrorSemantico;
import Semantico.Funcion.Funcion;
import Semantico.Tipo.Tipo;
import Semantico.Tipo.TipoPrimitivo;

public class NodoExpBinaria extends NodoExpresion {
    private NodoExpresion ladoIzq;
    private NodoExpresion ladoDer;
    private Token operador;

    public NodoExpBinaria(Funcion metodoContenedor, Clase claseContenedora){
        super(metodoContenedor,claseContenedora);
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
        ladoIzq.checkeoTipos();
        ladoDer.checkeoTipos();
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
                    if (op.equals("==") || op.equals("!=")) {
                        return new TipoPrimitivo("Bool");
                    }
                    else {
                        return tipoIzq;
                    }
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

    @Override
    public String genCodigo() {
        StringBuilder sb = new StringBuilder();
        sb.append(ladoIzq.genCodigo()).append(System.lineSeparator());
        sb.append("sw $a0, 0($sp)").append(System.lineSeparator());
        sb.append("addiu $sp, $sp, -4").append(System.lineSeparator());
        sb.append(ladoDer.genCodigo()).append(System.lineSeparator());
        sb.append("lw $t1, 4($sp)").append(System.lineSeparator());
        sb.append(genCodigoOperador()).append(System.lineSeparator());
        sb.append("addiu $sp, $sp, 4").append(System.lineSeparator());
        return sb.toString();
    }

    public String genCodigoOperador() {
        StringBuilder sb = new StringBuilder();
        switch(this.operador.obtenerLexema()){
            //Operaciones aritmeticas
            case "+":
            sb.append("add $a0, $t1, $a0").append(System.lineSeparator());
            break;
            case "-":
            sb.append("sub $a0, $t1, $a0").append(System.lineSeparator());
            break;
            case "*":
            sb.append("mul $a0, $t1, $a0").append(System.lineSeparator());
            break;
            case "/":
            sb.append("div $t1, $a0").append(System.lineSeparator()); //recuperar Lo
            sb.append("mflo $a0").append(System.lineSeparator());
            break;
            case "%":
            sb.append("div $t1, $a0").append(System.lineSeparator()); //recuperar Hi
            sb.append("mfhi $a0").append(System.lineSeparator());
            break;
            //Operaciones logicas relacionales
            case "<":
            sb.append("slt $a0, $t1, $a0").append(System.lineSeparator()); //izq < der
            break;
            case "<=":
            sb.append("slt $a0, $a0, $t1").append(System.lineSeparator()); // !(der < izq)
            sb.append("xori $a0, $a0, 1").append(System.lineSeparator());
            break;
            case ">":
            sb.append("slt $a0, $a0, $t1").append(System.lineSeparator()); //der < izq
            break;
            case ">=":
            sb.append("slt $a0, $t1, $a0").append(System.lineSeparator()); // !(izq < der)
            sb.append("xori $a0, $a0, 1").append(System.lineSeparator());
            break;
            //Operaciones logicas && y ||
            case "&&":
            sb.append("and $a0, $t1, $a0").append(System.lineSeparator());
            break;
            case "||":
            sb.append("or $a0, $t1, $a0").append(System.lineSeparator());
            break;
            //Operaciones == y !=
            case "==":
            sb.append("xor $a0, $t1, $a0").append(System.lineSeparator());
            sb.append("sltu $a0, $a0, 1").append(System.lineSeparator());
            break;
            case "!=":
            sb.append("xor $a0, $t1, $a0").append(System.lineSeparator());
            sb.append("sltu $a0, $0, $a0").append(System.lineSeparator());
            break;
            default:
            sb.append("");
        }
        return sb.toString();
    }

}
