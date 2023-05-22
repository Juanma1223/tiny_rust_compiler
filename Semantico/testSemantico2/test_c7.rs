/*CORRECTO*/
class Prueba {
	Nombre : nombre;

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