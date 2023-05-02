/*ERROR: No se puede redefinir un atributo*/
class Prueba {
	pub I32: a;
	Str: b;
	create(){
		a = 1;
	}
	fn get_a() -> I32 {
		return a;
	}
}

class Prueba2 : Prueba {
    pub I32: a;
}

fn main () {
	I32 : z;
	z = new I32[2];
}