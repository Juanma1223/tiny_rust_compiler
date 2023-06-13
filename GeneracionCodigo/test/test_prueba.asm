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

Prueba_prueba:
move $fp, $sp # Comienza la creacion de RA de prueba
subu $sp, $sp, 12
sw $ra, 4($sp)
sw $a0, 0($sp) # Comienzo asignacion
subu $sp, $sp, 4
li $a0, 1

lw $t1, -4($fp) # Acceso al CIR de self
sw $a0, -0($t1) # Guardamos en el CIR el valor asignado
lw $a0, 4($sp)
addiu $sp, $sp, 4 # Fin asignacion


lw $ra, 4($sp) # Comenzamos el pop del metodo prueba
addiu $sp, $sp, 12
lw $fp, 0($sp)
jr $ra


Prueba2_prueba2:
move $fp, $sp # Comienza la creacion de RA de prueba2
subu $sp, $sp, 12
sw $ra, 4($sp)

lw $ra, 4($sp) # Comenzamos el pop del metodo prueba2
addiu $sp, $sp, 12
lw $fp, 0($sp)
jr $ra


main:
move $fp, $sp # Comienza la creacion de RA de main
subu $sp, $sp, 16
sw $ra, 4($sp)

lw $ra, 4($sp) # Comenzamos el pop del metodo main
addiu $sp, $sp, 16
lw $fp, 0($sp)
jr $ra


li $v0, 10
syscall
