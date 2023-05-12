package Semantico.Nodo;

public class NodoAsignacion extends NodoExpresion{
    private Nodo ladoIzq;
    private Nodo ladoDer;

    public void establecerLadoIzq(Nodo ladoIzq){
        this.ladoIzq = ladoIzq;
    }

    public NodoExpresion establecerLadoDer(){
        NodoExpresion hijo = new NodoExpresion();
        hijo.establecerPadre(this);
        this.ladoDer = hijo;
        return hijo;
    }

}
