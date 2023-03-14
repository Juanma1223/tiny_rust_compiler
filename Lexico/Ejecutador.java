package Lexico;

import java.io.File;
import java.io.IOException;

public class Ejecutador {
    public static void main(String[] args) throws IOException {
        // Abrimos el archivo y almacenamos su informacion
        File file = new File("test.txt");
        Lexico anaLexico = new Lexico(file);
        while (file.canRead()){
            Token token = anaLexico.sigToken();
            // Imprimimos el token con su lexema y el numero de linea y columna donde se encuentra
            //System.out.println("| " + token.obtenerToken() + " | " + token.obtenerLexema() + " |" + " LINEA " + token.obtenerFila() + " (COLUMNA " + token.obtenerColumna() + ") |");
        }
    }
}
