package Semantico.Nodo;

import Lexico.Token;
import Semantico.Clase;
import Semantico.ErrorSemantico;
import Semantico.Funcion.Funcion;
import Semantico.Tipo.Tipo;

public class NodoArreglo extends NodoVariable {

    public NodoArreglo(Funcion metodoContenedor, Clase claseContenedora, Token token) {
        super(metodoContenedor, claseContenedora, token);
    }

    // En arreglos, el encadenado se utiliza para guardar la expresion de acceso al
    // mismo
    @Override
    public void checkeoTipos() {
        if (this.encadenado != null) {
            this.encadenado.checkeoTipos();
        }
        if (this.tablaDeSimbolos == null) {
            this.tablaDeSimbolos = padre.obtenerTablaDeSimbolos();
        }
        // Primero checkeamos que la expresion de acceso al arreglo resuelva en un tipo
        // entero
        Tipo tipoAcceso = this.encadenado.obtenerTipo();
        if (!tipoAcceso.obtenerTipo().equals("I32")) {
            new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                    "Los arreglos solo pueden ser accedidos haciendo uso de enteros!",true);
        }
    }

    @Override
    public Tipo obtenerTipo() {
        if (this.tipo == null) {
            this.tipo = tablaDeSimbolos
                    .obtenerVarEnAlcanceActual(metodoContenedor, claseContenedora, padre.obtenerToken())
                    .obtenerTipo();
        }
        return this.tipo;
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("\"Nodo\":").append("\"NodoArreglo\",").append(System.lineSeparator());
        sb.append("\"encadenado\":{").append(System.lineSeparator());
        sb.append(this.encadenado.toJson()).append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        return sb.toString();
    }

    @Override
    public String genCodigo(){
        StringBuilder sb = new StringBuilder();
        // Todos los tipos primitivos ocupan 4 bytes de maximo
        int memoriaArreglo = Integer.parseInt(this.token.obtenerLexema())*4;
        // Para generar un nuevo CIR, debemos guardar espacio en el heap y devolver un puntero en $a0
        sb.append("li $v0, 9 # Alocamos en el heap el constructor del arreglo").append(System.lineSeparator());
        sb.append("li $a0,"+memoriaArreglo).append(System.lineSeparator());
        sb.append("syscall").append(System.lineSeparator());
        // Guardamos en la ultima posicion del CIR 

        // Por ser esto una instanciacion, estamos en el lado derecho de una asignacion
        // por tanto, cargamos en el acumulador la posicion donde comienza el nuevo CIR
        sb.append("move $a0, $v0 # $a0 contiene el puntero al CIR del arreglo ").append(System.lineSeparator());
        // Luego de haber creado el CIR, queda en $a0 una referencia al inicio de la posicion del mismo
        return sb.toString();
    }
}


