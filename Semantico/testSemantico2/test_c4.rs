/*CORRECTO*/
class Prueba {
	fn suma(I32:a,I32:b) -> I32 {
		I32 : c;
		c = a+b;
		return c;
	}

	fn prueba2() -> Prueba {
		Prueba : prueba;
		return prueba;
	}

	fn prueba3() -> Prueba {
		Prueba : prueba;
		I32 : c;
		return prueba;
	}
}
fn main () {
	Prueba : p1;
	I32 : s0;
	I32 : s1;
	p1 = new Prueba();
	//LLAMADAS A METODOS ENCADENADOS
	s0 = p1.prueba2().suma(s0,s1);
	s0 = p1.prueba3().prueba2().suma(s0,s1);
}