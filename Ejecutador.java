import java.io.File;

import GeneracionCodigo.GeneradorCodigo;
import Semantico.ErrorSemantico;

public class Ejecutador {
    public static void main(String[] args) {

        // Leemos el argumento que es pasado por stdin
        if (args.length < 1) {
            // No recibimos nombre de archivo y por tanto no podemos proceder
            new ErrorSemantico(0, 0, "No se ingreso archivo de codigo fuente!",true);
        }

        // Abrimos el archivo y almacenamos su informacion
        File archivo = new File(args[0]);
        // Obtenemos la extension del archivo
        String extension = archivo.getPath().substring(archivo.getPath().length()-2,archivo.getPath().length());
        if(!extension.equals("rs")){
            new ErrorSemantico(0, 0, "El archivo ingresado no tiene extension .rs!",true);
        }
        // Instanciamos el Generador de Codigo
        new GeneradorCodigo(archivo);
        
        System.out.println("CORRECTO");
    }
}
