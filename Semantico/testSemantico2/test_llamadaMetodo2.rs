/*ERROR: LINEA 13 | COLUMNA 6 | Cantidad de argumentos incorrecta en el llamado a funcion!*/
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
	(p1.suma(s0));
}