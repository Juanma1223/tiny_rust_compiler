package Semantico.Nodo;

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
}
