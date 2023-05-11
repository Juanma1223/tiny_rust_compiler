/*ERROR: LINEA 8 | COLUMNA 15 |  No se puede cambiar la forma de un método heredado*/
class A {
    fn m1()->void
    {}
}
class B : A{

    static fn m1()->void
    {}
}
fn main(){}