.text
.globl main

main:
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

lw $t1, 4($sp)
add $a0, $t1, $a0

addiu $sp, $sp, 4


li $v0, 10
syscall


