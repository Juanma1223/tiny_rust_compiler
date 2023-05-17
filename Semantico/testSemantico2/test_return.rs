/*ERROR: LINEA 10 | COLUMNA 13 | Tipos incompatibles.*/
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