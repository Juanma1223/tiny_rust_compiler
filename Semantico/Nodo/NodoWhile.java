package Semantico.Nodo;

import Semantico.ErrorSemantico;
import Semantico.Tipo.Tipo;

public class NodoWhile extends NodoSentencia {
    private NodoExpresion condicion;
    private NodoBloque bloqueW;

    public NodoExpresion agregarCondicion(){
        NodoExpresion hijo = new NodoExpresion();
        hijo.establecerPadre(this);
        this.condicion = hijo;
        return hijo;
    }

    public void agregarCondicion(NodoExpresion condicion){
        this.condicion = condicion;
        condicion.establecerPadre(this);
    }
    
    public NodoBloque agregarBloqueW(){
        NodoBloque hijo = new NodoBloque();
        hijo.establecerPadre(this);
        this.bloqueW = hijo;
        return hijo;
    }

    @Override
    public void checkeoTipos(){
        Tipo tipo = condicion.obtenerTipo();
        if(!tipo.obtenerTipo().equals("Bool")){
            new ErrorSemantico(this.aux.obtenerFila(), this.aux.obtenerColumna(), "El tipo de la condicion while no es booleano!", true);
        }
        this.establecerTipo(tipo);
    }
}
