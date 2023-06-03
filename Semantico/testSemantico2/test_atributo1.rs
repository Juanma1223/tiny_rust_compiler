/*ERROR: LINEA 15 | COLUMNA 5 | El atributo nombre no se puede acceder porque es privado*/
class Prueba {
	Array I32: a;
	Str: nombre;

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