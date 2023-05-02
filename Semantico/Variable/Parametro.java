package Semantico.Variable;

import Semantico.Tipo.Tipo;

public class Parametro extends Variable{

    public Parametro(String nombre, Tipo tipo) {
        super(nombre, tipo);
    }

    public Parametro(String nombre, Tipo tipo, int fila, int columna) {
        super(nombre, tipo, fila, columna);
    }
    
}
