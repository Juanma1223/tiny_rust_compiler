package Lexico.Automata;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import Lexico.Token;

/* Esta clase se encarga de recorrer el codigo en busqueda de un identificador valido
 * asi como tambien reconocer identificadores no validos y elevar la excepcion correspondiente
 */
public class AutomataIdentificador extends Automata {
  public AutomataIdentificador(int filaActual, int columnaActual) {
    super(filaActual, columnaActual);
  }

  private final HashMap<String, String> p_reservadas = new HashMap<String, String>() {
    {
      put("class", "class");
      put("if", "if");
      put("else", "else");
      put("while", "while");
      put("true", "true");
      put("false", "false");
      put("new", "new");
      put("fn", "fn");
      put("create", "create");
      put("pub", "pub");
      put("static", "static");
      put("return", "return");
      put("self", "self");
      put("void", "void");
      put("Array", "Array");
      put("Int", "I32");
      put("Bool", "Bool");
      put("Char", "Char");
      put("String", "Str");
      put("nil", "nil");
    }
  };

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
        // Si encontramos un caracter que corresponda a una letra, o un _
        // se trata de un identificador valido
        // y continuamos construyendo el lexema
        if ((c > 64 && c < 91) || (c > 96 && c < 123) || (c > 95)) {
          lexema = lexema + character;
        }
        // Encontramos un espacio, devolvemos el token
        if (c == 32) {
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    if(sinConsumir){
      // Reseteamos la posicion del lector ya que no queremos consumir el token, solo leerlo
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
