# Fase 3: implementación

`Formula` contiene `int variables` y `List<int[]>`; cada literal es un entero
con signo. `Resultado` devuelve SAT, asignación, nodos, asignaciones y tiempo.
`FormulaService` parsea y valida; `SolverService` verifica, enumera y poda;
`Generador` crea instancias reproducibles; `Benchmark` escribe CSV; `Main`
orquesta el menú.

## Pruebas funcionales

| Caso | Entrada | Salida esperada |
|---|---|---|
| SAT simple | `1 1 / 1 1 1` | SAT |
| UNSAT mínima | `1 2 / 1 1 1 / -1 -1 -1` | UNSAT |
| Repetición | `1 1 / 1 1 1` | válida |
| Dos literales | `2 1 / 1 2` | inválida |
| Fuera de rango | `1 1 / 2 2 2` | inválida |
| Literal cero | `1 1 / 0 1 1` | inválida |
| Fórmula vacía | `0 0` | rechazada |
| Tautología | `2 1 / 1 -1 2` | aviso |

La clase `Pruebas` compara ambos solvers en 200 fórmulas aleatorias pequeñas y
comprueba las asignaciones devueltas.
