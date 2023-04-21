package Semantico;

import java.util.HashMap;

import Semantico.Funcion.Funcion;
import Semantico.Funcion.Metodo;
import Semantico.Tipo.Tipo;
import Semantico.Tipo.TipoArreglo;
import Semantico.Tipo.TipoPrimitivo;
import Semantico.Variable.Parametro;

public class TablaDeSimbolos {
    private Clase claseActual;
    private Funcion metodoActual;
    private HashMap<String, Clase> clases = new HashMap<String, Clase>();

    Tipo tStr = new TipoPrimitivo("Str");
    Tipo tI32 = new TipoPrimitivo("I32");
    Tipo tBool = new TipoPrimitivo("Bool");
    Tipo tChar = new TipoPrimitivo("Char");
    Tipo tArray = new TipoArreglo("Array");

    public TablaDeSimbolos(){
        insertarClaseObject();
        insertarClaseIO();
        insertarClasesPrimitivas();
    }

    public Clase obtenerClaseActual(){
        return this.claseActual;
    }

    public void establecerClaseActual(Clase claseActual){
        this.claseActual = claseActual;
    }

    public Funcion obtenerMetodoActual(){
        return this.metodoActual;
    }

    public void establecerMetodoActual(Funcion metodoActual){
        this.metodoActual = metodoActual;
    }

    public Clase obtenerClasePorNombre(String nombreClase){
        return clases.get("nombreClase");
    }

    public void insertarClase(Clase nuevaClase){
        this.clases.put(nuevaClase.obtenerNombre(), nuevaClase);
    }

    public void insertarClaseObject(){
        Clase cObject = new Clase("Object");
        cObject.establecerHerencia(null);
        this.clases.put("Object", cObject);
    }

    public void insertarClaseIO(){
        Clase cIO = new Clase("IO");
        cIO.establecerHerencia("Object");

        cIO.insertarMetodo(new Metodo("out_string", true)); //Agregar tipo retorno
        cIO.obtenerMetodoPorNombre("out_string").insertarParametro(new Parametro("s", tStr));

        cIO.insertarMetodo(new Metodo("out_i32", true)); //Agregar tipo retorno
        cIO.obtenerMetodoPorNombre("out_i32").insertarParametro(new Parametro("i", tI32));

        cIO.insertarMetodo(new Metodo("out_bool", true)); //Agregar tipo retorno
        cIO.obtenerMetodoPorNombre("out_bool").insertarParametro(new Parametro("b", tBool));

        cIO.insertarMetodo(new Metodo("out_char", true)); //Agregar tipo retorno
        cIO.obtenerMetodoPorNombre("out_char").insertarParametro(new Parametro("c", tChar));

        cIO.insertarMetodo(new Metodo("out_array", true)); //Agregar tipo retorno
        cIO.obtenerMetodoPorNombre("out_array").insertarParametro(new Parametro("a", tArray));

        cIO.insertarMetodo(new Metodo("in_string", true)); //Agregar tipo retorno

        cIO.insertarMetodo(new Metodo("in_i32", true)); //Agregar tipo retorno

        cIO.insertarMetodo(new Metodo("in_bool", true)); //Agregar tipo retorno

        cIO.insertarMetodo(new Metodo("in_char", true)); //Agregar tipo retorno

        this.clases.put("IO", cIO);
    }

    public void insertarClasesPrimitivas(){
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
        cStr.insertarMetodo(new Metodo("length", true)); //Agregar tipo retorno
        cStr.insertarMetodo(new Metodo("concat", true)); //Agregar tipo retorno
        cStr.obtenerMetodoPorNombre("concat").insertarParametro(new Parametro("s", tStr));
        cStr.insertarMetodo(new Metodo("substr", true)); //Agregar tipo retorno
        cStr.obtenerMetodoPorNombre("substr").insertarParametro(new Parametro("i", tI32));
        cStr.obtenerMetodoPorNombre("substr").insertarParametro(new Parametro("l", tI32));
        this.clases.put("Str", cStr);

        Clase cArray = new Clase("Array");
        cArray.establecerHerencia("Object");
        cArray.insertarMetodo(new Metodo("length", true)); //Agregar tipo retorno
        this.clases.put("Array", cArray);
    }
}
