package Semantico.Nodo;

public class NodoReturn extends NodoSentencia {
    private NodoExpresion retorno;

    public NodoExpresion agregarExpresion(){
        NodoExpresion hijo = new NodoExpresion();
        hijo.establecerPadre(this);
        this.retorno = hijo;
        return hijo;
    }
}
