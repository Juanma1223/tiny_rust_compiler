package Lexico;

import java.util.ArrayList;

/* La clase Lexico esta encargada de leer el archivo de codigo fuente y decidir
 * cual de los automatas reconocera cada token que vaya encontrando en el mismo
 */
public class Lexico{

    // Variable encargada de almacenar todos los tokens que el analizador encuentre en el archivo fuente
    private ArrayList<Token> pilaTokens;
    // Llevamos un conteo de la fila y columna que estamos revisando actualmente en el archivo fuente
    private int filaActual;
    private int columnaActual;

    // Este metodo se encarga de leer el archivo fuente e ir derivando los tokens
    // a sus respectivos automatas reconocedores
    public void genTokens(){

    }

    public Token sigToken(){
        // Tratamos nuestra ArrayList como una pila, devolvemos el primero y lo eliminamos
        Token primerToken = this.pilaTokens.get(0);
        this.pilaTokens.remove(primerToken);
        return primerToken;
    }
}