.text
.globl main
IO_out_i32:
move $fp, $sp
subu $sp, $sp, 20
sw $ra, 4($sp)

sw $a0, -4($fp)
li $v0, 1
syscall
lw $ra, 4($sp)
addiu $sp, $sp, 20
lw $fp, 0($sp)
jr $ra

IO_out_bool:
move $fp, $sp
subu $sp, $sp, 20
sw $ra, 4($sp)

sw $a0, -4($fp)
li $v0, 1
syscall
lw $ra, 4($sp)
addiu $sp, $sp, 20
lw $fp, 0($sp)
jr $ra

IO_out_char:
move $fp, $sp
subu $sp, $sp, 20
sw $ra, 4($sp)

sw $a0, -4($fp)
li $v0, 4
syscall
lw $ra, 4($sp)
addiu $sp, $sp, 20
lw $fp, 0($sp)
jr $ra

IO_out_string:
move $fp, $sp
subu $sp, $sp, 20
sw $ra, 4($sp)

sw $a0, -4($fp)
li $v0, 4
syscall
lw $ra, 4($sp)
addiu $sp, $sp, 20
lw $fp, 0($sp)
jr $ra

IO_in_i32:
move $fp, $sp
subu $sp, $sp, 20
sw $ra, 4($sp)

li $v0, 5
syscall
move $a0, $v0
lw $ra, 4($sp)
addiu $sp, $sp, 20
lw $fp, 0($sp)
jr $ra

IO_in_bool:
move $fp, $sp
subu $sp, $sp, 20
sw $ra, 4($sp)

li $v0, 5
syscall
move $a0, $v0
lw $ra, 4($sp)
addiu $sp, $sp, 20
lw $fp, 0($sp)
jr $ra

IO_in_char:
move $fp, $sp
subu $sp, $sp, 20
sw $ra, 4($sp)

li $v0, 8
syscall
lw $ra, 4($sp)
addiu $sp, $sp, 20
lw $fp, 0($sp)
jr $ra

IO_in_string:
move $fp, $sp
subu $sp, $sp, 20
sw $ra, 4($sp)

li $v0, 8
syscall
lw $ra, 4($sp)
addiu $sp, $sp, 20
lw $fp, 0($sp)
jr $ra


Fibonacci_sucesion_fib:
move $fp, $sp # Comienza la creacion de RA de sucesion_fib
subu $sp, $sp, 20
sw $ra, 4($sp)
sw $a0, 0($sp) # Comienzo asignacion
subu $sp, $sp, 4
li $a0, 0

lw $t1, 0($fp) # Acceso al CIR de self
sw $a0, -4($t1) # Guardamos en el CIR el valor asignado
lw $a0, 4($sp)
addiu $sp, $sp, 4 # Fin asignacion

sw $a0, 0($sp) # Comienzo asignacion
subu $sp, $sp, 4
li $a0, 1

lw $t1, 0($fp) # Acceso al CIR de self
sw $a0, -8($t1) # Guardamos en el CIR el valor asignado
lw $a0, 4($sp)
addiu $sp, $sp, 4 # Fin asignacion

sw $a0, 0($sp) # Comienzo asignacion
subu $sp, $sp, 4
li $a0, 0

lw $t1, 0($fp) # Acceso al CIR de self
sw $a0, -0($t1) # Guardamos en el CIR el valor asignado
lw $a0, 4($sp)
addiu $sp, $sp, 4 # Fin asignacion

sw $a0, 0($sp) # Comienzo asignacion
subu $sp, $sp, 4
li $a0, 0

sw $a0,-8($fp)
lw $a0, 4($sp)
addiu $sp, $sp, 4 # Fin asignacion

while1:
lw $a0, -8($fp) # Acceso a la variable idx

sw $a0, 0($sp)
addiu $sp, $sp, -4
lw $a0, -4($fp) # Acceso a parametro

lw $t1, 4($sp)
slt $a0, $a0, $t1
xori $a0, $a0, 1

addiu $sp, $sp, 4

bne $a0, 1, endwhile1
sw $fp, 0($sp)
lw $a0, -8($fp) # Acceso a la variable idx

sw $a0, -4($sp) # Guardamos el parameotro 0 en el RA
jal Fibonacci_out_idx

sw $fp, 0($sp)
lw $t1, 0($fp) # Acceso al CIR de  i
lw $a0, -4($t1) # Guardamos en $a0 el valor de la variable almacenada en el CIR

sw $a0, -4($sp) # Guardamos el parameotro 0 en el RA
jal Fibonacci_out_val

sw $a0, 0($sp) # Comienzo asignacion
subu $sp, $sp, 4
lw $t1, 0($fp) # Acceso al CIR de  i
lw $a0, -4($t1) # Guardamos en $a0 el valor de la variable almacenada en el CIR

