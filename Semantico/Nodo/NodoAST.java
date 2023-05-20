package Semantico.Nodo;

import java.util.ArrayList;

import Semantico.Clase;
import Semantico.Funcion.Funcion;

// Esta clase es la raíz de nuestra estructura de árbol
// Es quien mantiene los subarboles de cada clase
public class NodoAST extends Nodo {
    
    private ArrayList<NodoClase> clases = new ArrayList<>();

    public NodoAST(Funcion metodoContenedor, Clase claseContenedora){
        super(metodoContenedor,claseContenedora);
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }


    // Esta función crea un Nodo Clase hijo, lo agrega al árbol y lo retorna
    public NodoClase agregarHijo(){
        NodoClase hijo = new NodoClase(this.metodoContenedor,this.claseContenedora);
        hijo.establecerPadre(this);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.clases.add(hijo);
        return hijo;
    }

    @Override
    public void checkeoTipos(){
        clases.forEach((clase) -> clase.checkeoTipos());
    }

    public String toJson(String nombreArchivo) {
        StringBuilder sb = new StringBuilder();
        // Construimos el json de forma recursiva
        sb.append("{").append(System.lineSeparator());
        sb.append("\"nombre\":").append("\""+nombreArchivo+"\",").append(System.lineSeparator());
        sb.append("\"clases\":[").append(System.lineSeparator());
        int s = clases.size()-1;
        if (s>0) {
            for (int i = 0; i < s; i++) {
                sb.append(clases.get(i).toJson() + ",").append(System.lineSeparator());
            }
            sb.append(clases.get(s).toJson()).append(System.lineSeparator());
        } else {
            if (s==0) {
                sb.append(clases.get(s).toJson()).append(System.lineSeparator());
            }
        }
        sb.append("]").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        return sb.toString();
    }
}