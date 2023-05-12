package Semantico.Nodo;

public class NodoWhile extends NodoSentencia {
    private NodoExpresion condicion;
    private NodoSentencia sentencia;

    public NodoExpresion agregarCondicion(){
        NodoExpresion hijo = new NodoExpresion();
        hijo.establecerPadre(this);
        this.condicion = hijo;
        return hijo;
    }
    
    public NodoSentencia agregarSentencia(){
        NodoSentencia hijo = new NodoSentencia();
        hijo.establecerPadre(this);
        this.sentencia = hijo;
        return hijo;
    }
}
