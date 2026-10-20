# Use a conservative JTAG clock for the integrated FTDI debugger.
adapter speed 1000
init
reset halt
set device_id [expr {[lindex [read_memory 0xE0042000 32 1] 0] & 0xfff}]
if {$device_id != 0x419} {
    resume
    error "This firmware requires STM32F427/429 (device ID 0x419); connected ID: $device_id"
}
program Debug/SDK_Keyboard.elf verify reset exit
