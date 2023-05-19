package Semantico.Nodo;

public class NodoReturn extends NodoSentencia {
    private NodoExpresion retorno;

    public NodoExpresion agregarExpresion(){
        NodoExpresion hijo = new NodoExpresion();
        hijo.establecerPadre(this);
        this.retorno = hijo;
        return hijo;
    }

    public void agregarExpresion(NodoExpresion retorno){
        this.retorno = retorno;
        retorno.establecerPadre(this);
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("\"Nodo\":").append("\"NodoRetorno\",").append(System.lineSeparator());
        sb.append("\"hijos\":[").append(System.lineSeparator());
        sb.append("{").append(System.lineSeparator());
        sb.append(retorno.toJson()).append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        sb.append("]").append(System.lineSeparator());
        return sb.toString();
    }
}
