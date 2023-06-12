package Semantico;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import Semantico.Funcion.Constructor;
import Semantico.Funcion.Metodo;
import Semantico.Nodo.NodoAST;
import Semantico.Tipo.TipoReferencia;
import Semantico.Variable.Atributo;
import Semantico.Variable.Parametro;
import Semantico.Variable.Variable;
import Sintactico.Sintactico;

public class Semantico {
    private TablaDeSimbolos tablaDeSimbolos;
    private NodoAST AST;

    public Semantico(File archivo) {

        // Obtenemos la ruta del archivo sin la extension
        String ruta = archivo.getAbsolutePath();
        ruta = ruta.substring(0,ruta.length()-3);

        this.tablaDeSimbolos = new TablaDeSimbolos();
        this.AST = new NodoAST(null, null);
        this.AST.establecerTablaDeSimbolos(tablaDeSimbolos);
        // Corroboramos la validez sintactica del codigo y rellenamos la tabla de
        // simbolos
        Sintactico sintacticoTS = new Sintactico(archivo, this.tablaDeSimbolos, this.AST);
        tablaDeSimbolos = sintacticoTS.tablaDeSimbolos;
        // Consolidamos la tabla de simbolos con checkeos extra
        consolidarTS();
        // Realizamos el checkeo de tipos sobre el AST
        this.AST.checkeoTipos();
        // Creamos el json de la Tabla de Simbolos
        try (FileWriter escritorTDS = new FileWriter(ruta + ".ts.json")) {
            escritorTDS.write(this.tablaDeSimbolos.toJson(ruta + ".ts.json"));
        } catch (IOException e) {
            System.out.println("Error al intentar escribir el json de la TDS");
        }
        // Creamos el json del AST
        try (FileWriter escritorAST = new FileWriter(ruta + ".ast.json")) {
            escritorAST.write(this.AST.toJson(ruta + ".ast.json"));
        } catch (IOException e) {
            System.out.println("Error al intentar escribir el json del AST");
        }
    }

    public TablaDeSimbolos obtenerTDS() {
        return this.tablaDeSimbolos;
    }

    public NodoAST obtenerAST() {
        return this.AST;
    }

    private void consolidarTS() {
        chequeoTipos();
        establecerClases();
        herenciaCircular();
    }

