#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
rm -rf build
mkdir build
javac --release 17 -Xlint:all -d build $(find services tests -name '*.java')
java -ea -cp build tema2.Pruebas
java -ea -cp build tema2.VerificacionEquivalencia
java -ea -cp build tema2.VerificacionAvanzada
java -ea -cp build tema2.VerificacionPeorCaso
