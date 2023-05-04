/*ERROR: LINEA 27 | COLUMNA 2 | Ya hay un constructor declarado para esta clase
No puede haber más de un constructor por clase*/
class Prueba {
	I32: a;
	Str: b;
	pub Array I32: c;
	pub Char: d;
	pub Bool: e;
	create(I32: a, Str: b){
		a = 1;
        b = "pepe";
	}
	fn get_a() -> I32 {
		return a;
	}
	fn get_b() -> Str {
		return b;
	}
	static fn imprimo_algo() -> void {
		(IO.out_string("hola mundo"));
	}
}
class Prueba2 : Prueba{
	Bool: f;
	create(Bool: f){
	}
	create(Bool: g){
	}
	fn get_f() -> Bool {
		return f;
	}
}
fn main () {
	c = new I32[1+2];
}