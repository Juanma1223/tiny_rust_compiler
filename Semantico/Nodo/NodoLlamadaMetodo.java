package Semantico.Nodo;

import java.util.ArrayList;

import Lexico.Token;
import Semantico.Clase;
import Semantico.ErrorSemantico;
import Semantico.Funcion.Funcion;
import Semantico.Funcion.Metodo;
import Semantico.Tipo.Tipo;
import Semantico.Tipo.TipoReferencia;

public class NodoLlamadaMetodo extends NodoExpresion {

    Token token;
    ArrayList<NodoExpresion> argumentos = new ArrayList<>();
    private boolean estatico = false;
    private String clase;

    public NodoLlamadaMetodo(Funcion metodoContenedor, Clase claseContenedora, Token token) {
        super(metodoContenedor, claseContenedora);
        this.token = token;
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

    public void establecerForma(boolean estatico) {
        this.estatico = true;
    }

    public void establecerClase(String clase) {
        this.clase = clase;
    }

    public void agregarArgumento(NodoExpresion exp) {
        this.argumentos.add(exp);
        exp.establecerTablaDeSimbolos(tablaDeSimbolos);
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
            // Si el token es un id_clase el metodo es un constructor
            if (token.obtenerToken().equals("id_clase")) {
                Clase infoClase = tablaDeSimbolos.obtenerClasePorNombre(token.obtenerLexema());
                if (infoClase == null) {
                    new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(), "No se puede crear un objeto de la clase "
                    + token.obtenerLexema() + " porque no esta definida.",true);
                }
                this.tipo = new TipoReferencia(clase);
            } else if (estatico == true) {
                // Si el método es estático ya sabemos la clase padre
                Clase infoClase = tablaDeSimbolos.obtenerClasePorNombre(clase);
                if (infoClase == null) {
                    new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(), "La clase del metodo estatico "
                    + token.obtenerLexema() + " no esta definida.",true);
                }
                Metodo infoMetodo = infoClase.obtenerMetodoPorNombre(token.obtenerLexema());
                // Si el metodo es nulo, el metodo no se encuentra en la tabla de simbolos
                if (infoMetodo == null) {
                    new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(), "El metodo " + token.obtenerLexema()
                            + " no esta definido para la clase " + clase,true);
                }
                this.tipo = infoMetodo.obtenerTipoRetorno();
            } else {
                // Para verificar el tipo de retorno buscamos la informacion del metodo en la
                // tabla de simbolos
                if (padre instanceof NodoVariable){
                    // Si el padre es un NodoVariable, el metodo debe estar definido en la clase de dicha variable
                    Clase infoClase = tablaDeSimbolos.obtenerClasePorNombre(padre.obtenerTipoClase().obtenerTipo());
                    Metodo infoMetodo = infoClase.obtenerMetodoPorNombre(token.obtenerLexema());
                    // Si el metodo es nulo, el metodo no se encuentra en la tabla de simbolos
                    if (infoMetodo == null) {
                        new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(), "El metodo " + token.obtenerLexema()
                            + " no esta definido para la clase " + padre.obtenerTipoClase().obtenerTipo(),true);
                    }
                    this.tipo = infoMetodo.obtenerTipoRetorno();
                } else {
                    // Sino, el metodo debe estar definido en la clase contenedora
                    Metodo infoMetodo = claseContenedora.obtenerMetodoPorNombre(token.obtenerLexema());
                    // Si el metodo es nulo, el metodo no se encuentra en la tabla de simbolos
                    if (infoMetodo == null) {
                        new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(), "El metodo " + token.obtenerLexema()
                            + " no esta definido para la clase " + claseContenedora.obtenerNombre(),true);
                    }
                    this.tipo = infoMetodo.obtenerTipoRetorno();
                }
            }
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
