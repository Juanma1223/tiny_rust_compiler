/*CORRECTO*/
class Prueba {
	I32: a;
	pub Str: b;
	Array I32: c;
	Char: d;
	Bool: e;
	create(){
		a = 2*1+1;
		a = 2%3;
	}
	fn get_a() -> I32 {
		return a;
	}
	static fn imprimo_algo() -> void {
		
		(IO.out_string("hola mundo"));
	}
}
fn main () {
	c = new I32[1+2];
}