/*CORRECTO*/
fn main () {
    I32: a;
    Bool: b;
    Char: c;
    Str: d;
    //Test input
    (IO.out_string("Ingresa un entero: "));
    a = IO.in_i32();
    (IO.out_string("El entero ingresado es: "));
    (IO.out_i32(a));

    (IO.out_string("Ingresa un booleano: "));
    b = IO.in_bool();
    (IO.out_string("El booleano ingresado es: "));
    (IO.out_bool(b));
    
    (IO.out_string("Ingresa un caracter: "));
    c = IO.in_char();
    (IO.out_string("El caracter ingresado es: "));
    (IO.out_char(c));

    (IO.out_string("Ingresa un string: "));
    d = IO.in_string();
    (IO.out_string("El string ingresado es: "));
    (IO.out_string(d));
}