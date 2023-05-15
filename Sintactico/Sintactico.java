package Sintactico;

import java.io.File;
import java.io.FileNotFoundException;

import Lexico.Lexico;
import Lexico.Token;
import Semantico.Clase;
import Semantico.ErrorSemantico;
import Semantico.TablaDeSimbolos;
import Semantico.Variable.Atributo;
import Semantico.Variable.Parametro;
import Semantico.Variable.Variable;
import Semantico.Funcion.Constructor;
import Semantico.Funcion.Metodo;
import Semantico.Nodo.Nodo;
import Semantico.Nodo.NodoAsignacion;
import Semantico.Nodo.NodoBloque;
import Semantico.Nodo.NodoClase;
import Semantico.Nodo.NodoExpBinaria;
import Semantico.Nodo.NodoExpresion;
import Semantico.Nodo.NodoIf;
import Semantico.Nodo.NodoLiteral;
import Semantico.Nodo.NodoMetodo;
import Semantico.Nodo.NodoReturn;
import Semantico.Nodo.NodoSentencia;
import Semantico.Nodo.NodoVariable;
import Semantico.Nodo.NodoWhile;
import Semantico.Tipo.Tipo;
import Semantico.Tipo.TipoArreglo;
import Semantico.Tipo.TipoPrimitivo;
import Semantico.Tipo.TipoReferencia;
import Semantico.Tipo.TipoVoid;

/* La clase Sintactico se encarga de la implementacion de un
 * Analizador Sintactico Descendente Predictivo Recursivo
 */
public class Sintactico {

    // Se instancia una unica vez el analizador lexico
    Lexico analizadorLexico;
    // Esta clase se encarga de interactuar con el analizador lexico
    // e implementar metodos de ayuda
    AuxiliarSintactico aux;

    public TablaDeSimbolos tablaDeSimbolos;
    public Nodo AST;

    // Este constructor recibe como argumento la ruta en el sistema operativo
    // donde se encuentra el archivo con el codigo fuente
    public Sintactico(File archivo, TablaDeSimbolos tablaDeSimbolos, Nodo AST) {
        try {
            this.analizadorLexico = new Lexico(archivo);
            this.aux = new AuxiliarSintactico(this.analizadorLexico);
            this.tablaDeSimbolos = tablaDeSimbolos;
            this.AST = AST;

            // Iniciamos el analisis sintactico
            this.start();
        } catch (FileNotFoundException e) {
            System.out.println("Error al abrir archivo de entrada!");
            System.exit(1);
        }
    }

    // Cada metodo corresponde a un no terminal de la gramatica
    // El metodo start es el inicial
    private void start() {
        claseP();
        metodoMain();
        exito();
    }