sw $a0, 0($sp)
addiu $sp, $sp, -4
lw $t1, 0($fp) # Acceso al CIR de  j
lw $a0, -8($t1) # Guardamos en $a0 el valor de la variable almacenada en el CIR

lw $t1, 4($sp)
addu $a0, $t1, $a0

addiu $sp, $sp, 4

lw $t1, 0($fp) # Acceso al CIR de self
sw $a0, -0($t1) # Guardamos en el CIR el valor asignado
lw $a0, 4($sp)
addiu $sp, $sp, 4 # Fin asignacion

sw $a0, 0($sp) # Comienzo asignacion
subu $sp, $sp, 4
lw $t1, 0($fp) # Acceso al CIR de  j
lw $a0, -8($t1) # Guardamos en $a0 el valor de la variable almacenada en el CIR

lw $t1, 0($fp) # Acceso al CIR de self
sw $a0, -4($t1) # Guardamos en el CIR el valor asignado
lw $a0, 4($sp)
addiu $sp, $sp, 4 # Fin asignacion

sw $a0, 0($sp) # Comienzo asignacion
subu $sp, $sp, 4
lw $t1, 0($fp) # Acceso al CIR de  suma
lw $a0, -0($t1) # Guardamos en $a0 el valor de la variable almacenada en el CIR

lw $t1, 0($fp) # Acceso al CIR de self
sw $a0, -8($t1) # Guardamos en el CIR el valor asignado
lw $a0, 4($sp)
addiu $sp, $sp, 4 # Fin asignacion

sw $a0, 0($sp) # Comienzo asignacion
subu $sp, $sp, 4
lw $a0, -8($fp) # Acceso a la variable idx

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 1

lw $t1, 4($sp)
addu $a0, $t1, $a0

addiu $sp, $sp, 4

sw $a0,-8($fp)
lw $a0, 4($sp)
addiu $sp, $sp, 4 # Fin asignacion


j while1
endwhile1:


lw $ra, 4($sp) # Comenzamos el pop del metodo sucesion_fib
addiu $sp, $sp, 20
lw $fp, 0($sp)
jr $ra


Fibonacci_out_idx:
move $fp, $sp # Comienza la creacion de RA de out_idx
subu $sp, $sp, 16
sw $ra, 4($sp)
sw $fp, 0($sp)
.data
string2: .asciiz "f_"
.text
la $a0, string2

sw $a0, -4($sp) # Guardamos el parameotro 0 en el RA
jal IO_out_string

sw $fp, 0($sp)
lw $a0, -4($fp) # Acceso a parametro

sw $a0, -4($sp) # Guardamos el parameotro 0 en el RA
jal IO_out_i32

sw $fp, 0($sp)
.data
string3: .asciiz "="
.text
la $a0, string3

sw $a0, -4($sp) # Guardamos el parameotro 0 en el RA
jal IO_out_string


lw $ra, 4($sp) # Comenzamos el pop del metodo out_idx
addiu $sp, $sp, 16
lw $fp, 0($sp)
jr $ra

Fibonacci_out_val:
move $fp, $sp # Comienza la creacion de RA de out_val
subu $sp, $sp, 16
sw $ra, 4($sp)
sw $fp, 0($sp)
lw $a0, -4($fp) # Acceso a parametro

sw $a0, -4($sp) # Guardamos el parameotro 0 en el RA
jal IO_out_i32

sw $fp, 0($sp)
.data
string4: .asciiz "\n"
.text
la $a0, string4

sw $a0, -4($sp) # Guardamos el parameotro 0 en el RA
jal IO_out_string


lw $ra, 4($sp) # Comenzamos el pop del metodo out_val
addiu $sp, $sp, 16
lw $fp, 0($sp)
jr $ra


main:
move $fp, $sp # Comienza la creacion de RA de main
subu $sp, $sp, 20
sw $ra, 4($sp)
sw $a0, 0($sp) # Comienzo asignacion
subu $sp, $sp, 4
li $v0, 9 # Alocamos en el heap el constructor de Fibonacci
li $a0,20
syscall
move $a0, $v0 # $a0 contiene el puntero al CIR de Fibonacci

sw $a0,-4($fp)
lw $a0, 4($sp)
addiu $sp, $sp, 4 # Fin asignacion

sw $a0, 0($sp) # Comienzo asignacion
subu $sp, $sp, 4
li $a0, 12

sw $a0,-8($fp)
lw $a0, 4($sp)
addiu $sp, $sp, 4 # Fin asignacion

sw $fp, 0($sp)
lw $t1,-4($fp)
sw $t1,0($sp)
lw $a0, -8($fp) # Acceso a la variable n

sw $a0, -4($sp) # Guardamos el parameotro 0 en el RA
jal Fibonacci_sucesion_fib



lw $ra, 4($sp) # Comenzamos el pop del metodo main
addiu $sp, $sp, 20
lw $fp, 0($sp)
jr $ra


li $v0, 10
syscall
