/*CORRECTO*/
class Prueba {
    fn m1()->I32 {
        return 1;
    }
    fn m2()->I32 {
        return 2;
    }
}
fn main () {
    I32: v1;
    Bool: v2, v3;
    Prueba: p1;
    p1 = new Prueba();
    v1 = 5;
    v2 = true;
    //Test operaciones logicas y aritmeticas
	v3 = ((p1.m1()+ v1 - -p1.m2() > 12) || p1.m1()*15 == p1.m2() && 97<=+12 || !v2 && v1/2 !=1 || v2);
    (IO.out_bool(v3));
}