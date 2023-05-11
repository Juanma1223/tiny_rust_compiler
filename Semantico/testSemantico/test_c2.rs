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
    fn get_f() -> Bool {
		return f;
	}
}
fn main () {
	c = new I32[1+2];
}