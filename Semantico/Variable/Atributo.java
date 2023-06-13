package Semantico.Variable;

import Semantico.Tipo.Tipo;

public class Atributo extends Variable {

    private boolean esPublico;
    private boolean esHeredado;

    public Atributo(String nombre, Tipo tipo, boolean esPublico) {
        super(nombre, tipo);
        this.esPublico = esPublico;
    }

    public Atributo(String nombre, Tipo tipo, int fila, int columna, boolean esPublico) {
        super(nombre, tipo, fila, columna);
        this.esPublico = esPublico;
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(System.lineSeparator());
        sb.append("\"nombre\":").append("\"" + super.obtenerNombre() + "\",").append(System.lineSeparator());
        sb.append("\"tipo\":").append("\"" + super.obtenerTipo().obtenerTipo() + "\",").append(System.lineSeparator());
        sb.append("\"public\":").append("\"" + this.obtenerVisibilidad() + "\",").append(System.lineSeparator());
        sb.append("\"posicion\":").append("\"" + super.obtenerPosicion() + "\"").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        return sb.toString();
    }

    public boolean obtenerVisibilidad() {
        return this.esPublico;
    }

    public void establecerHeredado(){
        this.esHeredado = true;
    }

    public boolean esHeredado(){
        return this.esHeredado;
    }

    @Override
    public Atributo obtenerAtributo(){
        return this;
    }

}
