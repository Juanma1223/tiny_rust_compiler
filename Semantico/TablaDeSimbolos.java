package Semantico;

import java.util.HashMap;

import Lexico.Token;
import Semantico.Funcion.Funcion;
import Semantico.Funcion.Metodo;
import Semantico.Tipo.Tipo;
import Semantico.Tipo.TipoArreglo;
import Semantico.Tipo.TipoPrimitivo;
import Semantico.Tipo.TipoVoid;
import Semantico.Variable.Parametro;
import Semantico.Variable.Variable;

public class TablaDeSimbolos {
    private Clase claseActual;
    private Funcion metodoActual;
    private HashMap<String, Clase> clases = new HashMap<String, Clase>();

    Tipo tStr = new TipoPrimitivo("Str");
    Tipo tI32 = new TipoPrimitivo("I32");
    Tipo tBool = new TipoPrimitivo("Bool");
    Tipo tChar = new TipoPrimitivo("Char");
    Tipo tArray = new TipoArreglo("Array");
    Tipo tVoid = new TipoVoid("void");

    public TablaDeSimbolos() {
        insertarClaseObject();
        insertarClaseIO();
        insertarClasesPrimitivas();
    }

    public String toJson(String nombreArchivo) {
        StringBuilder sb = new StringBuilder();
        // Construimos el json de forma recursiva
        sb.append("{").append(System.lineSeparator());
        sb.append("\"nombre\":").append("\"" + nombreArchivo + "\",").append(System.lineSeparator());
        sb.append("\"clases\":[").append(System.lineSeparator());
        int i = 1;
        for (HashMap.Entry<String, Clase> clase : clases.entrySet()) {
            if (i < clases.size()) {
                sb.append(clase.getValue().toJson() + ",").append(System.lineSeparator());
            } else {
                sb.append(clase.getValue().toJson()).append(System.lineSeparator());
            }
            i++;

        }
        sb.append("]").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        return sb.toString();
    }

    public Clase obtenerClaseActual() {
        return this.claseActual;
    }

    public HashMap<String, Clase> obtenerClases() {
        return clases;
    }

    public void establecerClaseActual(Clase claseActual) {
        this.claseActual = claseActual;
    }

    public Funcion obtenerMetodoActual() {
        return this.metodoActual;
    }

    public void establecerMetodoActual(Funcion metodoActual) {
        this.metodoActual = metodoActual;
    }

    public Clase obtenerClasePorNombre(String nombreClase) {
        return clases.get(nombreClase);
    }

    public void insertarClase(Clase nuevaClase) {
        this.clases.put(nuevaClase.obtenerNombre(), nuevaClase);
    }

    public void insertarClaseObject() {
        Clase cObject = new Clase("Object");
        cObject.establecerHerencia(null);
        this.clases.put("Object", cObject);
    }

    public void insertarClaseIO() {
        Clase cIO = new Clase("IO");
        cIO.establecerHerencia("Object");

        cIO.insertarMetodo(new Metodo("out_string", true, tVoid));
        cIO.obtenerMetodoPorNombre("out_string").insertarParametro(new Parametro("s", tStr));

        cIO.insertarMetodo(new Metodo("out_i32", true, tVoid));
        cIO.obtenerMetodoPorNombre("out_i32").insertarParametro(new Parametro("i", tI32));

        cIO.insertarMetodo(new Metodo("out_bool", true, tVoid));
        cIO.obtenerMetodoPorNombre("out_bool").insertarParametro(new Parametro("b", tBool));

        cIO.insertarMetodo(new Metodo("out_char", true, tVoid));
        cIO.obtenerMetodoPorNombre("out_char").insertarParametro(new Parametro("c", tChar));

        cIO.insertarMetodo(new Metodo("out_array", true, tVoid));
        cIO.obtenerMetodoPorNombre("out_array").insertarParametro(new Parametro("a", tArray));

        cIO.insertarMetodo(new Metodo("in_string", true, tStr));

        cIO.insertarMetodo(new Metodo("in_i32", true, tI32));

        cIO.insertarMetodo(new Metodo("in_bool", true, tBool));

        cIO.insertarMetodo(new Metodo("in_char", true, tChar));

        this.clases.put("IO", cIO);
    }

    public void insertarClasesPrimitivas() {
        Clase cChar = new Clase("Char");
        cChar.establecerHerencia("Object");
        this.clases.put("Char", cChar);

        Clase cI32 = new Clase("I32");
        cI32.establecerHerencia("Object");
        this.clases.put("I32", cI32);

        Clase cBool = new Clase("Bool");
        cBool.establecerHerencia("Object");
        this.clases.put("Bool", cBool);

        Clase cStr = new Clase("Str");
        cStr.establecerHerencia("Object");
        cStr.insertarMetodo(new Metodo("length", false, tI32));
        cStr.insertarMetodo(new Metodo("concat", false, tStr));
        cStr.obtenerMetodoPorNombre("concat").insertarParametro(new Parametro("s", tStr));
        cStr.insertarMetodo(new Metodo("substr", false, tStr));
        cStr.obtenerMetodoPorNombre("substr").insertarParametro(new Parametro("i", tI32));
        cStr.obtenerMetodoPorNombre("substr").insertarParametro(new Parametro("l", tI32));
        this.clases.put("Str", cStr);

        Clase cArray = new Clase("Array");
        cArray.establecerHerencia("Object");
        cArray.insertarMetodo(new Metodo("length", false, tI32));
        this.clases.put("Array", cArray);
    }

    // Este metodo obtiene una variable en el scope actual buscando primero en el metodo contenedor de la variable
    // y luego en su clase
    public Variable obtenerVarEnAlcanceActual(Funcion metodoPadre, Clase claseMadre, Token tokenActual) {
        Variable infoVariable = metodoPadre.obtenerParametroPorNombre(tokenActual.obtenerLexema());
        if (infoVariable == null) {
            // Si no encontramos la variable en los parametros del metodo que recorre el sintactico
            // actualmente, la buscamos en las variables locales del metodo
            infoVariable = metodoPadre.obtenerVariablePorNombre(tokenActual.obtenerLexema());
            if (infoVariable == null) {
                // Si no encontramos la variable en el metodo que recorre el sintactico
                // actualmente, la buscamos en la clase
                infoVariable = claseMadre.obtenerAtributoPorNombre(tokenActual.obtenerLexema());
                if (infoVariable == null) {
                    // La variable debe ser asignada luego de consolidar la tabla de simbolos y por
                    // tanto debe
                    // ser encontrada luego de realizar la resolucion de nombres
                    return new Variable(tokenActual.obtenerLexema(), null);
                }
            }
        }
        return infoVariable;
    }
}
