class Prueba {
	I32: a,b;
	create(){
		a=0;
		b=1;
	}
	fn suma(I32 : a, I32 : b) -> I32 {
		I32 : c;
		c = a+b;
		return c;
	}
}
fn main () {
	Prueba : p1;
	I32 : resultado;
	resultado = p1.suma(2,3);
	(IO.out_i32(resultado));
}