    // Este metodo corrobora que el analisis haya terminado con exito
    // Para ello, no debe venir nada despues del metodo main
    private void exito() {
        Token tokenActual = aux.tokenActual;
        if (!tokenActual.obtenerLexema().equals("EOF")) {
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "NO PUEDE VENIR NADA DESPUÉS DEL MÉTODO MAIN! Se esperaba: EOF, se encontró: "
                            + tokenActual.obtenerLexema());
        }
    }

    private void claseP() {
        if (aux.verifico("class")) {
            // Creamos un nuevo arbol de clase
            NodoClase ASTClase = AST.agregarHijo();
            clase(ASTClase);
            claseP();
        } else {
            Token tokenActual = aux.tokenActual;
            if (!tokenActual.obtenerLexema().equals("fn")) {
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                        "FALTA MÉTODO MAIN! Se esperaba: fn, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    private void metodoMain() {
        Clase nuevaClase = new Clase("Fantasma");
        nuevaClase.establecerHerencia("Object");
        tablaDeSimbolos.establecerClaseActual(nuevaClase);
        aux.matcheo("fn");
        aux.matcheo("main");
        aux.matcheo("(");
        aux.matcheo(")");
        Metodo nuevoMetodo = new Metodo("main", false);
        nuevoMetodo.establecerTipoRetorno(new TipoVoid("void"));
        tablaDeSimbolos.establecerMetodoActual(nuevoMetodo);
        NodoClase ASTClase = AST.agregarHijo();
        NodoMetodo ASTMetodo = ASTClase.agregarMetodo();
        bloqueMetodo(ASTMetodo);
        tablaDeSimbolos.obtenerClaseActual().insertarMetodo(nuevoMetodo);
        tablaDeSimbolos.insertarClase(nuevaClase);
    }

    // Recibe el arbol de la clase para ampliarlo con su contenido
    private void clase(NodoClase ASTClase) {
        aux.matcheo("class");
        Token tokenActual = aux.tokenActual;
        aux.matcheoId("id_clase");
        Clase checkeoClase = tablaDeSimbolos.obtenerClasePorNombre(tokenActual.obtenerLexema());
        if (checkeoClase == null) {
            Clase nuevaClase = new Clase(tokenActual.obtenerLexema(), tokenActual.obtenerFila(),
                    tokenActual.obtenerColumna());
            tablaDeSimbolos.establecerClaseActual(nuevaClase);
            restoClase(ASTClase);
            tablaDeSimbolos.insertarClase(nuevaClase);
        } else {
            ErrorSemantico error = new ErrorSemantico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "La clase " + tokenActual.obtenerLexema()
                            + " ya fue declarada. No puede haber dos clases con el mismo nombre");
        }

    }

    private void restoClase(NodoClase ASTClase) {
        if (aux.verifico(":")) {
            aux.matcheo(":");
            Token tokenActual = aux.tokenActual;
            aux.matcheoId("id_clase");
            tablaDeSimbolos.obtenerClaseActual().establecerHerencia(tokenActual.obtenerLexema());
            aux.matcheo("{");
            miembroP(ASTClase);
            aux.matcheo("}");
        } else if (aux.verifico("{")) {
            tablaDeSimbolos.obtenerClaseActual().establecerHerencia("Object");
            aux.matcheo("{");
            miembroP(ASTClase);
            aux.matcheo("}");
        } else {
            Token tokenActual = aux.tokenActual;
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba el comienzo de una clase con: { o :, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    private void miembroP(NodoClase ASTClase) {
        String[] ter = { "pub", "Bool", "I32", "Str", "Char", "id_clase", "Array", "create", "static", "fn" };
        if (aux.verifico(ter)) {
            miembro(ASTClase);
            miembroP(ASTClase);
        } else {
            Token tokenActual = aux.tokenActual;
            if (!tokenActual.obtenerLexema().equals("}")) {
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                        "Se esperaba el cierre de una clase con: }, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    private void miembro(NodoClase ASTClase) {
        String[] ter = { "pub", "Bool", "I32", "Str", "Char", "id_clase", "Array" };
        String[] ter1 = { "static", "fn" };
        if (aux.verifico(ter)) {
            atributo();
        } else {
            if (aux.verifico("create")) {
                NodoMetodo ASTMetodo = ASTClase.agregarMetodo();
                constructor(ASTMetodo);
            } else {
                if (aux.verifico(ter1)) {
                    // Insertamos el metodo en el AST de la clase y continuamos el AST por el metodo
                    NodoMetodo ASTMetodo = ASTClase.agregarMetodo();
                    metodo(ASTMetodo);
                } else {
                    Token tokenActual = aux.tokenActual;
                    ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                            "Se esperaba: atributo, constructor o método, se encontró: " + tokenActual.obtenerLexema());
                }
            }
        }
    }

    private void atributo() {
        boolean visibilidad = false;
        if (aux.verifico("pub")) {
            aux.matcheo("pub");
            visibilidad = true;
        }
        Tipo tAtr = tipo();
        aux.matcheo(":");
        listaDeclAtributos(visibilidad, tAtr);
        aux.matcheo(";");
    }

    // NUEVO METODO
    private void listaDeclAtributos(boolean visibilidad, Tipo tAtr) {
        Token tokenActual = aux.tokenActual;
        aux.matcheoId("id_objeto");
        Atributo checkeoAtributo = tablaDeSimbolos.obtenerClaseActual()
                .obtenerAtributoPorNombre(tokenActual.obtenerLexema());
        if (checkeoAtributo == null) {
            Atributo nuevoAtributo = new Atributo(tokenActual.obtenerLexema(), tAtr, tokenActual.obtenerFila(),
                    tokenActual.obtenerColumna(), visibilidad);
            tablaDeSimbolos.obtenerClaseActual().insertarAtributo(nuevoAtributo);
            listaDeclAtributosP(visibilidad, tAtr);
        } else {
            ErrorSemantico error = new ErrorSemantico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "El atributo " + tokenActual.obtenerLexema()
                            + " ya fue declarado. No puede haber dos atributos con el mismo nombre en una misma clase");
        }
    }

    // NUEVO METODO
    private void listaDeclAtributosP(boolean visibilidad, Tipo tAtr) {
        if (aux.verifico(",")) {
            aux.matcheo(",");
            listaDeclAtributos(visibilidad, tAtr);
        } else {
            Token tokenActual = aux.tokenActual;
            if (!tokenActual.obtenerLexema().equals(";")) {
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                        "Se esperaba: ;, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    private void constructor(NodoMetodo ASTMetodo) {
        Token tokenActual = aux.tokenActual;
        if (tablaDeSimbolos.obtenerClaseActual().tieneConstructor() == false) {
            aux.matcheo("create");
            Constructor nuevoConstructor = new Constructor();
            tablaDeSimbolos.establecerMetodoActual(nuevoConstructor);
            argumentosFormales();
            tablaDeSimbolos.obtenerClaseActual().establecerConstructor(nuevoConstructor);
            NodoBloque ASTBloque = ASTMetodo.agregarBloque();
            bloqueMetodo(ASTBloque);
        } else {
            new ErrorSemantico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Ya hay un constructor declarado para esta clase");
        }
    }

    private void metodo(NodoMetodo ASTMetodo) {
        boolean formaMetodo = false;
        if (aux.verifico("static")) {
            aux.matcheo("static");
            formaMetodo = true;
        }
        aux.matcheo("fn");
        Token tokenActual = aux.tokenActual;
        aux.matcheoId("id_objeto");
        Metodo checkeMetodo = tablaDeSimbolos.obtenerClaseActual().obtenerMetodoPorNombre(tokenActual.obtenerLexema());
        if (checkeMetodo == null) {
            Metodo nuevoMetodo = new Metodo(tokenActual.obtenerLexema(), formaMetodo, tokenActual.obtenerFila(),
                    tokenActual.obtenerColumna());
            tablaDeSimbolos.establecerMetodoActual(nuevoMetodo);
            argumentosFormales();
            aux.matcheo("->");
            Tipo t = tipoMetodo();
            nuevoMetodo.establecerTipoRetorno(t);
            tablaDeSimbolos.obtenerClaseActual().insertarMetodo(nuevoMetodo);
            NodoBloque ASTBloque = ASTMetodo.agregarBloque();
            bloqueMetodo(ASTBloque);
        } else {
            ErrorSemantico error = new ErrorSemantico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "El método " + tokenActual.obtenerLexema()
                            + " ya fue declarado. No puede haber dos métodos con el mismo nombre en una misma clase");
        }
    }

    private void bloqueMetodo(NodoBloque ASTBloque) {
        aux.matcheo("{");
        declVarLocalesP();
        sentenciaP(ASTBloque);
        aux.matcheo("}");
    }

    private void declVarLocalesP() {
        String[] ter = { "Bool", "I32", "Str", "Char", "id_clase", "Array" };
        String[] ter1 = { ";", "id_objeto", "self", "(", "if", "while", "{", "return", "}" };
        if (aux.verifico(ter)) {
            declVarLocales();
            declVarLocalesP();
        } else {
            if (aux.verifico(ter1) == false) {
                Token tokenActual = aux.tokenActual;
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                        "Se esperaba el comienzo de una sentencia: ;, id_objeto, self, (, if, while, {, return o }, se encontró: "
                                + tokenActual.obtenerLexema());
            }
        }

    }

    private void sentenciaP(NodoBloque ASTBloque) {
        String[] ter = { ";", "id_objeto", "self", "(", "if", "while", "{", "return" };
        if (aux.verifico(ter)) {
            sentencia(ASTBloque);
            sentenciaP(ASTBloque);
        } else {
            Token tokenActual = aux.tokenActual;
            if (!tokenActual.obtenerLexema().equals("}")) {
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                        "Se esperaba: }, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    private void declVarLocales() {
        Tipo tVar = tipo();
        aux.matcheo(":");
        listaDeclVariables(tVar);
        aux.matcheo(";");
    }

    private void listaDeclVariables(Tipo tVar) {
        Token tokenActual = aux.tokenActual;
        aux.matcheoId("id_objeto");
        Variable nuevaVariable = new Variable(tokenActual.obtenerLexema(), tVar, tokenActual.obtenerFila(),
                tokenActual.obtenerColumna());
        // Checkear que no se esta redefiniendo la variable en el metodo
        if (tablaDeSimbolos.obtenerMetodoActual().variableYaDeclarada(tokenActual.obtenerLexema())) {
            new ErrorSemantico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(), "La variable " +
                    tokenActual.obtenerLexema() + " ya fue declarada." +
                    " No puede haber dos variables con el mismo nombre dentro de una función");
        }
        tablaDeSimbolos.obtenerMetodoActual().insertarVariable(nuevaVariable);
        listaDeclVariablesP(tVar);
    }

    private void listaDeclVariablesP(Tipo tVar) {
        if (aux.verifico(",")) {
            aux.matcheo(",");
            listaDeclVariables(tVar);
        } else {
            Token tokenActual = aux.tokenActual;
            if (!tokenActual.obtenerLexema().equals(";")) {
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                        "Se esperaba: ;, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    private void argumentosFormales() {
        aux.matcheo("(");
        listaArgumentosFormalesP();
    }

    private void listaArgumentosFormalesP() {
        String[] ter = { "Bool", "I32", "Str", "Char", "id_clase", "Array" };
        if (aux.verifico(ter)) {
            listaArgumentosFormales();
            aux.matcheo(")");
        } else {
            aux.matcheo(")");
        }
    }

    private void listaArgumentosFormales() {
        Parametro nuevoParametro = argumentoFormal();
        // Checkear que no se esta redefiniendo el parámetro en el método
        if (tablaDeSimbolos.obtenerMetodoActual().parametroYaDeclarado(nuevoParametro.obtenerNombre())) {
            new ErrorSemantico(nuevoParametro.obtenerFila(), nuevoParametro.obtenerColumna(), "El parámetro " +
                    nuevoParametro.obtenerNombre() + " ya fue declarado. " +
                    "No puede haber dos parámetros con el mismo nombre dentro de una función");
        }
        tablaDeSimbolos.obtenerMetodoActual().insertarParametro(nuevoParametro);
        listaArgumentosFormales2();
    }

    private void listaArgumentosFormales2() {
        if (aux.verifico(",")) {
            aux.matcheo(",");
            listaArgumentosFormales();
        } else {
            Token tokenActual = aux.tokenActual;
            if (!tokenActual.obtenerLexema().equals(")")) {
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                        "Se esperaba: ), se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    private Parametro argumentoFormal() {
        Tipo tPar = tipo();
        aux.matcheo(":");
        Token tokenActual = aux.tokenActual;
        aux.matcheoId("id_objeto");
        Parametro p = new Parametro(tokenActual.obtenerLexema(), tPar, tokenActual.obtenerFila(),
                tokenActual.obtenerColumna());
        return p;
    }

    private Tipo tipoMetodo() {
        String[] ter = { "Bool", "I32", "Str", "Char", "id_clase", "Array" };
        if (aux.verifico(ter)) {
            return tipo();
        } else {
            aux.matcheo("void");
            return new TipoVoid("void");
        }
    }

    private Tipo tipo() {
        String[] ter = { "Bool", "I32", "Str", "Char" };
        if (aux.verifico(ter)) {
            return tipoPrimitivo();
        }
        if (aux.verifico("id_clase")) {
            return tipoReferencia();
        }
        if (aux.verifico("Array")) {
            return tipoArray();
        } else {
            Token tokenActual = aux.tokenActual;
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba un tipo primitivo, referencia o Array, se encontró: " + tokenActual.obtenerLexema());
            return null;
        }
    }

    private Tipo tipoPrimitivo() {
        Token tokenActual = aux.tokenActual;
        String[] ter = { "Bool", "I32", "Str", "Char" };
        if (aux.verifico(ter)) {
            aux.matcheo(tokenActual.obtenerLexema());
            return new TipoPrimitivo(tokenActual.obtenerLexema());
        } else {
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba un tipo primitivo, se encontró: " + tokenActual.obtenerLexema());
            return null;
        }
    }

    private Tipo tipoReferencia() {
        Token tokenActual = aux.tokenActual;
        aux.matcheoId("id_clase");
        return new TipoReferencia(tokenActual.obtenerLexema());
    }

    private Tipo tipoArray() {
        aux.matcheo("Array");
        Tipo tArray = tipoPrimitivo();
        return new TipoArreglo(tArray.obtenerTipo());
    }

    private void sentencia(NodoBloque ASTBloque) {
        if (aux.verifico(";")) {
            aux.matcheo(";");
        } else if (aux.verifico("id_objeto") || aux.verifico("self")) {
            NodoAsignacion ASTAsignacion = ASTBloque.agregarAsignacion();
            asignacion(ASTAsignacion);
            aux.matcheo(";");
        } else if (aux.verifico("(")) {
            NodoExpresion expresion = sentenciaSimple();
            ASTBloque.agregarExpresion(expresion);
        } else if (aux.verifico("if")) {
            aux.matcheo("if");
            aux.matcheo("(");
            NodoIf ASTIf = ASTBloque.agregarIf();
            ASTIf.agregarCondicion(expresion());
            aux.matcheo(")");
            NodoBloque ASTSentenciaThen = ASTIf.agregarSentenciaThen();
            sentencia(ASTSentenciaThen);
            NodoBloque ASTSentenciaElse = ASTIf.agregarSentenciaElse();
            sentencia2(ASTSentenciaElse);
        } else if (aux.verifico("while")) {
            NodoWhile ASTWhile = ASTBloque.agregarWhile();
            aux.matcheo("while");
            aux.matcheo("(");
            NodoExpresion condicion = expresion();
            ASTWhile.agregarCondicion(condicion);
            aux.matcheo(")");
            NodoBloque ASTSentenciaW = ASTWhile.agregarBloqueW();
            sentencia(ASTSentenciaW);
        } else if (aux.verifico("{")) {
            bloque(ASTBloque);
        } else if (aux.verifico("return")) {
            aux.matcheo("return");
            NodoReturn ASTReturn = ASTBloque.agregarReturn();
            NodoExpresion expresionRetorno = expresionP();
            ASTReturn.agregarExpresion(expresionRetorno);
        } else {
            Token tokenActual = aux.tokenActual;
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba el comienzo de una sentencia, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    private void sentencia2(NodoBloque ASTBloque) {
        if (aux.verifico("else")) {
            aux.matcheo("else");
            sentencia(ASTBloque);
        }
    }

    private NodoExpresion expresionP() {
        String[] ter = { "+", "-", "!", "nil", "true", "false", "lit_ent", "lit_cad", "lit_car", "(", "self",
                "id_objeto",
                "id_clase", "new" };
        if (aux.verifico(";")) {
            aux.matcheo(";");
            return new NodoExpresion();
        } else {
            if (aux.verifico(ter)) {
                NodoExpresion retorno = expresion();
                aux.matcheo(";");
                return retorno;
            } else {
                Token tokenActual = aux.tokenActual;
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                        "Se esperaba el comienzo de una expresion, se encontró: " + tokenActual.obtenerLexema());
            }
            return null;
        }
    }

    private void bloque(NodoBloque ASTBloque) {
        aux.matcheo("{");
        sentenciaP(ASTBloque);
        aux.matcheo("}");
    }

    private void asignacion(NodoAsignacion ASTAsignacion) {
        if (aux.verifico("id_objeto")) {
            NodoVariable ladoIzq = new NodoVariable(ASTAsignacion, aux.tokenActual);
            ASTAsignacion.establecerLadoIzq(ladoIzq);
            asignacionVarSimple();
            aux.matcheo("=");
            NodoExpresion ladoDer = expresion();
            ASTAsignacion.establecerLadoDer(ladoDer);
        } else if (aux.verifico("self")) {
            asignacionSelfSimple();
            aux.matcheo("=");
            asignacionVarSimple();
            NodoExpresion ladoDer = expresion();
            ASTAsignacion.establecerLadoDer(ladoDer);
        } else {
            Token tokenActual = aux.tokenActual;
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba id_objeto o self, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    private NodoExpresion asignacionVarSimple() {
        aux.matcheoId("id_objeto");
        return asignacionVarSimpleP();
    }

    private NodoExpresion asignacionVarSimpleP() {
        if (aux.verifico(".")) {
            return encadenadoSimpleP();
        } else if (aux.verifico("[")) {
            aux.matcheo("[");
            NodoExpresion retorno = expresion();
            aux.matcheo("]");
            return retorno;
        } else {
            Token tokenActual = aux.tokenActual;
            if (!tokenActual.obtenerLexema().equals("=")) {
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                        "Se esperaba: =, se encontró: " + tokenActual.obtenerLexema());
            }
            return null;
        }
    }

    private NodoExpresion encadenadoSimpleP() {
        if (aux.verifico(".")) {
            aux.matcheo(".");
            aux.matcheoId("id_objeto");
            return encadenadoSimpleP();
        }
        return null;
    }

    private void asignacionSelfSimple() {
        aux.matcheo("self");
        encadenadoSimpleP();
    }

    private NodoExpresion sentenciaSimple() {
        aux.matcheo("(");
        NodoExpresion retorno = expresion();
        aux.matcheo(")");
        return retorno;
    }

    private NodoExpresion expresion() {
        return expOr();
    }

    private NodoExpresion expOr() {
        NodoExpresion and = expAnd();
        if (and != null) {
            return and;
        } else {
            return expOrP();
        }
    }

    // LAMBDA
    private NodoExpresion expOrP() {
        if (aux.verifico("||")) {
            aux.matcheo("||");
            NodoExpresion expAnd = expAnd();
            if (expAnd != null) {
                return expAnd;
            } else {
                return expOrP();
            }
        }
        return null;
    }

    private NodoExpresion expAnd() {
        NodoExpresion igual = expIgual();
        if (igual != null) {
            return igual;
        } else {
            return expAndP();
        }
    }

    // LAMBDA
    private NodoExpresion expAndP() {
        if (aux.verifico("&&")) {
            aux.matcheo("&&");
            NodoExpresion expIgual = expIgual();
            if (expIgual != null) {
                return expIgual;
            } else {
                return expAndP();
            }
        }
        return null;
    }

    private NodoExpresion expIgual() {
        NodoExpresion compuesta = expCompuesta();
        if (compuesta != null) {
            return compuesta;
        } else {
            return expIgualP();
        }
    }

    // LAMBDA
    private NodoExpresion expIgualP() {
        String[] terOpIgual = { "==", "!=" };
        if (aux.verifico(terOpIgual)) {
            opIgual();
            NodoExpresion expCompuesta = expCompuesta();
            if (expCompuesta != null) {
                return expCompuesta;
            } else {
                return expIgualP();
            }
        }
        return null;
    }

    private NodoExpresion expCompuesta() {
        NodoExpresion add = expAdd();
        if (add != null) {
            return add;
        } else {
            return expCompuestaP();
        }
    }

    // LAMBDA
    private NodoExpresion expCompuestaP() {
        String[] terOpCompuesto = { "<", ">", "<=", ">=" };
        if (aux.verifico(terOpCompuesto)) {
            opCompuesto();
            return expAdd();
        }
        return null;
    }

    private NodoExpresion expAdd() {
        NodoExpresion expMul = expMul();
        // Checkeamos si es una expresion binaria o solamente un literal
        NodoExpresion expAddP = expAddP();
        if (expAddP == null) {
            return expMul;
        } else {
            NodoExpBinaria expBinaria = new NodoExpBinaria();
            expBinaria.establecerLadoIzq(expMul);
            expBinaria.establecerLadoDer(expAddP);
            // Hacemos uso de la variable auxiliar de los nodos
            expBinaria.establecerOp(expAddP.aux);
            return expBinaria;
        }
    }

    // LAMBDA
    private NodoExpresion expAddP() {
        String[] terOpAdd = { "+", "-" };
        if (aux.verifico(terOpAdd)) {
            Token operador = opAdd();
            NodoExpresion expMul = expMul();
            if (expMul != null) {
                expMul.aux = operador;
                return expMul;
            } else {
                NodoExpresion expAddP = expAddP();
                expAddP.aux = operador;
                return expAddP;
            }
        }
        return null;
    }

    private NodoExpresion expMul() {
        NodoExpresion un = expUn();
        NodoExpresion expMulP = expMulP();
        if (expMulP == null) {
            return un;
        } else {
            NodoExpBinaria expBinaria = new NodoExpBinaria();
            expBinaria.establecerOp(expMulP.aux);
            expBinaria.establecerLadoIzq(un);
            expBinaria.establecerLadoDer(expMulP);
            return expBinaria;
        }
    }

    // LAMBDA
    private NodoExpresion expMulP() {
        String[] terOpMul = { "*", "/", "%" };
        if (aux.verifico(terOpMul)) {
            Token operador = opMul();
            NodoExpresion expUn = expUn();
            if (expUn != null) {
                expUn.aux = operador;
                return expUn;
            } else {
                NodoExpresion expMulP = expMulP();
                expMulP.aux = operador;
                return expMulP;
            }
        }
        return null;
    }

    private NodoExpresion expUn() {
        String[] ter = { "+", "-", "!" };
        if (aux.verifico(ter)) {
            opUnario();
            return expUn();
        } else {
            return operando();
        }
    }

    private void opIgual() {
        Token tokenActual = aux.tokenActual;
        String[] ter = { "==", "!=" };
        if (aux.verifico(ter)) {
            aux.matcheo(tokenActual.obtenerLexema());
        } else {
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba \"==\" o \"!=\", se encontró: " + tokenActual.obtenerLexema());
        }
    }

    private void opCompuesto() {
        Token tokenActual = aux.tokenActual;
        String[] ter = { "<", ">", "<=", ">=" };
        if (aux.verifico(ter)) {
            aux.matcheo(tokenActual.obtenerLexema());
        } else {
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba \"<\", \">\", \"<=\", o \">=\", se encontró: " + tokenActual.obtenerLexema());
        }
    }

    private Token opAdd() {
        Token tokenActual = aux.tokenActual;
        String[] ter = { "+", "-" };
        if (aux.verifico(ter)) {
            aux.matcheo(tokenActual.obtenerLexema());
            return tokenActual;
        } else {
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba \"+\" o \"-\", se encontró: " + tokenActual.obtenerLexema());
            return tokenActual;
        }
    }

    private void opUnario() {
        Token tokenActual = aux.tokenActual;
        String[] ter = { "+", "-", "!" };
        if (aux.verifico(ter)) {
            aux.matcheo(tokenActual.obtenerLexema());
        } else {
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba \"+\", \"-\" o \"!\", se encontró: " + tokenActual.obtenerLexema());
        }
    }

    private Token opMul() {
        Token tokenActual = aux.tokenActual;
        String[] ter = { "*", "/", "%" };
        if (aux.verifico(ter)) {
            aux.matcheo(tokenActual.obtenerLexema());
            return tokenActual;
        } else {
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba \"*\", \"/\" o \"%\", se encontró: " + tokenActual.obtenerLexema());
            return tokenActual;
        }
    }

    private NodoExpresion operando() {
        String[] terLiteral = { "nil", "true", "false", "lit_ent", "lit_cad", "lit_car" };
        String[] terPrimario = { "(", "self", "id_objeto", "id_clase", "new" };
        if (aux.verifico(terLiteral)) {
            return literal();
        } else if (aux.verifico(terPrimario)) {
            NodoExpresion primario = primario();
            encadenadoP();
            if (primario instanceof NodoVariable) {
                return primario;
            }
            // Aca todavia no se que hacer xd
            return new NodoExpresion();
        } else {
            Token tokenActual = aux.tokenActual;
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba literal o primario, se encontró: " + tokenActual.obtenerLexema());
            return null;
        }
    }

    // LAMBDA
    private NodoExpresion encadenadoP() {
        if (aux.verifico(".")) {
            aux.matcheo(".");
            aux.matcheoId("id_objeto");
            encadenado2();
            // TO DO
            return new NodoExpresion();
        } else {
            if (aux.tokenActual.obtenerToken() == "id_objeto") {
                return new NodoVariable(null, aux.tokenActual);
            }
        }
        return null;
    }

    private NodoLiteral literal() {
        Token tokenActual = aux.tokenActual;
        aux.matcheo(tokenActual.obtenerLexema());
        return new NodoLiteral(tokenActual);
    }

    private NodoExpresion primario() {
        if (aux.verifico("(")) {
            // TO DO
            expresionParentizada();
            return new NodoExpresion();
        } else if (aux.verifico("self")) {
            // TO DO
            accesoSelf();
            return new NodoExpresion();
        } else if (aux.verifico("id_objeto")) {
            Token tokenActual = aux.tokenActual;
            NodoExpresion primarioP = primarioP();
            aux.matcheoId("id_objeto");
            return primarioP;
        } else if (aux.verifico("id_clase")) {
            // TO DO
            llamadaMetodoEstatico();
            return new NodoExpresion();
        } else if (aux.verifico("new")) {
            // TO DO
            return llamadaConstructor();
        } else {
            Token tokenActual = aux.tokenActual;
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba \"(\",\"self\",\"new\",\"id_clase\",\"id_objeto\" se encontró: "
                            + tokenActual.obtenerLexema());
            return null;
        }
    }

    private NodoExpresion primarioP() {
        if (aux.verifico("(")) {
            // TO DO
            llamadaMetodoP();
            return new NodoExpresion();
        } else {
            return accesoVarP();
        }
    }

    private void expresionParentizada() {
        aux.matcheo("(");
        expresion();
        aux.matcheo(")");
        encadenadoP();
    }

    private void accesoSelf() {
        aux.matcheo("self");
        encadenadoP();
    }

    private NodoExpresion accesoVarP() {
        if (aux.verifico("[")) {
            aux.matcheo("[");
            NodoExpresion expresion = expresion();
            aux.matcheo("]");
            return expresion;
        } else {
            return encadenadoP();
        }
    }

    private void llamadaMetodoP() {
        argumentosActuales();
        encadenadoP();
    }

    private void llamadaMetodo() {
        aux.matcheoId("id_objeto");
        argumentosActuales();
        encadenadoP();
    }

    private void llamadaMetodoEstatico() {
        aux.matcheoId("id_clase");
        aux.matcheo(".");
        llamadaMetodo();
        encadenadoP();
    }

    private NodoExpresion llamadaConstructor() {
        aux.matcheo("new");
        return llamadaConstructorP();
    }

    private NodoExpresion llamadaConstructorP() {
        if (aux.verifico("id_clase")) {
            aux.matcheoId("id_clase");
            argumentosActuales();
            encadenadoP();
            // TO DO
            return new NodoExpresion();
        } else {
            tipoPrimitivo();
            aux.matcheo("[");
            NodoExpresion expresion = expresion();
            aux.matcheo("]");
            return expresion;
        }
    }

    private void argumentosActuales() {
        aux.matcheo("(");
        listaExpresionesP();
    }

    private void listaExpresionesP() {
        if (aux.verifico(")")) {
            aux.matcheo(")");
        } else {
            listaExpresiones();
            aux.matcheo(")");
        }
    }

    private void listaExpresiones() {
        expresion();
        listaExpresiones2();
    }

    private void listaExpresiones2() {
        if (aux.verifico(",")) {
            aux.matcheo(",");
            listaExpresiones();
        }
    }

    private void encadenado2() {
        if (aux.verifico("[")) {
            aux.matcheo("[");
            expresion();
            aux.matcheo("]");
        } else if (aux.verifico("(")) {
            argumentosActuales();
            encadenadoP();
        } else {
            encadenadoP();
        }
    }
}
