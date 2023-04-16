package Semantico;

public class Semantico {
    private TablaDeSimbolos tablaDeSimbolos;

    public Semantico(){
        this.tablaDeSimbolos = new TablaDeSimbolos();
    }

    public TablaDeSimbolos obtenerTDS(){
        return this.tablaDeSimbolos;
    }
}
