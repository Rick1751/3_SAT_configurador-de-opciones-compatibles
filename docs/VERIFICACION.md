# Verificación de la segunda corrección

Fecha: 2026-11-08. Todas las cifras de esta revisión se obtuvieron de los CSV
actuales en `data/resultados/`.

| ID | Verificación | Comando | Estado | Evidencia real |
|---|---|---|---|---|
| P1 | UNSAT peor caso exacto | `java -ea -cp build tema2.VerificacionPeorCaso` | OK | n=16: 65536/131071; n=20: 1048576/2097151 |
| P2 | Regresión completa | `./tests/correr_todas.sh` | OK | 200 básicas; 500 equivalencias; 200 2-SAT; 100 reducciones |
| P3 | Benchmark real | `timeout 600 java -cp build tema2.Main` opción 11 | OK | Terminó con progreso; 240 mediciones |
| P4 | Timeout registrado | inspección de `Benchmark.java` y CSV | OK | Tiempo >20 s produce `timeout=true`; ninguna fila actual excedió el límite |
| P5 | Encontrar/verificar | `head data/resultados/resultados_encontrar_vs_verificar.csv` | OK | 12 filas, 5 repeticiones por n/familia agregadas; razones >1 |
| P6 | Gráficas | `python services/charts-service/generar_graficas.py` | OK | 01–07 y 06b generadas, no vacías |
| P7 | Tablas | `python services/charts-service/generar_tablas_md.py` | OK | `docs/tablas_generadas.md` escrito desde resumen |
| P8 | Pseudocódigo y complejidad | inspección de 10 `.puml` | CORREGIDO | Contenido numerado, costes, árbol y clases incluidas |
| P9 | PlantUML | `plantuml -checkonly $(find diagramas -name '*.puml')` | NO VERIFICADO | `plantuml` ausente; `sudo pacman` no pudo pedir contraseña |
| P10 | Informe autocontenido | `wc -l docs/informe_tecnico.md` y revisión | CORREGIDO | 15 apartados integrados, figuras y tabla de resultados |
| P11 | PDF | `./scripts/generar_informe_pdf.sh` | NO VERIFICADO | Pandoc existe, pero falta `xelatex.fmt`; no se inventó PDF |
| P12 | Índice y enlaces | script Python de existencia | OK | Archivos enlazados presentes |

## Resultados numéricos comprobados

El CSV crudo contiene 240 filas: cuatro familias, dos algoritmos, seis tamaños
y cinco repeticiones. La verificación exacta imprime:

```text
n=12 fuerza=4096 backtracking=8191
n=14 fuerza=16384 backtracking=32767
```

En `resultados_encontrar_vs_verificar.csv`, para n=22 las razones fuerza
bruta/verificación son 9.571x (SAT plantada), 956.722x (aleatoria) y
30.569.768x (UNSAT peor caso). La verificación usa `verificarSinValidar`,
20.000 llamadas de calentamiento y 100.000 llamadas
cronometradas por instancia.

El barrido de α tiene 20 instancias por punto; el máximo observado de nodos fue
201.063 en α=4.00, con fracción SAT 0.750. Esta cifra describe la muestra real,
no reemplaza el umbral teórico aproximado 4.26.

## Pendientes técnicos

- Instalar PlantUML con `sudo pacman -S --needed plantuml graphviz`, ejecutar
  `-checkonly` y renderizar en `diagramas/render/`.
- Instalar/configurar el formato XeLaTeX o usar WeasyPrint para generar el PDF.

## Pendientes del estudiante

Completar integrantes y portada, capturas y respuestas reales de NotebookLM,
reflexión grupal, confirmación de edición/páginas bibliográficas y entrega PDF.
No se fabricaron respuestas, citas ni capturas.
