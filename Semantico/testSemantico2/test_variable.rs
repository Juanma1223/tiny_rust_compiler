/*ERROR: LINEA 14 | COLUMNA 2 | La variable p no esta definida en el alcance actual*/
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
	p = new Prueba();
	(p1.suma());
}