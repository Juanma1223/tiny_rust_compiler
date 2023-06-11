//12
//--
//f_0=0
//f_1=1
//f_2=1
//f_3=2
//f_4=3
//f_5=5
//f_6=8
//f_7=13
//f_8=21
//f_9=34
//f_10=55
//f_11=89
//f_12=144
class Fibonacci {
    pub I32: suma;
    I32: i,j;
    fn sucesion_fib(I32: n)-> void{
        I32: idx;
        i=0; j=1; suma=0; idx = 0;
        while (idx <= n){
            (out_idx(idx));
            (out_val(i));
            suma = i + j;
            i = j;
            j = suma;
            idx = idx + 1;
        }
    }
    create(){
        i=0;
        j=0;
        suma=0;
    }
    fn out_idx(I32: num) -> void{
        (IO.out_string("f_"));
        (IO.out_i32(num));
        (IO.out_string("="));
    }
    fn out_val(I32: s) -> void{
        (IO.out_i32(s));
        (IO.out_string("\n"));
    }
}
fn main(){
    Fibonacci: fib;
    I32: n;
    fib = new Fibonacci();
    // n = IO.in_i32();
	n = 5;
    (fib.sucesion_fib(n));
}