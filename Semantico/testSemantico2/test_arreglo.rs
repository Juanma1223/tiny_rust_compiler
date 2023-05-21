/*ERROR: LINEA 6 | COLUMNA 3 | La expresion debe ser de tipo I32*/
class Prueba {
	Array I32: a;

	fn prueba() -> void {
		a["pepe"] = 1;
	}
}

fn main () {
	Prueba : p1;
	p1 = new Prueba();
}