    .data
input_addr:      .word  0x80
output_addr:     .word  0x84
stack_top:       .word  0x1000

    .text
    .org     0x100
_start:
    lui      t0, %hi(input_addr)
    addi     t0, t0, %lo(input_addr)
    lw       t0, 0(t0)
    lw       a0, 0(t0)
    lui      t0, %hi(stack_top)
    addi     t0, t0, %lo(stack_top)
    lw       sp, 0(t0)
    jal      ra, main

    lui      t0, %hi(output_addr)
    addi     t0, t0, %lo(output_addr)
    lw       t0, 0(t0)
    sw       a0, 0(t0)
    halt

main:
    addi     sp, sp, -4
    sw       ra, 0(sp)
    mv       a1, zero                        ; a1 = idx
    mv       a2, zero                        ; a2 = res
    mv       a3, a0                          ; a3 = val
    jal      ra, reverse_bytes

    lw       ra, 0(sp)
    addi     sp, sp, 4
    jr       ra

reverse_bytes:
    addi     sp, sp, -4
    sw       ra, 0(sp)
    addi     t0, zero, 4                     ; t0 = BYTES_IN_WORD
    bne      a1, t0, reverse_step
    mv       a0, a2
    j        end
reverse_step:
    jal      ra, extract_byte                ; a0 = extract_byte(a1=idx, a3=val)
    slli     a2, a2, 8                       ; a2 += reversed_byte
    or       a2, a2, a0
    addi     a1, a1, 1
    jal      ra, reverse_bytes
end:
    lw       ra, 0(sp)
    addi     sp, sp, 4
    jr       ra

extract_byte:
    slli     t0, a1, 3                       ; t0 = idx * 8
    srl      a0, a3, t0                      ; a0 = val >> t0
    andi     a0, a0, 0xFF
    jr       ra
