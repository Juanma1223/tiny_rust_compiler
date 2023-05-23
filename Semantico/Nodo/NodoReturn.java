package Semantico.Nodo;

import Semantico.Clase;
import Semantico.ErrorSemantico;
import Semantico.Funcion.Funcion;
import Semantico.Funcion.Metodo;
import Semantico.Tipo.Tipo;

public class NodoReturn extends NodoSentencia {
    private NodoExpresion retorno;

    public NodoReturn(Funcion metodoContenedor, Clase claseContenedora){
        super(metodoContenedor,claseContenedora);
        this.metodoContenedor = metodoContenedor;
        this.claseContenedora = claseContenedora;
    }

    public void agregarExpresion(NodoExpresion retorno){
        this.retorno = retorno;
        this.retorno.establecerTablaDeSimbolos(tablaDeSimbolos);
        retorno.establecerPadre(this);
    }

    public Tipo obtenerTipo(){
        return this.retorno.obtenerTipo();
    }

    @Override
    public void checkeoTipos(){
        retorno.checkeoTipos();
        // Debemos comprobar que el metodo retorna el mismo tipo que la expresion de retorno
        Metodo infoMetodo = tablaDeSimbolos.obtenerClasePorNombre(claseContenedora.obtenerNombre()).obtenerMetodoPorNombre(metodoContenedor.obtenerNombre());
        Tipo tipoRetorno = this.obtenerTipo();
        Tipo tipoMetodo = infoMetodo.obtenerTipoRetorno();
        if(!tipoMetodo.obtenerTipo().equals(tipoRetorno.obtenerTipo())){
            new ErrorSemantico(this.aux.obtenerFila(), this.aux.obtenerColumna(), "El retorno del metodo "+metodoContenedor.obtenerNombre()+" no coincide con su tipo de retorno",true);
        }
    }

    public String toJson() {
        // Construimos el json de forma recursiva
        StringBuilder sb = new StringBuilder();
        sb.append("\"Nodo\":").append("\"NodoRetorno\",").append(System.lineSeparator());
        sb.append("\"hijos\":[").append(System.lineSeparator());
        sb.append("{").append(System.lineSeparator());
        sb.append(retorno.toJson()).append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        sb.append("]").append(System.lineSeparator());
        return sb.toString();
    }
}
