/*ERROR: LINEA 6 | COLUMNA 3 | La expresion debe ser de tipo I32*/
class Prueba {
	Array I32: a;

	fn prueba() -> void {
		a[3+4] = 3;
		a[1] = "Hola";
	}
}

fn main () {
}