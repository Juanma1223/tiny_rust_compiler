/*ERROR: LINEA 26 | COLUMNA 8 | No se puede cambiar el tipo de los parametros del método heredado*/
class A {

    fn m1()->void

    {}

    static fn m2()->void

    {}

    fn m3(I32: p1, Str: p2)->void

    {}

    fn m4(I32: p3, Bool: p4)->void

    { I32: v1, v2;

     Str: v3;}

}

class B : A{

    fn m3(Bool: p1, Str: p2)->void

    {}

}

fn main(){}