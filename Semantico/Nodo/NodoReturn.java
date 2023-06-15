package Semantico.Nodo;

import Semantico.Clase;
import Semantico.ErrorSemantico;
import Semantico.Funcion.Funcion;
import Semantico.Funcion.Metodo;
import Semantico.Tipo.Tipo;
import Semantico.Tipo.TipoArreglo;
import Semantico.Tipo.TipoPrimitivo;
import Semantico.Tipo.TipoReferencia;
import Semantico.Tipo.TipoVoid;

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
        if(infoMetodo != null){
            if(infoMetodo.obtenerNombre().equals("main")) {
                new ErrorSemantico(infoMetodo.obtenerFila(), infoMetodo.obtenerColumna(),
                            "El metodo main no puede tener una sentencia de retorno.",
                            true);
            }
            Tipo tipoRetorno = this.obtenerTipo();
            Tipo tipoMetodo = infoMetodo.obtenerTipoRetorno();
            if(tipoMetodo instanceof TipoPrimitivo) {
                if(tipoRetorno instanceof TipoPrimitivo) {
                    if(!tipoMetodo.obtenerTipo().equals(tipoRetorno.obtenerTipo())){
                        new ErrorSemantico(this.aux.obtenerFila(), this.aux.obtenerColumna(),
                        "El retorno del metodo "+metodoContenedor.obtenerNombre()+" no coincide con su tipo de retorno",true);
                    }
                } else {
                    new ErrorSemantico(this.aux.obtenerFila(), this.aux.obtenerColumna(),
                        "El retorno del metodo "+metodoContenedor.obtenerNombre()+" no coincide con su tipo de retorno",true);
                }
            } else if(tipoMetodo instanceof TipoReferencia) {
                if(tipoRetorno instanceof TipoReferencia) {
                    if(!tipoRetorno.obtenerTipo().equals("nil")){
                        Clase infoClase = tablaDeSimbolos.obtenerClasePorNombre(tipoRetorno.obtenerTipo());
                        if(!tipoMetodo.obtenerTipo().equals(tipoRetorno.obtenerTipo())){
                            if (!infoClase.esSubclaseDe(tipoMetodo.obtenerTipo())) {
                                new ErrorSemantico(this.aux.obtenerFila(), this.aux.obtenerColumna(),
                                "El retorno del metodo "+metodoContenedor.obtenerNombre()+" no coincide con su tipo de retorno",true);
                            }
                        }
                    }
                } else {
                    new ErrorSemantico(this.aux.obtenerFila(), this.aux.obtenerColumna(),
                        "El retorno del metodo "+metodoContenedor.obtenerNombre()+" no coincide con su tipo de retorno",true);
                }
            } else if(tipoMetodo instanceof TipoArreglo) {
                if(tipoRetorno instanceof TipoArreglo) {
                    if(!tipoMetodo.obtenerTipo().equals(tipoRetorno.obtenerTipo())){
                        new ErrorSemantico(this.aux.obtenerFila(), this.aux.obtenerColumna(),
                        "El retorno del metodo "+metodoContenedor.obtenerNombre()+" no coincide con su tipo de retorno",true);
                    }
                } else {
                    new ErrorSemantico(this.aux.obtenerFila(), this.aux.obtenerColumna(),
                        "El retorno del metodo "+metodoContenedor.obtenerNombre()+" no coincide con su tipo de retorno",true);
                }
            } else {
                if(!(tipoRetorno instanceof TipoVoid)) {
                    new ErrorSemantico(this.aux.obtenerFila(), this.aux.obtenerColumna(),
                        "El retorno del metodo "+metodoContenedor.obtenerNombre()+" no coincide con su tipo de retorno",true);
                }
            }
        } else {
            new ErrorSemantico(this.aux.obtenerFila(), this.aux.obtenerColumna(),
                        "El constructor no puede tener sentencia de retorno",true);
        }
        
    }

    @Override
    public String genCodigo(){
        // Generamos codigo para la expresion que se encuentra en el retorno
        StringBuilder sb = new StringBuilder();
        sb.append(retorno.genCodigo()).append(System.lineSeparator());
        return sb.toString();
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
