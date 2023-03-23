package Sintactico;

import java.net.http.HttpResponse.PushPromiseHandler;

public class Sintactico {

    public void start() {
        claseP();
        metodoMain();
    }

    public void claseP() {
        clase();
        claseP();
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
        macheo("idClase");
        restoClase();
    }

    public void restoClase() {
        macheo(":");
        macheo("idClase");
        macheo("{");
        miembro();
        macheo("}");
    }

    public void miembroP() {
        miembro();
        miembroP();

    }

    public void miembro() {
        atributo();
        constructor();
        metodo();
    }

    public void atributo() {
        macheo("pub");
        tipo()
        macheo(":");
        listaDeclVariables();
        macheo(":");
    }

    public void constructor() {
        macheo("create");
        argumentosFormales();
        bloqueMetodo();
    }

    public void metodo() {
        macheo("static");
        macheo("fn");
        macheo("id");
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