    // Este método chequea que los tipos de los atributos, parámetros y variables
    // declaradas existan
    private void chequeoTipos() {
        for (HashMap.Entry<String, Clase> clase : tablaDeSimbolos.obtenerClases().entrySet()) {
            HashMap<String, Atributo> atributos = clase.getValue().obtenerAtributos();
            for (HashMap.Entry<String, Atributo> atributo : atributos.entrySet()) {
                Atributo atributoActual = atributo.getValue();
                if (atributoActual.obtenerTipo() instanceof TipoReferencia) {
                    if (tablaDeSimbolos.obtenerClasePorNombre(atributoActual.obtenerTipo().obtenerTipo()) == null) {
                        new ErrorSemantico(atributoActual.obtenerFila(), atributoActual.obtenerColumna(),
                                "El tipo del atributo " +
                                        atributoActual.obtenerNombre() + " no está declarado.");
                    }
                }
            }
            if (clase.getValue().tieneConstructor()) {
                Constructor constructor = clase.getValue().obtenerConstructor();
                HashMap<String, Parametro> parametros = constructor.obtenerParametros();
                for (HashMap.Entry<String, Parametro> parametro : parametros.entrySet()) {
                    Parametro parametroActual = parametro.getValue();
                    if (parametroActual.obtenerTipo() instanceof TipoReferencia) {
                        if (tablaDeSimbolos
                                .obtenerClasePorNombre(parametroActual.obtenerTipo().obtenerTipo()) == null) {
                            new ErrorSemantico(parametroActual.obtenerFila(), parametroActual.obtenerColumna(),
                                    "El tipo del parámetro " +
                                            parametroActual.obtenerNombre() + " no está declarado.");
                        }
                    }
                }
                HashMap<String, Variable> variables = constructor.obtenerVariables();
                for (HashMap.Entry<String, Variable> variable : variables.entrySet()) {
                    Variable variableActual = variable.getValue();
                    if (variableActual.obtenerTipo() instanceof TipoReferencia) {
                        if (tablaDeSimbolos.obtenerClasePorNombre(variableActual.obtenerTipo().obtenerTipo()) == null) {
                            new ErrorSemantico(variableActual.obtenerFila(), variableActual.obtenerColumna(),
                                    "El tipo de la variable " +
                                            variableActual.obtenerNombre() + " no está declarado.");
                        }
                    }
                }
            }
            HashMap<String, Metodo> metodos = clase.getValue().obtenerMetodos();
            for (HashMap.Entry<String, Metodo> metodo : metodos.entrySet()) {
                Metodo metodoActual = metodo.getValue();
                if (metodoActual.obtenerTipoRetorno() instanceof TipoReferencia) {
                    if (tablaDeSimbolos
                            .obtenerClasePorNombre(metodoActual.obtenerTipoRetorno().obtenerTipo()) == null) {
                        new ErrorSemantico(metodoActual.obtenerFila(), metodoActual.obtenerColumna(),
                                "El tipo de retorno del método " +
                                        metodoActual.obtenerNombre() + " no está declarado.");
                    }
                }
                HashMap<String, Parametro> parametros = metodoActual.obtenerParametros();
                for (HashMap.Entry<String, Parametro> parametro : parametros.entrySet()) {
                    Parametro parametroActual = parametro.getValue();
                    if (parametroActual.obtenerTipo() instanceof TipoReferencia) {
                        if (tablaDeSimbolos
                                .obtenerClasePorNombre(parametroActual.obtenerTipo().obtenerTipo()) == null) {
                            new ErrorSemantico(parametroActual.obtenerFila(), parametroActual.obtenerColumna(),
                                    "El tipo del parámetro " +
                                            parametroActual.obtenerNombre() + " no está declarado.");
                        }
                    }
                }
                HashMap<String, Variable> variables = metodoActual.obtenerVariables();
                for (HashMap.Entry<String, Variable> variable : variables.entrySet()) {
                    Variable variableActual = variable.getValue();
                    if (variableActual.obtenerTipo() instanceof TipoReferencia) {
                        if (tablaDeSimbolos.obtenerClasePorNombre(variableActual.obtenerTipo().obtenerTipo()) == null) {
                            new ErrorSemantico(variableActual.obtenerFila(), variableActual.obtenerColumna(),
                                    "El tipo de la variable " +
                                            variableActual.obtenerNombre() + " no está declarado.");
                        }
                    }
                }
            }
        }
    }

    // Este metodo lee el nombre de las clases que heredan de otra e
    // inserta los atributos y metodos de la superclase en la subclase
    private void establecerClases() {
        for (HashMap.Entry<String, Clase> clase : tablaDeSimbolos.obtenerClases().entrySet()) {
            Clase superclase = tablaDeSimbolos.obtenerClasePorNombre(clase.getValue().obtenerHerencia());
            clase.getValue().heredarAtributosMetodos(superclase);
            herenciaCircularClase(clase.getValue());
        }
    }

    // Para cada clase de la tabla de simbolos verificar que no tenga herencia
    // circular
    private void herenciaCircular() {
        for (HashMap.Entry<String, Clase> clase : tablaDeSimbolos.obtenerClases().entrySet()) {
            herenciaCircularClase(clase.getValue());
        }
    }

    // Revisar herencia circular para una clase
    private void herenciaCircularClase(Clase clase) {
        Clase claseActual = clase;
        HashMap<String, Clase> clasesRevisadas = new HashMap<String, Clase>();
        while (claseActual.obtenerNombre() != "Object") {
            if (clasesRevisadas.get(claseActual.obtenerHerencia()) != null) {
                new ErrorSemantico(claseActual.obtenerFila(), claseActual.obtenerColumna(),
                        "Herencia circular encontrada para la clase " + claseActual.obtenerNombre() +
                                " que hereda de " + claseActual.obtenerHerencia());
            } else {
                // Insertamos la clase como ya revisada
                clasesRevisadas.put(clase.obtenerNombre(), claseActual);
                // Revisamos la siguiente clase
                String claseAnterior = claseActual.obtenerNombre();
                claseActual = tablaDeSimbolos.obtenerClasePorNombre(claseActual.obtenerHerencia());
                // Si la clase actual es null significa que la clase anterior hereda de otra
                // clase inexistente
                if (claseActual == null) {
                    claseActual = tablaDeSimbolos.obtenerClasePorNombre(claseAnterior);
                    new ErrorSemantico(claseActual.obtenerFila(), claseActual.obtenerColumna(),
                            "La clase " + claseActual.obtenerNombre() +
                                    " hereda de una clase no declarada.");
                }
            }
        }
    }
}
