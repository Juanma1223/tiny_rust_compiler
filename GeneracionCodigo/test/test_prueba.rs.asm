.text
.globl Fantasma_main

Prueba_suma:
subu $sp, $sp, 20
sw $fp, 8($sp)
sw $ra, 4($sp)

la $t1, ($a0)
li $a0, 3

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 4

lw $t1, 4($sp)
add $a0, $t1, $a0

addiu $sp, $sp, 4

sw $a0, ($t1)



lw $ra, 4($sp)
lw $fp, 8($sp)
addiu $sp, $sp, 20
jr $ra

la $t1, ($a0)
li $a0, 3

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 4

lw $t1, 4($sp)
add $a0, $t1, $a0

addiu $sp, $sp, 4

sw $a0, ($t1)





Fantasma_main:
subu $sp, $sp, 20
sw $fp, 8($sp)
sw $ra, 4($sp)
jal Prueba_suma



lw $ra, 4($sp)
lw $fp, 8($sp)
addiu $sp, $sp, 20
jr $ra
jal Prueba_suma





li $v0, 10
syscall
