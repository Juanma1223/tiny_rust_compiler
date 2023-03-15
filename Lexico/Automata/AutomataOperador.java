package Lexico.Automata;

import java.io.BufferedReader;
import java.io.IOException;

import Lexico.Token;

/* Esta clase se encarga de recorrer el codigo en busqueda de un operador valido
 * asi como tambien reconocer operadores no validos y elevar la excepcion correspondiente
 */
public class AutomataOperador extends Automata{
    public AutomataOperador(int filaActual, int columnaActual){
        super(filaActual, columnaActual);
    }

    @Override
    public Token reconocerToken(BufferedReader lector,boolean sinConsumir){
        Token token = new Token();
        String lexema = "";
        // Leemos hasta encontrar EOF o el fin del token
        try {
            int c;
            while((c = lector.read()) != -1) {
              char character = (char) c;

              if(c == 40){
                lexema = lexema+character;
                token.establecerToken("op_par_abre");
              }
              if(c == 41){
                lexema = lexema+character;
                token.establecerToken("op_par_cierra");
              }
              if(c == 91){
                lexema = lexema+character;
                token.establecerToken("op_cor_abre");
              }
              if(c == 93){
                lexema = lexema+character;
                token.establecerToken("op_cor_cierra");
              }
              if(c == 123){
                lexema = lexema+character;
                token.establecerToken("op_llave_abre");
              }
              if(c == 125){
                lexema = lexema+character;
                token.establecerToken("op_llave_cierra");
              }
              if(c == 59){
                lexema = lexema+character;
                token.establecerToken("op_punto_coma");
              }
              if(c == 44){
                lexema = lexema+character;
                token.establecerToken("op_coma");
              }
              if(c == 58){
                lexema = lexema+character;
                token.establecerToken("op_dos_puntos");
              }
              if(c == 46){
                lexema = lexema+character;
                token.establecerToken("op_punto");
              }
              //Agregar igual
              if(c == 61){
                lexema = lexema+character;
                token.establecerToken("op_asignacion");
              }
              //Agregar menor o igual
              if(c == 60){
                lexema = lexema+character;
                token.establecerToken("op_menor");
              }
              //Agregar mayor o igual
              if(c == 62){
                lexema = lexema+character;
                token.establecerToken("op_mayor");
              }
              if(c == 43){
                lexema = lexema+character;
                token.establecerToken("op_suma");
              }
              //Agregar op-flecha
              if(c == 45){
                lexema = lexema+character;
                token.establecerToken("op_resta");
              }
              if(c == 42){
                lexema = lexema+character;
                token.establecerToken("op_mult");
              }
              if(c == 47){
                lexema = lexema+character;
                token.establecerToken("op_div");
              }
              if(c == 37){
                lexema = lexema+character;
                token.establecerToken("op_mod");
              }
              //Agregar otro &
              if(c == 38){
                lexema = lexema+character;
                token.establecerToken("op_and");
              }
              //Agregar otro |
              if(c == 124){
                lexema = lexema+character;
                token.establecerToken("op_or");
              }
              //Agregar distinto
              if(c == 33){
                lexema = lexema+character;
                token.establecerToken("op_not");
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
