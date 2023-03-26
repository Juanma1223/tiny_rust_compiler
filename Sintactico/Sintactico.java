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
    
    // Este constructor recibe como argumento la ruta en el sistema operativo donde se encuentra el
    // archivo con codigo fuente
    public Sintactico(String dirArchivo){
        try {
            // File archivo = new File(dirArchivo);
            File archivo = new File("test/test.rs");
            this.analizadorLexico = new Lexico(archivo);
            this.aux = new AuxiliarSintactico(this.analizadorLexico);
        } catch (FileNotFoundException e) {
            System.out.println("Error al abrir archivo de entrada!");
            System.exit(1);
        }
    }

    public void start() {
        claseP();
        metodoMain();
    }

    public void claseP() {
        if(aux.verifico("class")){
            clase();
            claseP();
        }
        else{
            Token tokenActual = aux.tokenActual();
            if(tokenActual.obtenerLexema() != "fn"){
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                  "Se esperaba: fn, se encontró: " + tokenActual.obtenerLexema());
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
        if(aux.verifico(":")){
            aux.matcheo(":");
            aux.matcheoId("id_clase");
            aux.matcheo("{");
            miembroP();
            aux.matcheo("}");
        }
        else if(aux.verifico("{")){
            aux.matcheo("{");
            miembroP();
            aux.matcheo("}");
        }
        else{
            Token tokenActual = aux.tokenActual();
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                  "Se esperaba: : o (, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    public void miembroP() {
        String[] ter = {"pub", "Bool", "I32", "Str", "Char", "idClase", "Array", "create", "static", "fn"};
        if(aux.verifico(ter)){
            miembro();
            miembroP();
        }
        else{
            Token tokenActual = aux.tokenActual();
            if(tokenActual.obtenerLexema() != "}"){
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                  "Se esperaba: }, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    public void miembro() {
        String[] ter = {"pub", "Bool", "I32", "Str", "Char", "id_clase", "Array"};
        String[] ter1 = {"static", "fn"};
        if(aux.verifico(ter)){
            atributo();
        }
        else if(aux.verifico("create")){
            constructor();
        }
        else if(aux.verifico(ter1)){
            metodo();
        }
        else{
            Token tokenActual = aux.tokenActual();
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                  "Se esperaba: atributo, constructor o método, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    public void atributo() {
        if(aux.verifico("pub")){
            aux.matcheo("pub");
        }
        String[] ter = {"Bool", "I32", "Str", "Char", "id_clase", "Array"};
        if(aux.verifico(ter)){
            tipo();
            aux.matcheo(":");
            listaDeclVariables();
            aux.matcheo(";");
        }
    }

    public void constructor() {
        aux.matcheo("create");
        argumentosFormales();
        bloqueMetodo();
    }

    public void metodo() {
        if(aux.verifico("static")){
            aux.matcheo("static");
        }
        aux.matcheo("fn");
        aux.matcheo("id_objeto");
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
        String[] ter = {"Bool", "I32", "Str", "Char", "id_clase", "Array"};
        String[] ter1 = {";", "id_objeto", "self", "(", "if", "while", "{", "return", "}"};
        if(aux.verifico(ter)){
            declVarLocales();
            declVarLocalesP();
        }
        else{
            if(aux.verifico(ter1) == false){
                Token tokenActual = aux.tokenActual();
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                  "Se esperaba: ;, id_objeto, self, (, if, while, {, return, }, se encontró: " + tokenActual.obtenerLexema());
            }
        }

    }

    public void sentenciaP() {
        String[] ter = {";", "id_objeto", "self", "(", "if", "while", "{", "return"};
        if(aux.verifico(ter)){
            sentencia();
            sentenciaP();
        }
        else{
            Token tokenActual = aux.tokenActual();
            if(tokenActual.obtenerLexema() != "}"){
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
        aux.matcheo("id_objeto");
        listaDeclVariablesP();
    }

    public void listaDeclVariablesP() {
        if(aux.verifico(",")){
            aux.matcheo(",");
            listaDeclVariables();
        }
        else{
            Token tokenActual = aux.tokenActual();
            if(tokenActual.obtenerLexema() != ";"){
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
        String[] ter = {"Bool", "I32", "Str", "Char", "id_clase", "Array"};
        if(aux.verifico(ter)){
            listaArgumentosFormales();
            aux.matcheo(")");
        }
        else{
            aux.matcheo(")");
        }
    }

    public void listaArgumentosFormales() {
        argumentoFormal();
        listaArgumentosFormales2();
    }

    public void listaArgumentosFormales2() {
        if(aux.verifico(",")){
            aux.matcheo(",");
            listaArgumentosFormales();
        }
        else{
            Token tokenActual = aux.tokenActual();
            if(tokenActual.obtenerLexema() != ")"){
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                  "Se esperaba: ), se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    public void argumentoFormal() {
        tipo();
        aux.matcheo(":");
        aux.matcheo("id_objeto");
    }

    public void tipoMetodo() {
        String[] ter = {"Bool", "I32", "Str", "Char", "id_clase", "Array"};
        if(aux.verifico(ter)){
            tipo();
        }
        else{
            aux.matcheo("void");
        }
    }

    public void tipo() {
        String[] ter = {"Bool", "I32", "Str", "Char"};
        if(aux.verifico(ter)){
            tipoPrimitivo();
        }
        if(aux.verifico("id_clase")){
            tipoReferencia();
        }
        if(aux.verifico("Array")){
            tipoArray();
        }
        else{
            Token tokenActual = aux.tokenActual();
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
            "Se esperaba un tipo primitivo, referencia o Array, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    public void tipoPrimitivo() {
        Token tokenActual = aux.tokenActual();
        String[] ter = {"Bool", "I32", "Str", "Char"};
        if(aux.verifico(ter)){
            aux.matcheo(tokenActual.obtenerLexema());
        }
        else{
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
            "Se esperaba un tipo primitivo, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    public void tipoReferencia() {
        aux.matcheo("id_clase");
    }

    public void tipoArray() {
        aux.matcheo("Array");
        tipoPrimitivo();
    }

    public void sentencia() {
        if(aux.verifico(";")){
            aux.matcheo(";");
        }
        else if(aux.verifico("id_objeto") || aux.verifico("self")){
            asignacion();
            aux.matcheo(";");
        }
        else if(aux.verifico("if")){
            aux.matcheo("if");
            aux.matcheo("(");
            expresion();
            aux.matcheo(")");
            sentencia();
            sentencia2();
        }
        else if(aux.verifico("while")){
            aux.matcheo("while");
            aux.matcheo("(");
            expresion();
            aux.matcheo(")");
            sentencia();
        }
        else if(aux.verifico("{")){
            bloque();
        }
        else if(aux.verifico("return")){
            aux.matcheo("return");
            expresionP();
        }
        else{
            Token tokenActual = aux.tokenActual();
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
            "Se esperaba el comienzo de una sentencia, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    //DUDA, queda asi o hay que añadir los siguientes?
    public void sentencia2() {
        if(aux.verifico("else")){
            aux.matcheo("else");
            sentencia();
        }
    }

    //añadir las opciones en el else de lo que puede venir
    public void expresionP() {
        if(aux.verifico(";")){
            aux.matcheo(";");
        }
        else{
            expresion();
            aux.matcheo(";");
        }
    }

    public void bloque() {
        aux.matcheo("{");
        sentenciaP();
        aux.matcheo("}");
    }

    public void asignacion() {
        if(aux.verifico("id_objeto")){
            asignacionVarSimple();
            aux.matcheo("=");
            expresion();
        }
        else if(aux.verifico("self")){
            asignacionSelfSimple();
            aux.matcheo("=");
            expresion();
        }
        else{
            Token tokenActual = aux.tokenActual();
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
            "Se esperaba id_objeto o self, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    public void asignacionVarSimple() {
        aux.matcheo("id");
        asignacionVarSimpleP();
    }

    //corregir
    public void asignacionVarSimpleP() {
        if(aux.verifico(".")){
            encadenadoSimpleP();
        }
        else{
            aux.matcheo("[");
            expresion();
            aux.matcheo("]");
        }
    }

    public void encadenadoSimpleP() {
        if(aux.verifico(".")){
            aux.matcheo(".");
            aux.matcheo("id_objeto");
            encadenadoSimpleP();
        }
    }

    //LLEGUE HASTA ACA
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

    }
    
}
