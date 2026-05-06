    .data
input_addr:      .word  0x80
output_addr:     .word  0x84
buffer_addr:     .word  0x400
stack_top:       .word  0x1000

    .text
    .org     0x100
_start:
    movea.l  stack_top, A7
    movea.l  (A7), A7
    movea.l  input_addr, A0
    movea.l  (A0), A0
    movea.l  output_addr, A1
    movea.l  (A1), A1
    jsr      main
    halt

main:
    link     A6, -8
    clr.l    -4(A6)                          ; -4(A6) = input_length
    clr.l    -8(A6)                          ; -8(A6) = output_length
    movea.l  buffer_addr, A2                 ; A2 = buffer_addr
    movea.l  (A2), A2
    clr.l    D2                              ; D2 = curr_length
main_loop:
    move.l   -4(A6), D0                      ; D0 = input_length
    cmp.b    64, D0                          ; 64 = MAX_INPUT_LEN
    beq      err_overflow
    move.b   (A0), D4                        ; D4 = curr_char
    add.b    1, -4(A6)

    cmp.b    10, D4                          ; 10 = EOL
    beq      end_of_input
    cmp.b    0, D4                           ; 0 = EOF
    beq      err_eof
    cmp.b    0, D2                           ; D2 = curr_length
    beq      update_pair

    cmp.b    D1, D4                          ; D1 = last_char
    bne      write_in_buf
    cmp.b    9, D2                           ; 9 = MAX_COMRESSION_SIZE
    beq      write_in_buf
    add.b    1, D2                           ; (D2 = curr_length)++
    jmp      main_loop

write_in_buf:
    jsr      save_pair
update_pair:
    move.b   D4, D1                          ; D1 = last_char
    move.l   1, D2                           ; D2 = curr_length
    jmp      main_loop

end_of_input:
    cmp.b    0, D2                           ; Если строка пустая, то завершаем
    beq      exit
    jsr      save_pair
    jsr      output
    jmp      exit

save_pair:
    move.l   -8(A6), D0                      ; D0 = output_length
    add.b    2, D0
    cmp.b    62, D0                          ; 62+2 = MAX_INPUT_LEN
    bgt      err_overflow
    move.l   D2, D3                          ; D3 = counter
    add.l    48, D3                          ; 48 - сдвиг для кодировки
    move.b   D3, (A2)+                       ; записываем счетчик в буфер
    move.b   D1, (A2)+                       ; записываем символ в буфер

    move.l   D0, -8(A6)                      ; D0 = output_length
    rts

err_overflow:
    move.l   0xcccccccc, (A1)
    jmp      exit
err_eof:
    move.l   0xffffffff, (A1)
exit:
    unlk     A6
    rts

output:
    move.l   -8(A6), D5                      ; D5 = output_length
    movea.l  buffer_addr, A3
    movea.l  (A3), A3
    clr.b    D6                              ; D6 = output_index
output_loop:
    cmp.b    D5, D6
    beq      finish_output
    move.b   (A3)+, (A1)                     ; A1 = output_addr
    add.b    1, D6
    jmp      output_loop
finish_output:
    rts
