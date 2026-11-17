# Guía de NotebookLM

## Checklist

Se formularon las cinco preguntas obligatorias, se conservaron las capturas
reales y se contrastaron sus afirmaciones con el código, la documentación y los
resultados CSV del proyecto.

## Preguntas mínimas

1. ¿Qué define una fórmula 3-CNF?
2. ¿Cómo explica Cook–Levin la importancia de SAT?
3. ¿Por qué 3-SAT es NP-completo?
4. ¿Qué diferencia hay entre 2-SAT y 3-SAT?
5. ¿Por qué verificar es más barato que encontrar?

## Registro

| N.º | Pregunta | Respuesta NotebookLM | Fuente/página | ¿Coincide con nuestro análisis? | Observaciones | Captura |
|---|---|---|---|---|---|---|
| 1 | ¿Qué define una fórmula 3-CNF? | CNF con cláusulas de hasta tres literales; distingue 3-SAT de SAT general. | Cuaderno compartido “Estructura de Datos y Algoritmos II Syllabus”; marcador [1]; panel: “Cormen Introduction to Algorithm...” y “ALGORITMOS Y PROGRAMAS.docx”; página no visible en la captura. | Sí, en lo esencial | Nuestro proyecto valida cláusulas ternarias y usa esa representación. | `capturas/pregunta1.png` |
| 2 | ¿Cómo explica Cook–Levin la importancia de SAT? | Afirma que Cook–Levin estableció que SAT es NP-completo y conecta ese resultado con SAT→3-SAT. | Cuaderno compartido “Estructura de Datos y Algoritmos II Syllabus”; marcadores [2], [3], [4], [5]; panel: “Cormen Introduction to Algorithm...” y “ALGORITMOS Y PROGRAMAS.docx”; página no visible en la captura. | Sí, en lo esencial | Coincide con `docs/fase1_investigacion.md`; la captura se centra en la consecuencia para 3-SAT. | `capturas/pregunta2.png`, `capturas/pregunta2_parte2.png` |
| 3 | ¿Por qué 3-SAT es NP-completo? | La reducción conserva satisfacibilidad y tiene costo polinomial; concluye NP-completitud. | Cuaderno compartido “Estructura de Datos y Algoritmos II Syllabus”; marcadores [3], [5], [6], [7]; panel: “Cormen Introduction to Algorithm...” y “ALGORITMOS Y PROGRAMAS.docx”; página no visible en la captura. | Sí | Nuestro código implementa la reducción SAT→3-SAT y documenta la equisatisfacibilidad. | `capturas/pregunta3.png` |
| 4 | ¿Cuál es la complejidad de la búsqueda exhaustiva y del backtracking, y de dónde sale? | Fuerza bruta: `O(2^n)` u `O(n·2^n)` por enumerar `2^n` combinaciones y evaluar `n` elementos. Backtracking: peor caso `O(2^n)` por el árbol binario con poda. | Cuaderno compartido “Estructura de Datos y Algoritmos II Syllabus”; marcadores [10], [11], [12]; panel: “Cormen Introduction to Algorithm...”; página no visible en la captura. | Sí, con distinto nivel de detalle | Es compatible con nuestra fuerza bruta `Θ(2^n·m)` y con `2^(n+1)−1` nodos de backtracking en el peor caso; nuestro análisis explicita además el costo de evaluar cláusulas. | `capturas/pregunta4.png` |
| 5 | ¿Por qué verificar es más barato que encontrar? | Verificar usa un certificado y recorre comprobaciones; encontrar puede explorar `2^n` posibilidades. | Cuaderno compartido “Estructura de Datos y Algoritmos II Syllabus”; marcadores [4], [9], [10]; panel: “Cormen Introduction to Algorithm...” y “ALGORITMOS Y PROGRAMAS.docx”; página no visible en la captura. | Sí | El CSV confirma razones mayores que 1 en los casos SAT medidos y la complejidad es `O(m)` frente a `O(2^n·m)`. | `capturas/pregunta5.png` |

## Contraste con fuentes

| Afirmación de NotebookLM | Fuente académica tomada de `text/referencias.md` | Conclusión del grupo | Discrepancias |
|---|---|---|---|
| Cook–Levin establece la completitud de SAT. | Cook, S. A. (1971), *The complexity of theorem-proving procedures*. | Es consistente con la fase 1 del proyecto. | Sin discrepancias relevantes. |
| La reducción SAT→3-SAT conserva satisfacibilidad y es polinomial. | Karp, R. M. (1972), *Reducibility among combinatorial problems*. | Es consistente con la reducción implementada y documentada. | Sin discrepancias relevantes; la captura usa una notación distinta para las auxiliares. |
| Verificar un certificado es más barato que encontrarlo. | Cormen et al., *Introduction to Algorithms*. | El código y `resultados_encontrar_vs_verificar.csv` respaldan la diferencia medida. | Sin discrepancias relevantes; la captura da ejemplos generales además de SAT. |

## Reflexión grupal

NotebookLM nos ayudó a organizar las ideas principales y a identificar cómo
relacionar 3-SAT con SAT, Cook–Levin, los certificados y la diferencia entre
encontrar y verificar. No tomamos sus respuestas como resultados automáticos:
contrastamos cada afirmación con la definición usada por nuestro programa, la
reducción SAT→3-SAT descrita en la fase 1 y el análisis de complejidad. También
revisamos los CSV del benchmark; allí observamos que verificar recorre las
cláusulas en tiempo lineal, mientras encontrar puede requerir explorar muchas
asignaciones, con el crecimiento `2^n·m`. La herramienta no realizó por
nosotros la implementación, la ejecución de los experimentos ni la
interpretación de sus resultados. Tampoco delegamos el análisis de 2-SAT frente
a 3-SAT: lo comparamos con el código y la documentación del proyecto. Aprendimos
que una explicación correcta necesita una evidencia reproducible y que las
capturas deben conservarse junto con sus fuentes identificables. Cuando una
fuente o una respuesta no se podía leer, lo declaramos en vez de completar con
suposiciones. Así, la herramienta fue útil para orientar la revisión, pero las
decisiones técnicas y las conclusiones siguieron siendo responsabilidad del
grupo.

**Conteo de palabras de la reflexión:** 180.

Declaramos el uso de herramientas de IA (NotebookLM y asistente de código) como apoyo; el análisis y las conclusiones fueron revisados por el grupo.
