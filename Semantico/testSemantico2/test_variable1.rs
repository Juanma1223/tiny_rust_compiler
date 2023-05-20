/*ERROR: Prueba1 no esta declarado*/
class Prueba {
	I32: a,b;
	create(){
		a=0;
		b=1;
	}
	fn suma() -> I32 {
		return a+b;
	}
}
fn main () {
	Prueba : p1;
	(new Prueba1());
	(p1.suma());
}