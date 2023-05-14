package Semantico.Nodo;

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
}
