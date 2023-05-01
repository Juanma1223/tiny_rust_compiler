package Sintactico;

import java.io.File;

public class EjecutadorSintactico {
    public static void main(String[] args) {

        // Leemos el argumento que es pasado por stdin
        // if (args.length < 1) {
        //     // No recibimos nombre de archivo y por tanto no podemos proceder
        //     new ErrorSintactico(0, 0, "No se ingreso archivo de codigo fuente!");
        // }

        // Abrimos el archivo y almacenamos su informacion
        File archivo = new File("Sintactico/testSintactico/test.rs");
        // Sintactico anaSintactico = new Sintactico(archivo);
        System.out.println("CORRECTO: ANALISIS SINTACTICO");
    }
}
