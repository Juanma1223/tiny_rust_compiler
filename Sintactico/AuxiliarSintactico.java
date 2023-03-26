package Sintactico;

import java.io.IOException;

import Lexico.Lexico;
import Lexico.Token;

public class AuxiliarSintactico{

    Lexico analizadorLexico;

    public AuxiliarSintactico(Lexico analizadorLexico){
        this.analizadorLexico = analizadorLexico;
    }

    public Token tokenActual(){
        try {
            return analizadorLexico.sigToken(true);
        } catch (IOException e) {
            System.out.println("Error inesperado al obtener un Token");
            System.exit(1);
            return new Token();
        }
    }

    // Esta funcion matchea el siguiente Token que devuelve el analizador lexico
    // tenga el lexema enviado por parametro
    public Boolean matcheo(String lexema){
        try {
            Token tokenActual = analizadorLexico.sigToken(false);
            if (tokenActual.obtenerLexema() == lexema){
                return true;
            }else{
                return false;
            }
        } catch (IOException e) {
            System.out.println("Error inesperado al obtener un Token");
            return false;
        }
    }

    // Esta funcion "matchea" un token con un token de tipo id, diferenciando
    // entre id de clase o generico
    public Boolean matcheoId(String tipoId){
        try {
            Token tokenActual = analizadorLexico.sigToken(false);
            if (tokenActual.obtenerToken() == tipoId){
                return true;
            }else{
                return false;
            }
        } catch (IOException e) {
            System.out.println("Error inesperado al obtener un Token");
            return false;
        }
    }

    // Esta funcion se utiliza para verificar que el lexema del token actual 
    // sea el correcto en base a una lista de posibles terminos, lo hace sin consumir dicho token
    public Boolean verifico(String[] terminos){
        try {
            Token tokenActual = analizadorLexico.sigToken(true);
            for(String termino : terminos){
                if (tokenActual.obtenerLexema() == termino){
                    // Solo retorna verdadero si alguno de los terminos es el correcto
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            System.out.println("Error inesperado al obtener un Token");
            return false;
        }
    }

    // Esta funcion se utiliza para verificar que el lexema del token actual 
    // sea el correcto en base a un unico termino, lo hace sin consumir dicho token
    // Este metodo es una sobrecarga
    public Boolean verifico(String termino){
        try {
            Token tokenActual = analizadorLexico.sigToken(true);
                if (tokenActual.obtenerLexema() == termino){
                    // Solo retorna verdadero si alguno de los terminos es el correcto
                    return true;
                }else{
                    return false;
                }
        } catch (IOException e) {
            System.out.println("Error inesperado al obtener un Token");
            return false;
        }
    }
}