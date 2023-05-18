package Semantico.Nodo;

import Lexico.Token;
import Semantico.ErrorSemantico;
import Semantico.Tipo.Tipo;

public class NodoAsignacion extends NodoExpresion{
    private Nodo ladoIzq;
    private Nodo ladoDer;
    private Token operador;

    public void establecerLadoIzq(Nodo ladoIzq){
        ladoIzq.establecerPadre(this);
        this.ladoIzq = ladoIzq;
    }


    public void establecerLadoDer(NodoExpresion ladoDer){
        ladoDer.establecerPadre(this);
        this.ladoDer = ladoDer;
    }

    public void establecerOp(Token operador){
        this.operador = operador;
    }

    @Override
    public void checkeoTipos(){
        Tipo tipoIzq = ladoIzq.obtenerTipo();
        Tipo tipoDer = ladoDer.obtenerTipo();
        if(!tipoIzq.obtenerTipo().equals(tipoDer.obtenerTipo())){
            new ErrorSemantico(0, 0, "Los tipos de la asignacion no coinciden! (falta agregar fila y columna)");
        }
        this.establecerTipo(tipoDer);
    }

}
