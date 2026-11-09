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
16. **¿Por qué `unsat_peor_caso` no lleva relleno?** El relleno puede falsar cláusulas antes de llegar a las últimas variables y podar el prefijo.
17. **¿Por qué aparecen `2^(n+1)-1` nodos?** Es la suma de todos los niveles del árbol binario completo de decisiones.
18. **¿Por qué se calienta la JVM?** El JIT compila métodos usados y evita comparar el arranque con el código optimizado.
19. **¿Por qué verificar no incluye validar?** La fórmula se valida una vez; mezclar ambos costos oculta el costo del certificado.
20. **¿Qué mide `getThreadAllocatedBytes`?** Bytes asignados por el hilo durante la llamada, no memoria residente total.
21. **¿Por qué 2-SAT no se generaliza directamente a 3-SAT?** Las implicaciones de dos literales no representan una cláusula ternaria completa.
22. **¿Qué es el umbral 4.26?** La razón aproximada m/n donde instancias aleatorias 3-SAT concentran transición y dificultad.
23. **¿Qué significa una cláusula tautológica?** Contiene x y ¬x, por lo que siempre es verdadera; se informa como aviso.
24. **¿Qué significa una repetición?** Una posición repetida sigue siendo válida en la definición de tres posiciones.
25. **¿Por qué se usa una máscara?** Codifica las n decisiones en bits y evita crear un arreglo por asignación.
26. **¿Qué mide profundidad máxima?** El mayor nivel recursivo alcanzado; está acotado por n.
27. **¿Qué es una instancia plantada?** Una fórmula generada para que una asignación conocida satisfaga todas las cláusulas.
28. **¿Qué hace Kosaraju?** Ejecuta DFS en el grafo y su inverso para etiquetar componentes fuertemente conexas.
29. **¿Qué prueba una reducción?** Que la respuesta SAT se conserva en ambos sentidos y que el tamaño crece polynomialmente.
30. **¿Qué es JIT?** Compilación durante la ejecución de bytecode caliente a código nativo.
