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
}
