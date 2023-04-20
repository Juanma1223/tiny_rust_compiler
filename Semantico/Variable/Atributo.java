package Semantico.Variable;

import Semantico.Tipo.Tipo;

public class Atributo extends Variable{
    
    private boolean esPublico;
    
    public Atributo(String nombre, Tipo tipo, boolean esPublico) {
        super(nombre,tipo);
        this.esPublico = esPublico;
    }
    
    public boolean obtenerVisibilidad(){
        return this.esPublico;
    }

}
