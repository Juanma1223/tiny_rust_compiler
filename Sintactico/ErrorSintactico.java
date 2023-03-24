package Sintactico;

/* La clase ErrorSintactico esta encargada de mostrar los errores sintacticos
 * y detener la ejecucion del programa cuando estos ocurren
 */
public class ErrorSintactico extends RuntimeException {

    public ErrorSintactico(int fila, int columna, String mensaje){
        System.out.println("ERROR: SINTACTICO");
        System.out.println("| NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |");
        System.out.println("| LINEA " + Integer.toString(fila) + " | COLUMNA " + Integer.toString(columna) + " | " + mensaje + "|");
        // Terminamos la ejecucion del programa con error sintactico
        System.exit(1);
    }
}
