package Sintactico;

import java.io.File;

import Semantico.TablaDeSimbolos;
import Semantico.Nodo.NodoAST;

public class EjecutadorSintactico {
    public static void main(String[] args) {

        // Leemos el argumento que es pasado por stdin
        if (args.length < 1) {
            // No recibimos nombre de archivo y por tanto no podemos proceder
            new ErrorSintactico(0, 0, "No se ingreso archivo de codigo fuente!");
        }

        // Abrimos el archivo y almacenamos su informacion
        File archivo = new File(args[0]);
        // Instanciamos el Analizador Sintactico
        // Si bien en esta etapa no nos interesa la TDS y el AST, debemos pasarlos por parametro para que el analizador funcione
        Sintactico anaSintactico = new Sintactico(archivo,new TablaDeSimbolos(),new NodoAST(null, null));

        System.out.println("CORRECTO: ANALISIS SINTACTICO");
    }
}
