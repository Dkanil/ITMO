    .data
input:           .word  0x80
output:          .word  0x84

    .text
    .org 0x100
_start:
    @p input b! @b           \ stack: [N], b: 0x80
    dup -if read_value
    end
read_value:
    dup if end
    lit -1 + >r              \ stack: [], r: N - 1
    lit 0
    lit 0 a!                 \ stack: [HW], a: LW
    lit 1 eam
sum_loop:
    @b                       \ stack: [HW, V], a: LW
    sign_extend              \ x32 -> x64 (V -> V_HW, V_LW)
    sum_two                  \ stack: [HW, V_LW, V_HW], a: LW -> stack: [HW_new], a: LW_new
    next sum_loop
end:
    a over                   \ stack: [LW, HW]
    @p output a! ! !
    halt

sign_extend:
    dup -if v_pos
    lit -1 ;
v_pos:
    lit 0 ;

sum_two:
    over a                   \ stack: [HW, V_HW, V_LW, LW]
    + a! +
    ;
