/*CORRECTO*/
fn main () {
    //Test while
	while(false) {
        (IO.out_string("Esto no se deberia imprimir"));
    }
    (IO.out_string("Fuera del while"));
}