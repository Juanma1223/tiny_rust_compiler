package Lexico.Automata;

import java.io.BufferedReader;
import java.io.IOException;

import Lexico.Token;

/* Esta clase se encarga de recorrer el codigo en busqueda de un literal valido
 * asi como tambien reconocer literales no validos y elevar la excepcion correspondiente
 */
public class AutomataLiteral extends Automata{
    public AutomataLiteral(int filaActual, int columnaActual){
        super(filaActual, columnaActual);
    }

    @Override
    public Token reconocerToken(BufferedReader lector,boolean sinConsumir){
        Token token = new Token();
        String lexema = "";
        try {
            int c;
            c = lector.read();
            char character = (char) c;

            if(c == 48){
                token.establecerToken("lit_ent");
                lexema = lexema+character;
            }

            if(c > 48 && c < 58){
                token.establecerToken("lit_ent");
                lexema = lexema+character;
                c = lector.read();
                while(c != 32){ //mientras c sea distinto de espacio
                    if(c > 47 && c < 58){
                        character = (char) c;
                        lexema = lexema+character;
                        c = lector.read();
                    } else{
                        //error número inválido
                    }
                }
            }

            if(c == 39){
                token.establecerToken("lit_car");
                c = lector.read();
                if(c != 92 && c != 10 && c != 39){ //si c no es la barra invertida, un salto de linea o una comilla simple
                    character = (char) c;
                    lexema = lexema+character;
                    c = lector.read();
                    if(c != 39){
                        //error caracter sin cerrar
                    }
                } else{
                    if(c == 92){ //c es la barra invertida
                        c = lector.read();
                        if(c != 110 && c != 116){ //c es dintinto de n o t
                            character = (char) c;
                            lexema = lexema+character;
                            c = lector.read();
                            if(c != 39){
                            //error caracter sin cerrar
                            }
                        }
                    } else{
                        //caracter invalido
                    }
                }
            }

            if(c == 34){
                token.establecerToken("lit_cad");
                c = lector.read();
                while(c != 34){ //mientras c sea distinto de "
                    character = (char) c;
                    lexema = lexema+character;
                    c = lector.read();
                    if(c == 10){ //si c es un salto de linea
                        //error cadena sin cerrar
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
