package Lexico;

/* La clase ErrorLexico esta encargada de mostrar los errores lexicos
 * y detener la ejecucion del programa cuando estos ocurren
 */
public class ErrorLexico extends RuntimeException{
    
    public ErrorLexico(int fila, int columna, String mensaje){
        System.out.println("ERROR: LEXICO");
        System.out.println("| NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |");
        System.out.println("| LINEA " + Integer.toString(fila) + " | COLUMNA " + Integer.toString(columna) + " | " + mensaje + "|");
        // Terminamos la ejecucion del programa con error lexico
        System.exit(1);
    }
}
