package Sintactico;

import java.io.IOException;

import Lexico.Lexico;
import Lexico.Token;

public class AuxiliarSintactico {

    Lexico analizadorLexico;

    Token tokenActual;

    public AuxiliarSintactico(Lexico analizadorLexico) {
        this.analizadorLexico = analizadorLexico;
        // Obtenemos el primer token del archivo
        this.sigToken();
    }

    public void sigToken() {
        try {
            this.tokenActual = analizadorLexico.sigToken(false);
        } catch (IOException e) {
            System.out.println("Error inesperado al obtener un Token");
            System.exit(1);
        }
    }

    // Esta funcion matchea el siguiente Token que devuelve el analizador lexico
    // tenga el lexema enviado por parametro
    public void matcheo(String lexema) {
        if(!lexema.equals(tokenActual.obtenerLexema())){
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba:" + lexema + ", se encontró: " + tokenActual.obtenerLexema());
        }
        // Consume el token
        this.sigToken();
    }

    // Esta funcion "matchea" un token con un token de tipo id, diferenciando
    // entre id de clase o generico
    public void matcheoId(String tipoId) {
        if(!tipoId.equals(tokenActual.obtenerToken())){
            ErrorSintactico error = new ErrorSintactico(tokenActual.obtenerFila(), tokenActual.obtenerColumna(),
                    "Se esperaba:" + tipoId + ", se encontró: " + tokenActual.obtenerToken());
        }
        // Consume el token
        this.sigToken();
    }

    // Esta funcion se utiliza para verificar que el lexema o tipo del token actual
    // sea el correcto en base a una lista de posibles terminos, lo hace sin
    // consumir dicho token
    public Boolean verifico(String[] terminos) {
        for (String termino : terminos) {
            if (termino.equals(tokenActual.obtenerLexema()) || (termino.equals(tokenActual.obtenerToken()))) {
                // Solo retorna verdadero si alguno de los terminos es el correcto
                return true;
            }
        }
        return false;
    }

    // Esta funcion se utiliza para verificar que el lexema o tipo del token actual
    // sea el correcto en base a un unico termino, lo hace sin consumir dicho token
    // Este metodo es una sobrecarga
    public Boolean verifico(String termino) {
        if ((termino.equals(tokenActual.obtenerLexema())) || (termino.equals(tokenActual.obtenerToken()))) {
            // Solo retorna verdadero si alguno de los terminos es el correcto
            return true;
        } else {
            return false;
        }
    }
}