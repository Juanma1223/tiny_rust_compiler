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


main:
move $fp, $sp
subu $sp, $sp, 16
sw $fp, 8($sp)
sw $ra, 4($sp)
li $a0, 1

sw $a0, -4($sp)
jal IO_out_i32

.data
char1: .asciiz "a"
.text
la $a0, char1

sw $a0, -4($sp)
jal IO_out_char

li $a0, 0

sw $a0, -4($sp)
jal IO_out_bool

.data
string2: .asciiz "Hola mundo"
.text
la $a0, string2

sw $a0, -4($sp)
jal IO_out_string


lw $ra, 4($sp)
lw $fp, 8($sp)
addiu $sp, $sp, 16
jr $ra


li $v0, 10
syscall
