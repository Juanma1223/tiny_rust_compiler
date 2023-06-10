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


Prueba_suma:
move $fp, $sp # Comienza la creacion de RA de suma
subu $sp, $sp, 28
sw $ra, 4($sp)
sw $a0, 0($sp) # Comienzo asignacion
subu $sp, $sp, 4
lw $a0, -4($fp) # Acceso a parametro

sw $a0, 0($sp)
addiu $sp, $sp, -4
lw $a0, -8($fp) # Acceso a parametro

lw $t1, 4($sp)
addu $a0, $t1, $a0

addiu $sp, $sp, 4

sw $a0,-12($fp)
lw $a0, 4($sp)
addiu $sp, $sp, 4 # Fin asignacion

lw $a0, -12($fp) # Acceso a la variable c



lw $ra, 4($sp) # Comenzamos el pop del metodo suma
addiu $sp, $sp, 28
lw $fp, 0($sp)
jr $ra


main:
move $fp, $sp # Comienza la creacion de RA de main
subu $sp, $sp, 28
sw $ra, 4($sp)
sw $a0, 0($sp) # Comienzo asignacion
subu $sp, $sp, 4
li $v0, 9 # Alocamos en el heap el constructor de Prueba
li $a0,12
syscall
move $a0, $v0 # $a0 contiene el puntero al CIR de Prueba

sw $a0,-4($fp)
lw $a0, 4($sp)
addiu $sp, $sp, 4 # Fin asignacion

sw $a0, 0($sp) # Comienzo asignacion
subu $sp, $sp, 4
li $a0, 5

lw $t1, -4($fp) # Cargamos el puntero al CIR de p1
sw $a0, -0($t1) # Guardamos en el CIR el valor asignado
lw $a0, 4($sp)
addiu $sp, $sp, 4 # Fin asignacion

sw $fp, 0($sp)
lw $t1, -4($fp) # Acceso al CIR de  p1
lw $a0, -0($t1) # Guardamos en $a0 el valor de la variable almacenada en el CIR


sw $a0, -4($sp) # Guardamos el parameotro 0 en el RA
jal IO_out_i32

sw $a0, 0($sp) # Comienzo asignacion
subu $sp, $sp, 4
li $a0, 7

lw $t1, -4($fp) # Cargamos el puntero al CIR de p1
sw $a0, -4($t1) # Guardamos en el CIR el valor asignado
lw $a0, 4($sp)
addiu $sp, $sp, 4 # Fin asignacion

sw $fp, 0($sp)
sw $fp, 0($sp)
lw $t1, -4($fp) # Acceso al CIR de  p1
lw $a0, -0($t1) # Guardamos en $a0 el valor de la variable almacenada en el CIR


sw $a0, -4($sp) # Guardamos el parameotro 0 en el RA
lw $t1, -4($fp) # Acceso al CIR de  p1
lw $a0, -4($t1) # Guardamos en $a0 el valor de la variable almacenada en el CIR


sw $a0, -8($sp) # Guardamos el parameotro 1 en el RA
jal Prueba_suma


sw $a0, -4($sp) # Guardamos el parameotro 0 en el RA
jal IO_out_i32


lw $ra, 4($sp) # Comenzamos el pop del metodo main
addiu $sp, $sp, 28
lw $fp, 0($sp)
jr $ra


li $v0, 10
syscall
