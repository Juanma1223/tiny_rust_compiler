package Sintactico;

import java.net.http.HttpResponse.PushPromiseHandler;

public class Sintactico {

    AuxiliarSintactico aux = new AuxiliarSintactico();
    
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
            if(tokenActual.obtenerLexema() != "fn"){
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                  "Se esperaba: fn, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    public void metodoMain() {
        aux.macheo("fn");
        aux.macheo("main");
        aux.macheo("(");
        aux.macheo(")");
        bloqueMetodo();
    }
    
    public void clase() {
        aux.macheo("class");
        aux.macheoId("id_clase");
        restoClase();
    }

    public void restoClase() {
        if(aux.verifico(":")){
            aux.macheo(":");
            aux.macheoId("id_clase");
            aux.macheo("{");
            miembroP();
            aux.macheo("}");
        }
        if(aux.verifico("{")){
            aux.macheo("{");
            miembroP();
            aux.macheo("}");
        }
    }

    public void miembroP() {
        String[] ter = {"pub", "Bool", "I32", "Str", "Char", "idClase", "Array", "create", "static", "fn"};
        if(aux.verifico(ter)){
            miembro();
            miembroP();
        }
        else{
            if(tokenActual.obtenerLexema() != "}"){
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                  "Se esperaba: }, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    public void miembro() {
        String[] ter = {"pub", "Bool", "I32", "Str", "Char", "id_clase", "Array"};
        if(aux.verifico(ter)){
            atributo();
        }
        if(aux.verifico("create")){
            constructor();
        }
        String[] ter1 = {"static", "fn"};
        if(aux.verifico(ter1)){
            metodo();
        }
    }

    public void atributo() {
        if(aux.verifico("pub")){
            aux.macheo("pub");
        }
        String[] ter = {"Bool", "I32", "Str", "Char", "id_clase", "Array"};
        if(aux.verifico(ter)){
            tipo();
            aux.macheo(":");
            listaDeclVariables();
            aux.macheo(";");
        }
    }

    public void constructor() {
        aux.macheo("create");
        argumentosFormales();
        bloqueMetodo();
    }

    public void metodo() {
        if(aux.verifico("static")){
            aux.macheo("static");
        }
        aux.macheo("fn");
        aux.macheo("id_objeto");
        argumentosFormales();
        aux.macheo("->");
        tipoMetodo();
        bloqueMetodo();
    }

    public void bloqueMetodo() {
        aux.macheo("{");
        declVarLocalesP();
        sentenciaP();
        aux.macheo("}");
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
            if(tokenActual.obtenerLexema() != "}"){
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                  "Se esperaba: }, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    public void declVarLocales() {
        tipo();
        aux.macheo(":");
        listaDeclVariables();
        aux.macheo(";");
    }

    public void listaDeclVariables() {
        aux.macheo("id_objeto");
        listaDeclVariablesP();
    }

    public void listaDeclVariablesP() {
        if(aux.verifico(",")){
            aux.macheo(",");
            listaDeclVariables();
        }
        else{
            if(tokenActual.obtenerLexema() != ";"){
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                  "Se esperaba: ;, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    public void argumentosFormales() {
        aux.macheo("(");
        listaArgumentosFormalesP();
    }

    public void listaArgumentosFormalesP() {
        String[] ter = {"Bool", "I32", "Str", "Char", "id_clase", "Array"};
        if(aux.verifico(ter)){
            listaArgumentosFormales();
            aux.macheo(")");
        }
        else{
            aux.macheo(")");
        }
    }

    public void listaArgumentosFormales() {
        argumentoFormal();
        listaArgumentosFormales2();
    }

    public void listaArgumentosFormales2() {
        if(aux.verifico(",")){
            aux.macheo(",");
            listaArgumentosFormales();
        }
        else{
            if(tokenActual.obtenerLexema() != ")"){
                ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                  "Se esperaba: ), se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    public void argumentoFormal() {
        tipo();
        aux.macheo(":");
        aux.macheo("id_objeto");
    }

    public void tipoMetodo() {
        String[] ter = {"Bool", "I32", "Str", "Char", "id_clase", "Array"};
        if(aux.verifico(ter)){
            tipo();
        }
        else{
            aux.macheo("void");
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
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
            "Se esperaba un tipo primitivo, referencia o Array, se encontró: " + tokenActual.obtenerLexema());
        }
    }

    //LLEGUE HASTA ACA
    public void tipoPrimitivo() {
        aux.macheo(tokenActual.obtenerLexema());
    }

    public void tipoReferencia() {
        aux.macheo("id_clase");
    }

    public void tipoArray() {
        aux.macheo("Array");
        tipoPrimitivo();
    }

    public void sentencia() {

    }

    public void sentencia2() {
        macheo("else");
        sentencia();
    }

    public void expresionP() {
        expresion();
        macheo(";");
    }

    public void bloque() {
        macheo("{");
        sentenciaP();
        macheo("}");
    }

    public void asignacion() {
        asignacionVarSimple();
        macheo("=");
        expresion();

        asignacionSelfSimple();
        macheo("=");
        expresion();
    }

    public void asignacionVarSimple() {
        macheo("id");
        asignacionVarSimpleP();
    }

    public void asignacionVarSimpleP() {
        encadenadoSimpleP();

        macheo("[");
        expresion();
        macheo("]");
    }

    public void encadenadoSimpleP() {
        macheo(".");
        macheo("id");
        encadenadoSimpleP();
    }

    public void asignacionSelfSimple() {
        macheo("self");
        encadenadoSimpleP();
    }

    public void sentenciaSimple() {
        macheo("(");
        expresion();
        macheo(")");
    }

    public void expresion() {

    }
    
}
