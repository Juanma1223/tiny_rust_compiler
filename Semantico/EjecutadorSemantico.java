package Semantico;

import java.io.File;

public class EjecutadorSemantico {
    public static void main(String[] args) {

        // Leemos el argumento que es pasado por stdin
        if (args.length < 1) {
            // No recibimos nombre de archivo y por tanto no podemos proceder
            new ErrorSemantico(0, 0, "No se ingreso archivo de codigo fuente!",true);
        }

        // Abrimos el archivo y almacenamos su informacion
        File archivo = new File(args[0]);
        // Instanciamos el Analizador Semantico
        new Semantico(archivo);

        System.out.println("CORRECTO: ANALISIS SEMANTICO");
    }
}
