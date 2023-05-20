package Semantico.Nodo;

import Semantico.Clase;
import Semantico.Funcion.Funcion;
import Semantico.Funcion.Metodo;
import Semantico.Tipo.Tipo;

public class NodoMetodo extends NodoBloque {
    private String nombre;
    private NodoBloque bloque;

    public NodoMetodo(Funcion metodoContenedor, Clase claseContenedora){
        super(metodoContenedor,claseContenedora);
        this.claseContenedora = claseContenedora;
        this.metodoContenedor = metodoContenedor;
    }

    public void establecerNombre(String nombre) {
        this.nombre = nombre;
    }

    public NodoBloque agregarBloque(Funcion metodoContenedor){
        NodoBloque hijo = new NodoBloque(metodoContenedor,this.claseContenedora);
        hijo.establecerPadre(this);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
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

    @Override
    public Tipo obtenerTipo(){
        // Si el tipo es nulo, debemos resolverlo usando la tabla de simbolos
        if(this.tipo == null){
            Metodo infoMetodo = tablaDeSimbolos.obtenerClasePorNombre(claseContenedora.obtenerNombre()).obtenerMetodoPorNombre(nombre);
            this.tipo = infoMetodo.obtenerTipoRetorno();
        }
        return this.tipo;
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
