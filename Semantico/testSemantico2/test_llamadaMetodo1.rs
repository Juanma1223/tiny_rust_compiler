/*ERROR: Parámetros incompatibles en tipo*/
class Prueba {
	fn suma(I32:a,I32:b) -> I32 {
		I32 : c;
		c = a+b;
		return c;
	}
}
fn main () {
	Prueba : p1;
	Str: s0, s1;
	p1 = new Prueba();
	(p1.suma(s0,s1));
}