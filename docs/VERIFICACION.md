# Verificación de corrección

Fecha de ejecución: 2026-11-08. Las cifras de benchmark citadas aquí provienen
de `data/resultados/` generado por la ejecución de la opción 11.

## Tabla de evidencias

| ID | Verificación | Comando | Estado | Evidencia real |
|---|---|---|---|---|
| A1 | Higiene | `git ls-files \| grep -E '\.class\|__pycache__\|build/'` | CORREGIDO | Sin resultados después de retirar artefactos del índice |
| A2 | Compilación Java 17 | `javac --release 17 -Xlint:all ...` | OK | Compiló sin errores ni warnings |
| A3 | Pruebas | `./tests/correr_todas.sh` | OK | 200 casos; 500 equivalencias; 200 2-SAT y 100 reducciones |
| B1 | Solver sin fórmula | `printf '6\n13\n' \| java -cp build tema2.Main` | CORREGIDO | Mensaje amigable, sin stack trace ni `null` |
| B2 | Fórmula inválida | opción 2 + `invalida.txt` + opción 6 | CORREGIDO | Explica cláusula de 2 literales y literal 0 |
| B3 | Repeticiones | opción 1 + `1 1 -2` + opción 4 | CORREGIDO | `FÓRMULA 3-CNF VÁLIDA` y aviso separado |
| B4 | Entrada no numérica | `printf 'abc\n99\n\n13\n' ...` | CORREGIDO | `Ingrese un número entero.` y reintento |
| C1 | Fuerza bruta | inspección de `SolverService` | CORREGIDO | Evalúa máscara directamente; solo crea asignación al hallar SAT |
| C2 | Profundidad | `java -ea ... VerificacionEquivalencia` | OK | `Resultado` incluye `profundidadMaxima` |
| D1 | Benchmark | opción 11 con `timeout 180` | OK | Terminó dentro de 180 s con progreso por configuración |
| D2 | CSV | `wc -l data/resultados/*.csv` | OK | 180 mediciones crudas, 36 filas resumen y CSV auxiliares |
| E1 | Gráficas | `python services/charts-service/generar_graficas.py` | OK | PNG 01–07 y 06b no vacíos (8 archivos) |
| E2 | Tablas | `python services/charts-service/generar_tablas_md.py` | OK | Generó `docs/tablas_generadas.md` sin dependencia `tabulate` |
| F1 | PlantUML | `plantuml -checkonly $(find diagramas -name '*.puml')` | NO VERIFICADO | `plantuml` no está instalado en este entorno |
| G1 | Documentación | comprobación de archivos y enlaces | CORREGIDO | Informe de 15 apartados, anexo y enlaces creados |
| H1 | NotebookLM | inspección de `docs/notebooklm_guia.md` | OK | Plantillas, preguntas y capturas sin respuestas inventadas |

## Versiones reales

```text
java -version  -> OpenJDK 27.0.1
javac -version -> 27.0.1
python --version -> Python 3.14.9
plantuml -version -> comando no encontrado
dot -V -> graphviz 16.1.0
```

Aunque el compilador instalado es Java 27, el comando verificado usa
`javac --release 17`, por lo que las clases no usan APIs posteriores a Java 17.

## Pruebas funcionales

```text
200 pruebas de equivalencia y validaciones básicas: OK
total=500 coincidencias=500 discrepancias=0 SAT=316 UNSAT=184
2SAT=200, reducción=100, n=100000 tiempo_ms=70.184
```

El benchmark final tiene familias `aleatoria_4_26`, `sat_plantada` y
`unsat_peor_caso`, seis tamaños de 12 a 22 y cinco repeticiones por
configuración. La familia UNSAT contiene las ocho cláusulas que prohíben las
combinaciones de las tres últimas variables, dejando libre el prefijo.

El CSV crudo tiene las columnas `bytes_asignados`, `profundidad_maxima`,
`memoria_analitica_bytes` y `timeout`. La medición de memoria usa
`ThreadMXBean#getThreadAllocatedBytes`; la generación, el parseo y la escritura
están fuera del cronómetro del solver.

La escalabilidad 2-SAT se midió hasta n=1.000.000; el CSV registra
2.216.921.648 ns para esa configuración.

## Problemas corregidos

1. Validación separada en errores y avisos mediante `ResultadoValidacion`.
2. Rechazo explícito de fórmulas inválidas antes de resolver.
3. Entrada numérica con reintento y mensajes sin excepciones internas.
4. Fuerza bruta sin `new boolean[n]` por máscara.
5. Profundidad máxima de backtracking.
6. Configurador con nombres en restricciones (`-OLED Bateria Cargador`).
7. Benchmark con calentamiento, familias, repeticiones, memoria y resúmenes.
8. Gráficas basadas en medianas y columnas reales.
9. Tablas Markdown generadas automáticamente.
10. `.gitignore`, compilación `--release 17`, informe técnico y anexo.

## Pendientes del estudiante

- Instalar PlantUML y ejecutar `plantuml -checkonly ...` y el render en
  `diagramas/render/`.
- Completar capturas, respuestas y reflexión real de NotebookLM.
- Añadir integrantes y datos de portada.
- Confirmar edición y páginas de referencias bibliográficas.
- Generar el PDF final si se requiere una herramienta externa.
