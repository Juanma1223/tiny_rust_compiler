package Semantico.Nodo;

public class NodoAsignacion {
    private Nodo ladoIzq;
    private Nodo ladoDer;

    public void establecerLadoIzq(Nodo ladoIzq){
        this.ladoIzq = ladoIzq;
    }

    public void establecerLadoDer(Nodo ladoDer){
        this.ladoDer = ladoDer;
    }
}
