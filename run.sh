#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
rm -rf build
mkdir -p build
javac -d build $(find services tests -name '*.java')
java -cp build tema2.Main
