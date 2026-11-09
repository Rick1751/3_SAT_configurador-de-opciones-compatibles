# Fase 6: análisis y conclusiones

1. El tiempo depende de n, m, distribución, orden de variables y poda.
2. Al aumentar n, el espacio potencial crece exponencialmente. En el CSV actual,
las mediciones de fuerza bruta para n=8, 10, 12, 14, 16 y 18 son observables en
`data/resultados/resultados_tiempo.csv`.
3. La tendencia experimental coincide cualitativamente con la expectativa
teórica: fuerza bruta explora más asignaciones, mientras que el backtracking
poda. La comparación concreta se resume por mediana en
`data/resultados/resumen_tiempo.csv`.
4. Encontrar explora potencialmente `2^n` asignaciones; verificar un certificado cuesta `O(m)`.
5. La implementación no incluye propagación unitaria, heurísticas ni CDCL.
6. DPLL, selección de variable, literales puros y CDCL son mejoras naturales.
7. Puede usarse para prototipos de configuración, planificación y validación de reglas.

La solución prioriza transparencia educativa: dos algoritmos exactos, entradas
explicables y mediciones reproducibles, a cambio de no competir con solvers SAT
industriales.
