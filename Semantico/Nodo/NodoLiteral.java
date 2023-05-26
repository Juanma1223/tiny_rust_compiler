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
        this.obtenerTipo();
    }
    
    @Override
    public Tipo obtenerTipo(){
        Tipo tLit;
        switch(this.token.obtenerToken()){
            case "lit_ent":
            tLit = new TipoPrimitivo("I32");
            break;
            case "lit_car":
            tLit = new TipoPrimitivo("Char");
            break;
            case "lit_cad":
            tLit = new TipoPrimitivo("Str");
            break;
            case "p_true":
            tLit = new TipoPrimitivo("Bool");
            break;
            case "p_false":
            tLit = new TipoPrimitivo("Bool");
            break;
            default:
            tLit = new Tipo("");
        }
        //Si el literal tiene un encadenado es porque está dentro
        //de una expresion parentizada
        if(this.encadenado != null){
            this.tipo = this.encadenado.obtenerTipoEncadenado(tLit);
            return this.tipo;
        }else{
            this.tipo = tLit;
            return this.tipo;
        }
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("\"Nodo\":").append("\"NodoLiteral\",").append(System.lineSeparator());
        if (this.encadenado != null) {
            sb.append("\"valor\":").append("\"" + token.obtenerLexema() + "\",").append(System.lineSeparator());
            sb.append("\"encadenado\":{").append(System.lineSeparator());
            sb.append(this.encadenado.toJson()).append(System.lineSeparator());
            sb.append("}").append(System.lineSeparator());
        } else {
            sb.append("\"valor\":").append("\"" + token.obtenerLexema() + "\"").append(System.lineSeparator());
        }
        return sb.toString();
    }

}
