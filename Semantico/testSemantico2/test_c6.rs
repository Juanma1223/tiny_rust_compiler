/*CORRECTO*/
class Prueba {
	Array I32: a;
	pub Str: nombre;

	fn prueba() -> void {
		a[1] = 1;
	}

}
fn main () {
	Prueba : p1;
	p1 = new Prueba();
    //ACCESO ATRIBUTO
	p1.nombre = "pepe";
}