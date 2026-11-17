# Guía de NotebookLM

## Checklist

Se formularon las cinco preguntas obligatorias, se conservaron las capturas
reales y se contrastaron sus afirmaciones con el código, la documentación y los
resultados CSV del proyecto.

## Preguntas mínimas

1. ¿Qué es 3-SAT y qué diferencia hay con SAT general?
2. ¿Por qué 3-SAT es NP-completo? Explica la reducción desde SAT.
3. ¿Por qué 2-SAT es polinomial pero 3-SAT no se conoce que lo sea?
4. ¿Cuál es la complejidad de la búsqueda exhaustiva y del backtracking, y de dónde sale?
5. ¿Por qué verificar es más barato que encontrar? ¿Qué relación tiene con NP?

## Registro

| N.º | Pregunta | Respuesta NotebookLM | Fuente/página | ¿Coincide con nuestro análisis? | Observaciones | Captura |
|---|---|---|---|---|---|---|
| 1 | ¿Qué es 3-SAT y qué diferencia hay con SAT general? | SAT decide si existe una asignación que hace verdadera una fórmula CNF; 3-SAT restringe cada cláusula a exactamente o como máximo tres literales, mientras SAT general admite un número arbitrario. | Cuaderno compartido “Estructura de Datos y Algoritmos II Syllabus”; marcador [1]; panel: “Cormen Introduction to Algorithm...” y “ALGORITMOS Y PROGRAMAS.docx”; página no visible en la captura. | Sí, en lo esencial | La respuesta visible distingue SAT general y 3-SAT. | `capturas/pregunta1.png` |
| 2 | ¿Por qué 3-SAT es NP-completo? Explica la reducción desde SAT. | 3-SAT pertenece a NP y es NP-difícil mediante una reducción polinomial desde SAT; las cláusulas largas se transforman con variables auxiliares y se conserva la satisfacibilidad. | Cuaderno compartido “Estructura de Datos y Algoritmos II Syllabus”; marcadores [2], [3], [4], [5]; panel: “Cormen Introduction to Algorithm...” y “ALGORITMOS Y PROGRAMAS.docx”; página no visible en la captura. | Sí, en lo esencial | La captura presenta las dos condiciones y el mecanismo de transformación. | `capturas/pregunta2.png`, `capturas/pregunta2_parte2.png` |
| 3 | ¿Por qué 2-SAT es polinomial pero 3-SAT no se conoce que lo sea? | 2-SAT usa un grafo de implicaciones y SCC: es satisfacible si `x` y `¬x` no están en la misma componente, en `O(V+E)`. Una cláusula de tres literales no se reduce a implicaciones binarias; un algoritmo polinomial para 3-SAT implicaría `P=NP`. | Cuaderno compartido “Estructura de Datos y Algoritmos II Syllabus”; marcadores [3], [5], [6], [7]; panel: “Cormen Introduction to Algorithm...” y “ALGORITMOS Y PROGRAMAS.docx”; página no visible en la captura. | Sí, en lo esencial | El resumen sigue los puntos legibles de la captura. | `capturas/pregunta3.png` |
| 4 | ¿Cuál es la complejidad de la búsqueda exhaustiva y del backtracking, y de dónde sale? | Fuerza bruta: `O(2^n)` u `O(n·2^n)` por enumerar `2^n` combinaciones y evaluar `n` elementos. Backtracking: peor caso `O(2^n)` por el árbol binario con poda. | Cuaderno compartido “Estructura de Datos y Algoritmos II Syllabus”; marcadores [10], [11], [12]; panel: “Cormen Introduction to Algorithm...”; página no visible en la captura. | Sí, con distinto nivel de detalle | Es compatible con nuestra fuerza bruta `Θ(2^n·m)` y con `2^(n+1)−1` nodos de backtracking en el peor caso; nuestro análisis explicita además el costo de evaluar cláusulas. | `capturas/pregunta4.png` |
| 5 | ¿Por qué verificar es más barato que encontrar? ¿Qué relación tiene con NP? | Encontrar explora un espacio de hasta `2^n`; verificar recibe un certificado y realiza comprobaciones. La asimetría se relaciona con NP, donde verificar una respuesta afirmativa puede hacerse en tiempo polinomial. | Cuaderno compartido “Estructura de Datos y Algoritmos II Syllabus”; marcadores [4], [9], [10]; panel: “Cormen Introduction to Algorithm...” y “ALGORITMOS Y PROGRAMAS.docx”; página no visible en la captura. | Sí, en lo esencial | El resumen recoge la relación explícita con NP que aparece en la captura. | `capturas/pregunta5.png` |

## Contraste con fuentes

| Afirmación de NotebookLM | Fuente académica tomada de `text/referencias.md` | Conclusión del grupo | Discrepancias |
|---|---|---|---|
| 3-SAT es un caso restringido de SAT general. | Cormen et al., *Introduction to Algorithms*. | La captura distingue una fórmula CNF general de cláusulas de hasta tres literales. | Sin discrepancias relevantes. |
| La reducción SAT→3-SAT conserva satisfacibilidad y es polinomial. | Karp, R. M. (1972), *Reducibility among combinatorial problems*. | La captura describe variables auxiliares, equivalencia y costo polinomial. | Sin discrepancias relevantes. |
| 2-SAT se resuelve con implicaciones y componentes fuertemente conexas, mientras 3-SAT no se conoce polinomial. | Cormen et al., *Introduction to Algorithms*. | La captura explica la diferencia estructural y relaciona 3-SAT con `P=NP`. | Sin discrepancias relevantes. |

## Reflexión grupal

NotebookLM nos ayudó a ordenar cinco respuestas sobre SAT, 3-SAT y complejidad.
En las capturas vimos que 3-SAT restringe el número de literales de cada
cláusula, que la reducción desde SAT usa variables auxiliares y que la
satisfacibilidad se conserva. También identificamos la explicación de 2-SAT
mediante un grafo de implicaciones y componentes fuertemente conexas, además de
la relación entre verificación, certificados y NP. La respuesta sobre búsqueda
exhaustiva y backtracking nos permitió distinguir la enumeración de `2^n`
combinaciones del árbol binario con poda. Las citas numéricas y los nombres
visibles en el panel ayudaron a ubicar el material, aunque las páginas exactas
no aparecen. No añadimos afirmaciones que no estuvieran legibles en las
capturas: cuando una formulación requería contexto, conservamos solo la idea
visible. Así aprendimos que una síntesis responsable debe separar lo que la
fuente muestra de lo que se podría inferir, y que las diferencias entre 2-SAT y
3-SAT dependen de la estructura de sus cláusulas. La herramienta sirvió como
apoyo de lectura, no como sustituto de nuestra revisión.

**Conteo de palabras de la reflexión:** 172.

Declaramos el uso de herramientas de IA (NotebookLM y asistente de código) como apoyo; el análisis y las conclusiones fueron revisados por el grupo.
