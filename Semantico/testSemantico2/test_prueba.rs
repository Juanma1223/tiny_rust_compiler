class Prueba {
	Array I32: a;
	Str: nombre;

	fn prueba() -> void {
		a[1] = 1;
	}

	fn suma(I32:a,I32:b) -> I32 {
		I32 : c;
		c = a+b;
		return c;
	}

	fn prueba2() -> Prueba {
		Prueba : prueba;
		return prueba;
	}

	fn prueba3() -> I32 {
		I32 : c;
		return c;
	}
}
fn main () {
	Prueba : p1;
	I32 : s0;
	I32 : s1;
	p1 = new Prueba();
	p1.nombre = "pepe";
	//s0 = p1.prueba3().suma(s0,s1);
	//s0 = p1.prueba3().prueba2().suma(s0,s1);
}