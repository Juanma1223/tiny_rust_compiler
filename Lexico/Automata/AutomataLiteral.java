package Lexico.Automata;

import java.io.BufferedReader;
import java.io.IOException;

import Lexico.ErrorLexico;
import Lexico.Token;

/* Esta clase se encarga de recorrer el codigo en busqueda de un literal valido
 * asi como tambien reconocer literales no validos y elevar la excepcion correspondiente
 */
public class AutomataLiteral extends Automata {
    public AutomataLiteral(int filaActual, int columnaActual) {
        super(filaActual, columnaActual);
    }

    @Override
    public Token reconocerToken(BufferedReader lector, boolean sinConsumir) {
        Token token = new Token();
        String lexema = "";
        token.establecerFila(super.obtenerFila());
        token.establecerColumna(super.obtenerColumna());

        try {
            int c;
            c = lector.read();
            char character = (char) c;
            // Leemos un caracter y aumentamos en uno la columna
            super.establecerColumna(super.obtenerColumna() + 1);

            if (c == 48) {
                token.establecerToken("lit_ent");
                lexema = lexema + character;
            }

            if (c > 48 && c < 58) {
                token.establecerToken("lit_ent");
                lexema = lexema + character;
                lector.mark(1);
                c = lector.read();
                // Leemos un caracter y aumentamos en uno la columna
                super.establecerColumna(super.obtenerColumna() + 1);
                while (c > 47 && c < 58) { // mientras c sea un numero
                    character = (char) c;
                    lexema = lexema + character;
                    lector.mark(1);
                    c = lector.read();
                    // Leemos un caracter y aumentamos en uno la columna
                    super.establecerColumna(super.obtenerColumna() + 1);
                }
                if ((c > 64 && c < 91) || (c > 96 && c < 123)){ //si sigue una letra
                    // error número inválido
                    ErrorLexico err = new ErrorLexico(super.obtenerFila(),super.obtenerColumna(),"Numero invalido: "+character);
                }
                else {
                    // No consumimos el caracter y retornamos el token
                    lector.reset();
                    // Como leimos un caracter y no lo consumimos, disminuimos la columna
                    super.establecerColumna(super.obtenerColumna() - 1);
                }
            }

            if (c == 39) {
                token.establecerToken("lit_car");
                c = lector.read();
                // Leemos un caracter y aumentamos en uno la columna
                super.establecerColumna(super.obtenerColumna() + 1);
                if (c != 92 && c != 10 && c != 39) { // si c no es la barra invertida, un salto de linea o una comilla
                                                     // simple
                    character = (char) c;
                    lexema = lexema + character;
                    c = lector.read();
                    // Leemos un caracter y aumentamos en uno la columna
                    super.establecerColumna(super.obtenerColumna() + 1);
                    if (c != 39) {
                        // error caracter sin cerrar
                        ErrorLexico err = new ErrorLexico(super.obtenerFila(),super.obtenerColumna(),"Caracter sin cerrar");
                    }
                } else {
                    if (c == 92) { // c es la barra invertida
                        c = lector.read();
                        // Leemos un caracter y aumentamos en uno la columna
                        super.establecerColumna(super.obtenerColumna() + 1);
                        if (c != 110 && c != 116) { // c es dintinto de n o t
                            character = (char) c;
                            lexema = lexema + character;
                            c = lector.read();
                            // Leemos un caracter y aumentamos en uno la columna
                            super.establecerColumna(super.obtenerColumna() + 1);
                            if (c != 39) {
                                // error caracter sin cerrar
                                ErrorLexico err = new ErrorLexico(super.obtenerFila(),super.obtenerColumna(),"Caracter sin cerrar");
                            }
                        }
                    } else {
                        // caracter invalido
                        ErrorLexico err = new ErrorLexico(super.obtenerFila(),super.obtenerColumna(),"Caracter invalido: "+character);
                    }
                }
            }

            if (c == 34) {
                token.establecerToken("lit_cad");
                c = lector.read();
                // Leemos un caracter y aumentamos en uno la columna
                super.establecerColumna(super.obtenerColumna() + 1);
                while (c != 34) { // mientras c sea distinto de "
                    character = (char) c;
                    lexema = lexema + character;
                    c = lector.read();
                    // Leemos un caracter y aumentamos en uno la columna
                    super.establecerColumna(super.obtenerColumna() + 1);
                    if (c == 10 || c==13) { // si c es un salto de linea o un retorno de carro
                        // error cadena sin cerrar
                        ErrorLexico err = new ErrorLexico(super.obtenerFila(),super.obtenerColumna(),"String sin cerrar");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        token.establecerLexema(lexema);
        return token;
    }
}
