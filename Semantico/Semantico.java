package Semantico;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import Semantico.Nodo.Nodo;
import Sintactico.Sintactico;

public class Semantico {
    private TablaDeSimbolos tablaDeSimbolos;
    private Nodo AST;

    public Semantico(File archivo) {
        this.tablaDeSimbolos = new TablaDeSimbolos();
        this.AST = new Nodo();
        // Corroboramos la validez sintactica del codigo y rellenamos la tabla de
        // simbolos
        Sintactico sintacticoTS = new Sintactico(archivo, this.tablaDeSimbolos, this.AST);
        tablaDeSimbolos = sintacticoTS.tablaDeSimbolos;
        // Consolidamos la tabla de simbolos con checkeos extra
        consolidarTS();
        try (FileWriter escritor = new FileWriter(archivo + ".json")) {
            escritor.write(this.tablaDeSimbolos.toJson(archivo.getName()));
        } catch (IOException e) {
            System.out.println("Error al intentar escribir el json");
        }
    }

    public TablaDeSimbolos obtenerTDS() {
        return this.tablaDeSimbolos;
    }

    private void consolidarTS() {
        establecerClases();
        herenciaCircular();
    }

    // Este metodo lee el nombre de las clases que heredan de otra e
    // inserta los atributos y metodos de la superclase en la subclase
    private void establecerClases(){
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
                // Si la clase actual es null significa que la clase anterior hereda de otra clase inexistente
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
