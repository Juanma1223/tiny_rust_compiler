package Semantico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import Semantico.Variable.Atributo;
import Semantico.Variable.Parametro;
import Semantico.Funcion.Metodo;
import Semantico.Funcion.Constructor;

public class Clase {
    private String nombre;
    private String heredaDe;
    private HashMap<String, Atributo> atributos = new HashMap<String, Atributo>();
    private HashMap<String, Metodo> metodos = new HashMap<String, Metodo>();
    private Constructor constructor = new Constructor();
    private int fila, columna;
    private Boolean tieneConstructor = false;
    private TablaDeSimbolos tablaDeSimbolos;
    private int tamMemoria = 0;

    // Este valor indica un identificador para cada instancia de la clase
    public int numeroInstancia = 0;

    public Clase(String nombre, TablaDeSimbolos tablaDeSimbolos) {
        this.nombre = nombre;
        this.tablaDeSimbolos = tablaDeSimbolos;
    }

    public Clase(String nombre, int fila, int columna, TablaDeSimbolos tablaDeSimbolos) {
        this.nombre = nombre;
        this.fila = fila;
        this.columna = columna;
        this.tablaDeSimbolos = tablaDeSimbolos;
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(System.lineSeparator());
        sb.append("\"nombre\":").append("\"" + nombre + "\",").append(System.lineSeparator());
        sb.append("\"heredaDe\":").append("\"" + heredaDe + "\",").append(System.lineSeparator());
        sb.append("\"metodos\":[").append(System.lineSeparator());
        int i = 1;
        for (HashMap.Entry<String, Metodo> metodo : metodos.entrySet()) {
            if (i < metodos.size()) {
                sb.append(metodo.getValue().toJson() + ",").append(System.lineSeparator());
            } else {
                sb.append(metodo.getValue().toJson()).append(System.lineSeparator());
            }
            i++;
        }
        sb.append("],").append(System.lineSeparator());
        sb.append("\"atributos\":[").append(System.lineSeparator());
        i = 1;
        for (HashMap.Entry<String, Atributo> atributo : atributos.entrySet()) {
            if (i < atributos.size()) {
                sb.append(atributo.getValue().toJson() + ",").append(System.lineSeparator());
            } else {
                sb.append(atributo.getValue().toJson()).append(System.lineSeparator());
            }
            i++;
        }
        sb.append("],").append(System.lineSeparator());
        sb.append("\"constructor\":").append(constructor.toJson()).append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        return sb.toString();
    }

    public String obtenerNombre() {
        return this.nombre;
    }

    public void establecerHerencia(String heredaDe) {
        this.heredaDe = heredaDe;
    }

