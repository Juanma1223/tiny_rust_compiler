package Semantico.Variable;

import Semantico.Tipo.Tipo;

public class Variable {
    private String nombre;
    private Tipo tipo;
    private int posicion;

    public Variable(String nombre, Tipo tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(System.lineSeparator());
        sb.append("\"nombre\":").append("\"" + nombre + "\",").append(System.lineSeparator());
        sb.append("\"tipo\":").append("\"" + tipo.obtenerTipo() + "\",").append(System.lineSeparator());
        sb.append("\"posicion\":").append("\"" + tipo.obtenerTipo() + "\"").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        return sb.toString();
    }

    public String obtenerNombre() {
        return this.nombre;
    }

    public Tipo obtenerTipo() {
        return this.tipo;
    }

    public int obtenerPosicion() {
        return this.posicion;
    }

    public void establecerPosicion(int posicion) {
        this.posicion = posicion;
    }
}
