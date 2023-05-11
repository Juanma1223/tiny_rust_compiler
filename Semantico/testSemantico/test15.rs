/*ERROR: LINEA 8 | COLUMNA 15 |  No se puede redefinir un método de clase*/
class A {
    static fn m1()->void
    {}
}
class B : A{

    static fn m1()->void
    {}
}
fn main(){}