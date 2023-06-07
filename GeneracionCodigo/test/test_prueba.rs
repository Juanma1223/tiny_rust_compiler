class Prueba {
	fn suma(I32 : a, I32 : b) -> I32 {
		(IO.out_i32(a+b));
		return (a+b);
	}
}
fn main () {
	Prueba : p1;
	I32 : resultado;
	//p1 = new Prueba();
	resultado = p1.suma(2,3);
	(IO.out_i32(resultado));
}