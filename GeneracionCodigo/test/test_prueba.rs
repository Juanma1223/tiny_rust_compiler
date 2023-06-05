class Prueba {
	I32: a,b;
	create(){
		a=0;
		b=1;
	}
	fn suma(I32 : n) -> I32 {
		I32 : a;
		I32 : b;
		a = 3+4;
		return 5;
	}
}
fn main () {
	Prueba : p1;
	(p1.suma(2));
	(IO.out_i32(3));
}