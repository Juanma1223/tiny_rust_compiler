class Prueba {
	fn suma(I32 : a, I32 : b) -> I32 {
		I32: c;
		c = a+b;
		(IO.out_i32(c));
		c = resta_uno(c);
		return (c);
	}
	fn resta_uno(I32:r) -> I32 {
		return (r-1);
	}
}
fn main () {
	Prueba : p1;
	I32 : resultado;
	I32 : resultado2;
	//p1 = new Prueba();
	resultado = p1.suma(2,3) + 2;
	(IO.out_i32(resultado));
	resultado2 = resultado+3;
	(IO.out_i32(resultado2));
}