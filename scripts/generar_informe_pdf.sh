#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
pandoc docs/informe_tecnico.md -o informe_tecnico.pdf --toc --number-sections \
  --resource-path=docs:. --pdf-engine=xelatex
