# Fase 5: experimentación

La opción 11 fija semillas, usa cinco repeticiones y tamaños 8, 10, 12, 14, 16
y 18. Java mide con `System.nanoTime()` sin incluir entrada ni impresión. Los
CSV reales se escriben en `data/resultados/` y contienen las familias SAT,
aleatoria y UNSAT.

Tras la ejecución actual, `resultados_tiempo.csv` contiene 180 mediciones
(3 familias × 2 algoritmos × 6 tamaños × 5 repeticiones), y
`resumen_tiempo.csv` contiene 36 grupos con mediana y desviación. Las gráficas
PNG se encuentran en `graficas/`.
