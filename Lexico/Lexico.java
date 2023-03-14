package Lexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import Lexico.Automata.Automata;
import Lexico.Automata.AutomataIdentificador;
import Lexico.Automata.AutomataOperador;

/* La clase Lexico esta encargada de leer el archivo de codigo fuente y decidir
 * cual de los automatas reconocera cada token que vaya encontrando en el mismo
 */
public class Lexico {

    // Variable encargada de almacenar todos los tokens que el analizador encuentre
    // en el archivo fuente
    private ArrayList<Token> pilaTokens;
    // Llevamos un conteo de la fila y columna que estamos revisando actualmente en
    // el archivo fuente
    private int filaActual;
    private int columnaActual;

    // Tenemos un unico lector que conserva el estado de lectura en el que estamos
    private BufferedReader lector;

    public Lexico(File file) throws FileNotFoundException {
        lector = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file),
                        Charset.forName("UTF-8")));
    }

    // Este metodo se encarga de leer el archivo fuente e ir derivando los tokens
    // a sus respectivos automatas reconocedores
    public Token sigToken() throws IOException {
        Token token = new Token();
        int c;
        // Leemos hasta encontrar EOF o el inicio de un token
        while ((c = lector.read()) != -1) {
            char character = (char) c;
            // Segun el caracter que encontramos, multiplexamos en los distintos automatas
            // reconocedores
            // de Tokens

            // Si encontramos un caracter que corresponda a una letra, se trata de un
            // identificador
            if ((c > 64 && c < 91) || (c > 96 && c < 123)) {
                Automata automataIdentificador = new AutomataIdentificador(filaActual, columnaActual);
                token = automataIdentificador.reconocerToken(lector);
                
                // Insertamos el caracter consumido para multiplexar
                token.establecerLexema(character + token.obtenerLexema());
                token.establecerFila(filaActual);
                token.establecerColumna(columnaActual);
                
                // Imprimimos el token con su lexema a modo de prueba
                System.out.println("| " + token.obtenerToken() + " | " + token.obtenerLexema() + " |" + " LINEA " + token.obtenerFila() + " (COLUMNA " + token.obtenerColumna() + ") |");

                columnaActual = columnaActual + token.obtenerLexema().length();

                break;
            }

            // Si encontramos un caracter que corresponda a un simbolo, se trata de un
            // operador
            if ((c == 33) || (c > 34 && c < 39) || (c > 39 && c < 48)) {
                Automata automataOperador = new AutomataOperador(filaActual, columnaActual);
                token = automataOperador.reconocerToken(lector);
                
                // Insertamos el caracter consumido para multiplexar
                token.establecerLexema(character + token.obtenerLexema());
                token.establecerFila(filaActual);
                token.establecerColumna(columnaActual);
                
                // Imprimimos el token con su lexema a modo de prueba
                System.out.println("| " + token.obtenerToken() + " | " + token.obtenerLexema() + " |" + " LINEA " + token.obtenerFila() + " (COLUMNA " + token.obtenerColumna() + ") |");

                columnaActual = columnaActual + token.obtenerLexema().length();
            }

            // Implementar el llamado al automata
        }
        return token;
    }
}