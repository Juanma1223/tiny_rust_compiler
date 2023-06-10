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
        if (!(this.nombre.equals("constructor") || this.nombre.equals("main"))) {
            if (this.bloque != null && this.obtenerTipo().obtenerTipo() != "void") {
                ArrayList<NodoSentencia> sentencias = this.bloque.obtenerSentencias();
                Metodo infoMetodo = tablaDeSimbolos.obtenerClasePorNombre(claseContenedora.obtenerNombre())
                        .obtenerMetodoPorNombre(nombre);
                int s = sentencias.size();
                if (s > 1) {
                    for (int i = 0; i < s - 1; i++) {
                        if (sentencias.get(i) instanceof NodoReturn) {
                            new ErrorSemantico(infoMetodo.obtenerFila(), infoMetodo.obtenerColumna(),
                                    "El metodo " + infoMetodo.obtenerNombre()
                                            + " tiene sentencia de retorno dentro del metodo." +
                                            " La sentencia de retorno debe estar al final.",
                                    true);
                        }
                    }
                    if (!(sentencias.get(s - 1) instanceof NodoReturn)) {
                        new ErrorSemantico(infoMetodo.obtenerFila(), infoMetodo.obtenerColumna(),
                                "El metodo " + infoMetodo.obtenerNombre()
                                        + " no tiene sentencia de retorno al final del metodo.",
                                true);
                    }
                } else if (s == 1) {
                    if (!(sentencias.get(s - 1) instanceof NodoReturn)) {
                        new ErrorSemantico(infoMetodo.obtenerFila(), infoMetodo.obtenerColumna(),
                                "El metodo " + infoMetodo.obtenerNombre()
                                        + " no tiene sentencia de retorno al final del metodo.",
                                true);
                    }
                } else {
                    new ErrorSemantico(infoMetodo.obtenerFila(), infoMetodo.obtenerColumna(),
                            "El metodo " + infoMetodo.obtenerNombre()
                                    + " no tiene sentencia de retorno al final del metodo.",
                            true);
                }
            }
        } else if (this.nombre.equals("main")) {
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

        // El codigo del constructor es distinto a una llamada de metodo
        if (this.aux != null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        // Usamos la tabla de simbolos para obtener informacion acerca del metodo
        Metodo infoMetodo = this.claseContenedora.obtenerMetodoPorNombre(this.nombre);

        // Generamos el registro de activacion del metodo que estamos llamando
        String nombreEtiqueta = "";
        if (claseContenedora.obtenerNombre() == "Fantasma") {
            nombreEtiqueta = "main:";
        } else {
            nombreEtiqueta = claseContenedora.obtenerNombre() + "_" + nombre + ":";
        }
        sb.append(nombreEtiqueta).append(System.lineSeparator());
        
        // Apuntamos el $fp a la primer posicion del stack frame
        sb.append("move $fp, $sp # Comienza la creacion de RA de "+this.nombre).append(System.lineSeparator());
        // Obtenemos la cantidad de memoria que requerimos alocar y 
        // desplazamos el stack pointer
        sb.append("subu $sp, $sp, " + infoMetodo.obtenerTamMemoria()).append(System.lineSeparator());
        // Guardamos el punto de retorno al codigo del llamador
        sb.append("sw $ra, 4($sp)").append(System.lineSeparator());

        // Luego de la construccion del RA generamos el codigo del metodo
        sb.append(this.bloque.genCodigo()).append(System.lineSeparator());

        // Cuando el metodo retorna a este punto de su ejecucion,
        // hacemos pop del RA actual
        sb.append("lw $ra, 4($sp) # Comenzamos el pop del metodo "+this.nombre).append(System.lineSeparator());
        sb.append("addiu $sp, $sp, " + infoMetodo.obtenerTamMemoria()).append(System.lineSeparator());
        sb.append("lw $fp, 0($sp)").append(System.lineSeparator());
        // Retornamos la ejecucion al punto posterior de la llamada
        sb.append("jr $ra").append(System.lineSeparator());
        return sb.toString();
    }
}
