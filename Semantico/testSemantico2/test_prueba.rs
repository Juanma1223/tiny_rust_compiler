/*CORRECTO*/
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
		if(e==true) {
			;
		} else {
			(a+1);
		}
		return a;
	}
	fn get_b(I32:a,I32:x) -> I32 {
		return a;
	}
	static fn imprimo_algo() -> void {
		// (IO.out_string("hola mundo"));
	}
}
class Prueba2 : Prueba{
	Bool: f;
	create(Bool: f){
	}
	fn get_f() -> Bool {
		return f;
	}
	fn get_b(I32:a,I32:x) -> I32 {
		return a;
	}
}
fn main () {
	c = new I32[1+2];
}