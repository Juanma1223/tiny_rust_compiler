/*CORRECTO*/
class Persona {
	Str : nombre;
	I32 : edad;
	create(Str: nombre) {
		self.nombre = nombre;
	}
	fn obtener_edad(Persona: p) -> I32 {
		return p.edad;
	}

}
fn main () {
	Persona : p;
	p = new Persona("pepe");
}