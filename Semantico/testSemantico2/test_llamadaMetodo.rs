/*ERROR: LINEA 13 | COLUMNA 6 | El metodo suma no esta definido para la clase Prueba*/
class Prueba {
	I32: a;
	Str: b;
	create(){
		a=0;
		b="hola";
	}
}
fn main () {
	Prueba : p1;
	p1 = new Prueba();
	(p1.suma());
}