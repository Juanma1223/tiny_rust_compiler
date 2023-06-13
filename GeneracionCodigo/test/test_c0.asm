.text
.globl main
IO_out_i32:
move $fp, $sp
subu $sp, $sp, 16
sw $ra, 4($sp)

sw $a0, -4($fp)
li $v0, 1
syscall
lw $ra, 4($sp)
addiu $sp, $sp, 16
lw $fp, 0($sp)
jr $ra

IO_out_bool:
move $fp, $sp
subu $sp, $sp, 16
sw $ra, 4($sp)

sw $a0, -4($fp)
li $v0, 1
syscall
lw $ra, 4($sp)
addiu $sp, $sp, 16
lw $fp, 0($sp)
jr $ra

IO_out_char:
move $fp, $sp
subu $sp, $sp, 16
sw $ra, 4($sp)

sw $a0, -4($fp)
li $v0, 4
syscall
lw $ra, 4($sp)
addiu $sp, $sp, 16
lw $fp, 0($sp)
jr $ra

IO_out_string:
move $fp, $sp
subu $sp, $sp, 16
sw $ra, 4($sp)

sw $a0, -4($fp)
li $v0, 4
syscall
lw $ra, 4($sp)
addiu $sp, $sp, 16
lw $fp, 0($sp)
jr $ra

IO_in_i32:
move $fp, $sp
subu $sp, $sp, 16
sw $ra, 4($sp)

li $v0, 5
syscall
move $a0, $v0
lw $ra, 4($sp)
addiu $sp, $sp, 16
lw $fp, 0($sp)
jr $ra

IO_in_bool:
move $fp, $sp
subu $sp, $sp, 16
sw $ra, 4($sp)

li $v0, 5
syscall
move $a0, $v0
lw $ra, 4($sp)
addiu $sp, $sp, 16
lw $fp, 0($sp)
jr $ra

IO_in_char:
move $fp, $sp
subu $sp, $sp, 16
sw $ra, 4($sp)

li $v0, 8
syscall
lw $ra, 4($sp)
addiu $sp, $sp, 16
lw $fp, 0($sp)
jr $ra

IO_in_string:
move $fp, $sp
subu $sp, $sp, 16
sw $ra, 4($sp)

li $v0, 8
syscall
lw $ra, 4($sp)
addiu $sp, $sp, 16
lw $fp, 0($sp)
jr $ra


Str_length:
move $fp, $sp
subu $sp, $sp, 12
sw $ra, 4($sp)
lw $a0, 8($sp) # cargo la palabra de self
li $t0, 0 # inicializa contador en 0
loop_length:
lb $t1, 0($a0) # carga el siguiente caracter en t1
beqz $t1, exit_length # chequea si se llego al final de la cadena
addiu $a0, $a0, 1 # incrementa el puntero de la cadena
addiu $t0, $t0, 1 # incrementa el contador
j loop_length # vuelve al loop
exit_length:
move $a0 $t0 # guarda el resultado en el acumulador
lw $ra, 4($sp)
addiu $sp, $sp, 12
lw $fp, 0($sp)
jr $ra

main:
move $fp, $sp # Comienza la creacion de RA de main
subu $sp, $sp, 12
sw $ra, 4($sp)
sw $fp, 0($sp)
li $a0, 1

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 2

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 3

lw $t1, 4($sp)
mul $a0, $t1, $a0 # Multiplicacion

addiu $sp, $sp, 4

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 3

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 2

lw $t1, 4($sp)
bne $a0, $0, division1 # Control division por cero
.data
string2: .asciiz "ERROR: Division por cero"
.text
la $a0, string2
li $v0, 4 # Print string
syscall
li $v0, 10 # Exit
syscall
division1:
div $t1, $a0 # Division
mflo $a0 # Recupero resultado division

addiu $sp, $sp, 4

lw $t1, 4($sp)
subu $a0, $t1, $a0 # Resta

addiu $sp, $sp, 4

lw $t1, 4($sp)
addu $a0, $t1, $a0 # Suma

addiu $sp, $sp, 4

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 4

lw $t1, 4($sp)
bne $a0, $0, modulo3 # Control division por cero
.data
string4: .asciiz "ERROR: Modulo-Division por cero"
.text
la $a0, string4
li $v0, 4 # Print string
syscall
li $v0, 10 # Exit
syscall
modulo3:
div $t1, $a0 # Division
mfhi $a0 # Recupero resultado modulo

addiu $sp, $sp, 4

sw $a0, -4($sp) # Guardamos el parameotro 0 en el RA
jal IO_out_i32


lw $ra, 4($sp) # Comenzamos el pop del metodo main
addiu $sp, $sp, 12
lw $fp, 0($sp)
jr $ra


li $v0, 10
syscall
