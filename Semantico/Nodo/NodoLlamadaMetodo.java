package Semantico.Nodo;

import java.util.ArrayList;

import Lexico.Token;
import Semantico.Clase;
import Semantico.ErrorSemantico;
import Semantico.Funcion.Funcion;
import Semantico.Funcion.Metodo;
import Semantico.Tipo.Tipo;

public class NodoLlamadaMetodo extends NodoExpresion {

    Token token;
    ArrayList<NodoExpresion> argumentos = new ArrayList<>();

    public NodoLlamadaMetodo(Funcion metodoContenedor, Clase claseContenedora, Token token) {
        super(metodoContenedor, claseContenedora);
        this.token = token;
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

    public void agregarArgumento(NodoExpresion exp) {
        this.argumentos.add(exp);
        exp.establecerPadre(this);
    }

    @Override
    public Tipo obtenerTipo() {
        if (this.tipo == null) {
            // Hay un caso raro en el cual la tabla de simbolos no se asigna y por tanto se
            // hace en este punto
            if (this.tablaDeSimbolos == null) {
                this.tablaDeSimbolos = padre.tablaDeSimbolos;
            }
            // Para verificar el tipo de retorno buscamos la informacion del metodo en la
            // tabla de simbolos
            Clase infoClase = tablaDeSimbolos.obtenerClasePorNombre(padre.obtenerTipoClase().obtenerTipo());
            Metodo infoMetodo = infoClase.obtenerMetodoPorNombre(token.obtenerLexema());
            // Si el metodo es nulo, el metodo no se encuentra en la tabla de simbolos
            if (infoMetodo == null) {
                new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(), "El metodo " + token.obtenerLexema()
                        + " no esta definido para la clase " + padre.obtenerTipoClase().obtenerTipo());
            }
            this.tipo = infoMetodo.obtenerTipoRetorno();
        }
        return this.tipo;
    }

    @Override
    public void checkeoTipos() {
        this.obtenerTipo();
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("\"Nodo\":").append("\"NodoLlamadaMetodo\",").append(System.lineSeparator());
        if (this.encadenado != null) {
            sb.append("\"valor\":").append("\"" + token.obtenerLexema() + "\",").append(System.lineSeparator());
            sb.append("\"encadenado\":{").append(System.lineSeparator());
            sb.append(this.encadenado.toJson()).append(System.lineSeparator());
            sb.append("}").append(System.lineSeparator());
        } else {
            sb.append("\"valor\":").append("\"" + token.obtenerLexema() + "\"").append(System.lineSeparator());
        }
        return sb.toString();
    }

}
