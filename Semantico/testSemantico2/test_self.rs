/*ERROR: LINEA 5 | COLUMNA 8 | La variable nombre no esta definida en el alcance actual*/
class Prueba {

	create() {
		self.nombre = "pepe";
	}

}
fn main () {
	Str: nombre;
	Prueba : p1;
	p1 = new Prueba();
}