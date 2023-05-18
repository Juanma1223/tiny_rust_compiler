package Semantico.Nodo;

public class NodoMetodo extends NodoBloque {
    private String nombre;
    private NodoBloque bloque;

    public void establecerNombre(String nombre) {
        this.nombre = nombre;
    }

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

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(System.lineSeparator());
        sb.append("\"nombre\":").append("\"" + nombre + "\",").append(System.lineSeparator());
        sb.append("\"Bloque\":{").append(System.lineSeparator());
        if (this.bloque != null) {
            sb.append(this.bloque.toJson()).append(System.lineSeparator());
        }
        sb.append("}").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        return sb.toString();
    }
}
