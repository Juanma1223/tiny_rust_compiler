package Lexico;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EjecutadorLexico {
    public static void main(String[] args) throws IOException {

        // Leemos el argumento que es pasado por stdin
        if (args.length < 1) {
            // No recibimos nombre de archivo y por tanto no podemos proceder
            new ErrorLexico(0, 0, "No se ingreso archivo de codigo fuente!");
        }

        // Abrimos el archivo y almacenamos su informacion
        File archivo = new File(args[0]);
        // Instanciamos el Analizador Lexico
        Lexico anaLexico = new Lexico(archivo);
        // Pila en la que guardamos los tokens para posteriormente imprimirlos por
        // pantalla
        ArrayList<Token> tokens = new ArrayList<Token>();
        boolean leyendo = true;
        // Leemos todos los tokens
        while (leyendo) {
            Token token = anaLexico.sigToken(false);
            // Imprimimos el token con su lexema y el numero de linea y columna donde se
            // encuentra
            if (token.obtenerToken() == null) {
                leyendo = false;
            }
            if (token.obtenerToken() != "Comentario" && leyendo == true) {
                // Insertamos los tokens que no sean comentarios
                tokens.add(token);
            }
        }
        if (args.length == 2) {
            // Creamos el archivo de salida
            try {
                FileWriter escritor = new FileWriter(args[1]);
                escritor.write("CORRECTO: ANALISIS LEXICO\n");
                escritor.write("| TOKEN | LEXEMA | NUMERO DE LINEA (NUMERO DE COLUMNA) |\n");
                for (Token token : tokens) {
                    escritor.write("| " + token.obtenerToken() + " | " + token.obtenerLexema() + " |" + " LINEA "
                            + token.obtenerFila() + " (COLUMNA " + token.obtenerColumna() + ") |");
                    escritor.write("\n");
                }
            } catch (IOException e) {
                System.out.println("Ocurrio un error mientras se escribir al archivo");
                e.printStackTrace();
            }
        } else {
            System.out.println("CORRECTO: ANALISIS LEXICO");
            System.out.println("| TOKEN | LEXEMA | NUMERO DE LINEA (NUMERO DE COLUMNA) |");
            // Imprimimos los tokens
            for (Token token : tokens) {
                System.out.println("| " + token.obtenerToken() + " | " + token.obtenerLexema() + " |" + " LINEA "
                        + token.obtenerFila() + " (COLUMNA " + token.obtenerColumna() + ") |");
            }
        }
    }
}
