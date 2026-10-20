#!/usr/bin/env bash
# Load the existing Debug build onto SDK-1.1M (STM32F427/429, FTDI/JTAG).
set -euo pipefail
project_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
cube_dir="${STM32CUBEIDE_HOME:-/opt/st/stm32cubeide_1.19.0}"
shopt -s nullglob
openocd_bins=("$cube_dir"/plugins/com.st.stm32cube.ide.mcu.externaltools.openocd.linux64_*/tools/bin/openocd)
script_dirs=("$cube_dir"/plugins/com.st.stm32cube.ide.mcu.debug.openocd_*/resources/openocd/st_scripts)
if ((${#openocd_bins[@]} == 0 || ${#script_dirs[@]} == 0)); then
  echo "OpenOCD not found. Set STM32CUBEIDE_HOME to your STM32CubeIDE installation." >&2
  exit 1
fi
if [[ ! -f "$project_dir/Debug/SDK_Keyboard.elf" ]]; then
  echo "Build missing. Run bash build.sh first." >&2
  exit 1
fi
cd -- "$project_dir"
exec "${openocd_bins[0]}" -s "${script_dirs[0]}" -f SDK11M.cfg -f flash.tcl
