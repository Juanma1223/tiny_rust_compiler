package Semantico.Nodo;

import java.util.ArrayList;

import Semantico.Clase;
import Semantico.Funcion.Funcion;

// Esta clase es la raíz de nuestra estructura de árbol
// Es quien mantiene los subarboles de cada clase
public class NodoAST extends Nodo {

    private ArrayList<NodoClase> clases = new ArrayList<>();

    public NodoAST(Funcion metodoContenedor, Clase claseContenedora) {
        super(metodoContenedor, claseContenedora);
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

    // Esta función crea un Nodo Clase hijo, lo agrega al árbol y lo retorna
    public NodoClase agregarHijo() {
        NodoClase hijo = new NodoClase(this.metodoContenedor, this.claseContenedora);
        hijo.establecerPadre(this);
        hijo.establecerTablaDeSimbolos(tablaDeSimbolos);
        this.clases.add(hijo);
        return hijo;
    }

    @Override
    public void checkeoTipos() {
        clases.forEach((clase) -> clase.checkeoTipos());
    }

    public String toJson(String nombreArchivo) {
        StringBuilder sb = new StringBuilder();
        // Construimos el json de forma recursiva
        sb.append("{").append(System.lineSeparator());
        sb.append("\"nombre\":").append("\"" + nombreArchivo + "\",").append(System.lineSeparator());
        sb.append("\"clases\":[").append(System.lineSeparator());
        int s = clases.size() - 1;
        if (s > 0) {
            for (int i = 0; i < s; i++) {
                sb.append(clases.get(i).toJson() + ",").append(System.lineSeparator());
            }
            sb.append(clases.get(s).toJson()).append(System.lineSeparator());
        } else {
            if (s == 0) {
                sb.append(clases.get(s).toJson()).append(System.lineSeparator());
            }
        }
        sb.append("]").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        return sb.toString();
    }

    @Override
    public String genCodigo() {
        StringBuilder sb = new StringBuilder();
        sb.append(".text").append(System.lineSeparator());
        sb.append(".globl main").append(System.lineSeparator()); // main
        sb.append(genCodigoIO()).append(System.lineSeparator());
        clases.forEach((clase) -> sb.append(clase.genCodigo()).append(System.lineSeparator()));
        sb.append("li $v0, 10").append(System.lineSeparator()); // exit
        sb.append("syscall").append(System.lineSeparator());
        return sb.toString();
    }

    public String genCodigoIO() {
        StringBuilder sb = new StringBuilder();

        sb.append("IO_out_i32:").append(System.lineSeparator());
        String codigoI32out = "\nsw $a0, -4($fp)"  // Cargamos en el acumulador el parametro recibido
                        + "\nli $v0, 1"
                        + "\nsyscall";
        sb.append(genCodigoMetodoIO(codigoI32out)).append(System.lineSeparator()); // print_int

        sb.append("IO_out_bool:").append(System.lineSeparator());
        sb.append(genCodigoMetodoIO(codigoI32out)).append(System.lineSeparator()); // print_int

        sb.append("IO_out_char:").append(System.lineSeparator());
        String codigoStrout = "\nsw $a0, -4($fp)"  // Cargamos en el acumulador el parametro recibido
                        + "\nli $v0, 4"
                        + "\nsyscall";
        sb.append(genCodigoMetodoIO(codigoStrout)).append(System.lineSeparator()); // print_string

        sb.append("IO_out_string:").append(System.lineSeparator());
        sb.append(genCodigoMetodoIO(codigoStrout)).append(System.lineSeparator()); // print_string

        sb.append("IO_in_i32:").append(System.lineSeparator());
        String codigoI32in = "\nli $v0, 5"
                        + "\nsyscall"
                        + "\nmove $a0, $v0"; // Recuperamos el valor de v0 en el acumulador
        sb.append(genCodigoMetodoIO(codigoI32in)).append(System.lineSeparator()); // read_int

        sb.append("IO_in_bool:").append(System.lineSeparator());
        sb.append(genCodigoMetodoIO(codigoI32in)).append(System.lineSeparator()); // read_int

        sb.append("IO_in_char:").append(System.lineSeparator());
        String codigoStrin = "\nli $v0, 8"
                        + "\nsyscall";
        sb.append(genCodigoMetodoIO(codigoStrin)).append(System.lineSeparator()); // read_string

        sb.append("IO_in_string:").append(System.lineSeparator());
        sb.append(genCodigoMetodoIO(codigoStrin)).append(System.lineSeparator()); // read_string

        return sb.toString();
    }

    public String genCodigoMetodoIO(String codigoIO) {
        StringBuilder sb = new StringBuilder();

        // Apuntamos el $fp a la primer posicion del stack frame
        sb.append("move $fp, $sp").append(System.lineSeparator());
        // Obtenemos la cantidad de memoria que requerimos alocar y 
        // desplazamos el stack pointer
        sb.append("subu $sp, $sp, " + 20).append(System.lineSeparator());
        // Guardamos el punto de retorno al codigo del llamador
        sb.append("sw $ra, 4($sp)").append(System.lineSeparator());

        //Generamos el codigo de IO
        sb.append(codigoIO).append(System.lineSeparator());

        // Cuando el metodo retorna a este punto de su ejecucion,
        // hacemos pop del RA actual
        sb.append("lw $ra, 4($sp)").append(System.lineSeparator());
        sb.append("addiu $sp, $sp, " + 20).append(System.lineSeparator());
        sb.append("lw $fp, 0($sp)").append(System.lineSeparator());
        // Retornamos la ejecucion al punto posterior de la llamada
        sb.append("jr $ra").append(System.lineSeparator());

        return sb.toString();
    }

}
