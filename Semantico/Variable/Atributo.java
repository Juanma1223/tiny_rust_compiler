package Semantico.Variable;

public class Atributo extends Variable{
    
    public Atributo(String nombre) {
        super(nombre);
    }

    private boolean esPublico;

    public boolean obtenerVisibilidad(){
        return this.esPublico;
    }

}
