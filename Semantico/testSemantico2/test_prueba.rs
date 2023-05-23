/*CORRECTO*/
class Prueba {
	Str : nombre;

	create() {
		self.nombre = "pepe";
	}
	fn m1() -> Prueba {

	}

}

class Nombre {
	Str : apellido;
}

fn main () {
	(new Prueba().m1());
}