    // Esta clase debe heredar los atributos y metodos de la superclase
    public void heredarAtributosMetodos(Clase superClase) {
        if (superClase != null) {
            this.heredaDe = superClase.obtenerNombre();
            // Actualizamos la posicion de los atributos para corresponderlas con las de la
            // superclase
            int offset = superClase.atributos.size();
            for (HashMap.Entry<String, Atributo> atributo : this.atributos.entrySet()) {
                Atributo atributoActual = atributo.getValue();
                atributoActual.establecerPosicion(atributoActual.obtenerPosicion() + offset);
                this.atributos.put(atributoActual.obtenerNombre(), atributoActual);
            }
            // Agregamos metodos y atributos de la superclase a la subclase
            for (HashMap.Entry<String, Atributo> atributo : superClase.atributos.entrySet()) {
                // Si el atributo ya se encuentra definido en la subclase, lanzamos un error
                // ya que no se pueden redefinir atributos
                if (this.atributos.get(atributo.getValue().obtenerNombre()) != null) {
                    Atributo atr = this.atributos.get(atributo.getValue().obtenerNombre());
                    new ErrorSemantico(atr.obtenerFila(), atr.obtenerColumna(),
                            "El atributo " + atr.obtenerNombre() + " no se puede redefinir en la clase "
                                    + this.obtenerNombre()
                                    + " ya que ya se encuentra definido en su superclase.");
                } else {
                    this.atributos.put(atributo.getValue().obtenerNombre(), atributo.getValue());
                }
            }
            offset = superClase.metodos.size();
            for (HashMap.Entry<String, Metodo> metodo : superClase.metodos.entrySet()) {
                // Si el metodo se esta redefiniendo, no debe insertarse
                if (this.metodos.get(metodo.getValue().obtenerNombre()) == null) {
                    this.metodos.put(metodo.getValue().obtenerNombre(), metodo.getValue());
                } else {
                    Metodo m1 = this.metodos.get(metodo.getValue().obtenerNombre());
                    if (!(m1.obtenerTipoRetorno().obtenerTipo()
                            .equals(metodo.getValue().obtenerTipoRetorno().obtenerTipo()))) {
                        new ErrorSemantico(m1.obtenerFila(), m1.obtenerColumna(),
                                "No se puede cambiar el tipo de retorno de un método heredado");
                    }
                    if (m1.obtenerParametros().size() != metodo.getValue().obtenerParametros().size()) {
                        new ErrorSemantico(m1.obtenerFila(), m1.obtenerColumna(),
                                "No se puede cambiar la cantidad de parámetros de un método heredado");
                    }
                    if (metodo.getValue().obtenerEsEstatico() == true) {
                        new ErrorSemantico(m1.obtenerFila(), m1.obtenerColumna(),
                                "No se puede redefinir un método de clase");
                    } else {
                        if (m1.obtenerEsEstatico() != false) {
                            new ErrorSemantico(m1.obtenerFila(), m1.obtenerColumna(),
                                    "No se puede cambiar la forma de un método heredado");
                        }
                    }
                    // Obtenemos los parametros ordenados de cada metodo para poder comparar los
                    // tipos por posicion
                    ArrayList<Parametro> paramOrdenados1 = metodo.getValue().obtenerParamsOrdenados();
                    ArrayList<Parametro> paramOrdenados2 = m1.obtenerParamsOrdenados();
                    // Recorremos los parametros checkeando que los tipos coincidan en cada posicion
                    for (int i = 0; i < paramOrdenados1.size(); i++) {
                        String tipoParam1 = paramOrdenados1.get(i).obtenerTipo().obtenerTipo();
                        String tipoParam2 = paramOrdenados2.get(i).obtenerTipo().obtenerTipo();
                        if (!tipoParam1.equals(tipoParam2)) {
                            new ErrorSemantico(m1.obtenerFila(), m1.obtenerColumna(),
                                    "No se puede cambiar el tipo de los parametros del método heredado");
                        }
                    }
                }
            }
            // Actualizamos la posicion de los metodos de la subclase en funcion de los de
            // la superclase
            for (HashMap.Entry<String, Metodo> metodo : this.metodos.entrySet()) {
                // No actualizamos la posicion en aquellos metodos que vienen desde la
                // superclase
                if (superClase.metodos.get(metodo.getValue().obtenerNombre()) == null) {
                    Metodo metodoActual = metodo.getValue();
                    metodoActual.establecerPosicion(metodoActual.obtenerPosicion() + offset);
                    this.metodos.put(metodoActual.obtenerNombre(), metodoActual);
                }
            }
        } else {
            // Utilizado para el caso Object
            this.heredaDe = "null";
        }
    }

    public String obtenerHerencia() {
        return this.heredaDe;
    }

    public Constructor obtenerConstructor() {
        return this.constructor;
    }

    public Boolean tieneConstructor() {
        return tieneConstructor;
    }

    public void establecerConstructor(Constructor constructor) {
        this.constructor = constructor;
        tieneConstructor = true;
    }

    public Atributo obtenerAtributoPorNombre(String nombre) {
        return this.atributos.get(nombre);
    }

    public HashMap<String, Atributo> obtenerAtributos() {
        return this.atributos;
    }

    public Metodo obtenerMetodoPorNombre(String nombre) {
        return this.metodos.get(nombre);
    }

    public HashMap<String, Metodo> obtenerMetodos() {
        return this.metodos;
    }

    public void insertarMetodo(Metodo metodo) {
        // Guardamos el orden de declaracion de los metodos mediante su posicion
        int posicion = this.metodos.size();
        metodo.establecerPosicion(posicion);
        this.metodos.put(metodo.obtenerNombre(), metodo);
    }

    public void insertarAtributo(Atributo atributo) {
        // Guardamos el orden de declaracion de los atributos mediante su posicion
        int posicion = this.atributos.size();
        atributo.establecerPosicion(posicion);
        this.atributos.put(atributo.obtenerNombre(), atributo);
    }

