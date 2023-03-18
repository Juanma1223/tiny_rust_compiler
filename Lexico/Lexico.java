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
import Lexico.Automata.AutomataComentario;
import Lexico.Automata.AutomataIdentificador;
import Lexico.Automata.AutomataLiteral;
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
    private int filaActual = 1;
    private int columnaActual = 1;

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
    public Token sigToken(boolean sinConsumir) throws IOException {
        Token token = new Token();
        int c;
        boolean leyendo = true;
        // Leemos hasta encontrar EOF o el inicio de un token
        while (leyendo) {
            // Marcamos el lector antes de consumir el caracter para no enviar un caracter
            // menos al automata
            lector.mark(1);
            c = lector.read();
            char character = (char) c;
            // En caso de encontrar espacios o tabulaciones, las ignoramos y no reseteamos
            // el lector
            if (c == -1) {
                // Encontramos EOF en lugar de un token
                leyendo = false;
            }
            if (c != 32 && c != 9 && c != 10 && c != 11 && c != 13) {
                // Restablecemos el lector un caracter hacia atras para no consumirlo
                lector.reset();

                // Segun el caracter que encontramos, multiplexamos en los distintos automatas
                // reconocedores
                // de Tokens

                // Si encontramos un caracter que corresponda a una letra, se trata de un
                // identificador
                if ((c > 64 && c < 91) || (c > 96 && c < 123)) {

                    Automata automataIdentificador = new AutomataIdentificador(filaActual, columnaActual);
                    token = automataIdentificador.reconocerToken(lector, sinConsumir);
                    // Obtenemos la fila y columna en la que termino de leer el automata
                    filaActual = automataIdentificador.obtenerFila();
                    columnaActual = automataIdentificador.obtenerColumna();
                    return token;
                }

                // Si encontramos un caracter que corresponda a un simbolo, se trata de un
                // operador o un simbolo invalido
                if ((c == 33) || (c > 34 && c < 39) || (c > 39 && c < 47)
                        || (c > 57 && c < 65 || (c >= 91 && c < 97 || (c > 122 && c < 127)))) {

                    Automata automataOperador = new AutomataOperador(filaActual, columnaActual);
                    token = automataOperador.reconocerToken(lector, sinConsumir);
                    // Obtenemos la fila y columna en la que termino de leer el automata
                    filaActual = automataOperador.obtenerFila();
                    columnaActual = automataOperador.obtenerColumna();
                    return token;
                }

                // Si encontramos una / puede ser un operador o un comentario
                if (c == 47) {
                    lector.mark(1);
                    c = lector.read();
                    if (c == 47 || c == 42) { // si sigue otra / o un * es un comentario
                        lector.reset();
                        Automata automataComentario = new AutomataComentario(filaActual, columnaActual);
                        token = automataComentario.reconocerToken(lector, sinConsumir);

                        // Obtenemos la fila y columna en la que termino de leer el automata
                        filaActual = automataComentario.obtenerFila();
                        columnaActual = automataComentario.obtenerColumna();

                        return token;
                    } else { // sino es un operador
                        lector.reset();
                        Automata automataOperador = new AutomataOperador(filaActual, columnaActual);
                        token = automataOperador.reconocerToken(lector, sinConsumir);

                        // Obtenemos la fila y columna en la que termino de leer el automata
                        filaActual = automataOperador.obtenerFila();
                        columnaActual = automataOperador.obtenerColumna();

                        return token;
                    }
                }

                // Si encontramos un caracter que corresponda a un numero o comillas se trata de
                // un
                // literal
                if ((c > 47 && c < 58) || (c == 34) || (c == 39)) {

                    Automata automataLiteral = new AutomataLiteral(filaActual, columnaActual);
                    token = automataLiteral.reconocerToken(lector, sinConsumir);

                    // Obtenemos la fila y columna en la que termino de leer el automata
                    filaActual = automataLiteral.obtenerFila();
                    columnaActual = automataLiteral.obtenerColumna();

                    return token;
                }
            } else {
                // Si encontramos una nueva linea debemos actualizar la fila y reiniciar el
                // conteo de columna
                if (c == 10) { //sacamos || c == 11 || c == 13
                    this.filaActual += 1;
                    this.columnaActual = 1;
                } else {
                    // Encontramos alguna tabulacion o espacio y solo aumentamos el numero de
                    // columna
                    this.columnaActual += 1;
                }
            }
        }
        return token;
    }
}