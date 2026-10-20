#!/usr/bin/env bash
# Build with the installed STM32CubeIDE; the open GUI workspace is not used.
set -euo pipefail
project_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
cube_dir="${STM32CUBEIDE_HOME:-/opt/st/stm32cubeide_1.19.0}"
if [[ ! -x "$cube_dir/headless-build.sh" ]]; then
  echo "STM32CubeIDE not found. Set STM32CUBEIDE_HOME to its installation directory." >&2
  exit 1
fi
build_temp="$(mktemp -d /tmp/sdk-pong-build.XXXXXX)"
trap 'rm -rf -- "$build_temp"' EXIT
cp -a -- "$cube_dir/configuration" "$build_temp/configuration"
"$cube_dir/headless-build.sh" -configuration "$build_temp/configuration" \
  -data "$build_temp/workspace" -import "$project_dir" -build SDK_Keyboard/Debug
