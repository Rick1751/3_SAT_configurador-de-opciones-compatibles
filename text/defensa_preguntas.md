# Preguntas de defensa

1. **¿Por qué aparece `2^n`?** Cada variable tiene dos valores y hay n decisiones.
2. **¿Por qué verificar es `O(m)`?** Se inspeccionan tres posiciones por cláusula.
3. **¿Backtracking mejora el peor caso?** No; solo poda favorablemente casos concretos.
4. **¿Las repeticiones son válidas?** Sí, son posiciones y no rompen 3-CNF.
5. **¿Qué es una tautología?** Una cláusula con un literal y su negación.
6. **¿Por qué 2-SAT es polinomial?** Sus implicaciones se analizan con SCC.
7. **¿Qué es un certificado?** Una asignación que se verifica eficientemente.
8. **¿Qué sucede en α≈4.26?** Las instancias aleatorias suelen ser más difíciles.
9. **¿SAT y 3-SAT son iguales?** Tienen la misma dureza de NP, pero entradas distintas.
10. **¿Uso real?** Compatibilidad de características, planificación y hardware.
11. **¿Limitación principal?** Enumeración exponencial sin heurísticas avanzadas.
12. **¿Cómo mejorar?** DPLL, propagación unitaria, literales puros y CDCL.
13. **¿Qué mide `nanoTime`?** Tiempo transcurrido de alta resolución.
14. **¿Qué pasa con cláusulas inválidas?** El validador explica el motivo y las rechaza.
15. **¿Por qué no usar un solucionador externo?** La restricción exige implementar la lógica desde cero.
