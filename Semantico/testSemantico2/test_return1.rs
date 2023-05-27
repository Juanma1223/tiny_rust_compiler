/*ERROR: LINEA 9 | COLUMNA 5 | El metodo salida tiene sentencia de retorno dentro del metodo. La sentencia de retorno debe estar al final.*/
class Prueba {
	I32: a;
	Str: b;
	create(){
		a=0;
		b="hola";
	}
	fn salida() -> I32 {
		return a;
        a = 1;
	}
}
fn main () {

}