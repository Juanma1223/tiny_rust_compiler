package Semantico;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import Sintactico.Sintactico;

public class Semantico {
    private TablaDeSimbolos tablaDeSimbolos;

    public Semantico(File archivo) {
        this.tablaDeSimbolos = new TablaDeSimbolos();
        // Corroboramos la validez sintactica del codigo y rellenamos la tabla de
        // simbolos
        Sintactico sintacticoTS = new Sintactico(archivo, this.tablaDeSimbolos);
        tablaDeSimbolos = sintacticoTS.tablaDeSimbolos;
        // Consolidamos la tabla de simbolos con checkeos extra
        consolidarTS();
        try (FileWriter escritor = new FileWriter(archivo+".json")) {
            escritor.write(this.tablaDeSimbolos.toJson(archivo.getName()));
        } catch (IOException e) {
            System.out.println("Error al intentar escribir el json");
        }
    }

    public TablaDeSimbolos obtenerTDS() {
        return this.tablaDeSimbolos;
    }

    private void consolidarTS() {
        herenciaCircular();
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
                new ErrorSemantico(claseActual.obtenerFila(), claseActual.obtenerColumna(), "Herencia circular encontrada para la clase " + claseActual.obtenerNombre() +
                        " que hereda de " + claseActual.obtenerHerencia());
            }else{
                // Insertamos la clase como ya revisada
                clasesRevisadas.put(clase.obtenerNombre(), claseActual);
                // Revisamos la siguiente clase
                claseActual = tablaDeSimbolos.obtenerClasePorNombre(claseActual.obtenerHerencia());
            }
        }
    }
}
