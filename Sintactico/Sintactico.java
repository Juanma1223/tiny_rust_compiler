package Sintactico;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.http.HttpResponse.PushPromiseHandler;

import Lexico.Lexico;
import Lexico.Token;

public class Sintactico {

    // Se instancia una unica vez el analizador lexico
    Lexico analizadorLexico;
    // Esta clase se encarga de interactual con el analizador lexico
    // e implementar metodos de ayuda
    AuxiliarSintactico aux;

    // Este constructor recibe como argumento la ruta en el sistema operativo donde
    // se encuentra el
    // archivo con codigo fuente
    public Sintactico(String dirArchivo) {
        try {
            File archivo = new File(dirArchivo);
            this.analizadorLexico = new Lexico(archivo);
            this.aux = new AuxiliarSintactico(this.analizadorLexico);

            // Iniciamos el analizis sintactico
            this.start();
        } catch (FileNotFoundException e) {
            System.out.println("Error al abrir archivo de entrada!");
            System.exit(1);
        }
    }

    public void start() {
        claseP(); 
        metodoMain();
        exito();
    }

    public void exito(){
        Token tokenActual = aux.tokenActual;
        if (!tokenActual.obtenerLexema().equals("EOF")) {
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "NO PUEDE VENIR NADA DESPUÉS DEL MÉTODO MAIN! Se esperaba: EOF, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    public void claseP() {
        if (aux.verifico("class")) {
            clase();
            claseP();
        } else {
            Token tokenActual = aux.tokenActual;
            if (!tokenActual.obtenerLexema().equals("fn")) {
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                        "FALTA MÉTODO MAIN! Se esperaba: fn, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    public void metodoMain() {
        aux.matcheo("fn");
        aux.matcheo("main");
        aux.matcheo("(");
        aux.matcheo(")");
        bloqueMetodo();
    }

    public void clase() {
        aux.matcheo("class");
        aux.matcheoId("id_clase");
        restoClase();
    }

    public void restoClase() {
        if (aux.verifico(":")) {
            aux.matcheo(":");
            aux.matcheoId("id_clase");
            aux.matcheo("{");
            miembroP();
            aux.matcheo("}");
        } else if (aux.verifico("{")) {
            aux.matcheo("{");
            miembroP();
            aux.matcheo("}");
        } else {
            Token tokenActual = aux.tokenActual;
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba: : o {, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    public void miembroP() {
        String[] ter = { "pub", "Bool", "I32", "Str", "Char", "idClase", "Array", "create", "static", "fn" };
        if (aux.verifico(ter)) {
            miembro();
            miembroP();
        } else {
            Token tokenActual = aux.tokenActual;
            if (!tokenActual.obtenerLexema().equals("}")) {
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                        "Se esperaba: }, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    public void miembro() {
        String[] ter = { "pub", "Bool", "I32", "Str", "Char", "id_clase", "Array" };
        String[] ter1 = { "static", "fn" };
        if (aux.verifico(ter)) {
            atributo();
        } else {
            if (aux.verifico("create")) {
                constructor();
            } else {
                if (aux.verifico(ter1)) {
                    metodo();
                } else {
                    Token tokenActual = aux.tokenActual;
                    ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                            "Se esperaba: atributo, constructor o método, se encontró: " + tokenActual.obtenerLexema());
                }
            }
        }
    }

    public void atributo() {
        if (aux.verifico("pub")) {
            aux.matcheo("pub");
        }
        tipo();
        aux.matcheo(":");
        listaDeclVariables();
        aux.matcheo(";");
    }

    public void constructor() {
        aux.matcheo("create");
        argumentosFormales();
        bloqueMetodo();
    }

    public void metodo() {
        if (aux.verifico("static")) {
            aux.matcheo("static");
        }
        aux.matcheo("fn");
        aux.matcheoId("id_objeto");
        argumentosFormales();
        aux.matcheo("->");
        tipoMetodo();
        bloqueMetodo();
    }

    public void bloqueMetodo() {
        aux.matcheo("{");
        declVarLocalesP();
        sentenciaP();
        aux.matcheo("}");
    }

    public void declVarLocalesP() {
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

    public void sentenciaP() {
        String[] ter = { ";", "id_objeto", "self", "(", "if", "while", "{", "return" };
        if (aux.verifico(ter)) {
            sentencia();
            sentenciaP();
        } else {
            Token tokenActual = aux.tokenActual;
            if (!tokenActual.obtenerLexema().equals("}")) {
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                        "Se esperaba: }, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    public void declVarLocales() {
        tipo();
        aux.matcheo(":");
        listaDeclVariables();
        aux.matcheo(";");
    }

    public void listaDeclVariables() {
        aux.matcheoId("id_objeto");
        listaDeclVariablesP();
    }

    public void listaDeclVariablesP() {
        if (aux.verifico(",")) {
            aux.matcheo(",");
            listaDeclVariables();
        } else {
            Token tokenActual = aux.tokenActual;
            if (!tokenActual.obtenerLexema().equals(";")) {
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                        "Se esperaba: ;, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    public void argumentosFormales() {
        aux.matcheo("(");
        listaArgumentosFormalesP();
    }

    public void listaArgumentosFormalesP() {
        String[] ter = { "Bool", "I32", "Str", "Char", "id_clase", "Array" };
        if (aux.verifico(ter)) {
            listaArgumentosFormales();
            aux.matcheo(")");
        } else {
            aux.matcheo(")");
        }
    }

    public void listaArgumentosFormales() {
        argumentoFormal();
        listaArgumentosFormales2();
    }

    public void listaArgumentosFormales2() {
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

    public void argumentoFormal() {
        tipo();
        aux.matcheo(":");
        aux.matcheoId("id_objeto");
    }

    public void tipoMetodo() {
        String[] ter = { "Bool", "I32", "Str", "Char", "id_clase", "Array" };
        if (aux.verifico(ter)) {
            tipo();
        } else {
            aux.matcheo("void");
        }
    }

    public void tipo() {
        String[] ter = { "Bool", "I32", "Str", "Char" };
        if (aux.verifico(ter)) {
            tipoPrimitivo();
            return;
        }
        if (aux.verifico("id_clase")) {
            tipoReferencia();
            return;
        }
        if (aux.verifico("Array")) {
            tipoArray();
            return;
        } else {
            Token tokenActual = aux.tokenActual;
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba un tipo primitivo, referencia o Array, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    public void tipoPrimitivo() {
        Token tokenActual = aux.tokenActual;
        String[] ter = { "Bool", "I32", "Str", "Char" };
        if (aux.verifico(ter)) {
            aux.matcheo(tokenActual.obtenerLexema());
        } else {
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba un tipo primitivo, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    public void tipoReferencia() {
        aux.matcheoId("id_clase");
    }

    public void tipoArray() {
        aux.matcheo("Array");
        tipoPrimitivo();
    }

    public void sentencia() {
        
        if (aux.verifico(";")) {
            aux.matcheo(";");
        } else if (aux.verifico("id_objeto") || aux.verifico("self")) {
            asignacion();
            aux.matcheo(";");
        } else if (aux.verifico("(")) {
            sentenciaSimple();
        } else if (aux.verifico("if")) {
            aux.matcheo("if");
            aux.matcheo("(");
            expresion();
            aux.matcheo(")");
            sentencia();
            sentencia2();
        } else if (aux.verifico("while")) {
            aux.matcheo("while");
            aux.matcheo("(");
            expresion();
            aux.matcheo(")");
            sentencia();
        } else if (aux.verifico("{")) {
            bloque();
        } else if (aux.verifico("return")) {
            aux.matcheo("return");
            expresionP();
        } else {
            Token tokenActual = aux.tokenActual;
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba el comienzo de una sentencia, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    public void sentencia2() {
        if (aux.verifico("else")) {
            aux.matcheo("else");
            sentencia();
        }
    }

    public void expresionP() {
        String[] ter = { "+", "-", "!", "nil", "true", "false", "lit_ent", "lit_cad", "lit_car", "(", "self", "id_objeto",
                "id_clase", "new" };
        if (aux.verifico(";")) {
            aux.matcheo(";");
        } else {
            if (aux.verifico(ter)) {
                expresion();
                aux.matcheo(";");
            } else {
                Token tokenActual = aux.tokenActual;
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                        "Se esperaba el comienzo de una expresion, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    public void bloque() {
        aux.matcheo("{");
        sentenciaP();
        aux.matcheo("}");
    }

    public void asignacion() {
        if (aux.verifico("id_objeto")) {
            asignacionVarSimple();
            aux.matcheo("=");
            expresion();
        } else if (aux.verifico("self")) {
            asignacionSelfSimple();
            aux.matcheo("=");
            expresion();
        } else {
            Token tokenActual = aux.tokenActual;
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba id_objeto o self, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    public void asignacionVarSimple() {
        aux.matcheoId("id_objeto");
        asignacionVarSimpleP();
    }

    public void asignacionVarSimpleP() {
        if (aux.verifico(".")) {
            encadenadoSimpleP();
        } else if (aux.verifico("[")) {
            aux.matcheo("[");
            expresion();
            aux.matcheo("]");
        } else {
            Token tokenActual = aux.tokenActual;
            if (!tokenActual.obtenerLexema().equals("=")) {
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                        "Se esperaba: =, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    public void encadenadoSimpleP() {
        if (aux.verifico(".")) {
            aux.matcheo(".");
            aux.matcheoId("id_objeto");
            encadenadoSimpleP();
        }
    }

    public void asignacionSelfSimple() {
        aux.matcheo("self");
        encadenadoSimpleP();
    }

    public void sentenciaSimple() {
        aux.matcheo("(");
        expresion();
        aux.matcheo(")");
    }

    public void expresion() {
        expOr();
    }

    private void expOr() {
        expAnd();
        expOrP();
    }

    // LAMBDA
    private void expOrP() {
        if (aux.verifico("||")) {
            aux.matcheo("||");
            expAnd();
            expOrP();
        }
    }

    private void expAnd() {
        expIgual();
        expAndP();
    }

    // LAMBDA
    private void expAndP() {
        if (aux.verifico("&&")) {
            aux.matcheo("&&");
            expIgual();
            expAndP();
        }
    }

    private void expIgual() {
        expCompuesta();
        expIgualP();
    }

    // LAMBDA
    private void expIgualP() {
        String[] terOpIgual = { "==", "!=" };
        if (aux.verifico(terOpIgual)) {
            opIgual();
            expCompuesta();
            expIgualP();
        }
    }

    private void expCompuesta() {
        expAdd();
        expCompuestaP();
    }

    // LAMBDA
    private void expCompuestaP() {
        String[] terOpCompuesto = { "<", ">", "<=", ">=" };
        if (aux.verifico(terOpCompuesto)) {
            opCompuesto();
            expAdd();
        }
    }

    private void expAdd() {
        expMul();
        expAddP();
    }

    // LAMBDA
    private void expAddP() {
        String[] terOpAdd = { "+", "-" };
        if (aux.verifico(terOpAdd)) {
            opAdd();
            expMul();
            expAddP();
        }
    }

    private void expMul() {
        expUn();
        expMulP();
    }

    // LAMBDA
    private void expMulP() {
        String[] terOpMul = { "*", "/", "%" };
        if (aux.verifico(terOpMul)) {
            opMul();
            expUn();
            expMulP();
        }
    }

    private void expUn() {
        String[] ter = { "+", "-", "!" };
        if (aux.verifico(ter)) {
            opUnario();
            expUn();
        } else {
            operando();
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

    private void opAdd() {
        Token tokenActual = aux.tokenActual;
        String[] ter = { "+", "-" };
        if (aux.verifico(ter)) {
            aux.matcheo(tokenActual.obtenerLexema());
        } else {
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba \"+\" o \"-\", se encontró: " + tokenActual.obtenerLexema());
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

    private void opMul() {
        Token tokenActual = aux.tokenActual;
        String[] ter = { "*", "/", "%" };
        if (aux.verifico(ter)) {
            aux.matcheo(tokenActual.obtenerLexema());
        } else {
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba \"*\", \"/\" o \"%\", se encontró: " + tokenActual.obtenerLexema());
        }
    }

    private void operando() {
        String[] terLiteral = { "nil", "true", "false", "lit_ent", "lit_cad", "lit_car" };
        String[] terPrimario = { "(", "self", "id_objeto", "id_clase", "new" };
        if (aux.verifico(terLiteral)) {
            literal();
        } else if (aux.verifico(terPrimario)) {
            primario();
            encadenadoP();
        } else {
            Token tokenActual = aux.tokenActual;
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba literal o primario, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    // LAMBDA
    private void encadenadoP() {
        if (aux.verifico(".")) {
            aux.matcheo(".");
            aux.matcheoId("id_objeto");
            encadenado2();
        }

    }

    private void literal() {
        Token tokenActual = aux.tokenActual;
        aux.matcheo(tokenActual.obtenerLexema());
    }

    private void primario() {
        if (aux.verifico("(")) {
            expresionParentizada();
        } else if (aux.verifico("self")) {
            accesoSelf();
        } else if (aux.verifico("id_objeto")) {
            aux.matcheoId("id_objeto");
            primarioP();
        } else if (aux.verifico("id_clase")) {
            llamadaMetodoEstatico();
        } else if (aux.verifico("new")) {
            llamadaConstructor();
        } else {
            Token tokenActual = aux.tokenActual;
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba \"(\",\"self\",\"new\",\"id_clase\",\"id_objeto\" se encontró: "
                            + tokenActual.obtenerLexema());
        }
    }

    // FALTA
    private void primarioP() {
        if (aux.verifico("(")) {
            llamadaMetodoP();
        } else {
            accesoVarP();
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

    // FALTA
    private void accesoVarP() {
        if (aux.verifico("[")) {
            aux.matcheo("[");
            expresion();
            aux.matcheo("]");
        } else {
            encadenadoP();
        }

    }

    // REVISAR!!
    private void llamadaMetodoP() {
        argumentosActuales();
        encadenadoP();

    }

    // REVISAR!!
    private void llamadaMetodo() {
        aux.matcheoId("id_objeto");
        argumentosActuales();
        encadenadoP();

    }

    // REVISAR!!
    private void llamadaMetodoEstatico() {
        aux.matcheoId("id_clase");
        aux.matcheo(".");
        llamadaMetodo();
        encadenadoP();
    }

    private void llamadaConstructor() {
        aux.matcheo("new");
        llamadaConstructorP();
    }

    private void llamadaConstructorP() {
        if (aux.verifico("id_clase")) {
            aux.matcheoId("id_clase");
            argumentosActuales();
            encadenadoP();
        } else {
            tipoPrimitivo();
            aux.matcheo("[");
            expresion();
            aux.matcheo("]");
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
