/*CORRECTO*/
class Prueba {
	Array I32: a;
	// I32: nombre;

	fn prueba() -> void {
		a[1] = 1;
	}

}
fn main () {
	Prueba : p1;
	p1 = new Prueba();
	p1.nombre = "pepe";
}