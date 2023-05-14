package Semantico.Nodo;

public class NodoMetodo extends NodoBloque {
    private NodoBloque bloque;

    public NodoBloque agregarBloque(){
        NodoBloque hijo = new NodoBloque();
        hijo.establecerPadre(this);
        this.bloque = hijo;
        return hijo;
    }
}
