package Lexico;

import java.io.File;
import java.io.IOException;

public class Ejecutador {
    public static void main(String[] args) throws IOException {
        // Abrimos el archivo y almacenamos su informacion
        File file = new File("test/test.rs");
        Lexico anaLexico = new Lexico(file);
        while (file.canRead()){
            anaLexico.sigToken();
        }
    }
}
