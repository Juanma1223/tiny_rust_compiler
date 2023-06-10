import java.io.File;

import GeneracionCodigo.GeneradorCodigo;

public class Ejecutador {
    public static void main(String[] args) {

        // Leemos el argumento que es pasado por stdin
        //if (args.length < 1) {
        // // No recibimos nombre de archivo y por tanto no podemos proceder
            // new ErrorSemantico(0, 0, "No se ingreso archivo de codigo fuente!",true);
        //}

        // Abrimos el archivo y almacenamos su informacion
        // File archivo = new File(args[0]);

        //File archivo = new File("/mnt/hdd/Facultad/Facultad/4to_año/Compiladores/Compilador/tiny_rust_compiler/GeneracionCodigo/test/test_prueba.rs");
        File archivo = new File("/C:/Users/marie/Documents/Compiladores/tiny_rust_compiler/GeneracionCodigo/test/test_c6.rs");
        new GeneradorCodigo(archivo);
        System.out.println("CORRECTO");
    }
}
