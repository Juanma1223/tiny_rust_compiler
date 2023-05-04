package Semantico;

import java.util.HashMap;

import Semantico.Variable.Atributo;
import Semantico.Funcion.Metodo;
import Semantico.Funcion.Constructor;

public class Clase {
    private String nombre;
    private String heredaDe;
    private HashMap<String, Atributo> atributos = new HashMap<String, Atributo>();
    private HashMap<String, Metodo> metodos = new HashMap<String, Metodo>();
    private Constructor constructor = new Constructor();
    private int fila, columna;

    public Clase(String nombre) {
        this.nombre = nombre;
    }

    public Clase(String nombre, int fila, int columna) {
        this.nombre = nombre;
        this.fila = fila;
        this.columna = columna;
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

    public void establecerHerencia(String heredaDe){
        this.heredaDe = heredaDe;
    }

    // Esta clase debe heredar los atributos y metodos de la superclase
    public void heredarAtributosMetodos(Clase superClase) {
        if (superClase != null) {
            this.heredaDe = superClase.obtenerNombre();
            // Actualizamos la posicion de los atributos para corresponderlas con las de la superclase
            int offset = superClase.atributos.size();
            for (HashMap.Entry<String, Atributo> atributo : this.atributos.entrySet()) {
                Atributo atributoActual = atributo.getValue();
                atributoActual.establecerPosicion(atributoActual.obtenerPosicion()+offset);
                this.atributos.put(atributoActual.obtenerNombre(), atributoActual);
            }
            // Agregamos metodos y atributos de la superclase a la subclase
            for (HashMap.Entry<String, Atributo> atributo : superClase.atributos.entrySet()) {
                // Si el atributo ya se encuentra definido en la subclase, lanzamos un error
                // ya que no se pueden redefinir atributos
                if (this.atributos.get(atributo.getValue().obtenerNombre()) != null) {
                    new ErrorSemantico(0, 0,
                            "El atributo " + atributo.getValue().obtenerNombre() + " en la clase "
                                    + this.obtenerNombre()
                                    + " ya esta definido en la superclase");
                } else {
                    this.atributos.put(atributo.getValue().obtenerNombre(), atributo.getValue());
                }
            }
            offset = superClase.metodos.size();
            for (HashMap.Entry<String, Metodo> metodo : superClase.metodos.entrySet()) {
                // Si el metodo se esta redefiniendo, no debe insertarse
                if(this.metodos.get(metodo.getValue().obtenerNombre()) == null){
                    this.metodos.put(metodo.getValue().obtenerNombre(), metodo.getValue());
                }
            }
            // Actualizamos la posicion de los metodos de la subclase en funcion de los de la superclase
            for (HashMap.Entry<String, Metodo> metodo : this.metodos.entrySet()) {
                // No actualizamos la posicion en aquellos metodos que vienen desde la superclase
                if(superClase.metodos.get(metodo.getValue().obtenerNombre()) == null){
                    Metodo metodoActual = metodo.getValue();
                    metodoActual.establecerPosicion(metodoActual.obtenerPosicion()+offset);
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

    public void establecerConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

    public Atributo obtenerAtributoPorNombre(String nombre) {
        return this.atributos.get(nombre);
    }

    public Metodo obtenerMetodoPorNombre(String nombre) {
        return this.metodos.get(nombre);
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

    public int obtenerFila() {
        return this.fila;
    }

    public int obtenerColumna() {
        return this.columna;
    }

}
