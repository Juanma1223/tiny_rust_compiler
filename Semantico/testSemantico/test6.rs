/*ERROR: LINEA 7 | COLUMNA 25 | El parámetro a ya fue declarado.
No puede haber dos parámetros con el mismo nombre dentro de una función */
class Prueba {
	create(){
		a = 1;
	}
	fn get_a(I32: a, Bool: a) -> I32 {
		return a;
	}
}
fn main () {

}