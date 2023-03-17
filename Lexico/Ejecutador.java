package Lexico;

import java.io.File;
import java.io.IOException;

public class Ejecutador {
    public static void main(String[] args) throws IOException {
        // Abrimos el archivo y almacenamos su informacion
        File file = new File("test.txt");
        Lexico anaLexico = new Lexico(file);
       while(true) {
            Token token = anaLexico.sigToken(false);
            // Imprimimos el token con su lexema y el numero de linea y columna donde se
            // encuentra
            // Imprimimos el token con su lexema y el numero de linea y columna donde se
            // encuentra
            if (token.obtenerToken() == null){
                break;
            }
            if(token.obtenerToken() != "Comentario"){
                System.out.println("| " + token.obtenerToken() + " | " + token.obtenerLexema() + " |" + " LINEA "
                        + token.obtenerFila() + " (COLUMNA " + token.obtenerColumna() + ") |");
            }
        }
    }
}
