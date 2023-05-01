package Semantico.Variable;

import Semantico.Tipo.Tipo;

public class Atributo extends Variable {

    private boolean esPublico;

    public Atributo(String nombre, Tipo tipo, boolean esPublico) {
        super(nombre, tipo);
        this.esPublico = esPublico;
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(System.lineSeparator());
        sb.append("\"nombre\":").append("\"" + super.obtenerNombre() + "\",").append(System.lineSeparator());
        sb.append("\"tipo\":").append("\"" + super.obtenerTipo().obtenerTipo() + "\",").append(System.lineSeparator());
        sb.append("\"posicion\":").append("\"" + super.obtenerPosicion() + "\"").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        return sb.toString();
    }

    public boolean obtenerVisibilidad() {
        return this.esPublico;
    }

}
