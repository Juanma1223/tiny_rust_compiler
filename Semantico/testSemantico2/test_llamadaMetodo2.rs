/*ERROR: Parámetros incompatibles en cantidad*/
class Prueba {
	fn suma(I32:a,I32:b) -> I32 {
		I32 : c;
		c = a+b;
		return c;
	}
}
fn main () {
	Prueba : p1;
	p1 = new Prueba();
	Str: s0, s1;
	(p1.suma(s0));
}