/*ERROR: LINEA 11 | COLUMNA 15 | El metodo suma no puede ser accedido desde el metodo prueba2*/
class Prueba {
	fn suma(I32:a,I32:b) -> I32 {
		I32 : c;
		c = a+b;
		return c;
	}

	static fn prueba2() -> Prueba {
        I32 : s0,s1,s2;
        s2 = (suma(s0,s1));
		return prueba;
	}
}
fn main () {
	Prueba : p1;
	p1 = new Prueba();
}