package Semantico.Nodo;

import java.util.ArrayList;

// Esta clase mantiene los subarboles de cada método
// dentro de un Nodo Clase
public class NodoClase extends Nodo {
    
    private String nombre;
    private ArrayList<NodoMetodo> metodos = new ArrayList<>();

    public void establecerNombre(String nombre) {
        this.nombre = nombre;
    }

    public NodoMetodo agregarMetodo(){
        NodoMetodo hijo = new NodoMetodo();
        this.metodos.add(hijo);
        hijo.establecerPadre(this);
        return hijo;
    }

    @Override
    public void checkeoTipos(){
        metodos.forEach((metodo) -> metodo.checkeoTipos());
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(System.lineSeparator());
        sb.append("\"nombre\":").append("\"" + nombre + "\",").append(System.lineSeparator());
        sb.append("\"metodos\":[").append(System.lineSeparator());
        for (int i = 0; i < metodos.size(); i++) {
            sb.append(metodos.get(i).toJson() + ",").append(System.lineSeparator());
        }
        sb.append("],").append(System.lineSeparator());
        //sb.append("\"constructor\":").append(constructor.toJson()).append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        return sb.toString();
    }

}
