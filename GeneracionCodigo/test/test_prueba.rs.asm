.text
.globl Fantasma_main

Prueba_suma:
move $fp, $sp
subu $sp, $sp, 20
sw $fp, 8($sp)
sw $ra, 4($sp)
li $a0, 5

sw $a0, 0($fp)


lw $ra, 4($sp)
lw $fp, 8($sp)
addiu $sp, $sp, 20
jr $ra


main
move $fp, $sp
subu $sp, $sp, 20
sw $fp, 8($sp)
sw $ra, 4($sp)
jal Prueba_suma



lw $ra, 4($sp)
lw $fp, 8($sp)
addiu $sp, $sp, 20
jr $ra


li $v0, 10
syscall
