package Semantico.Funcion;

import java.util.HashMap;

import Semantico.Tipo.Tipo;
import Semantico.Variable.Parametro;
import Semantico.Variable.Variable;

public class Metodo extends Funcion {
    private String nombre;
    private boolean esEstatico;
    private Tipo tipoRetorno;
    private int posicion;

    public Metodo(String nombre, boolean esEstatico) {
        this.nombre = nombre;
        this.esEstatico = esEstatico;
    }

    public Metodo(String nombre, boolean esEstatico, Tipo tipoRetorno) {
        this.nombre = nombre;
        this.esEstatico = esEstatico;
        this.tipoRetorno = tipoRetorno;
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(System.lineSeparator());
        sb.append("\"nombre\":").append("\"" + nombre + "\",").append(System.lineSeparator());
        sb.append("\"static\":").append("\"" + esEstatico + "\",").append(System.lineSeparator());
        sb.append("\"retorno\":").append("\"" + tipoRetorno.obtenerTipo() + "\",").append(System.lineSeparator());
        sb.append("\"posicion\":").append("\"" + posicion + "\",").append(System.lineSeparator());
        sb.append("\"paramF\":[").append(System.lineSeparator());
        int i = 1;
        for (HashMap.Entry<String, Parametro> parametro : super.obtenerParametros().entrySet()) {
            if(i < super.obtenerParametros().size()){
                sb.append(parametro.getValue().toJson() + ",").append(System.lineSeparator());
            }else{
                sb.append(parametro.getValue().toJson()).append(System.lineSeparator());
            }
            i++;
        }
        sb.append("]").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        return sb.toString();
    }

    public String obtenerNombre() {
        return this.nombre;
    }

    public boolean obtenerEsEstatico() {
        return this.esEstatico;
    }

    public Tipo obtenerTipoRetorno() {
        return this.tipoRetorno;
    }

    public void establecerTipoRetorno(Tipo tipoRetorno) {
        this.tipoRetorno = tipoRetorno;
    }

    public int obtenerPosicion() {
        return this.posicion;
    }

    public void establecerPosicion(int posicion) {
        this.posicion = posicion;
    }

}
