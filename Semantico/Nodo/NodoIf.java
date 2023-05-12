package Semantico.Nodo;

public class NodoIf extends NodoSentencia {
    private NodoExpresion condicion;
    private NodoSentencia sentenciaThen;
    private NodoSentencia sentenciaElse;

    public NodoExpresion agregarCondicion(){
        NodoExpresion hijo = new NodoExpresion();
        hijo.establecerPadre(this);
        this.condicion = hijo;
        return hijo;
    }

    public NodoSentencia agregarSentenciaThen(){
        NodoSentencia hijo = new NodoSentencia();
        hijo.establecerPadre(this);
        this.sentenciaThen = hijo;
        return hijo;
    }

    public NodoSentencia agregarSentenciaElse(){
        NodoSentencia hijo = new NodoSentencia();
        hijo.establecerPadre(this);
        this.sentenciaElse = hijo;
        return hijo;
    }
}
