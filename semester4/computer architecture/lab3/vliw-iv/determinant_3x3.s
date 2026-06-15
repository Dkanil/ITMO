    .data
input_addr:      .word  0x80
output_addr:     .word  0x84

    .text
    .org 0x100
_start:
    lui s1, %hi(input_addr)      / lui s11, %hi(output_addr)       / nop            / nop
    addi s1, s1, %lo(input_addr) / addi s11, s11, %lo(output_addr) / nop            / nop
    nop                          / nop                             / lw s1, 0(s1)   / nop

    lui s5, %hi(0xCCCCCCCC)      / nop                             / lw t0, 0(s1)   / nop                    ; t0 = x0
    addi s5, s5, %lo(0xCCCCCCCC) / nop                             / lw t1, 0(s1)   / nop                    ; t1 = x1
    nop                          / nop                             / lw t2, 0(s1)   / nop                    ; t2 = x2
    nop                          / nop                             / lw t3, 0(s1)   / nop                    ; t3 = x3
    nop                          / nop                             / lw t4, 0(s1)   / nop                    ; t4 = x4
    mul s2, t2, t4               / mul a4, t1, t3                  / lw t5, 0(s1)   / nop                    ; t5 = x5, s2 = x2*x4, a4 = x1*x3
    mul s4, t1, t5               / mul s8, t0, t5                  / lw t6, 0(s1)   / nop                    ; t6 = x6, s4 = x1*x5, s8 = x0*x5
    mul a2, t0, t4               / mul a0, s2, t6                  / lw a6, 0(s1)   / nop                    ; a6 = x7, a2 = x0*x4, a0 = low(x2*x4*x6)
    mul s10, t2, t3              / mulh a1, s2, t6                 / lw a7, 0(s1)   / nop                    ; a7 = x8, s10 = x2*x3, a1 = high(x2*x4*x6)
    mul t0, a2, a7               / mulh t1, a2, a7                 / lw s11, 0(s11) / nop                    ; t1:t0 = x0*x4*x8
    mul t2, s4, t6               / mulh t3, s4, t6                 / nop            / nop                    ; t3:t2 = x1*x5*x6
    mul t4, s10, a6              / mulh t5, s10, a6                / nop            / nop                    ; t5:t4 = x2*x3*x7
    mul s6, s8, a6               / mulh s7, s8, a6                 / nop            / nop                    ; s7:s6 = x0*x5*x7
    mul tp, a4, a7               / mulh s0fp, a4, a7               / nop            / nop                    ; s0fp:tp = x1*x3*x8

    add t0, t0, t2               / add t1, t1, t3                  / nop            / bleu t2, t0, carry1    ; res = x0*x4*x8 + x1*x5*x6
    addi t1, t1, 1               / nop                             / nop            / nop
carry1:
    add t0, t0, t4               / add t1, t1, t5                  / nop            / bleu t4, t0, carry2    ; res = res + x2*x3*x7
    addi t1, t1, 1               / nop                             / nop            / nop

carry2:
    sub t1, t1, s7               / nop                             / nop            / bleu s6, t0, borrow1   ; res = res - x0*x5*x7
    addi t1, t1, -1              / nop                             / nop            / nop
borrow1:
    sub t1, t1, s0fp             / sub t0, t0, s6                  / nop            / bleu tp, t0, borrow2   ; res = res - x1*x3*x8
    addi t1, t1, -1              / nop                             / nop            / nop
borrow2:
    sub t1, t1, a1               / sub t0, t0, tp                  / nop            / bleu a0, t0, borrow3   ; res = res - x2*x4*x6
    addi t1, t1, -1              / nop                             / nop            / nop
borrow3:
    sub t0, t0, a0               / nop                             / nop            / bgt t1, zero, overflow

    nop                          / nop                             / sw t0, 4(s1)   / j end
overflow:
    nop                          / nop                             / sw s5, 4(s1)   / nop
end:
    nop                          / nop                             / nop            / halt
