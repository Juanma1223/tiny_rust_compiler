package Lexico.Automata;

import java.io.BufferedReader;
import java.io.IOException;

import Lexico.Token;

/* Esta clase se encarga de recorrer el codigo en busqueda de un identificador valido
 * asi como tambien reconocer identificadores no validos y elevar la excepcion correspondiente
 */
public class AutomataIdentificador extends Automata{
    public AutomataIdentificador(int filaActual, int columnaActual){
        super(filaActual, columnaActual);
    }

    @Override
    public Token reconocerToken(BufferedReader lector){
        Token token = new Token();
        // Establecemos el tipo de token Identificador
        token.establecerToken("Identificador");
        String lexema = "";
        // Leemos hasta encontrar EOF o el fin del token
        try {
            int c;
            while((c = lector.read()) != -1) {
              char character = (char) c;
              // Si encontramos un caracter que corresponda a una letra, se trata de un identificador valido
              // y continuamos construyendo el lexema
              if((c > 64 && c < 91) || (c > 96 && c < 123)){
                lexema = lexema+character;
              }
              // Encontramos un espacio, devolvemos el token
              if(c == 32){
                break;
              }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        token.establecerLexema(lexema);
        return token;
    }

}
