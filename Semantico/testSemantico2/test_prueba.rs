/*CORRECTO*/
class Prueba {
	Prueba2: a;
	I32: z;
	I32: b;
	pub Array I32: c;
	pub Str: d;
	pub Bool: e;
	create(I32: a, Str: b){
		z = 1;
        d = "pepe";
	}
	fn get_a() -> Str {
		if(1<=2) {
			;
		} else {
			(2+1);
		}
		while(false) {
			(false || true && true || false);
			d=(a.get_f());
		}
		return d;
	}
	fn get_b(I32:a,I32:x) -> I32 {
		return z;
	}
	static fn imprimo_algo() -> void {
		// (IO.out_string("hola mundo"));
	}
}
class Prueba2 : Prueba{
	Str: f;
	create(){
		f = 1;
	}
	fn get_f() -> Str {
		return f;
	}
	fn get_z() -> I32 {
		z = 1;
		return z;
	}

}
fn main () {
	//c = new I32[1+2];
}