package Semantico.Nodo;

import Lexico.Token;
import Semantico.Clase;
import Semantico.ErrorSemantico;
import Semantico.Funcion.Funcion;
import Semantico.Tipo.Tipo;
import Semantico.Tipo.TipoArreglo;
import Semantico.Tipo.TipoPrimitivo;
import Semantico.Tipo.TipoReferencia;
import Semantico.Variable.Atributo;
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
        if (this.tipo == null) {
            // Hay casos donde no se asigna la tabla de simbolos, si este es el caso
            // la buscamos en el padre o ancestros mediante el metodo heredado
            // obtenerTablaDeSimbolos
            if (this.tablaDeSimbolos == null) {
                this.tablaDeSimbolos = obtenerTablaDeSimbolos();
            }

            if (token.obtenerLexema().equals("self")) {
                Tipo tSelf = new TipoReferencia(claseContenedora.obtenerNombre());
                this.tipo = tSelf;
                if (this.encadenado != null) {
                    this.tipo = this.encadenado.obtenerTipoEncadenado(tSelf);
                    return this.tipo;
                } else {
                    return this.tipo;
                }
            } else {
                Variable infoVariable = this.tablaDeSimbolos.obtenerVarEnAlcanceActual(metodoContenedor, claseContenedora,
                    token);
                if (infoVariable.obtenerTipo() == null) {
                    // Si el tipo sigue siendo nulo, entonces la variable no se encuentra definida
                    new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                         "La variable " + token.obtenerLexema() + " no esta definida en el alcance actual",true);
                    return new Tipo(null);
                }
                Tipo tVar = infoVariable.obtenerTipo();
                this.tipo = tVar;
                if (this.encadenado != null) {
                    if(this.encadenado instanceof NodoArreglo){
                        if(tVar instanceof TipoArreglo){
                            this.encadenado.checkeoTipos();
                            this.tipo = new TipoPrimitivo(this.tipo.obtenerTipo());
                            return this.tipo;
                        } else {
                            new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(), "La variable " + token.obtenerLexema() + 
                            " no es de tipo arreglo", true);
                            return new Tipo(null);
                        }
                    } else {
                        this.tipo = this.encadenado.obtenerTipoEncadenado(tVar);
                        return this.tipo;
                    }
                } else {
                    return this.tipo;
                }

            }
        } else {
            return this.tipo;
        }
    }

    @Override
    public Tipo obtenerTipoEncadenado(Tipo tipoPadre) {
        if (this.tablaDeSimbolos == null) {
            this.tablaDeSimbolos = obtenerTablaDeSimbolos();
        }
        // El atributo debe estar definido en la clase del tipoPadre
        Clase infoClase = tablaDeSimbolos.obtenerClasePorNombre(tipoPadre.obtenerTipo());
        Atributo infoAtributo = this.tablaDeSimbolos.obtenerVarEncadenada(infoClase, token);
        if (infoAtributo.obtenerTipo() == null) {
            // Si el tipo es nulo, entonces el atributo no se encuentra definido
            new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                "El atributo " + token.obtenerLexema() + " no esta definido en el alcance actual",true);
            return new Tipo(null);
        } else {
            // Si la clase padre es distinta de la actual se debe verificar que el atributo sea publico
            if (!(tipoPadre.obtenerTipo().equals(claseContenedora.obtenerNombre()))) {
                // Si el atributo es privado entonces no se puede acceder
                if (infoAtributo.obtenerVisibilidad() == false) {
                    new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                    "El atributo " + token.obtenerLexema() + " no se puede acceder porque es privado",true);
                    return new Tipo(null);
                }
            }
        }
        Tipo tVar = infoAtributo.obtenerTipo();
        this.tipo = tVar;
        if (this.encadenado != null) {
            if(this.encadenado instanceof NodoArreglo){
                if(tVar instanceof TipoArreglo){
                    this.encadenado.checkeoTipos();
                    this.tipo = new TipoPrimitivo(this.tipo.obtenerTipo());
                    return this.tipo;
                } else {
                    new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(), "La variable " + token.obtenerLexema() + 
                    " no es de tipo arreglo", true);
                    return new Tipo(null);
                }
            } else {
                this.tipo = this.encadenado.obtenerTipoEncadenado(tVar);
                return this.tipo;
            }
        } else {
            return this.tipo;
        }
    }

    @Override
    public void checkeoTipos() {
        this.obtenerTipo();
    }

    @Override
    public Token obtenerToken() {
        return this.token;
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
