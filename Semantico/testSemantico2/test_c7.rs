/*CORRECTO*/
class Prueba {
	pub Nombre : nombre;

	create() {
	}

}

class Nombre {
	pub Str : apellido;
}

fn main () {
	Prueba : p1;
	p1 = new Prueba();
    //ACCESO VARIABLES ENCADENADAS
	p1.nombre.apellido = "pepe";
}