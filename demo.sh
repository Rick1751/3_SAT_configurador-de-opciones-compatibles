#!/usr/bin/env bash
# DEMO GUIADA: escribe las entradas por ti y espera en silencio a que pulses Enter en cada paso.
# Uso: ./demo.sh      (Enter = siguiente paso; no se muestra ningún texto extra)
cd "$(dirname "$0")"
mkdir -p build
javac --release 17 -d build $(find services tests -name '*.java') || exit 1

FIFO=$(mktemp -u); mkfifo "$FIFO"
java -Dfile.encoding=UTF-8 -cp build tema2.Main < "$FIFO" &
JAVA_PID=$!
exec 3>"$FIFO"

# paso "nota para ti (NO se muestra)" "entrada1" "entrada2" ...
# Espera un Enter en silencio (no escribe nada en pantalla) y luego envía las entradas.
paso() {
  shift
  { read -rs < /dev/tty; } 2>/dev/null || sleep 3
  for linea in "$@"; do echo "$linea" >&3; sleep 0.5; done
  sleep 2
}

paso "Fórmula de ejemplo con 3 opciones (x1,x2,x3) y 4 reglas de compatibilidad (cada cláusula son 3 literales; negativo = negado)." \
  1 3 4 "1 2 3" "-1 2 3" "1 -2 3" "1 2 -3"
paso "El programa valida la estructura antes de resolver: exactamente 3 literales por cláusula, variables en rango." 4
paso "FUERZA BRUTA: prueba las 2^n combinaciones. Peor caso Θ(2^n·m). Muestra una asignación que cumple todo." 5
paso "BACKTRACKING: asigna una variable por vez y poda cuando una cláusula queda totalmente falsa. Peor caso sigue siendo O(2^n·m)." 6
paso "VERIFICAR es barato (O(m)): probamos x1=V, x2=V, x3=F → debe satisfacer." 8 1 1 0
paso "Ahora una asignación mala x1=F, x2=F, x3=F → indica qué cláusula falla. Encontrar es exponencial, verificar es lineal: eso es NP." 8 0 0 0
paso "MODO CONFIGURADOR: opciones con nombres reales y reglas en lenguaje sencillo (el '-' niega)." \
  3 3 OLED Bateria Cargador 2 "-OLED Bateria Cargador" "OLED OLED OLED" 6
paso "Validación de una fórmula INVÁLIDA: el programa explica por qué se rechaza." 2 data/instancias/invalida.txt 4
paso "Fórmula INSATISFACIBLE (UNSAT): se explora todo el espacio de búsqueda y se informa." 2 data/instancias/unsat_minima.txt 6
paso "Generamos el PEOR CASO UNSAT (familia 3, n=16, solo las 8 cláusulas que prohíben las 3 últimas variables, semilla 1)." 9 3 16 8 1
paso "COMPARAMOS: mira la columna de NODOS: fuerza bruta prueba 2^n=65536 asignaciones y backtracking visita 2^(n+1)-1=131071 nodos (el doble). En el peor caso la poda no ayuda. Ojo: el tiempo en vivo puede variar porque la JVM aun no se calienta; en el benchmark con calentamiento, n=22: backtracking 265 ms vs fuerza bruta 107 ms." 7
paso "Tabla de complejidad teórica de cada algoritmo." 12
paso "Fin de la demo. Salimos del programa." 13

exec 3>&-
wait $JAVA_PID 2>/dev/null
rm -f "$FIFO"
echo; echo "=== DEMO TERMINADA ==="