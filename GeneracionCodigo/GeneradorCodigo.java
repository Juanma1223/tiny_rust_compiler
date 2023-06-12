package GeneracionCodigo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Semantico.Semantico;

public class GeneradorCodigo {

    public GeneradorCodigo(File archivo) {
        Semantico semantico = new Semantico(archivo);

        // Obtenemos la ruta del archivo sin la extension
        String ruta = archivo.getAbsolutePath();
        ruta = ruta.substring(0,ruta.length()-3);

        // Creamos el codigo intermedio .asm
        try (FileWriter escritorASM = new FileWriter(ruta + ".asm")) {
            escritorASM.write(semantico.obtenerAST().genCodigo());
        } catch (IOException e) {
            System.out.println("Error al intentar escribir el codigo asm");
        }
    }
}
