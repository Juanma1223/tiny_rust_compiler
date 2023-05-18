package Semantico.Nodo;

public class NodoMetodo extends NodoBloque {
    private NodoBloque bloque;

    public NodoBloque agregarBloque(){
        NodoBloque hijo = new NodoBloque();
        hijo.establecerPadre(this);
        this.bloque = hijo;
        return hijo;
    }

    @Override
    public void checkeoTipos(){
        // El bloque puede llegar a ser null
        if(this.bloque != null){
            this.bloque.checkeoTipos();
        }
    }
}
