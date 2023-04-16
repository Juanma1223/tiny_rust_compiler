package Semantico.Atributo;

public class Variable extends Atributo{
    private boolean esPublico;
    private boolean esEstatico;

    public boolean obtenerEsPublico(){
        return this.esPublico;
    }

    public boolean obtenerEsEstatico(){
        return this.esEstatico;
    }
}
