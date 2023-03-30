<<<<<<< HEAD
/
/*CORRECTO*/
class Prueba {
	I32: a;
	pub Str: b;
	Array I32: c;
	Char: d;
	Bool: e;
	create(){// Esto es un comentario simple
		a = 42;
	}
	fn get_a() -> I32 {
        // Esto es un comentario simple
		return a;
	}
	static fn imprimo_algo() -> void {
		IO.out_string("hola mundo");
	}
}
fn main () {
	c = new I32[1+2];
}
///
=======
/*CORRECTO*/
///comentario
class Prueba {
	I32: a;
	pub Str: b;
	Array I32: c;
	Char: d;
	Bool: hola__mundo;
	create(){// Esto es un comentario simple
		a = 42;
	}
	fn get_a() -> I32 {
        // Esto es un comentario simple
		return a;
	}
	static fn imprimo_algo() -> void {
		IO.out_string("hola mundo");
	}
}
fn main () {
	c = new I32[1+2];
}
>>>>>>> d4d3087e82fc5c63d60b91212434dbaf3eeb78b6
