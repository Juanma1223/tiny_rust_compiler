package Sintactico;

import java.net.http.HttpResponse.PushPromiseHandler;

public class Sintactico {

    public void macheo(String terminal) {
        if(tokenActual.obtenerLexema() == terminal){
            tokenActual = analizadorLexico.sigToken();
        }
        else{
            ErrorSintactico err = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                  "Se esperaba: " + terminal + ", se encontró: " + tokenActual.obtenerLexema());
        }
    }

    public void macheoId(String terminal) {
        if(tokenActual.obtenerToken() == terminal){
            tokenActual = analizadorLexico.sigToken();
        }
        else{
            ErrorSintactico err = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                  "Se esperaba: " + terminal + ", se encontró: " + tokenActual.obtenerToken());
        }
    }

    public  boolean verifico(String t){
        if((tokenActual.obtenerLexema() == t) || (tokenActual.obtenerToken() == t)){
            return true;
        }
        else{
            return false;
        }       
    }

    public boolean verifico(String[] t){
        for (String string : t) {
            if((tokenActual.obtenerLexema() == string) || (tokenActual.obtenerToken() == string)){
                return true; 
            }
        }
        return false;            
    }
    
    public void start() {
        claseP();
        metodoMain();
    }

    public void claseP() {
        if(verifico("class")){
            clase();
            claseP();
        }
        else{
            if(tokenActual.obtenerLexema() != "fn"){
                ErrorSintactico err = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                  "Se esperaba: fn, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    public void metodoMain() {
        macheo("fn");
        macheo("main");
        macheo("(");
        macheo(")");
        bloqueMetodo();
    }
    
    public void clase() {
        macheo("class");
        macheoId("id_clase");
        restoClase();
    }

    public void restoClase() {
        if(verifico(":")){
            macheo(":");
            macheoId("id_clase");
            macheo("{");
            miembroP();
            macheo("}");
        }
        if(verifico("{")){
            macheo("{");
            miembroP();
            macheo("}");
        }
    }

    public void miembroP() {
        String[] ter = {"pub", "Bool", "I32", "Str", "Char", "idClase", "Array", "create", "static", "fn"};
        if(verifico(ter)){
            miembro();
            miembroP();
        }
        else{
            if(tokenActual.obtenerLexema() != "}"){
                ErrorSintactico err = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                  "Se esperaba: }, se encontró: " + tokenActual.obtenerLexema());
            }
        }
    }

    public void miembro() {
        String[] ter = {"pub", "Bool", "I32", "Str", "Char", "id_clase", "Array"};
        if(verifico(ter)){
            atributo();
        }
        if(verifico("create")){
            constructor();
        }
        String[] ter1 = {"static", "fn"};
        if(verifico(ter1)){
            metodo();
        }
    }

    public void atributo() {
        if(verifico("pub")){
            macheo("pub");
        }
        String[] ter = {"Bool", "I32", "Str", "Char", "id_clase", "Array"};
        if(verifico(ter)){
            tipo();
            macheo(":");
            listaDeclVariables();
            macheo(";");
        }
    }

    public void constructor() {
        macheo("create");
        argumentosFormales();
        bloqueMetodo();
    }

    public void metodo() {
        if(verifico("static")){
            macheo("static");
        }
        macheo("fn");
        macheo("id_objeto");
        argumentosFormales();
        macheo("->");
        tipoMetodo();
        bloqueMetodo();
    }

    public void bloqueMetodo() {
        macheo("{");
        declVarLocalesP();
        sentenciaP();
        macheo("}");
    }

    //LLEGUE HASTA ACA
    public void declVarLocalesP() {
        declVarLocales();
        declVarLocalesP();
    }

    public void sentenciaP() {
        sentencia();
        sentenciaP();
    }

    public void declVarLocales() {
        tipo();
        macheo(":");
        listaDeclVariables();
        macheo(";");
    }

    public void listaDeclVariables() {
        macheo("idObjeto");
        listaDeclVariablesP();
    }

    public void listaDeclVariablesP() {
        macheo(",");
        listaDeclVariables();
    }

    public void argumentosFormales() {
        macheo("(");
        listaArgumentosFormalesP();
    }

    public void listaArgumentosFormalesP() {
        listaArgumentosFormales();
        macheo(")");
    }

    public void listaArgumentosFormales() {
        argumentoFormal();
        listaArgumentosFormales2();
    }

    public void listaArgumentosFormales2() {
        macheo(",");
        listaArgumentosFormales();
    }

    public void argumentoFormal() {
        tipo();
        macheo(":");
        macheo("idObjeto");
    }

    public void tipoMetodo() {
        tipo();
        macheo("void");
    }

    public void tipo() {
        tipoPrimitivo();
        tipoReferencia();
        tipoArray();
    }

    public void tipoPrimitivo() {
        macheo("tipoPrimitivo");
    }

    public void tipoReferencia() {
        macheo("idClase");
    }

    public void tipoArray() {
        macheo("Array");
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
