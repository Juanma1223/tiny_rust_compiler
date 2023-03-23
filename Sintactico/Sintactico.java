package Sintactico;

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
        lista_declaracion_variables();
        macheo(":");
    }

    public void constructor() {
        macheo("create");
        argumentos_formales();
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

    
}
