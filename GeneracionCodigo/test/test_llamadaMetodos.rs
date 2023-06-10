class Prueba {
	//Test llamada metodos
	fn suma(I32 : a, I32 : b) -> I32 {
		I32: c;
		c = a+b;
		(IO.out_i32(c)); //c vale 5
		//LLamada metodo resta
		c = resta_uno(c); // c vale 4
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
	//LLamada metodo suma
	resultado = p1.suma(2,3) + 2; //resultado vale 6
	(IO.out_i32(resultado));
	resultado2 = resultado+3; //resultado vale 9
	(IO.out_i32(resultado2));
}