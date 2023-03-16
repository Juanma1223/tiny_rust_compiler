package Lexico.Automata;

import java.io.BufferedReader;
import java.io.IOException;

import Lexico.Token;

public class AutomataComentario extends Automata {
    public AutomataComentario(int filaActual, int columnaActual) {
        super(filaActual, columnaActual);
    }

    @Override
    public Token reconocerToken(BufferedReader lector, boolean sinConsumir) {
        // Si no queremos avanzar en la lectura y solo queremos ver el siguiente token
        if (sinConsumir) {
            try {
                // Marcamos la posicion del lector para luego reiniciarlo
                lector.mark(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Token token = new Token();
        // Establecemos el tipo de token Identificador
        token.establecerToken("Identificador");
        String lexema = "";
        // Leemos hasta encontrar EOF o el fin del token
        try {
            int c;
            while ((c = lector.read()) != -1) {
                char character = (char) c;
                // Aumentamos en 1 la columna
                super.establecerColumna(super.obtenerColumna() + 1);
                // Si encontramos un caracter que corresponda a una letra, o un _
                // se trata de un identificador valido
                // y continuamos construyendo el lexema
                if ((c > 64 && c < 91) || (c > 94 && c < 123) || (c > 47 && c < 58)) {
                    lexema = lexema + character;
                }

                // Encontramos un espacio, devolvemos el token
                if (c == 32) {
                    break;
                }
                // Si encontramos un salto de linea, actualizamos fila, reiniciamos las columnas
                // y pasamos y devolvemos token
                if (c == 10) {
                    super.establecerColumna(0);
                    super.establecerFila(super.obtenerFila() + 1);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sinConsumir) {
            // Reseteamos la posicion del lector ya que no queremos consumir el token, solo
            // leerlo
            try {
                lector.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        token.establecerLexema(lexema);
        return token;
    }
}
