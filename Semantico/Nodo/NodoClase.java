package Semantico.Nodo;

import java.util.ArrayList;

import Semantico.Clase;
import Semantico.Funcion.Funcion;

// Esta clase mantiene los subarboles de cada método
// dentro de un Nodo Clase
public class NodoClase extends Nodo {

    private String nombre;
    private ArrayList<NodoMetodo> metodos = new ArrayList<>();

    public NodoClase(Funcion metodoContenedor, Clase claseContenedora) {
        super(metodoContenedor, claseContenedora);
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

    public void establecerNombre(String nombre) {
        this.nombre = nombre;
    }

    public NodoMetodo agregarMetodo(Funcion metodoContenedor, Clase claseContenedora) {
        NodoMetodo hijo = new NodoMetodo(metodoContenedor,claseContenedora);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.metodos.add(hijo);
        hijo.establecerPadre(this);
        return hijo;
    }

    @Override
    public void checkeoTipos() {
        metodos.forEach((metodo) -> metodo.checkeoTipos());
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(System.lineSeparator());
        sb.append("\"nombre\":").append("\"" + nombre + "\",").append(System.lineSeparator());
        sb.append("\"metodos\":[").append(System.lineSeparator());
        int s = metodos.size() - 1;
        if (s > 0) {
            for (int i = 0; i < s; i++) {
                sb.append(metodos.get(i).toJson() + ",").append(System.lineSeparator());
            }
            sb.append(metodos.get(s).toJson()).append(System.lineSeparator());
        } else {
            if (s == 0) {
                sb.append(metodos.get(s).toJson()).append(System.lineSeparator());
            }
        }
        sb.append("]").append(System.lineSeparator());
        // sb.append("\"constructor\":").append(constructor.toJson()).append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        return sb.toString();
    }

}
