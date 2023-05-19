package Semantico.Nodo;

import java.util.ArrayList;

import Lexico.Token;
import Semantico.Tipo.Tipo;

public class NodoLlamadaMetodo extends NodoExpresion {
    
    Token token;
    Tipo tipo;
    ArrayList<NodoExpresion> argumentos = new ArrayList<>();

    public NodoLlamadaMetodo(Token token){
        this.token = token;
    }

    public void agregarArgumento(NodoExpresion exp){
        this.argumentos.add(exp);
        exp.establecerPadre(this);
    }
    
}
