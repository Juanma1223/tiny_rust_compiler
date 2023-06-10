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
                Variable infoVariable = this.tablaDeSimbolos.obtenerVarEnAlcanceActual(metodoContenedor,
                        claseContenedora,
                        token);
                if (infoVariable.obtenerTipo() == null) {
                    // Si el tipo sigue siendo nulo, entonces la variable no se encuentra definida
                    new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                            "La variable " + token.obtenerLexema() + " no esta definida en el alcance actual", true);
                    return new Tipo(null);
                }
                Tipo tVar = infoVariable.obtenerTipo();
                this.tipo = tVar;
                if (this.encadenado != null) {
                    if (this.encadenado instanceof NodoArreglo) {
                        if (tVar instanceof TipoArreglo) {
                            this.encadenado.checkeoTipos();
                            this.tipo = new TipoPrimitivo(this.tipo.obtenerTipo());
                            return this.tipo;
                        } else {
                            new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                                    "La variable " + token.obtenerLexema() +
                                            " no es de tipo arreglo",
                                    true);
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
                    "El atributo " + token.obtenerLexema() + " no esta definido en el alcance actual", true);
            return new Tipo(null);
        } else {
            // Si la clase padre es distinta de la actual se debe verificar que el atributo
            // sea publico
            if (!(tipoPadre.obtenerTipo().equals(claseContenedora.obtenerNombre()))) {
                // Si el atributo es privado entonces no se puede acceder
                if (infoAtributo.obtenerVisibilidad() == false) {
                    new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                            "El atributo " + token.obtenerLexema() + " no se puede acceder porque es privado", true);
                    return new Tipo(null);
                }
            }
        }
        Tipo tVar = infoAtributo.obtenerTipo();
        this.tipo = tVar;
        if (this.encadenado != null) {
            if (this.encadenado instanceof NodoArreglo) {
                if (tVar instanceof TipoArreglo) {
                    this.encadenado.checkeoTipos();
                    this.tipo = new TipoPrimitivo(this.tipo.obtenerTipo());
                    return this.tipo;
                } else {
                    new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                            "La variable " + token.obtenerLexema() +
                                    " no es de tipo arreglo",
                            true);
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
    public String genCodigo() {
        StringBuilder sb = new StringBuilder();
        Metodo infoMetodo = claseContenedora.obtenerMetodoPorNombre(metodoContenedor.obtenerNombre());
        Variable infoVariable = metodoContenedor.obtenerParametroPorNombre(token.obtenerLexema());
        //Si la variable tiene encadenado
        if(this.encadenado != null){
            // Redireccionamos la ejecucion al metodo correspondiente
            sb.append(this.encadenado.genCodigo()).append(System.lineSeparator());
        }else{
            if (infoVariable != null) {
                //La variable es un parámetro del método
                int offset = infoMetodo.offsetParametro(infoVariable.obtenerPosicion());
                sb.append("lw $a0, -" + offset + "($fp) # Acceso a parametro").append(System.lineSeparator());
            } else {
                infoVariable = metodoContenedor.obtenerVariablePorNombre(token.obtenerLexema());
                if (infoVariable != null) {
                    //La variable es una variable local del método
                    int offset = infoMetodo.offsetVariable(infoVariable.obtenerNombre());
                    sb.append("lw $a0, -" + offset + "($fp) # Acceso a la variable "+infoVariable.obtenerNombre()).append(System.lineSeparator());
    
                } else {
                    // Obtenemos el padre para obtener la posicion de memoria en el heap del CIR
                    NodoVariable padre = this.padre.obtenerNodoVariable();
                    String nombreVariablePadre = padre.obtenerToken().obtenerLexema();
                    Variable variablePadre = metodoContenedor.obtenerVariablePorNombre(nombreVariablePadre);
                    infoVariable = claseContenedora.obtenerAtributoPorNombre(token.obtenerLexema());
                    Clase clasePadre = tablaDeSimbolos.obtenerClasePorNombre(variablePadre.obtenerTipo().obtenerTipo());
                    int offset = metodoContenedor.offsetVariable(nombreVariablePadre);
                    //La variable es un atributo de la clase
                    sb.append("lw $t1, -" + offset + "($fp) # Acceso al CIR de  "+nombreVariablePadre).append(System.lineSeparator());
                    offset = clasePadre.offsetAtributo(token.obtenerLexema());
                    sb.append("lw $a0, -" + offset + "($t1) # Guardamos en $a0 el valor de la variable almacenada en el CIR").append(System.lineSeparator());

                }
            }
        }
        return sb.toString();
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

    @Override
    public NodoVariable obtenerNodoVariable(){
        return this;
    }

}
