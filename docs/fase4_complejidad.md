# Fase 4: complejidad

## Conteo de operaciones

La validación visita m cláusulas y tres posiciones: `Θ(m)`. La verificación
puede detenerse en la primera cláusula falsa, por lo que su mejor caso es
`Ω(1)`, y en el peor recorre `3m`, es decir `O(m)`.

Fuerza bruta genera `2^n` máscaras. Para cada máscara revisa hasta m cláusulas
y tres literales, por lo que el peor caso es `Θ(2^n·3m)=Θ(2^n·m)`. El código
evalúa directamente bits y solo crea el certificado al encontrar SAT. Su
espacio adicional es `O(n+m)` considerando fórmula y asignación.

Backtracking agrega un nivel por variable. Sin poda visita el árbol completo:
`1+2+...+2^n = 2^(n+1)-1` nodos. Cada nodo puede recorrer m cláusulas, dando
`O(2^n·m)`; la profundidad máxima es `n`, por lo que el espacio adicional es
`O(n)`. La poda mejora instancias prácticas, pero no la cota asintótica.

2-SAT crea `2n` nodos, dos aristas por cada una de las m cláusulas y ejecuta
dos DFS de Kosaraju: `Θ(n+m)` tiempo y espacio. La reducción visita la suma de
los tamaños de las cláusulas, `Θ(Σ|Ci|)`, y conserva equisatisfacibilidad.
Generar una instancia de tres literales por cláusula cuesta `Θ(m)`.

## Casos

| Algoritmo | Mejor | Promedio (`α=m/n`) | Peor | Espacio |
|---|---|---|---|---|
| Validación | Θ(m) | Θ(m) | Θ(m) | O(1) adicional |
| Verificación | Ω(1) | O(m) | Θ(m) | O(1) |
| Fuerza bruta | Ω(m) | Θ(2^n m) | Θ(2^n m) | O(n+m) |
| Backtracking | Ω(nm) | depende de poda | O(2^n m) | O(n) |
| 2-SAT | Θ(n+m) | Θ(n+m) | Θ(n+m) | O(n+m) |

El promedio se interpreta sobre fórmulas uniformes con razón `α`; cerca de
`α≈4.26` las instancias aleatorias suelen concentrar dificultad. JIT, cachés,
instancias SAT que terminan temprano y ruido de tamaños pequeños explican que
las razones medidas no sean exactamente potencias de dos.
