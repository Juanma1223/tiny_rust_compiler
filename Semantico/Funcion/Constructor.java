package Semantico.Funcion;

import java.util.HashMap;

import Semantico.Variable.Parametro;

public class Constructor extends Funcion {
    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(System.lineSeparator());
        sb.append("\"paramF\":[").append(System.lineSeparator());
        for (HashMap.Entry<String, Parametro> parametro : super.obtenerParametros().entrySet()) {
            sb.append(parametro.getValue().toJson() + ",").append(System.lineSeparator());
        }
        sb.append("]").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        return sb.toString();
    }
}
