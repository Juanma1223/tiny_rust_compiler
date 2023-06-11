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
import Semantico.Variable.Variable;

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
        if (tipoIzq instanceof TipoPrimitivo) {
            if (tipoDer instanceof TipoPrimitivo) {
                if (!tipoIzq.obtenerTipo().equals(tipoDer.obtenerTipo())) {
                    new ErrorSemantico(operador.obtenerFila(), operador.obtenerColumna(),
                            "Los tipos de la asignacion no coinciden!", true);
                }
            } else {
                new ErrorSemantico(operador.obtenerFila(), operador.obtenerColumna(),
                        "Los tipos de la asignacion no coinciden!", true);
            }
        } else if (tipoIzq instanceof TipoReferencia) {
            if (tipoDer instanceof TipoReferencia) {
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
        } else if (tipoIzq instanceof TipoArreglo) {
            if (tipoDer instanceof TipoArreglo) {
                if (!tipoIzq.obtenerTipo().equals(tipoDer.obtenerTipo())) {
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
        // Obtenemos la variable del lado izquierdo para saber a donde apuntar en el RA
        // o el CIR
        NodoVariable ladoIzqNodoVariable = ladoIzq.obtenerNodoVariable();
        String nombreVariable = ladoIzqNodoVariable.token.obtenerLexema();
        String alcanceVariable = this.alcanceVariable(nombreVariable, ladoIzqNodoVariable);
        Metodo infoMetodo = claseContenedora.obtenerMetodoPorNombre(metodoContenedor.obtenerNombre());
        Variable infoVariable;
        sb.append("sw $a0, 0($sp) # Comienzo asignacion").append(System.lineSeparator());
        sb.append("subu $sp, $sp, 4").append(System.lineSeparator());
        sb.append(ladoDer.genCodigo()).append(System.lineSeparator());
        // Asignamos a la posicion de memoria de la variable el valor del lado derecho
        switch (alcanceVariable) {
            case "parametro":
                // Por ser un parametro del metodo guardamos el valor en el RA, posicion de
                // parametros
                infoVariable = metodoContenedor.obtenerParametroPorNombre(nombreVariable);
                sb.append("sw $a0,-" + infoMetodo.offsetParametro(infoVariable.obtenerPosicion()) + "($fp)")
                        .append(System.lineSeparator());
                break;
            case "local":
                // Por ser una variable local del metodo guardamos el valor en el RA, posicion
                // de variables
                sb.append("sw $a0,-" + infoMetodo.offsetVariable(nombreVariable) + "($fp)")
                        .append(System.lineSeparator());
                break;
            case "clase":
                infoVariable = metodoContenedor.obtenerVariablePorNombre(nombreVariable);
                String nombreClase = infoVariable.obtenerTipo().obtenerTipo();
                Clase infoClase = tablaDeSimbolos.obtenerClasePorNombre(nombreClase);
                // Por ser un atributo de clase guardamos el valor en el CIR

                // Obtenemos la primera posicion del CIR mediante el puntero guardado en la
                // variable y la guardamos en $t1
                sb.append("lw $t1, -" + infoMetodo.offsetVariable(nombreVariable)
                        + "($fp) # Cargamos el puntero al CIR de " + nombreVariable)
                        .append(System.lineSeparator());
                // Guardamos el valor de la asignacion en el CIR
                String nombreAtributo = ladoIzqNodoVariable.encadenado.obtenerToken().obtenerLexema();
                sb.append("sw $a0, -" + infoClase.offsetAtributo(nombreAtributo)
                        + "($t1) # Guardamos en el CIR el valor asignado")
                        .append(System.lineSeparator());
                break;
            case "atributo":
                // Este es un atributo de clase, pero nos referimos a la clase actual y no una instancia
                infoVariable = claseContenedora.obtenerAtributoPorNombre(nombreVariable);
                // La variable es un atributo de la clase
                int offsetSelf = metodoContenedor.offsetSelf();
                sb.append("lw $t1, -" + offsetSelf + "($fp) # Acceso al CIR de self")
                        .append(System.lineSeparator());
                int offset = claseContenedora.offsetAtributo(nombreVariable);
                sb.append("sw $a0, -" + offset
                        + "($t1) # Guardamos en el CIR el valor asignado")
                        .append(System.lineSeparator());
            default:
                break;
        }
        sb.append("lw $a0, 4($sp)").append(System.lineSeparator());
        sb.append("addiu $sp, $sp, 4 # Fin asignacion").append(System.lineSeparator());
        return sb.toString();
    }

    // Este metodo nos retorna si la variable es un parametro, variable local de
    // metodo o parte de una clase
    public String alcanceVariable(String nombreVariable, NodoVariable ladoIzq) {
        Variable infoVariable = metodoContenedor.obtenerParametroPorNombre(nombreVariable);
        // Si la variable tiene encadenado
        if (infoVariable != null) {
            // La variable es un parámetro del método
            return "parametro";
        } else {
            infoVariable = metodoContenedor.obtenerVariablePorNombre(nombreVariable);
            if (infoVariable != null) {
                // La variable es una variable local del método
                // int offset = infoMetodo.offsetVariable(infoVariable.obtenerNombre());
                // sb.append("lw $a0, -" + offset + "($fp) # Acceso a la variable " +
                // infoVariable.obtenerNombre())
                // .append(System.lineSeparator());
                if (ladoIzq.encadenado != null) {
                    return "clase";
                } else {
                    return "local";
                }
            }
        }
        return "atributo";
    }

}
