package Lexico.Automata;

import java.io.BufferedReader;
import java.io.IOException;

import Lexico.ErrorLexico;
import Lexico.Token;

/* Esta clase se encarga de recorrer el codigo en busqueda de un operador valido
 * asi como tambien reconocer operadores no validos y elevar la excepcion correspondiente
 */
public class AutomataOperador extends Automata {
  public AutomataOperador(int filaActual, int columnaActual) {
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

      if (c == 40) {
        lexema = lexema + character;
        token.establecerToken("op_par_abre");
      }
      else if (c == 41) {
        lexema = lexema + character;
        token.establecerToken("op_par_cierra");
      }
      else if (c == 91) {
        lexema = lexema + character;
        token.establecerToken("op_cor_abre");
      }
      else if (c == 93) {
        lexema = lexema + character;
        token.establecerToken("op_cor_cierra");
      }
      else if (c == 123) {
        lexema = lexema + character;
        token.establecerToken("op_llave_abre");
      }
      else if (c == 125) {
        lexema = lexema + character;
        token.establecerToken("op_llave_cierra");
      }
      else if (c == 59) {
        lexema = lexema + character;
        token.establecerToken("op_punto_coma");
      }
      else if (c == 44) {
        lexema = lexema + character;
        token.establecerToken("op_coma");
      }
      else if (c == 58) {
        lexema = lexema + character;
        token.establecerToken("op_dos_puntos");
      }
      else if (c == 46) {
        lexema = lexema + character;
        token.establecerToken("op_punto");
      }
      else if (c == 61) {
        lexema = lexema + character;
        lector.mark(1);
        c = lector.read();
        if (c == 61) {
          character = (char) c;
          // Leemos un caracter y aumentamos en uno la columna
          super.establecerColumna(super.obtenerColumna() + 1);
          lexema = lexema + character;
          token.establecerToken("op_igual");
        } else {
          token.establecerToken("op_asignacion");
          // No consumimos el siguiente caracter
          lector.reset();
        }
      }
      else if (c == 60) {
        lexema = lexema + character;
        lector.mark(1);
        c = lector.read();
        if (c == 61) {
          character = (char) c;
          // Leemos un caracter y aumentamos en uno la columna
          super.establecerColumna(super.obtenerColumna() + 1);
          lexema = lexema + character;
          token.establecerToken("op_menor_o_igual");
        } else {
          token.establecerToken("op_menor");
          // No consumimos el siguiente caracter
          lector.reset();
        }
      }
      else if (c == 62) {
        lexema = lexema + character;
        lector.mark(1);
        c = lector.read();
        if (c == 61) {
          character = (char) c;
          // Leemos un caracter y aumentamos en uno la columna
          super.establecerColumna(super.obtenerColumna() + 1);
          lexema = lexema + character;
          token.establecerToken("op_mayor_o_igual");
        } else {
          token.establecerToken("op_mayor");
          // No consumimos el siguiente caracter
          lector.reset();
        }
      }
      else if (c == 43) {
        lexema = lexema + character;
        token.establecerToken("op_suma");
      }
      else if (c == 45) {
        lexema = lexema + character;
        lector.mark(1);
        c = lector.read();
        if (c == 62) {
          character = (char) c;
          // Leemos un caracter y aumentamos en uno la columna
          super.establecerColumna(super.obtenerColumna() + 1);
          lexema = lexema + character;
          token.establecerToken("op_flecha");
        } else {
          token.establecerToken("op_resta");
          // No consumimos el siguiente caracter
          lector.reset();
        }
      }
      else if (c == 42) {
        lexema = lexema + character;
        token.establecerToken("op_mult");
      }
      else if (c == 47) {
        lexema = lexema + character;
        token.establecerToken("op_div");
      }
      else if (c == 37) {
        lexema = lexema + character;
        token.establecerToken("op_mod");
      }
      else if (c == 38) {
        lexema = lexema + character;
        lector.mark(1);
        c = lector.read();
        if (c == 38) {
          character = (char) c;
          // Leemos un caracter y aumentamos en uno la columna
          super.establecerColumna(super.obtenerColumna() + 1);
          lexema = lexema + character;
          token.establecerToken("op_and");
        } else {
          // Error operador mal formado
          ErrorLexico err = new ErrorLexico(token.obtenerFila(),token.obtenerColumna(),"Operador mal formado: "+lexema+(char) c);
        }
      }
      else if (c == 124) {
        lexema = lexema + character;
        lector.mark(1);
        c = lector.read();
        if (c == 124) {
          character = (char) c;
          // Leemos un caracter y aumentamos en uno la columna
          super.establecerColumna(super.obtenerColumna() + 1);
          lexema = lexema + character;
          token.establecerToken("op_or");
        } else {
          // Error operador mal formado
          ErrorLexico err = new ErrorLexico(token.obtenerFila(),token.obtenerColumna(),"Operador mal formado: "+lexema+(char) c);
        }
      }
      else if (c == 33) {
        lexema = lexema + character;
        lector.mark(1);
        c = lector.read();
        if (c == 61) {
          character = (char) c;
          // Leemos un caracter y aumentamos en uno la columna
          super.establecerColumna(super.obtenerColumna() + 1);
          lexema = lexema + character;
          token.establecerToken("op_distinto");
        } else {
          token.establecerToken("op_not");
          // No consumimos el siguiente caracter
          lector.reset();
        }
      } 
      // No encontramos ningun caso valido
      if(lexema == ""){
        // error simbolo invalido
        ErrorLexico err = new ErrorLexico(token.obtenerFila(), token.obtenerColumna(),
            "Simbolo invalido: " + character);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    token.establecerLexema(lexema);
    return token;
  }

}
