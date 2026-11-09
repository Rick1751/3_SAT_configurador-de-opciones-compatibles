# Fase 4: complejidad

| Algoritmo | Mejor | Promedio | Peor | Espacio |
|---|---:|---:|---:|---:|
| Validación | Θ(m) | Θ(m) | Θ(m) | O(1) adicional |
| Verificación | Ω(1) | O(m) | Θ(m) | O(1) |
| Fuerza bruta | Ω(m) | O(2^n m) | Θ(2^n m) | O(n+m) |
| Backtracking | O(nm) | dependiente de poda | O(2^n m) | O(n) |
| 2-SAT | Θ(n+m) | Θ(n+m) | Θ(n+m) | O(n+m) |

Cada una de las `2^n` hojas representa una decisión booleana; en cada hoja se
revisan m cláusulas. El modelo experimental usa fórmulas uniformes con
`alpha=m/n`, incluyendo el umbral aproximado `alpha=4.26`; el caso promedio
depende de esa razón y de la distribución. Backtracking no mejora la cota
asintótica, pero poda ramas completas en la práctica.
