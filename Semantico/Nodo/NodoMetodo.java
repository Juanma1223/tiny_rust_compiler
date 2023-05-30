package Semantico.Nodo;

import java.util.ArrayList;

import Semantico.Clase;
import Semantico.ErrorSemantico;
import Semantico.Funcion.Funcion;
import Semantico.Funcion.Metodo;
import Semantico.Tipo.Tipo;

public class NodoMetodo extends NodoBloque {
    private String nombre;
    private NodoBloque bloque;

    public NodoMetodo(Funcion metodoContenedor, Clase claseContenedora) {
        super(metodoContenedor, claseContenedora);
        this.claseContenedora = claseContenedora;
        this.metodoContenedor = metodoContenedor;
    }

    public void establecerNombre(String nombre) {
        this.nombre = nombre;
    }

    public NodoBloque agregarBloque(Funcion metodoContenedor) {
        NodoBloque hijo = new NodoBloque(metodoContenedor, this.claseContenedora);
        hijo.establecerPadre(this);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.bloque = hijo;
        return hijo;
    }

    @Override
    public void checkeoTipos() {
        // El bloque puede llegar a ser null
        if (this.bloque != null) {
            this.bloque.checkeoTipos();
        }
        // Ademas debemos revisar que el bloque del metodo tenga un retorno
        // Salvo el constructor
        if (!(this.nombre.equals("constructor") || this.nombre.equals("main"))){
            if (this.bloque != null && this.obtenerTipo().obtenerTipo() != "void") {
                ArrayList<NodoSentencia> sentencias = this.bloque.obtenerSentencias();
                Metodo infoMetodo = tablaDeSimbolos.obtenerClasePorNombre(claseContenedora.obtenerNombre())
                            .obtenerMetodoPorNombre(nombre);
                int s = sentencias.size();
                if(s>1){
                    for (int i = 0; i < s-1; i++) {
                        if (sentencias.get(i) instanceof NodoReturn) {
                            new ErrorSemantico(infoMetodo.obtenerFila(), infoMetodo.obtenerColumna(),
                                "El metodo " + infoMetodo.obtenerNombre() + " tiene sentencia de retorno dentro del metodo." +
                                " La sentencia de retorno debe estar al final.",
                                true);
                        }
                    }
                    if (!(sentencias.get(s-1) instanceof NodoReturn)) {
                        new ErrorSemantico(infoMetodo.obtenerFila(), infoMetodo.obtenerColumna(),
                                "El metodo " + infoMetodo.obtenerNombre() + " no tiene sentencia de retorno al final del metodo.",
                                true);
                    }
                }
                else if (s == 1) {
                    if (!(sentencias.get(s-1) instanceof NodoReturn)) {
                    new ErrorSemantico(infoMetodo.obtenerFila(), infoMetodo.obtenerColumna(),
                            "El metodo " + infoMetodo.obtenerNombre() + " no tiene sentencia de retorno al final del metodo.",
                            true);
                    }
                }
                else {
                    new ErrorSemantico(infoMetodo.obtenerFila(), infoMetodo.obtenerColumna(),
                            "El metodo " + infoMetodo.obtenerNombre() + " no tiene sentencia de retorno al final del metodo.",
                            true);
                }
            }
        } else if (this.nombre.equals("main")){
            if (this.bloque != null) {
                ArrayList<NodoSentencia> sentencias = this.bloque.obtenerSentencias();
                Boolean tieneRetorno = false;
                for (NodoSentencia sentencia : sentencias) {
                    if (sentencia instanceof NodoReturn) {
                        tieneRetorno = true;
                        break;
                    }
                }
                if (tieneRetorno) {
                    Metodo infoMetodo = tablaDeSimbolos.obtenerClasePorNombre(claseContenedora.obtenerNombre())
                            .obtenerMetodoPorNombre(nombre);
                    new ErrorSemantico(infoMetodo.obtenerFila(), infoMetodo.obtenerColumna(),
                            "El metodo main no puede tener una sentencia de retorno.",
                            true);
                }
            }
        }
    }

    @Override
    public Tipo obtenerTipo() {
        // Si el tipo es nulo, debemos resolverlo usando la tabla de simbolos
        if (this.tipo == null) {
            Metodo infoMetodo = tablaDeSimbolos.obtenerClasePorNombre(claseContenedora.obtenerNombre())
                    .obtenerMetodoPorNombre(nombre);
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

    @Override
    public String genCodigo() {
        StringBuilder sb = new StringBuilder();
        sb.append(nombre+":").append(System.lineSeparator());
        sb.append(bloque.genCodigo()).append(System.lineSeparator());
        sb.append("li $v0, 10").append(System.lineSeparator()); //exit
        sb.append("syscall").append(System.lineSeparator());
        return sb.toString();
    }
}