    // Devuelve true si el atributo ya ha sido declarado en esta clase
    public Boolean atributoYaDeclarado(String nombre) {
        Atributo atrib = this.obtenerAtributoPorNombre(nombre);
        if (atrib == null) {
            return false;
        } else {
            return true;
        }
    }

    // Este metodo nos indica si esta clase es subclase de otra
    public Boolean esSubclaseDe(String superclase) {
        Clase clasePadre = tablaDeSimbolos.obtenerClasePorNombre(this.heredaDe);
        while (clasePadre != null) {
            if (clasePadre.nombre.equals(superclase)) {
                return true;
            }
            // Si no lo encontramos en el padre inmediato, buscamos hacia arriba
            clasePadre = tablaDeSimbolos.obtenerClasePorNombre(clasePadre.heredaDe);
        }
        return false;
    }

    public int obtenerFila() {
        return this.fila;
    }

    public int obtenerColumna() {
        return this.columna;
    }

    // Calculamos cuanto espacio en memoria se requiere para almacenar el CIR de
    // esta
    // clase
    public void calcularMemoria() {
        // Calculamos el espacio de las variables locales segun su tipo
        int memVariables = 0;
        for (HashMap.Entry<String, Atributo> variable : this.atributos.entrySet()) {
            switch (variable.getValue().obtenerTipo().obtenerTipo()) {
                // Sumamos en bytes, todos los tipos
                case "Str":
                    // Esto esta sujeto a cambio segun cuanto espacio les demos para los strings
                    memVariables = memVariables + 4;
                    break;
                case "Array":
                    // Aca vamos a tener que implementar el calculo de tamaño dentro del tipo
                    // arreglo en funcion de
                    // la cantidad de elementos y su tipo
                    break;
                default:
                    memVariables = memVariables + 4;
                    break;
            }
        }

        // Sumamos los valores fijos del CIR
        // Direccion de la VT = 4 bytes
        // Direccion de self = 4 bytes
        int memTotal = 8 + memVariables;
        this.tamMemoria = memTotal;
    }

    public int obtenerTamMemoria() {
        if(this.tamMemoria == 0){
            this.calcularMemoria();
        }    
        return this.tamMemoria;
    }

    // Este metodo calcula el offset de acceso a un atributo de clase en el CIR
    public int offsetAtributo(String nombreAtributo) {
        Atributo variable = this.obtenerAtributoPorNombre(nombreAtributo);
        ArrayList<Atributo> varOrdenadas = this.obtenerAtributosOrdenadas();
        int offset = 0;
        // Desplazamos la memoria tanto como el tipo y posicion de la variable lo
        // requiera
        for (int i = 0; i < variable.obtenerPosicion(); i++) {
            Atributo var = varOrdenadas.get(i);
            switch (var.obtenerTipo().obtenerTipo()) {
                // Sumamos en bytes, todos los tipos
                case "Str":
                    // Esto esta sujeto a cambio segun cuanto espacio les demos para los strings
                    offset += 4;
                    break;
                case "Array":
                    // Aca vamos a tener que implementar el calculo de tamaño dentro del tipo
                    // arreglo en funcion de
                    // la cantidad de elementos y su tipo
                    break;
                default:
                    offset += 4;
                    break;
            }
        }
        return offset;
    }

    public ArrayList<Atributo> obtenerAtributosOrdenadas(){
        ArrayList<Atributo> ordenados = new ArrayList<Atributo>();
        ArrayList<Integer> aux = new ArrayList<Integer>();
        // Insertamos las posiciones en un arreglo auxiliar para luego ordenarlas
        for(HashMap.Entry<String,Atributo> variable : this.atributos.entrySet()){
            aux.add(variable.getValue().obtenerPosicion());
        }
        Collections.sort(aux);
        // Insertamos los parametros ordenados por posicion en un LinkedHashMap
        for (int num : aux){
            for (HashMap.Entry<String, Atributo> variable : this.atributos.entrySet()){
                if(variable.getValue().obtenerPosicion() == num){
                    ordenados.add(variable.getValue());
                }
            }
        }
        return ordenados;
    }

    public int obtenerNumeroInstancia(){
        int instanciaActual = this.numeroInstancia;
        this.numeroInstancia += 1;
        return instanciaActual;
    }
}
