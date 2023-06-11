package Semantico.Nodo;

import java.util.ArrayList;

import Lexico.Token;
import Semantico.Clase;
import Semantico.ErrorSemantico;
import Semantico.Funcion.Funcion;
import Semantico.Funcion.Metodo;
import Semantico.Funcion.Constructor;
import Semantico.Tipo.Tipo;
import Semantico.Tipo.TipoArreglo;
import Semantico.Tipo.TipoPrimitivo;
import Semantico.Tipo.TipoReferencia;
import Semantico.Variable.Parametro;

public class NodoLlamadaMetodo extends NodoExpresion {

    Token token;
    ArrayList<NodoExpresion> argumentos = new ArrayList<>();
    private boolean estatico = false;
    // Este atributo define el nombre de la clase en la que el metodo fue definido
    private String clase;
    private Tipo tipoPadre;

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
                Tipo tConstructor = new TipoReferencia(clase);
                this.tipo = tConstructor;
                if (this.encadenado != null) {
                    this.tipo = this.encadenado.obtenerTipoEncadenado(tConstructor);
                    return this.tipo;
                } else {
                    return this.tipo;
                }
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
                if (infoMetodo.obtenerEsEstatico() == false) {
                    new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                            "El metodo " + token.obtenerLexema()
                                    + " no esta definido como metodo estatico",
                            true);
                }
                this.checkeoCantArgumentos(infoMetodo);
                Tipo tMetodoE = infoMetodo.obtenerTipoRetorno();
                this.tipo = tMetodoE;
                if (this.encadenado != null) {
                    this.tipo = this.encadenado.obtenerTipoEncadenado(tMetodoE);
                    return this.tipo;
                } else {
                    return this.tipo;
                }
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
                // Si no estamos dentro de un constructor hay que verificar que el metodo no sea
                // accedido desde
                // un metodo estatico
                if (metodoContenedor.obtenerNombre() != null) {
                    Metodo metodoPadre = (Metodo) metodoContenedor;
                    // Si el metodo es estatico no puede acceder a metodos dinamicos
                    if (metodoPadre.obtenerEsEstatico() == true) {
                        new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                                "El metodo " + token.obtenerLexema()
                                        + " no puede ser accedido desde el metodo " + metodoPadre.obtenerNombre(),
                                true);
                    }
                }
                this.checkeoCantArgumentos(infoMetodo);
                Tipo tMetodo = infoMetodo.obtenerTipoRetorno();
                this.tipo = tMetodo;
                if (this.encadenado != null) {
                    this.tipo = this.encadenado.obtenerTipoEncadenado(tMetodo);
                    return this.tipo;
                } else {
                    return this.tipo;
                }
            }
        } else {
            return this.tipo;
        }
    }

    @Override
    public Tipo obtenerTipoEncadenado(Tipo tipoPadre) {
        if (this.tablaDeSimbolos == null) {
            this.tablaDeSimbolos = obtenerTablaDeSimbolos();
        }
        this.tipoPadre = tipoPadre;
        // El metodo debe estar definido en la clase del tipoPadre
        Clase infoClase = tablaDeSimbolos.obtenerClasePorNombre(tipoPadre.obtenerTipo());
        Metodo infoMetodo = infoClase.obtenerMetodoPorNombre(token.obtenerLexema());
        // Si el metodo es nulo, el metodo no se encuentra en la tabla de simbolos
        if (infoMetodo == null) {
            new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                    "El metodo " + token.obtenerLexema()
                            + " no esta definido para la clase "
                            + tipoPadre.obtenerTipo(),
                    true);
        }
        this.checkeoCantArgumentos(infoMetodo);
        Tipo tMetodo = infoMetodo.obtenerTipoRetorno();
        this.tipo = tMetodo;
        if (this.encadenado != null) {
            this.tipo = this.encadenado.obtenerTipoEncadenado(tMetodo);
            return this.tipo;
        } else {
            return this.tipo;
        }
    }

    @Override
    public void checkeoTipos() {
        this.obtenerTipo();
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
            if (tipoArgDeclarado instanceof TipoPrimitivo) {
                if (tipoArgLlamado instanceof TipoPrimitivo) {
                    if (!tipoArgDeclarado.obtenerTipo().equals(tipoArgLlamado.obtenerTipo())) {
                        new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                                "El argumento en la posicion " + i + " deberia ser de tipo "
                                        + tipoArgDeclarado.obtenerTipo(),
                                true);
                    }
                } else {
                    new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                            "El argumento en la posicion " + i + " deberia ser de tipo "
                                    + tipoArgDeclarado.obtenerTipo(),
                            true);
                }
            } else if (tipoArgDeclarado instanceof TipoReferencia) {
                if (tipoArgLlamado instanceof TipoReferencia) {
                    Clase infoClase = tablaDeSimbolos.obtenerClasePorNombre(tipoArgLlamado.obtenerTipo());
                    if (!infoClase.esSubclaseDe(tipoArgDeclarado.obtenerTipo())) {
                        if (!tipoArgDeclarado.obtenerTipo().equals(tipoArgLlamado.obtenerTipo())) {
                            new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                                    "El argumento en la posicion " + i + " deberia ser de tipo "
                                            + tipoArgDeclarado.obtenerTipo(),
                                    true);
                        }
                    }
                } else {
                    new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                            "El argumento en la posicion " + i + " deberia ser de tipo "
                                    + tipoArgDeclarado.obtenerTipo(),
                            true);
                }
            } else if (tipoArgDeclarado instanceof TipoArreglo) {
                if (tipoArgLlamado instanceof TipoArreglo) {
                    if (!tipoArgDeclarado.obtenerTipo().equals(tipoArgLlamado.obtenerTipo())) {
                        new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                                "El argumento en la posicion " + i + " deberia ser de tipo "
                                        + tipoArgDeclarado.obtenerTipo(),
                                true);
                    }
                } else {
                    new ErrorSemantico(token.obtenerFila(), token.obtenerColumna(),
                            "El argumento en la posicion " + i + " deberia ser de tipo "
                                    + tipoArgDeclarado.obtenerTipo(),
                            true);
                }
            }
        }
    }

    @Override
    public String genCodigo() {
        // Checkeamos si es un constructor
        if (this.token != null) {
            if (this.token.obtenerToken() == "id_clase") {
                Clase infoClase = this.tablaDeSimbolos.obtenerClasePorNombre(this.token.obtenerLexema());
                // Generamos el espacio en el heap para el CIR
                // Aca falta el manejo de envio de parametros al constructor
                StringBuilder sb = new StringBuilder();
                // Para generar un nuevo CIR, debemos guardar espacio en el heap y devolver un puntero en $a0
                sb.append("li $v0, 9 # Alocamos en el heap el constructor de "+this.token.obtenerLexema()).append(System.lineSeparator());
                sb.append("li $a0,"+infoClase.obtenerTamMemoria()).append(System.lineSeparator());
                sb.append("syscall").append(System.lineSeparator());
                // Guardamos en la ultima posicion del CIR 

                // Por ser esto una instanciacion, estamos en el lado derecho de una asignacion
                // por tanto, cargamos en el acumulador la posicion donde comienza el nuevo CIR
                sb.append("move $a0, $v0 # $a0 contiene el puntero al CIR de "+this.token.obtenerLexema()).append(System.lineSeparator());
                // Luego de haber creado el CIR, queda en $a0 una referencia al inicio de la posicion del mismo
                return sb.toString();
            }
        }
        if (this.tipoPadre == null) {
            if (this.estatico) {
                this.tipoPadre = new TipoReferencia(this.clase);
            } else {
                this.tipoPadre = new TipoReferencia(claseContenedora.obtenerNombre());
            }
        }
        Clase clasePadre = this.tablaDeSimbolos.obtenerClasePorNombre(this.tipoPadre.obtenerTipo());
        Metodo infoMetodo = clasePadre.obtenerMetodoPorNombre(token.obtenerLexema());
        // Generamos el registro de activacion del metodo que estamos llamando
        StringBuilder sb = new StringBuilder();
        // El llamador gurada su valor de $fp
        sb.append("sw $fp, 0($sp)").append(System.lineSeparator());
        
        // Guardamos la direccion de self de la clase a la que pertenece el metodo
        NodoVariable padreVariable = this.padre.obtenerNodoVariable();
        // Si el padre es una variable, entonces el self debe apuntar al CIR que se encuentra en la variable padre
        if(padreVariable.obtenerToken() != null){
            // Obtenemos la direccion del CIR que se encuentra en la variable padre
            int offset = metodoContenedor.offsetVariable(padreVariable.obtenerToken().obtenerLexema());
            // Guardamos la referencia en la posicion correspondiente en el RA
            sb.append("lw $t1,-"+offset+"($fp)").append(System.lineSeparator());
            int offsetSelf = metodoContenedor.offsetSelf();
            sb.append("sw $t1,-"+offsetSelf+"($sp)").append(System.lineSeparator());
        }
        
        // Luego, almacena los argumentos en la pila
        for (int i = 0; i < argumentos.size(); i++) {
            NodoExpresion argumento = argumentos.get(i);
            sb.append(argumento.genCodigo()).append(System.lineSeparator());
            int offset = infoMetodo.offsetParametro(i);
            sb.append("sw $a0, -" + offset + "($sp) # Guardamos el parameotro "+i+" en el RA").append(System.lineSeparator());
        }
        String prefijo;
        if (infoMetodo.obtenerEsEstatico()) {
            prefijo = this.clase;
        } else {
            prefijo = this.tipoPadre.obtenerTipo();
        }
        // Finalmente, redireccionamos la ejecucion al metodo correspondiente
        sb.append("jal " + prefijo + "_" + this.token.obtenerLexema())
                .append(System.lineSeparator());
        return sb.toString();
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
