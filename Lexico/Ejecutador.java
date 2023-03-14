package Lexico;

import java.io.File;
import java.io.IOException;

public class Ejecutador {
    public static void main(String[] args) throws IOException {
        // Abrimos el archivo y almacenamos su informacion
        File file = new File("/home/juanma/Facultad/tiny_rust_compiler/test.txt");
        Lexico anaLexico = new Lexico(file);
        anaLexico.sigToken();
        anaLexico.sigToken();
        anaLexico.sigToken();
    }
}
