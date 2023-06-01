.text
.globl Fantasma_main

Fantasma_main:
li $a0, 1

bne $a0, 1, else1
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


j endif1
else1:
li $a0, 1

xori $a0, $a0, 1


li $a0, 1

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 0

lw $t1, 4($sp)
or $a0, $t1, $a0

addiu $sp, $sp, 4

bne $a0, 1, else2
li $a0, 5

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 2

lw $t1, 4($sp)
div $t1, $a0
mflo $a0

addiu $sp, $sp, 4


j endif2
else2:
li $a0, 5

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 2

lw $t1, 4($sp)
div $t1, $a0
mfhi $a0

addiu $sp, $sp, 4


endif2:


endif1:

while3:
li $a0, 0

bne $a0, 1, endwhile3

j while3
endwhile3:




li $v0, 10
syscall
