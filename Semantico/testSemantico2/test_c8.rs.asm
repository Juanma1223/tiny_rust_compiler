.text
.globl Fantasma_main
Prueba_constructor:

la $t1, ($a0)
li $a0, 42

sw $a0, ($t1)



Prueba_m1:
while1:
li $a0, 1

sw $a0, 0($sp)
addiu $sp, $sp, -4

lw $t1, 4($sp)
slt $a0, $a0, $t1
xori $a0, $a0, 1

addiu $sp, $sp, 4

bne $a0, 1, endwhile1

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 0

lw $t1, 4($sp)
xor $a0, $t1, $a0
sltu $a0, $a0, 1

addiu $sp, $sp, 4

sw $a0, 0($sp)
addiu $sp, $sp, -4

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 1

lw $t1, 4($sp)
xor $a0, $t1, $a0
sltu $a0, $0, $a0

addiu $sp, $sp, 4

lw $t1, 4($sp)
or $a0, $t1, $a0

addiu $sp, $sp, 4

bne $a0, 1, else2

la $t1, ($a0)
li $a0, 1

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 2

lw $t1, 4($sp)
slt $a0, $t1, $a0

addiu $sp, $sp, 4

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 2

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 1

lw $t1, 4($sp)
slt $a0, $a0, $t1

addiu $sp, $sp, 4

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 1

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 2

lw $t1, 4($sp)
slt $a0, $a0, $t1
xori $a0, $a0, 1

addiu $sp, $sp, 4

lw $t1, 4($sp)
and $a0, $t1, $a0

addiu $sp, $sp, 4

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 2

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 1

lw $t1, 4($sp)
slt $a0, $t1, $a0
xori $a0, $a0, 1

addiu $sp, $sp, 4

lw $t1, 4($sp)
or $a0, $t1, $a0

addiu $sp, $sp, 4

lw $t1, 4($sp)
or $a0, $t1, $a0

addiu $sp, $sp, 4

sw $a0, ($t1)


la $t1, ($a0)
li $a0, 1

xori $a0, $a0, 1


xori $a0, $a0, 1


xori $a0, $a0, 1


sw $a0, ($t1)


j endif2
else2:

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 0

lw $t1, 4($sp)
slt $a0, $t1, $a0
xori $a0, $a0, 1

addiu $sp, $sp, 4

bne $a0, 1, else3

la $t1, ($a0)

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 1

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 2

lw $t1, 4($sp)
mul $a0, $t1, $a0

addiu $sp, $sp, 4

lw $t1, 4($sp)
add $a0, $t1, $a0

addiu $sp, $sp, 4

sw $a0, ($t1)


j endif3
else3:

la $t1, ($a0)

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 1

sw $a0, 0($sp)
addiu $sp, $sp, -4
li $a0, 2

lw $t1, 4($sp)
div $t1, $a0
mfhi $a0

addiu $sp, $sp, 4

lw $t1, 4($sp)
sub $a0, $t1, $a0

addiu $sp, $sp, 4

sw $a0, ($t1)


endif3:


endif2:

li $a0, 3

addiu $a0, $a0, 1


addiu $a0, $a0, 1


addiu $a0, $a0, -1


addiu $a0, $a0, 1


addiu $a0, $a0, -1


addiu $a0, $a0, 1



j while1
endwhile1:




Prueba_get_a:



Prueba_imprimo_algo:




Fantasma_main:

la $t1, ($a0)

sw $a0, ($t1)


la $t1, ($a0)

sw $a0, ($t1)





li $v0, 10
syscall
