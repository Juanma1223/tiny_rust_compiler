/*CORRECTO*/
class Prueba {
	I32: a;
	Str: b;
	pub Char: d;
	pub Bool: e;
	create(I32: a, Str: b){
		a = 1;
        b = "pepe";
	}
	fn get_a() -> I32 {
		return a;
	}
	fn get_b(I32:a,I32:x) -> Str {
		return b;
	}
	static fn imprimo_algo() -> void {
		//LLAMADA A METODO ESTATICO
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
	fn get_b(I32:a,I32:y) -> Str {
		return b;
	}
}
fn main () {
	//ARREGLO
	Array I32: c;
	c = new I32[1+2];
}