/*ERROR: LINEA 10 | COLUMNA 5 | El método get_a ya fue declarado. No puede haber dos métodos con el mismo nombre en una misma clase*/
class Prueba {
	I32: a;
	create(){
		a = 1;
	}
	fn get_a() -> I32 {
		return a;
	}
	fn get_a() -> Bool {
		return a;
	}
}
fn main () {

}