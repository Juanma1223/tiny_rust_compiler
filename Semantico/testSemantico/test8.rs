/*ERROR: LINEA 4 | COLUMNA 11 | El atributo a no se puede redefinir en la clase Prueba2
ya que ya se encuentra definido en esta superclase.*/
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