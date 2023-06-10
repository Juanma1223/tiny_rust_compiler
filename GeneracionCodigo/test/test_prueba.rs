class Prueba {
	pub I32 : n;
	pub I32 : k;
	fn suma(I32 : a, I32 : b) -> I32 {
		I32: c;
		c = a+b;
		return (c);
	}
}
fn main () {
	Prueba : p1;
	I32 : resultado;
	I32 : resultado2;
	p1 = new Prueba();
	p1.n = 5;
	p1.k = 7;
	(IO.out_i32(p1.suma(p1.n,p1.k)));
	// resultado2 = resultado+3;
	// (IO.out_i32(resultado2));
}