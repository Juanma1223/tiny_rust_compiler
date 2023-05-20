/*ERROR: LINEA 10 | COLUMNA 3 | El retorno del metodo salida no coincide con su tipo de retorno.*/
class Prueba {
	I32: a;
	Str: b;
	create(){
		a=0;
		b="hola";
	}
	fn salida() -> I32 {
		return b;
	}
}
fn main () {

}