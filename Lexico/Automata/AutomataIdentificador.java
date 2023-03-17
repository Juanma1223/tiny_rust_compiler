package Lexico.Automata;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
      put("class", "class");
      put("if", "if");
      put("else", "else");
      put("while", "while");
      put("true", "true");
      put("false", "false");
      put("new", "new");
      put("fn", "funcion");
      put("create", "create");
      put("pub", "pub");
      put("static", "static");
      put("return", "return");
      put("self", "self");
      put("void", "void");
      put("Array", "Array");
      put("I32", "int");
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

        // Encontramos un espacio,dos puntos, punto y coma, etc y devolvemos el token
        if (c == 32 || c == 58 || c == 59 || (c < 48 && c > 39) || c == 91 || c == 93) {
          // No queremos consumir caracteres de mas, por tanto volvemos a la marca del
          // lector
          lector.reset();
          break;
        }
        // Si encontramos un salto de linea, actualizamos fila, reiniciamos las columnas
        // y pasamos y devolvemos token
        if (c == 10) {
          super.establecerColumna(1);
          super.establecerFila(super.obtenerFila() + 1);
          break;
        }
        // Si encontramos un caracter que corresponda a una letra, o un _
        // se trata de un identificador valido
        // y continuamos construyendo el lexema
        if ((c > 64 && c < 91) || (c > 94 && c < 123) || (c > 47 && c < 58)) {
          lexema = lexema + character;
        } else {
          // Revisamos que no hayamos llegado al EOF
          if (c == -1) {
            ErrorLexico err = new ErrorLexico(super.obtenerFila(), super.obtenerColumna(),
                "Identificador no valido: Se encontro EOF");
          } else {
            // El caracter no es valido, devolvemos error
            ErrorLexico err = new ErrorLexico(super.obtenerFila(), super.obtenerColumna(),
                "Identificador mal formado: caracter " + character + " invalido");
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
    // Revisamos si el lexema es una palabra reservada
    if (pReservadas.get(lexema) != null) {
      // Reasignamos el tipo de token
      token.establecerToken(pReservadas.get(lexema));
    }
    token.establecerLexema(lexema);
    return token;
  }

}
