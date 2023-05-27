package Semantico.Funcion;

import java.util.HashMap;

import Semantico.Tipo.Tipo;
import Semantico.Tipo.TipoReferencia;
import Semantico.Variable.Parametro;

public class Constructor extends Funcion {
    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(System.lineSeparator());
        sb.append("\"paramF\":[").append(System.lineSeparator());
        int i = 1;
        for (HashMap.Entry<String, Parametro> parametro : super.obtenerParametros().entrySet()) {
            if (i < super.obtenerParametros().size()) {
                sb.append(parametro.getValue().toJson() + ",").append(System.lineSeparator());
            } else {
                sb.append(parametro.getValue().toJson()).append(System.lineSeparator());
            }
            i++;
        }
        sb.append("]").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        return sb.toString();
    }

    public Tipo obtenerTipoRetorno(){
        return new TipoReferencia(nombre);
    }
}
