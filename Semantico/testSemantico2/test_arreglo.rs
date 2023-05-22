/*ERROR: LINEA 6 | COLUMNA 5 | Los arreglos solo pueden ser accedidos haciendo uso de enteros!*/
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