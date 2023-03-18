package Lexico;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Ejecutador {
    public static void main(String[] args) throws IOException {
        // Abrimos el archivo y almacenamos su informacion
        File file = new File("test/test.rs");
        Lexico anaLexico = new Lexico(file);
        // Pila en la que guardamos los tokens para posteriormente imprimirlos por
        // pantalla
        ArrayList<Token> tokens = new ArrayList<Token>();
        // Leemos todos los tokens
        while (true) {
            Token token = anaLexico.sigToken(false);
            // Imprimimos el token con su lexema y el numero de linea y columna donde se
            // encuentra
            if (token.obtenerToken() == null) {
                break;
            }
            if (token.obtenerToken() != "Comentario") {
               // Insertamos los tokens que no sean comentarios
               tokens.add(token);
            }
        }
        System.out.println("CORRECTO: ANALISIS LEXICO");
        System.out.println("| TOKEN | LEXEMA | NUMERO DE LINEA (NUMERO DE COLUMNA) |");
        // Imprimimos los tokens
        for(Token token:tokens){
            System.out.println("| " + token.obtenerToken() + " | " + token.obtenerLexema() + " |" + " LINEA "
            + token.obtenerFila() + " (COLUMNA " + token.obtenerColumna() + ") |");
        }
    }
}
