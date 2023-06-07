.text
.globl main
IO_out_i32:
move $fp, $sp
subu $sp, $sp, 20
sw $fp, 8($sp)
sw $ra, 4($sp)
sw $a0, -4($fp)
li $v0, 1
syscall
lw $ra, 4($sp)
lw $fp, 8($sp)
addiu $sp, $sp, 20
jr $ra

IO_out_bool:
move $fp, $sp
subu $sp, $sp, 20
sw $fp, 8($sp)
sw $ra, 4($sp)
sw $a0, -4($fp)
li $v0, 1
syscall
lw $ra, 4($sp)
lw $fp, 8($sp)
addiu $sp, $sp, 20
jr $ra

IO_out_char:
move $fp, $sp
subu $sp, $sp, 20
sw $fp, 8($sp)
sw $ra, 4($sp)
sw $a0, -4($fp)
li $v0, 4
syscall
lw $ra, 4($sp)
lw $fp, 8($sp)
addiu $sp, $sp, 20
jr $ra

IO_out_string:
move $fp, $sp
subu $sp, $sp, 20
sw $fp, 8($sp)
sw $ra, 4($sp)
sw $a0, -4($fp)
li $v0, 4
syscall
lw $ra, 4($sp)
lw $fp, 8($sp)
addiu $sp, $sp, 20
jr $ra

IO_in_i32:
move $fp, $sp
subu $sp, $sp, 20
sw $fp, 8($sp)
sw $ra, 4($sp)
sw $a0, -4($fp)
li $v0, 5
syscall
lw $ra, 4($sp)
lw $fp, 8($sp)
addiu $sp, $sp, 20
jr $ra

IO_in_bool:
move $fp, $sp
subu $sp, $sp, 20
sw $fp, 8($sp)
sw $ra, 4($sp)
sw $a0, -4($fp)
li $v0, 5
syscall
lw $ra, 4($sp)
lw $fp, 8($sp)
addiu $sp, $sp, 20
jr $ra

IO_in_char:
move $fp, $sp
subu $sp, $sp, 20
sw $fp, 8($sp)
sw $ra, 4($sp)
sw $a0, -4($fp)
li $v0, 8
syscall
lw $ra, 4($sp)
lw $fp, 8($sp)
addiu $sp, $sp, 20
jr $ra

IO_in_string:
move $fp, $sp
subu $sp, $sp, 20
sw $fp, 8($sp)
sw $ra, 4($sp)
sw $a0, -4($fp)
li $v0, 8
syscall
lw $ra, 4($sp)
lw $fp, 8($sp)
addiu $sp, $sp, 20
jr $ra



Prueba_suma:
move $fp, $sp
subu $sp, $sp, 28
sw $fp, 8($sp)
sw $ra, 4($sp)
la $a0, -12($fp)

la $t0, ($a0)
la $a0, -4($fp)

sw $a0, 0($sp)
addiu $sp, $sp, -4
la $a0, -8($fp)

lw $t1, 4($sp)
addu $a0, $t1, $a0

addiu $sp, $sp, 4

sw $a0, ($t0)

la $a0, -12($fp)

sw $a0, 0($fp)


lw $ra, 4($sp)
lw $fp, 8($sp)
addiu $sp, $sp, 28
jr $ra


main:
move $fp, $sp
subu $sp, $sp, 24
sw $fp, 8($sp)
sw $ra, 4($sp)
la $a0, -4($fp)

la $t0, ($a0)
.data # Constructor de Prueba
.Prueba0: .space 12
.text
la $a0, Prueba0($0)

sw $a0, ($t0)

la $a0, -8($fp)

la $t0, ($a0)
la $a0, -4($fp)
li $a0, 2

sw $a0, -4($sp)
li $a0, 3

sw $a0, -8($sp)
jal Prueba_suma


sw $a0, ($t0)

la $a0, -8($fp)

sw $a0, -4($sp)
jal IO_out_i32


lw $ra, 4($sp)
lw $fp, 8($sp)
addiu $sp, $sp, 24
jr $ra


li $v0, 10
syscall
