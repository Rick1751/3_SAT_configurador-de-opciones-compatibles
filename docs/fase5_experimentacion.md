# Fase 5: experimentación

La opción 11 fija semillas, calienta cada solver con 20 ejecuciones, usa cinco
repeticiones y tamaños 12, 14, 16, 18, 20 y 22. Java mide con `nanoTime()` sin
incluir generación, entrada ni escritura. Los CSV reales se escriben en
`data/resultados/` y contienen las familias `aleatoria_4_26`, `sat_plantada` y
`unsat_peor_caso`.

La ejecución actual produjo 180 mediciones crudas (3 familias × 2 algoritmos ×
6 tamaños × 5 repeticiones) y 36 grupos resumen con mediana, promedio,
desviación, mínimos, máximos, bytes asignados y resultado agregado. Las tablas
se generan en [tablas_generadas.md](tablas_generadas.md) y las imágenes PNG se
encuentran en `graficas/`.
