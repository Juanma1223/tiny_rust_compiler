/*CORRECTO*/
class Prueba {
	I32: a;
	pub Str: b;
	Char: d;
	Bool: e;
	create(){
		a = 42;
	}
	fn get_a() -> I32 {
		return a;
	}
	static fn imprimo_algo() -> void {
		
		(IO.out_string("hola mundo"));
	}
}
fn main () {
	Array I32: c;
	c = new I32[1+2];
}