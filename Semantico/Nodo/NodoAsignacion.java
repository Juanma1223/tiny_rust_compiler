package Semantico.Nodo;

public class NodoAsignacion extends NodoExpresion{
    private Nodo ladoIzq;
    private Nodo ladoDer;

    public void establecerLadoIzq(Nodo ladoIzq){
        this.ladoIzq = ladoIzq;
    }


    public void establecerLadoDer(NodoExpresion ladoDer){
        ladoDer.establecerPadre(this);
        this.ladoDer = ladoDer;
    }

}
