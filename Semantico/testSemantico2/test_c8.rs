/*CORRECTO*/
class Prueba {
	I32: a,suma;
	Str: b;
	Char: d;
	Bool: e;
	create(){
		a = 42;
	}
	fn m1(I32: n)-> I32{
        while (1<= n){
            if (a==0 || a!=1){
                //OPERADORES LOGICOS
                e = 1<2 || 2>1 && 1<=2 || 2>=1;
                //OPERADOR UNARIO
				e = !!!true;
            }
            else if(a>=0){
                //OPERADORES ARITMETICOS
                suma=suma+1*2;
            }
            else{
                suma=suma-1%2;
            }
            //OPERADOR UNARIO
			(+-+-++3);
        }
		return a;
    }
	fn get_a() -> I32 {
		return (a);
	}
	static fn imprimo_algo() -> void {
		
		(IO.out_string("hola mundo"));
	}
}
fn main () {
	Array I32: c;
	Prueba: p;
	c = new I32[1+2];
	p = new Prueba();
	((p).imprimo_algo());
}