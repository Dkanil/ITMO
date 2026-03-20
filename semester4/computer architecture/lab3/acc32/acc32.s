    .data
.org             0x000
buf:             .byte  '________________________________'

c4:              .word  4
c8:              .word  8
c255:            .word  0xFF
eol:             .word  10
v32:             .word  32
a_val:           .word  97
z_val:           .word  122

tmp_char:        .word  0x00
curr_ptr:        .word  0x00
stop_flag:       .word  0
curr_shift:      .word  0x00
char_mask:       .word  0x00
curr_ptr_word:   .word  0x00

    .text
    .org         0x100
_start:
read:
    load_addr    0x80
    store        tmp_char                    ; tmp_char = acc
check_char:
    sub          eol                         ; acc -= 10 ('\n')
    beqz         success                     ; if acc == 0, goto success
    load_imm     1                           ; \
    add          curr_ptr                    ;  |
    sub          v32                         ;  | if curr_ptr == 31, goto handle_overflow
    beqz         handle_overflow             ; /

    load         tmp_char                    ; \
    sub          a_val                       ;  | if acc < 'a', goto pack_in_buf
    ble          pack_in_buf                 ; /
    load         tmp_char                    ; \
    sub          z_val                       ;  | if acc > 'z', goto pack_in_buf
    bgt          pack_in_buf                 ; /
to_upper:
    load         tmp_char                    ; acc = tmp_char
    sub          v32                         ; acc -= 32
    store        tmp_char                    ; tmp_char = acc
pack_in_buf:
    load         curr_ptr                    ; \
    rem          c4                          ;  |
    mul          c8                          ;  | curr_shift = (curr_ptr % 4) * 8
    store        curr_shift                  ; /

    load_imm     0xFF                        ; \
    shiftl       curr_shift                  ;  | create char_mask
    not                                      ;  |
    store        char_mask                   ; /

    load         tmp_char                    ;
    shiftl       curr_shift                  ; tmp_char = tmp_char << curr_shift
    store        tmp_char                    ;

    load         curr_ptr
    div          c4
    mul          c4
    store        curr_ptr_word


    load         curr_ptr_word               ; \
    load_acc                                 ;  |
    and          char_mask                   ;  | add tmp_char in buf
    add          tmp_char                    ;  |
    store_ind    curr_ptr_word               ; /

    load         stop_flag
    bnez         output                      ; if tmp_char == 0, goto output

    load_imm     1                           ; \
    add          curr_ptr                    ; | curr_ptr++
    store        curr_ptr                    ; /
    jmp          read

success:
    load_imm     1
    store        stop_flag
    load_imm     0
    store        tmp_char                    ; mem[tmp_char] = 0
    jmp          pack_in_buf

output:
    load_imm     0                           ; acc = 0
    store        curr_ptr                    ; mem[curr_ptr] = 0
ouput_loop:
    load         curr_ptr                    ; \
    rem          c4                          ;  | curr_shift = (curr_ptr % 4) * 8
    mul          c8                          ;  |
    store        curr_shift                  ; /

    load         curr_ptr                    ; \
    div          c4                          ;  |
    mul          c4                          ;  | curr_ptr_word = (curr_ptr / 4) * 4
    load_acc                                 ;  | load word from buf
    shiftr       curr_shift                  ;  | shift it to the right position
    and          c255                        ;  | get char from word
    beqz         end                         ;  | if acc == 0, goto end
    store_addr   0x84                        ; /

    load_imm     1                           ; \
    add          curr_ptr                    ;  |curr_ptr++
    store        curr_ptr                    ; /
    jmp          ouput_loop

handle_overflow:
    load_imm     0xCCCCCCCC                  ; acc = 0xCC
    store_addr   0x84
end:
    halt
