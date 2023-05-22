/*CORRECTO*/
class Prueba {
	Str: nombre;

	create() {
        //ACCCESO SELF
		self.nombre = "pepe";
	}

}
fn main () {
	Prueba : p1;
	p1 = new Prueba();
}