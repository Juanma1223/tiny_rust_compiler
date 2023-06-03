/*ERROR: LINEA 7 | COLUMNA 3 | La variable nombre no esta definida en el alcance actual*/
class Prueba {
	Str: nombre;

	static fn prueba() -> void {
        //los metodos estaticos no pueden acceder a atributos de la clase
		nombre = "pepe";
	}

}
fn main () {
	Prueba : p1;
	p1 = new Prueba();
}