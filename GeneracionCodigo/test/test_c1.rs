/*CORRECTO*/
fn main () {
    //Test operaciones unarias y de comparación
	(IO.out_bool(!false)); //Imprime 1
    (IO.out_bool(++2 != +-+-4)); //Imprime 0
    (IO.out_bool(!!true)); //Imprime 1
}