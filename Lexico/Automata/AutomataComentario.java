package Lexico.Automata;

import java.io.BufferedReader;
import java.io.IOException;

import Lexico.ErrorLexico;
import Lexico.Token;

public class AutomataComentario extends Automata {
    public AutomataComentario(int filaActual, int columnaActual) {
        super(filaActual, columnaActual);
    }

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
        token.establecerToken("Comentario");
        String lexema = "";
        boolean leyendo = true;
        // Guardamos la columna y fila inicial en caso de error
        int filaInicio = super.obtenerFila();
        int columnaInicio = super.obtenerColumna();
        
        try {
            // Establecemos si es un comentario multilinea o simple
            boolean simple = false;
            boolean multilinea = false;
            int c;
            // Consumimos dos veces para saber si el comentario es simple o multilinea
            c = lector.read();
            c = lector.read();
            if(c == 42){
                multilinea = true;
            }
            else{
                if(c == 47){
                    simple = true;
                }
            }
            while (leyendo) {
                c = lector.read();
                if(c == -1){
                    leyendo = false;
                    if(multilinea){
                        // Encontramos EOF y el comentario no se cerro
                        ErrorLexico err = new ErrorLexico(filaInicio, columnaInicio, "Comentario multilinea no cerrado");
                    }
                }
                // Aumentamos en 1 la columna
                super.establecerColumna(super.obtenerColumna() + 1);

                if (c == 10 || c == 11) {
                    if(multilinea){
                        // Por ser multilinea continuamos leyendo
                        super.establecerColumna(1);
                        super.establecerFila(super.obtenerFila() + 1);
                    }
                    else{
                        // Al ser simple, devolvemos las columnas y filas actualizadas
                        super.establecerColumna(1);
                        super.establecerFila(super.obtenerFila() + 1);
                        leyendo = false;
                    }
                }

                // Posible cierre de comentario multilinea
                if(c == 42 && multilinea){
                    // Leemos el siguiente caracter sin consumir
                    lector.mark(1);
                    c = lector.read();
                    if(c == 47){
                        // Cerro el comentario
                        leyendo = false;
                    }
                    else{
                        // Continuamos leyendo el comentario
                        lector.reset();
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
        token.establecerToken("Comentario");
        return token;
    }
}
