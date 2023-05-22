package Semantico.Nodo;

import Lexico.Token;
import Semantico.Clase;
import Semantico.Funcion.Funcion;
import Semantico.Tipo.Tipo;
import Semantico.Tipo.TipoPrimitivo;

public class NodoLiteral extends NodoExpresion{

    private Token token;

    public NodoLiteral(Funcion metodoContenedor, Clase claseContenedora, Token token){
        super(metodoContenedor,claseContenedora);
        this.token = token;
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

    @Override
    public void checkeoTipos(){
        if(this.encadenado != null){
            this.encadenado.checkeoTipos();
        }
    }
    
    @Override
    public Tipo obtenerTipo(){
        if(this.encadenado != null){
            return this.encadenado.obtenerTipo();
        }else{
            switch(this.token.obtenerToken()){
                case "lit_ent":
                return new TipoPrimitivo("I32");
                case "lit_car":
                return new TipoPrimitivo("Char");
                case "lit_cad":
                return new TipoPrimitivo("Str");
                case "p_true":
                return new TipoPrimitivo("Bool");
                case "p_false":
                return new TipoPrimitivo("Bool");
                default:
                return new Tipo("");
            }
        }
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("\"Nodo\":").append("\"NodoLiteral\",").append(System.lineSeparator());
        sb.append("\"valor\":").append("\""+token.obtenerLexema()+"\"").append(System.lineSeparator());
        return sb.toString();
    }

}
