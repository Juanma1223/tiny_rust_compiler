package Semantico;

public class ErrorSemantico extends RuntimeException {

    public ErrorSemantico(int fila, int columna, String mensaje){
        System.out.println("ERROR: SEMANTICO - DECLARACIONES");
        System.out.println("| NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |");
        System.out.println("| LINEA " + Integer.toString(fila) + " | COLUMNA " + Integer.toString(columna) + " | " + mensaje + "|");
        // Terminamos la ejecucion del programa con error semantico
        System.exit(1);
    }

}
