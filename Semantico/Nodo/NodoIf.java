package Semantico.Nodo;

import Semantico.ErrorSemantico;
import Semantico.Tipo.Tipo;

public class NodoIf extends NodoSentencia {
    private NodoExpresion condicion;
    private NodoBloque sentenciaThen;
    private NodoBloque sentenciaElse;

    public NodoExpresion agregarCondicion(){
        NodoExpresion hijo = new NodoExpresion();
        hijo.establecerPadre(this);
        this.condicion = hijo;
        return hijo;
    }

    public void agregarCondicion(NodoExpresion hijo){
        this.condicion = hijo;
        hijo.establecerPadre(this);
    }

    public NodoBloque agregarSentenciaThen(){
        NodoBloque hijo = new NodoBloque();
        hijo.establecerPadre(this);
        this.sentenciaThen = hijo;
        return hijo;
    }

    public NodoBloque agregarSentenciaElse(){
        NodoBloque hijo = new NodoBloque();
        hijo.establecerPadre(this);
        this.sentenciaElse = hijo;
        return hijo;
    }

    @Override
    public void checkeoTipos(){
        Tipo tipo = condicion.obtenerTipo();
        if(!tipo.obtenerTipo().equals("Bool")){
            new ErrorSemantico(this.aux.obtenerFila(), this.aux.obtenerColumna(), "El tipo de la condicion if no es booleano!", true);
        }
        this.establecerTipo(tipo);
        sentenciaThen.checkeoTipos();
        sentenciaElse.checkeoTipos();
    }
}
