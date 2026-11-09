# Fase 1: investigación

## Formulación

Una fórmula CNF con variables booleanas `x1,...,xn` tiene la forma
`F = C1 ∧ ... ∧ Cm`, donde cada cláusula es una disyunción de literales.
3-SAT restringe cada `Ci` a exactamente tres posiciones, permitiendo repetir
literales. La decisión pregunta si existe `a ∈ {0,1}^n` tal que `F(a)=1`;
la búsqueda devuelve además esa asignación, que es un certificado verificable.

3-SAT pertenece a NP porque, dado el certificado `a`, se recorren las m
cláusulas y sus tres literales en tiempo `O(m)`. Es NP-difícil mediante
`SAT ≤p 3-SAT`: una cláusula de una literal `(a)` se transforma en
`(a∨a∨a)`; una de dos `(a∨b)` en `(a∨b∨b)`; una de tres se copia. Para una
cláusula de k>3 literales se introducen auxiliares:
`(a∨b∨y1) ∧ (¬y1∨c∨y2) ∧ ... ∧ (¬yt∨u∨v)`. La extensión de una asignación
original con valores adecuados de las auxiliares prueba la equisatisfacibilidad
en ambos sentidos. El número de auxiliares y cláusulas es lineal en k.

## Cook–Levin y complejidad

Cook–Levin codifica una computación de una máquina no determinista como una
tabla tiempo-espacio; variables representan símbolos y estados en posiciones
de la tabla, y cláusulas fuerzan una celda válida, una transición y un único
inicio. La construcción es polinomial, por lo que toda lengua de NP se reduce a
SAT. Su importancia es establecer SAT como problema NP-completo y permitir
reducciones posteriores, incluida SAT a 3-SAT.

2-SAT es especial: cada `(a∨b)` equivale a `¬a→b` y `¬b→a`, y sus SCC
resuelven el problema en `Θ(n+m)`. Con tres literales ese grafo ya no representa
directamente la conjunción de restricciones; 3-SAT es NP-completo. Aplicaciones
incluyen configuradores de productos, hardware, planificación y compatibilidad
de recursos.
