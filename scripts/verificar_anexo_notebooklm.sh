#!/usr/bin/env bash
set -euo pipefail

root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
status=0

check_file() {
  local file="$1"
  local base="${file%/*}"
  while IFS= read -r reference; do
    local image
    image="$(printf '%s\n' "$reference" | sed -E 's/.*\]\((.*)\)/\1/')"
    local path="$base/$image"
    if [[ ! -f "$path" ]]; then
      printf 'FALTA: %s\n' "$path" >&2
      status=1
    fi
  done < <(grep -Eo '!\[[^]]*\]\([^)]*\.png\)' "$file")
}

check_file "$root/docs/anexo_notebooklm/anexo_plantilla.md"
check_file "$root/docs/informe_tecnico.md"

if (( status == 0 )); then
  echo "OK: todas las imágenes referenciadas existen"
fi

exit "$status"
