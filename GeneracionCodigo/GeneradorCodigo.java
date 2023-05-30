package GeneracionCodigo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Semantico.Semantico;

public class GeneradorCodigo {

    public GeneradorCodigo(File archivo) {
        Semantico semantico = new Semantico(archivo);

        // Creamos el codigo intermedio .asm
        try (FileWriter escritorASM = new FileWriter(archivo + ".asm")) {
            escritorASM.write(semantico.obtenerAST().genCodigo());
        } catch (IOException e) {
            System.out.println("Error al intentar escribir el codigo asm");
        }
    }
}
