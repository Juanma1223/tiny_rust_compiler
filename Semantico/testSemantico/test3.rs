/*ERROR: LINEA 2 | COLUMNA 8 | El atributo a ya fue declarado. No puede haber dos atributos con el mismo nombre en una misma clase*/
class Prueba {
	I32: a;
	Bool: a;
	create(){
		a = 1;
	}
	fn get_a() -> I32 {
		return a;
	}
}
fn main () {

}