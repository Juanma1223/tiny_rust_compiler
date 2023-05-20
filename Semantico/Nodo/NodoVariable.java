package Semantico.Nodo;

import Lexico.Token;
import Semantico.Clase;
import Semantico.ErrorSemantico;
import Semantico.Funcion.Funcion;
import Semantico.Tipo.Tipo;
import Semantico.Variable.Variable;

public class NodoVariable extends NodoExpresion {

    Token token;

    public NodoVariable(Funcion metodoContenedor, Clase claseContenedora, Token token) {
        super(metodoContenedor, claseContenedora);
        this.token = token;
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

    @Override
    public Tipo obtenerTipo() {
        // Cuando tenemos un encadenado nos interesa retornar el tipo que resuelve dicho encadenado
        if(this.encadenado != null && tipo != null){
            return encadenado.obtenerTipo();
        }
        // El tipo aun no esta definido, lo buscamos en la tabla de simbolos
        if (this.tipo == null) {
            Variable infoVariable = this.tablaDeSimbolos.obtenerVarEnAlcanceActual(metodoContenedor, claseContenedora,
                    token);
            // Si el tipo sigue siendo nulo, entonces la variable no se encuentra definida
            if (infoVariable.obtenerTipo() == null) {
                new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                        "La variable " + token.obtenerLexema() + " no esta definida en el alcance actual");
                return new Tipo(null);
            } else {
                this.tipo = infoVariable.obtenerTipo();
                return this.tipo;
            }
        } else {
            return this.tipo;
        }
    }

    // Esta funcion es utilizada cuando el tipo de la variable es una clase
    public Tipo obtenerTipoClase() {
        // El tipo aun no esta definido, lo buscamos en la tabla de simbolos
        if (this.tipo == null) {
            Variable infoVariable = this.tablaDeSimbolos.obtenerVarEnAlcanceActual(metodoContenedor, claseContenedora,
                    token);
            // Si el tipo sigue siendo nulo, entonces la variable no se encuentra definida
            if (infoVariable.obtenerTipo() == null) {
                new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                        "La variable " + token.obtenerLexema() + " no esta definida en el alcance actual");
                return new Tipo(null);
            } else {
                this.tipo = infoVariable.obtenerTipo();
                return this.tipo;
            }
        } else {
            return this.tipo;
        }
    }

    @Override
    public void checkeoTipos(){
        if(this.encadenado != null){
            this.encadenado.checkeoTipos();
        }
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("\"Nodo\":").append("\"NodoVariable\",").append(System.lineSeparator());
        if (this.encadenado != null) {
            sb.append("\"valor\":").append("\"" + token.obtenerLexema() + "\",").append(System.lineSeparator());
            sb.append("\"encadenado\":{").append(System.lineSeparator());
            sb.append(this.encadenado.toJson()).append(System.lineSeparator());
            sb.append("}").append(System.lineSeparator());
        } else {
            sb.append("\"valor\":").append("\"" + token.obtenerLexema() + "\"").append(System.lineSeparator());
        }
        return sb.toString();
    }

}
