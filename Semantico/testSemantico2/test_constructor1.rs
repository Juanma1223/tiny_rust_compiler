/*ERROR: LINEA 7 | COLUMNA 9 | El constructor no puede tener sentencia de retorno.*/
class Prueba {
	I32: a,b;
	create(){
		a=0;
		b=1;
        return ;
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