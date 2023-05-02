/*ERROR: LINEA 9 | COLUMNA 9 | La variable a ya fue declarada.
No puede haber dos variables con el mismo nombre dentro de una función*/
class Prueba {
	create(){
		a = 1;
	}
	fn get_a() -> I32 {
		I32: a;
		Bool: a;
		return a;
	}
}
fn main () {

}