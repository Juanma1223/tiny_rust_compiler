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
            sb.append("addu $a0, $t1, $a0 # Suma").append(System.lineSeparator());
            break;
            case "-":
            sb.append("subu $a0, $t1, $a0 # Resta").append(System.lineSeparator());
            break;
            case "*":
            sb.append("mul $a0, $t1, $a0 # Multiplicacion").append(System.lineSeparator());
            break;
            case "/":
            this.tablaDeSimbolos = obtenerTablaDeSimbolos();
            int numDiv = tablaDeSimbolos.obtenerLabel();
            //Revisamos que el divisor sea distinto de 0
            sb.append("bne $a0, $0, division"+numDiv+" # Control division por cero").append(System.lineSeparator());
            sb.append(".data").append(System.lineSeparator());
            int numString = tablaDeSimbolos.obtenerLabel(); // creamos un mensaje de error
            sb.append("string"+numString+": .asciiz ").append("\"" +"ERROR: Division por cero"+"\"").append(System.lineSeparator());
            sb.append(".text").append(System.lineSeparator());
            sb.append("la $a0, string"+numString).append(System.lineSeparator()); // cargamos el mensaje en el acumulador
            sb.append("li $v0, 4 # Print string").append(System.lineSeparator()); // print_string
            sb.append("syscall").append(System.lineSeparator());
            sb.append("li $v0, 10 # Exit").append(System.lineSeparator()); // exit
            sb.append("syscall").append(System.lineSeparator());
            sb.append("division"+numDiv+":").append(System.lineSeparator()); // realizamos la division
            sb.append("div $t1, $a0 # Division").append(System.lineSeparator());
            sb.append("mflo $a0 # Recupero resultado division").append(System.lineSeparator());  //recuperar Lo
            break;
            case "%":
            this.tablaDeSimbolos = obtenerTablaDeSimbolos();
            int numMod = tablaDeSimbolos.obtenerLabel();
            //Revisamos que el divisor sea distinto de 0
            sb.append("bne $a0, $0, modulo"+numMod+" # Control division por cero").append(System.lineSeparator());
            sb.append(".data").append(System.lineSeparator());
            int numStr = tablaDeSimbolos.obtenerLabel(); // creamos un mensaje de error
            sb.append("string"+numStr+": .asciiz ").append("\"" +"ERROR: Modulo-Division por cero"+"\"").append(System.lineSeparator());
            sb.append(".text").append(System.lineSeparator());
            sb.append("la $a0, string"+numStr).append(System.lineSeparator()); // cargamos el mensaje en el acumulador
            sb.append("li $v0, 4 # Print string").append(System.lineSeparator()); // print_string
            sb.append("syscall").append(System.lineSeparator());
            sb.append("li $v0, 10 # Exit").append(System.lineSeparator()); // exit
            sb.append("syscall").append(System.lineSeparator());
            sb.append("modulo"+numMod+":").append(System.lineSeparator()); // realizamos la division para obtener el modulo
            sb.append("div $t1, $a0 # Division").append(System.lineSeparator());
            sb.append("mfhi $a0 # Recupero resultado modulo").append(System.lineSeparator()); //recuperar Hi
            break;
            //Operaciones logicas relacionales
            case "<":
            sb.append("slt $a0, $t1, $a0 # Comparacion <").append(System.lineSeparator()); //izq < der
            break;
            case "<=":
            sb.append("slt $a0, $a0, $t1 # Comparacion <=").append(System.lineSeparator()); // !(der < izq)
            sb.append("xori $a0, $a0, 1 # Comparacion <=").append(System.lineSeparator());
            break;
            case ">":
            sb.append("slt $a0, $a0, $t1 # Comparacion >").append(System.lineSeparator()); //der < izq
            break;
            case ">=":
            sb.append("slt $a0, $t1, $a0 # Comparacion >=").append(System.lineSeparator()); // !(izq < der)
            sb.append("xori $a0, $a0, 1 # Comparacion >=").append(System.lineSeparator());
            break;
            //Operaciones logicas && y ||
            case "&&":
            sb.append("and $a0, $t1, $a0 # Operacion &&").append(System.lineSeparator());
            break;
            case "||":
            sb.append("or $a0, $t1, $a0 # Operacion ||").append(System.lineSeparator());
            break;
            //Operaciones == y !=
            case "==":
            sb.append("xor $a0, $t1, $a0 # Operacion ==").append(System.lineSeparator());
            sb.append("sltu $a0, $a0, 1 # Operacion ==").append(System.lineSeparator());
            break;
            case "!=":
            sb.append("xor $a0, $t1, $a0 # Operacion !=").append(System.lineSeparator());
            sb.append("sltu $a0, $0, $a0 # Operacion !=").append(System.lineSeparator());
            break;
            default:
            sb.append("");
        }
        return sb.toString();
    }

}
