/*ERROR: LINEA 14 | COLUMNA 7 | No se puede crear un objeto de la clase Prueba1 porque no esta definida.*/
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