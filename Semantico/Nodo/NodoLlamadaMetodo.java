package Semantico.Nodo;

import java.util.ArrayList;

import Lexico.Token;
import Semantico.Clase;
import Semantico.ErrorSemantico;
import Semantico.Funcion.Funcion;
import Semantico.Funcion.Metodo;
import Semantico.Funcion.Constructor;
import Semantico.Tipo.Tipo;
import Semantico.Tipo.TipoReferencia;
import Semantico.Variable.Parametro;
import Semantico.Variable.Variable;

public class NodoLlamadaMetodo extends NodoExpresion {

    Token token;
    ArrayList<NodoExpresion> argumentos = new ArrayList<>();
    private boolean estatico = false;
    // Este atributo define el nombre de la clase en la que el metodo fue definido
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
        // El nodo de llamada a metodo puede tener un encadenado, por tanto si lo tiene
        // el tipo retornado es del encadenado
        if (this.encadenado != null) {
            this.tipo = this.encadenado.obtenerTipo();
        } else {
            if (this.tipo == null) {
                // Hay casos donde no se asigna la tabla de simbolos, si este es el caso
                // la buscamos en el padre o ancestros mediante el metodo heredado
                // obtenerTablaDeSimbolos
                if (this.tablaDeSimbolos == null) {
                    this.tablaDeSimbolos = obtenerTablaDeSimbolos();
                }
                // Si el token es un id_clase el metodo es un constructor
                if (token.obtenerToken().equals("id_clase")) {
                    Clase infoClase = tablaDeSimbolos.obtenerClasePorNombre(token.obtenerLexema());
                    if (infoClase == null) {
                        new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                                "No se puede crear un objeto de la clase "
                                        + token.obtenerLexema() + " porque no esta definida.",
                                true);
                    }
                    Constructor constructor = infoClase.obtenerConstructor();
                    this.checkeoCantArgumentos(constructor);
                    this.tipo = new TipoReferencia(clase);
                } else if (estatico == true) {
                    // Si el método es estático ya sabemos la clase padre
                    Clase infoClase = tablaDeSimbolos.obtenerClasePorNombre(clase);
                    if (infoClase == null) {
                        new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(), "La clase del metodo estatico "
                                + token.obtenerLexema() + " no esta definida.", true);
                    }
                    Metodo infoMetodo = infoClase.obtenerMetodoPorNombre(token.obtenerLexema());
                    // Si el metodo es nulo, el metodo no se encuentra en la tabla de simbolos
                    if (infoMetodo == null) {
                        new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                                "El metodo " + token.obtenerLexema()
                                        + " no esta definido para la clase " + clase,
                                true);
                    }
                    this.checkeoCantArgumentos(infoMetodo);
                    this.tipo = infoMetodo.obtenerTipoRetorno();
                } else {
                    if (padre instanceof NodoLlamadaMetodo) {
                        // Si el padre es un NodoLlamadaMetodo, el metodo debe estar definido en la
                        // clase que retorna el metodo
                        Clase infoClase = tablaDeSimbolos.obtenerClasePorNombre(padre.obtenerTipoClase().obtenerTipo());
                        Metodo infoMetodo = infoClase.obtenerMetodoPorNombre(token.obtenerLexema());
                        // Si el metodo es nulo, el metodo no se encuentra en la tabla de simbolos
                        if (infoMetodo == null) {
                            new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                                    "El metodo " + token.obtenerLexema()
                                            + " no esta definido para la clase "
                                            + padre.obtenerTipoClase().obtenerTipo(),
                                    true);
                        }
                        this.checkeoCantArgumentos(infoMetodo);
                        this.tipo = infoMetodo.obtenerTipoRetorno();
                    } else if (padre instanceof NodoVariable) {
                        // Si el padre es un NodoVariable, el metodo debe estar definido en la clase de
                        // dicha variable
                        Clase infoClase = tablaDeSimbolos.obtenerClasePorNombre(padre.obtenerTipoClase().obtenerTipo());
                        Metodo infoMetodo = infoClase.obtenerMetodoPorNombre(token.obtenerLexema());
                        // Si el metodo es nulo, el metodo no se encuentra en la tabla de simbolos
                        if (infoMetodo == null) {
                            new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                                    "El metodo " + token.obtenerLexema()
                                            + " no esta definido para la clase "
                                            + padre.obtenerTipoClase().obtenerTipo(),
                                    true);
                        }
                        this.checkeoCantArgumentos(infoMetodo);
                        this.tipo = infoMetodo.obtenerTipoRetorno();
                    } else {
                        // Sino, el metodo debe estar definido en la clase contenedora
                        Metodo infoMetodo = claseContenedora.obtenerMetodoPorNombre(token.obtenerLexema());
                        // Si el metodo es nulo, el metodo no se encuentra en la tabla de simbolos
                        if (infoMetodo == null) {
                            new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                                    "El metodo " + token.obtenerLexema()
                                            + " no esta definido para la clase " + claseContenedora.obtenerNombre(),
                                    true);
                        }
                        this.checkeoCantArgumentos(infoMetodo);
                        this.tipo = infoMetodo.obtenerTipoRetorno();
                    }
                }
            }
        }
        return this.tipo;
    }

    @Override
    public void checkeoTipos() {
        this.obtenerTipo();
        if(this.encadenado != null){
            this.encadenado.checkeoTipos();
        }
    }

    // Este metodo checkea que la cantidad y tipo de argumentos enviados en la
    // llamada coincida con los del metodo original
    public void checkeoCantArgumentos(Funcion infoMetodo) {
        ArrayList<Parametro> argOrdenados = infoMetodo.obtenerParamsOrdenados();
        // Checkeamos que la cantidad de parametros sea la misma
        if (argOrdenados.size() != argumentos.size()) {
            new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                    "Cantidad de argumentos incorrecta en el llamado a funcion!", true);
        }
        // Checkeamos que el tipo de los parametros coincida
        for (int i = 0; i < argOrdenados.size(); i++) {
            Tipo tipoArgLlamado = argumentos.get(i).obtenerTipo();
            Tipo tipoArgDeclarado = argOrdenados.get(i).obtenerTipo();
            if (!tipoArgLlamado.obtenerTipo().equals(tipoArgDeclarado.obtenerTipo())) {
                new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                        "El argumento en la posicion " + i + " deberia ser de tipo " + tipoArgDeclarado.obtenerTipo(),
                        true);
            }
        }
    }

    // Esta funcion es utilizada cuando el tipo de retorno del metodo es una clase
    public Tipo obtenerTipoClase() {
        // El tipo aun no esta definido, lo buscamos en la tabla de simbolos
        if (this.tipo == null) {
            Tipo tipoPadre = padre.obtenerTipoClase();
            if(padre instanceof NodoVariable){
                // Si el padre es de tipo variable, usamos su tipo para obtener el tipo de retorno del metodo actual
                Clase infoClase = obtenerTablaDeSimbolos().obtenerClasePorNombre(tipoPadre.obtenerTipo());
                Metodo infoMetodo = infoClase.obtenerMetodoPorNombre(token.obtenerLexema());
                return infoMetodo.obtenerTipoRetorno();
            }else{
                return tipoPadre;
            }
        } else {
            return this.tipo;
        }
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
