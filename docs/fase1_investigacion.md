# Fase 1: investigación

SAT pregunta si una fórmula booleana tiene una asignación que la hace verdadera.
3-SAT restringe la entrada a CNF con exactamente tres posiciones por cláusula;
la salida es SAT/UNSAT y, para la versión de búsqueda, un certificado.

3-SAT pertenece a NP porque una asignación se verifica en tiempo lineal en el
número de cláusulas. Es NP-completo por la reducción polinomial de SAT a 3-SAT:
las cláusulas cortas se rellenan repitiendo literales y las largas se encadenan
con variables auxiliares. Cook-Levin fundamenta que todo problema de NP se
reduce a SAT mediante la codificación de una computación; por eso SAT es un
problema completo central. SAT general y 3-SAT son NP-completos, mientras que
2-SAT se resuelve en tiempo polinomial mediante implicaciones y SCC.

La versión de decisión solo responde SAT o UNSAT. La versión de búsqueda además
devuelve una asignación que sirve como certificado y puede verificarse en tiempo
polinomial.

Aplicaciones: configuradores de productos, verificación de hardware,
planificación y asignación de recursos. La representación usa variables,
literales, cláusulas y una lista de cláusulas; fuerza bruta decide entre `2^n`
asignaciones. Una cláusula con tres posiciones admite repetición y puede ser una
tautología.
