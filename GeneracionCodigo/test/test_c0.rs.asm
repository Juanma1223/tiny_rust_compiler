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

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 2

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 3

lw $t1, 4($sp)
mul $a0, $t1, $a0

addiu $sp, $sp, 4

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 3

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 2

lw $t1, 4($sp)
div $t1, $a0
mflo $a0

addiu $sp, $sp, 4

lw $t1, 4($sp)
sub $a0, $t1, $a0

addiu $sp, $sp, 4

lw $t1, 4($sp)
addu $a0, $t1, $a0

addiu $sp, $sp, 4

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 4

lw $t1, 4($sp)
div $t1, $a0
mfhi $a0

addiu $sp, $sp, 4

sw $a0, -4($sp)
jal IO_out_i32


lw $ra, 4($sp)
lw $fp, 8($sp)
addiu $sp, $sp, 16
jr $ra


li $v0, 10
syscall
