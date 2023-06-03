/*ERROR: LINEA 18 | COLUMNA 12 | El atributo apellido no se puede acceder porque es privado*/
class Prueba {
	pub Nombre : nombre;

	create() {
	}

}

class Nombre {
	Str : apellido;
}

fn main () {
	Prueba : p1;
	p1 = new Prueba();
    //ACCESO VARIABLES ENCADENADAS
	p1.nombre.apellido = "pepe";
}