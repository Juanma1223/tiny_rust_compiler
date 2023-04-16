package Semantico;

import Semantico.Funcion.Funcion;

public class TablaDeSimbolos {
    private Clase claseActual;
    private Funcion metodoActual;

    public Clase obtenerClaseActual(){
        return this.claseActual;
    }

    public Funcion obtenerMetodoActual(){
        return this.metodoActual;
    }
}
