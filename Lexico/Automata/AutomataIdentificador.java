package Lexico.Automata;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import Lexico.ErrorLexico;
import Lexico.Token;

/* Esta clase se encarga de recorrer el codigo en busqueda de un identificador valido
 * asi como tambien reconocer identificadores no validos y elevar la excepcion correspondiente
 */
public class AutomataIdentificador extends Automata {
  public AutomataIdentificador(int filaActual, int columnaActual) {
    super(filaActual, columnaActual);
  }

  private final HashMap<String, String> pReservadas = new HashMap<String, String>() {
    {
      put("class", "p_class");
      put("if", "p_if");
      put("else", "p_else");
      put("while", "p_while");
      put("true", "p_true");
      put("false", "p_false");
      put("new", "p_new");
      put("fn", "p_fn");
      put("main", "p_main");
      put("create", "p_create");
      put("pub", "p_pub");
      put("static", "p_static");
      put("return", "p_return");
      put("self", "p_self");
      put("void", "p_void");
      put("Array", "p_Array");
      put("I32", "p_Int");
      put("Bool", "p_Bool");
      put("Char", "p_Char");
      put("Str", "p_String");
      put("nil", "p_nil");
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
    token.establecerFila(super.obtenerFila());
    token.establecerColumna(super.obtenerColumna());
    // Leemos hasta encontrar EOF o el fin del token
    try {
      int c;
      boolean leyendo = true;
      while (leyendo) {
        // Marcamos la posicion antes de consumir el caracter en caso de querer
        // recuperarlo
        lector.mark(1);
        c = lector.read();
        char character = (char) c;
        // Aumentamos en 1 la columna
        super.establecerColumna(super.obtenerColumna() + 1);

        // Si encontramos un caracter que corresponda a una letra,
        // un numero o un _, se trata de un identificador valido
        // y continuamos construyendo el lexema
        if ((c > 64 && c < 91) || (c == 95) ||(c > 96 && c < 123) || (c > 47 && c < 58)) {
          lexema = lexema + character;
        }
        else {
          // Si sigue un simbolo invalido
          if ((c == 35) || (c == 36) || (c == 63) || (c == 64) || (c == 92) ||(c == 94) || (c == 96) || (c == 126)) {
            // El caracter no es valido, devolvemos error
            ErrorLexico err = new ErrorLexico(token.obtenerFila(), token.obtenerColumna(),
                "Identificador mal formado: caracter " + character + " invalido");
          }
          else{
            if ( c == 10 || c == 11 | c == 13 | c == -1) {
              leyendo = false;
            }
            else {
              // No queremos consumir caracteres de mas, por tanto volvemos a la marca del
              // lector
              lector.reset();
              super.establecerColumna(super.obtenerColumna() - 1);
              leyendo = false;
            }
          }
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
    // Revisamos si el lexema es una palabra reservada o un id de clase u objeto
    if (pReservadas.get(lexema) != null) {
      // Reasignamos el tipo de token
      token.establecerToken(pReservadas.get(lexema));
    }
    else {
        if (Character.isUpperCase(lexema.charAt(0))) {
          token.establecerToken("id_clase");
        }
        else {
          token.establecerToken("id_objeto");
        }
    }
    token.establecerLexema(lexema);
    return token;
  }

}