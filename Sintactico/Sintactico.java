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
import Semantico.Nodo.NodoAST;
import Semantico.Nodo.NodoArreglo;
import Semantico.Nodo.NodoAsignacion;
import Semantico.Nodo.NodoBloque;
import Semantico.Nodo.NodoClase;
import Semantico.Nodo.NodoExpBinaria;
import Semantico.Nodo.NodoExpUnaria;
import Semantico.Nodo.NodoExpresion;
import Semantico.Nodo.NodoIf;
import Semantico.Nodo.NodoLiteral;
import Semantico.Nodo.NodoLlamadaMetodo;
import Semantico.Nodo.NodoMetodo;
import Semantico.Nodo.NodoReturn;
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
    public NodoAST AST;

    // Este constructor recibe como argumento la ruta en el sistema operativo
    // donde se encuentra el archivo con el codigo fuente
    public Sintactico(File archivo, TablaDeSimbolos tablaDeSimbolos, NodoAST AST) {
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
        // Creamos el árbol de la clase Fantasma que contiene a main
        NodoClase ASTClaseM = AST.agregarHijo();
        ASTClaseM.establecerNombre("Fantasma");
        NodoMetodo ASTMetodoM = ASTClaseM.agregarMetodo(tablaDeSimbolos.obtenerMetodoActual(),
                tablaDeSimbolos.obtenerClaseActual());
        ASTMetodoM.establecerNombre("main");
        NodoBloque ASTBloqueM = ASTMetodoM.agregarBloque(tablaDeSimbolos.obtenerMetodoActual());
        bloqueMetodo(ASTBloqueM);
        tablaDeSimbolos.obtenerClaseActual().insertarMetodo(nuevoMetodo);
        tablaDeSimbolos.insertarClase(nuevaClase);
    }

    // Recibe el arbol de la clase para ampliarlo con su contenido
    private void clase(NodoClase ASTClase) {
        aux.matcheo("class");
        Token tokenActual = aux.tokenActual;
        ASTClase.establecerNombre(tokenActual.obtenerLexema());
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
                NodoMetodo ASTMetodo = ASTClase.agregarMetodo(tablaDeSimbolos.obtenerMetodoActual(),
                        tablaDeSimbolos.obtenerClaseActual());
                constructor(ASTMetodo);
            } else {
                if (aux.verifico(ter1)) {
                    // Insertamos el metodo en el AST de la clase y continuamos el AST por el metodo
                    NodoMetodo ASTMetodo = ASTClase.agregarMetodo(tablaDeSimbolos.obtenerMetodoActual(),
                            tablaDeSimbolos.obtenerClaseActual());
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
        ASTMetodo.establecerNombre("constructor");
        if (tablaDeSimbolos.obtenerClaseActual().tieneConstructor() == false) {
            aux.matcheo("create");
            Constructor nuevoConstructor = new Constructor();
            tablaDeSimbolos.establecerMetodoActual(nuevoConstructor);
            argumentosFormales();
            tablaDeSimbolos.obtenerClaseActual().establecerConstructor(nuevoConstructor);
            NodoBloque ASTBloque = ASTMetodo.agregarBloque(tablaDeSimbolos.obtenerMetodoActual());
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
        ASTMetodo.establecerNombre(tokenActual.obtenerLexema());
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
            NodoBloque ASTBloque = ASTMetodo.agregarBloque(tablaDeSimbolos.obtenerMetodoActual());
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
            NodoIf ASTIf = ASTBloque.agregarIf();
            ASTIf.aux = aux.tokenActual;
            aux.matcheo("if");
            aux.matcheo("(");
            ASTIf.agregarCondicion(expresion());
            aux.matcheo(")");
            NodoBloque ASTSentenciaThen = ASTIf.agregarSentenciaThen();
            sentencia(ASTSentenciaThen);
            NodoBloque ASTSentenciaElse = ASTIf.agregarSentenciaElse();
            sentencia2(ASTSentenciaElse);
        } else if (aux.verifico("while")) {
            NodoWhile ASTWhile = ASTBloque.agregarWhile();
            ASTWhile.aux = aux.tokenActual;
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
            NodoReturn ASTReturn = ASTBloque.agregarReturn();
            ASTReturn.aux = aux.tokenActual;
            aux.matcheo("return");
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
            return new NodoExpresion(tablaDeSimbolos.obtenerMetodoActual(), tablaDeSimbolos.obtenerClaseActual());
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
            NodoVariable ladoIzq = new NodoVariable(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual(), aux.tokenActual);
            ASTAsignacion.establecerLadoIzq(ladoIzq);
            asignacionVarSimple(ladoIzq);
            ASTAsignacion.establecerOp(aux.tokenActual);
            aux.matcheo("=");
            NodoExpresion ladoDer = expresion();
            ASTAsignacion.establecerLadoDer(ladoDer);
        } else if (aux.verifico("self")) {
            NodoVariable ladoIzq = new NodoVariable(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual(), aux.tokenActual);
            ASTAsignacion.establecerLadoIzq(ladoIzq);
            asignacionSelfSimple(ladoIzq);
            ASTAsignacion.establecerOp(aux.tokenActual);
            aux.matcheo("=");
            NodoExpresion ladoDer = expresion();
            ASTAsignacion.establecerLadoDer(ladoDer);
        } else {
            Token tokenActual = aux.tokenActual;
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba id_objeto o self, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    private void asignacionVarSimple(NodoExpresion var) {
        aux.matcheoId("id_objeto");
        asignacionVarSimpleP(var);
    }

    private void asignacionVarSimpleP(NodoExpresion var) {
        if (aux.verifico(".")) {
            encadenadoSimpleP(var);
        } else if (aux.verifico("[")) {
            aux.matcheo("[");
            NodoArreglo arreglo = new NodoArreglo(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual(), aux.tokenActual);
            NodoExpresion accesoArray = expresion();
            arreglo.establecerEncadenado(accesoArray);
            var.establecerEncadenado(arreglo);
            aux.matcheo("]");
        } else {
            Token tokenActual = aux.tokenActual;
            if (!tokenActual.obtenerLexema().equals("=")) {
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                        "Se esperaba: =, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    private void encadenadoSimpleP(NodoExpresion var) {
        if (aux.verifico(".")) {
            aux.matcheo(".");
            NodoVariable varEnc = new NodoVariable(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual(), aux.tokenActual);
            aux.matcheoId("id_objeto");
            var.establecerEncadenado(varEnc);
            encadenadoSimpleP(varEnc);
        }
    }

    private void asignacionSelfSimple(NodoExpresion var) {
        aux.matcheo("self");
        encadenadoSimpleP(var);
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
        NodoExpresion expOrP = expOrP();
        if (expOrP != null) {
            NodoExpBinaria newExpOr = new NodoExpBinaria(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual());
            newExpOr.establecerLadoIzq(and);
            newExpOr.establecerLadoDer(expOrP);
            // El operador es enviado haciendo uso del auxiliar
            newExpOr.establecerOp(expOrP.aux);
            return newExpOr;
        } else {
            return and;
        }
    }

    // LAMBDA
    private NodoExpresion expOrP() {
        if (aux.verifico("||")) {
            Token tokenActual = aux.tokenActual;
            aux.matcheo("||");
            NodoExpresion expAnd = expAnd();
            NodoExpresion expOrP = expOrP();
            if (expOrP != null) {
                NodoExpBinaria newExpOr = new NodoExpBinaria(tablaDeSimbolos.obtenerMetodoActual(),
                        tablaDeSimbolos.obtenerClaseActual());
                newExpOr.establecerLadoIzq(expAnd);
                newExpOr.establecerLadoDer(expOrP);
                newExpOr.aux = tokenActual;
                newExpOr.establecerOp(tokenActual);
                return newExpOr;
            } else {
                expAnd.aux = tokenActual;
                return expAnd;
            }
        }
        return null;
    }

    private NodoExpresion expAnd() {
        NodoExpresion igual = expIgual();
        NodoExpresion expAndP = expAndP();
        if (expAndP != null) {
            NodoExpBinaria newExpAnd = new NodoExpBinaria(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual());
            newExpAnd.establecerLadoIzq(igual);
            newExpAnd.establecerLadoDer(expAndP);
            newExpAnd.establecerOp(expAndP.aux);
            return newExpAnd;
        } else {
            return igual;
        }
    }

    // LAMBDA
    private NodoExpresion expAndP() {
        if (aux.verifico("&&")) {
            Token tokenActual = aux.tokenActual;
            aux.matcheo("&&");
            NodoExpresion expIgual = expIgual();
            NodoExpresion expAndP = expAndP();
            if (expAndP != null) {
                NodoExpBinaria newExpAnd = new NodoExpBinaria(tablaDeSimbolos.obtenerMetodoActual(),
                        tablaDeSimbolos.obtenerClaseActual());
                newExpAnd.establecerLadoIzq(expIgual);
                newExpAnd.establecerLadoDer(expAndP);
                newExpAnd.aux = tokenActual;
                newExpAnd.establecerOp(tokenActual);
                return newExpAnd;
            } else {
                expIgual.aux = tokenActual;
                return expIgual;
            }
        }
        return null;
    }

    private NodoExpresion expIgual() {
        NodoExpresion compuesta = expCompuesta();
        NodoExpresion expIgualP = expIgualP();
        if (expIgualP != null) {
            NodoExpBinaria newExpIgual = new NodoExpBinaria(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual());
            newExpIgual.establecerLadoIzq(compuesta);
            newExpIgual.establecerLadoDer(expIgualP);
            newExpIgual.establecerOp(expIgualP.aux);
            return newExpIgual;
        } else {
            return compuesta;
        }
    }

    // LAMBDA
    private NodoExpresion expIgualP() {
        String[] terOpIgual = { "==", "!=" };
        if (aux.verifico(terOpIgual)) {
            Token operador = opIgual();
            NodoExpresion expCompuesta = expCompuesta();
            NodoExpresion expIgualP = expIgualP();
            if (expIgualP != null) {
                NodoExpBinaria newExpCompuesta = new NodoExpBinaria(tablaDeSimbolos.obtenerMetodoActual(),
                        tablaDeSimbolos.obtenerClaseActual());
                newExpCompuesta.establecerLadoIzq(expCompuesta);
                newExpCompuesta.establecerLadoDer(expIgualP);
                newExpCompuesta.establecerOp(operador);
                return newExpCompuesta;
            } else {
                expCompuesta.aux = operador;
                return expCompuesta;
            }
        }
        return null;
    }

    private NodoExpresion expCompuesta() {
        NodoExpresion add = expAdd();
        Token op = aux.tokenActual;
        NodoExpresion expCompuestaP = expCompuestaP();
        if (expCompuestaP != null) {
            NodoExpBinaria newExpComp = new NodoExpBinaria(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual());
            newExpComp.establecerLadoIzq(add);
            newExpComp.establecerLadoDer(expCompuestaP);
            newExpComp.establecerOp(op);
            return newExpComp;
        } else {
            return add;
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
            NodoExpBinaria expBinaria = new NodoExpBinaria(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual());
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
            NodoExpresion expAddP = expAddP();
            if (expAddP != null) {
                NodoExpBinaria newExpAdd = new NodoExpBinaria(tablaDeSimbolos.obtenerMetodoActual(),
                        tablaDeSimbolos.obtenerClaseActual());
                newExpAdd.establecerLadoIzq(expMul);
                newExpAdd.establecerLadoDer(expAddP);
                newExpAdd.establecerOp(operador);
                return newExpAdd;
            } else {
                expMul.aux = operador;
                return expMul;
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
            NodoExpBinaria expBinaria = new NodoExpBinaria(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual());
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
            NodoExpresion expMulP = expMulP();
            if (expMulP != null) {
                NodoExpBinaria newExpMul = new NodoExpBinaria(tablaDeSimbolos.obtenerMetodoActual(),
                        tablaDeSimbolos.obtenerClaseActual());
                newExpMul.establecerLadoIzq(expUn);
                newExpMul.establecerLadoDer(expMulP);
                newExpMul.establecerOp(operador);
                return newExpMul;
            } else {
                expUn.aux = operador;
                return expUn;
            }
        }
        return null;
    }

    private NodoExpresion expUn() {
        String[] ter = { "+", "-", "!" };
        if (aux.verifico(ter)) {
            Token operador = opUnario();
            NodoExpUnaria newExpUn = new NodoExpUnaria(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual());
            newExpUn.establecerLadoDer(expUn());
            newExpUn.establecerOp(operador);
            return newExpUn;
        } else {
            return operando();
        }
    }

    private Token opIgual() {
        Token tokenActual = aux.tokenActual;
        String[] ter = { "==", "!=" };
        if (aux.verifico(ter)) {
            aux.matcheo(tokenActual.obtenerLexema());
            return tokenActual;
        } else {
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba \"==\" o \"!=\", se encontró: " + tokenActual.obtenerLexema());
            return tokenActual;
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

    private Token opUnario() {
        Token tokenActual = aux.tokenActual;
        String[] ter = { "+", "-", "!" };
        if (aux.verifico(ter)) {
            aux.matcheo(tokenActual.obtenerLexema());
            return tokenActual;
        } else {
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba \"+\", \"-\" o \"!\", se encontró: " + tokenActual.obtenerLexema());
            return tokenActual;
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
            encadenadoP(primario);
            return primario;
        } else {
            Token tokenActual = aux.tokenActual;
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba literal o primario, se encontró: " + tokenActual.obtenerLexema());
            return null;
        }
    }

    // LAMBDA
    private void encadenadoP(NodoExpresion exp) {
        if (aux.verifico(".")) {
            aux.matcheo(".");
            Token tokenO = aux.tokenActual;
            aux.matcheoId("id_objeto");
            encadenado2(exp, tokenO);
        }
    }

    private NodoLiteral literal() {
        Token tokenActual = aux.tokenActual;
        aux.matcheo(tokenActual.obtenerLexema());
        return new NodoLiteral(tablaDeSimbolos.obtenerMetodoActual(), tablaDeSimbolos.obtenerClaseActual(),
                tokenActual);
    }

    private NodoExpresion primario() {
        if (aux.verifico("(")) {

            return expresionParentizada();

        } else if (aux.verifico("self")) {

            return accesoSelf();

        } else if (aux.verifico("id_objeto")) {

            Token tokenO = aux.tokenActual;
            aux.matcheoId("id_objeto");
            NodoExpresion primario = primarioP(tokenO);
            return primario;

        } else if (aux.verifico("id_clase")) {

            return llamadaMetodoEstatico();

        } else if (aux.verifico("new")) {

            return llamadaConstructor();

        } else {
            Token tokenActual = aux.tokenActual;
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba \"(\",\"self\",\"new\",\"id_clase\",\"id_objeto\" se encontró: "
                            + tokenActual.obtenerLexema());
            return null;
        }
    }

    private NodoExpresion primarioP(Token token) {
        if (aux.verifico("(")) {
            NodoLlamadaMetodo llamadaM = new NodoLlamadaMetodo(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual(), token);
            llamadaM.establecerTablaDeSimbolos(tablaDeSimbolos);
            llamadaMetodoP(llamadaM);
            return llamadaM;
        } else {
            NodoVariable var = new NodoVariable(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual(), token);
            accesoVarP(var);
            return var;
        }
    }

    private NodoExpresion expresionParentizada() {
        aux.matcheo("(");
        NodoExpresion newExp = expresion();
        aux.matcheo(")");
        encadenadoP(newExp);
        return newExp;
    }

    private NodoExpresion accesoSelf() {
        NodoVariable newVar = new NodoVariable(tablaDeSimbolos.obtenerMetodoActual(),
                tablaDeSimbolos.obtenerClaseActual(), aux.tokenActual);
        aux.matcheo("self");
        encadenadoP(newVar);
        return newVar;
    }

    private void accesoVarP(NodoExpresion var) {
        if (aux.verifico("[")) {
            aux.matcheo("[");
            NodoArreglo arreglo = new NodoArreglo(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual(), aux.tokenActual);
            NodoExpresion accesoArray = expresion();
            aux.matcheo("]");
            arreglo.establecerEncadenado(accesoArray);
            var.establecerEncadenado(arreglo);
        } else {
            encadenadoP(var);
        }
    }

    private void llamadaMetodoP(NodoLlamadaMetodo varMetodo) {
        argumentosActuales(varMetodo);
        encadenadoP(varMetodo);
    }

    private NodoLlamadaMetodo llamadaMetodo(Token token) {
        // Usar token para definir el tipo del método (clase a la que pertenece)
        NodoLlamadaMetodo llamadaME = new NodoLlamadaMetodo(tablaDeSimbolos.obtenerMetodoActual(),
                tablaDeSimbolos.obtenerClaseActual(), aux.tokenActual);
        llamadaME.establecerClase(token.obtenerLexema());
        llamadaME.establecerForma(true);
        llamadaME.establecerTablaDeSimbolos(tablaDeSimbolos);
        aux.matcheoId("id_objeto");
        argumentosActuales(llamadaME);
        encadenadoP(llamadaME);
        return llamadaME;
    }

    private NodoLlamadaMetodo llamadaMetodoEstatico() {
        Token token = aux.tokenActual;
        aux.matcheoId("id_clase");
        aux.matcheo(".");
        return llamadaMetodo(token);
    }

    private NodoExpresion llamadaConstructor() {
        aux.matcheo("new");
        return llamadaConstructorP();
    }

    private NodoExpresion llamadaConstructorP() {
        if (aux.verifico("id_clase")) {
            NodoLlamadaMetodo llamadaC = new NodoLlamadaMetodo(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual(), aux.tokenActual);
            llamadaC.establecerClase(aux.tokenActual.obtenerLexema());
            llamadaC.establecerTablaDeSimbolos(tablaDeSimbolos);
            aux.matcheoId("id_clase");
            argumentosActuales(llamadaC);
            encadenadoP(llamadaC);
            return llamadaC;
        } else {
            //VER QUE HACER CON LA INICIALIZACION DEL ARRAY
            Tipo tArray = tipoPrimitivo();
            aux.matcheo("[");
            NodoExpresion expresion = expresion();
            aux.matcheo("]");
            expresion.establecerTipo(tArray);
            return expresion;
        }
    }

    private void argumentosActuales(NodoLlamadaMetodo varMetodo) {
        aux.matcheo("(");
        listaExpresionesP(varMetodo);
    }

    private void listaExpresionesP(NodoLlamadaMetodo varMetodo) {
        if (aux.verifico(")")) {
            aux.matcheo(")");
        } else {
            listaExpresiones(varMetodo);
            aux.matcheo(")");
        }
    }

    private void listaExpresiones(NodoLlamadaMetodo varMetodo) {
        NodoExpresion expM = expresion();
        varMetodo.agregarArgumento(expM);
        listaExpresiones2(varMetodo);
    }

    private void listaExpresiones2(NodoLlamadaMetodo varMetodo) {
        if (aux.verifico(",")) {
            aux.matcheo(",");
            listaExpresiones(varMetodo);
        }
    }

    private void encadenado2(NodoExpresion exp, Token token) {
        if (aux.verifico("[")) {
            NodoVariable var = new NodoVariable(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual(), token);
            exp.establecerEncadenado(var);
            aux.matcheo("[");
            NodoArreglo arreglo = new NodoArreglo(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual(), aux.tokenActual);
            NodoExpresion accesoArray = expresion();
            arreglo.establecerEncadenado(accesoArray);
            var.establecerEncadenado(arreglo);
            aux.matcheo("]");
        } else if (aux.verifico("(")) {
            NodoLlamadaMetodo llamadaM = new NodoLlamadaMetodo(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual(), token);
            llamadaM.establecerTablaDeSimbolos(tablaDeSimbolos);
            argumentosActuales(llamadaM);
            exp.establecerEncadenado(llamadaM);
            encadenadoP(llamadaM);
        } else {
            NodoVariable var = new NodoVariable(tablaDeSimbolos.obtenerMetodoActual(),
                    tablaDeSimbolos.obtenerClaseActual(), token);
            exp.establecerEncadenado(var);
            encadenadoP(var);
        }
    }
}
