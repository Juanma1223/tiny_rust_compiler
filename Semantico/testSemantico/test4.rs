/*ERROR: LINEA 24 | COLUMNA 7 | Herencia circular encontrada para la clase Prueba3 que hereda de Prueba.
No puede haber herencia circular*/
class Prueba : Prueba2{
	I32: a;
	pub Str: b;
	Array I32: c;
	Char: d;
	Bool: e;
	create(){
		a = 42;
	}
	fn get_a(I32 : a, Char : d) -> I32 {
		return a;
	}
	static fn imprimo_algo() -> void {
		(IO.out_string("hola mundo"));
	}
}

class Prueba2 : Prueba3 {

}

class Prueba3 : Prueba {

}

fn main () {
	I32 : z;
	z = new I32[2];
	c = new I32[1+2];
}