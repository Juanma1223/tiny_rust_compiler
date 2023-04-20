package Semantico.Atributo;

public class Variable extends Atributo{
    
    public Variable(String nombre) {
        super(nombre);
    }

    private boolean esPublico;

    public boolean obtenerEsPublico(){
        return this.esPublico;
    }

}
