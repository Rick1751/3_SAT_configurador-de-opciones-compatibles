# Informe técnico — Tema 2: 3-SAT

## 1. Portada

**Nombres:** Alan Tingo, Carlos Rojas, Esteban Valenzuela, Ricardo Leon
Ingeniería en Ciencias de datos, [GR2CD], [fecha].  
Proyecto Integrador — Unidad 2: Complejidad Computacional.

## 2. Introducción

Este proyecto implementa un configurador de opciones compatibles como una
fórmula 3-CNF. Cada opción es una variable booleana y cada restricción es una
cláusula de tres posiciones. El sistema decide SAT/UNSAT y muestra una
configuración cuando existe.

## 3. Objetivos

Representar y validar fórmulas, resolverlas con fuerza bruta y backtracking,
comparar encontrar frente a verificar, contrastar 2-SAT y producir mediciones
reproducibles sin bibliotecas SAT externas.

## 4. Descripción del problema

Una instancia contiene n variables y m cláusulas. Los literales positivos
representan seleccionar una opción y los negativos excluirla. La consola
permite ingresar, cargar, generar, validar, resolver y comparar configuraciones.

## 5. Fundamentos teóricos

SAT pregunta si una fórmula booleana es satisfacible. 3-SAT exige exactamente
tres posiciones por cláusula y pertenece a NP porque una asignación se verifica
en tiempo lineal en m. Es NP-difícil porque toda cláusula de SAT se transforma
en cláusulas ternarias equisatisfacibles: las cortas se rellenan y las largas
usan variables auxiliares. Cook–Levin codifica computaciones no deterministas
como SAT y establece la completitud de SAT. 2-SAT, en cambio, se resuelve con
SCC en `Θ(n+m)`.

## 6. Diseño y pseudocódigo

La fuerza bruta recorre máscaras `0..2^n-1`; el backtracking asigna cada variable
y poda cuando una cláusula queda totalmente falsa. El verificador evalúa tres
literales por cláusula. Los pseudocódigos completos están en
`../diagramas/pseudocodigo/`.


## 7. Estructuras de datos

`Formula` usa una lista de arreglos de enteros; el signo codifica negación.
`Resultado` contiene SAT, certificado, nodos, hojas, tiempo y profundidad.
La máscara evita arreglos temporales por asignación. 2-SAT usa dos nodos por
variable y listas de adyacencia.

## 8. Implementación en Java

`formula-service` analiza y valida; `solver-service` implementa fuerza bruta,
backtracking, 2-SAT y reducción; `generator-service` crea familias; 
`benchmark-service` mide; `console-app` orquesta el menú. Todo compila con
`javac --release 17` y solo usa APIs del JDK.

## 9. Pruebas funcionales

La suite ejecuta 200 fórmulas pequeñas, 500 equivalencias 3-SAT, 200
comparaciones 2-SAT y 100 reducciones. También verifica repetición, tautología,
literal cero, rango, cláusulas inválidas y entrada no numérica.

## 10. Complejidad temporal y espacial

Validar y verificar cuestan `Θ(m)` en el peor caso. Fuerza bruta cuesta
`Θ(2^n·m)` y usa `O(n+m)`; backtracking tiene peor caso
`O(2^n·m)`, profundidad `≤n` y espacio adicional `O(n)`. 2-SAT cuesta
`Θ(n+m)`. La derivación completa está en `fase4_complejidad.md`.

## 11. Resultados experimentales y gráficas

El benchmark usa cinco repeticiones, calentamiento y familias aleatoria,
plantada y UNSAT peor caso. Las tablas se generan desde `../data/resultados/`.

![Tamaño y tiempo](../graficas/01_tamano_vs_tiempo.png)
![Encontrar y verificar](../graficas/03_encontrar_vs_verificar.png)
![Umbral alpha](../graficas/07_alpha_umbral.png)

## 12. Discusión

El resultado UNSAT peor caso recorre el árbol completo, mientras que instancias
SAT pueden terminar antes. Las diferencias entre mediciones se explican por
JIT, cachés y el orden de las cláusulas. La verificación pura excluye la
validación estructural para medir el costo del certificado.

## 13. Conclusiones

La implementación muestra experimentalmente que encontrar una asignación puede
ser exponencial, pero verificarla es lineal en las cláusulas. Backtracking
reduce nodos en muchas instancias, aunque conserva la cota exponencial.

## 14. Referencias

Cook (1971), Karp (1972), Sipser, Cormen et al., Garey–Johnson y
Davis–Logemann–Loveland. La lista bibliográfica se mantiene en
`../text/referencias.md`.

## 15. Anexo NotebookLM

### Pregunta 01

**¿Qué define una fórmula 3-CNF?**

![Pregunta 01](anexo_notebooklm/capturas/pregunta1.png)

**Respuesta de NotebookLM:** La captura define SAT como la existencia de una
asignación que hace verdadera una fórmula en CNF y presenta 3-SAT como el caso
restringido en que cada cláusula contiene exactamente (o como máximo) tres
literales.

**Fuente citada:** Cuaderno compartido “Estructura de Datos y Algoritmos II
Syllabus”; marcador visible [1]; panel izquierdo con “ALGORITMOS Y
PROGRAMAS.docx”, “Cormen Introduction to Algorithm...” y “Estructuras de Datos
y Algoritmos ...”; página exacta no visible en la captura.

### Pregunta 02

**¿Cómo explica Cook–Levin la importancia de SAT?**

![Pregunta 02](anexo_notebooklm/capturas/pregunta2.png)
![Pregunta 02 (parte 2)](anexo_notebooklm/capturas/pregunta2_parte2.png)

**Respuesta de NotebookLM:** Las capturas afirman que Cook–Levin estableció
que SAT es NP-completo y relacionan ese hecho con la reducción de SAT a 3-SAT.

**Fuente citada:** Cuaderno compartido “Estructura de Datos y Algoritmos II
Syllabus”; marcadores visibles [2], [3], [4] y [5]; panel izquierdo con
“ALGORITMOS Y PROGRAMAS.docx”, “Cormen Introduction to Algorithm...” y
“Estructuras de Datos y Algoritmos ...”; página exacta no visible en la captura.

### Pregunta 03

**¿Por qué 3-SAT es NP-completo?**

![Pregunta 03](anexo_notebooklm/capturas/pregunta3.png)

**Respuesta de NotebookLM:** La parte legible explica que la transformación
conserva satisfacibilidad, usa un tamaño proporcional a la entrada y se ejecuta
en tiempo polinomial; concluye que 3-SAT es NP-completo.

**Fuente citada:** Cuaderno compartido “Estructura de Datos y Algoritmos II
Syllabus”; marcadores visibles [3], [5], [6] y [7]; panel izquierdo con
“ALGORITMOS Y PROGRAMAS.docx”, “Cormen Introduction to Algorithm...” y
“Estructuras de Datos y Algoritmos ...”; página exacta no visible en la captura.

### Pregunta 04

**¿Cuál es la complejidad de la búsqueda exhaustiva y del backtracking, y de
dónde sale?**

![Pregunta 04](anexo_notebooklm/capturas/pregunta4.png)

**Respuesta de NotebookLM:** La fuerza bruta cuesta `O(2^n)` u `O(n·2^n)`
porque enumera las `2^n` combinaciones y evalúa `n` elementos. El backtracking
tiene peor caso `O(2^n)` por su árbol de decisión binario con poda.

**Fuente citada:** Cuaderno compartido “Estructura de Datos y Algoritmos II
Syllabus”; marcadores visibles [10], [11] y [12]; panel izquierdo con
“ALGORITMOS Y PROGRAMAS.docx”, “Cormen Introduction to Algorithm...” y
“Estructuras de Datos y Algoritmos ...”; página exacta no visible en la captura.

### Pregunta 05

**¿Por qué verificar es más barato que encontrar?**

![Pregunta 05](anexo_notebooklm/capturas/pregunta5.png)

**Respuesta de NotebookLM:** Encontrar puede explorar `2^n` posibilidades,
mientras verificar recibe un certificado y comprueba sus cláusulas; la captura
relaciona esto con la definición de NP.

**Fuente citada:** Cuaderno compartido “Estructura de Datos y Algoritmos II
Syllabus”; marcadores visibles [4], [9] y [10]; panel izquierdo con “ALGORITMOS
Y PROGRAMAS.docx”, “Cormen Introduction to Algorithm...” y “Estructuras de
Datos y Algoritmos ...”; página exacta no visible en la captura.

### Registro

| N.º | Pregunta | Respuesta NotebookLM | Fuente/página | ¿Coincide con nuestro análisis? | Observaciones | Captura |
|---|---|---|---|---|---|---|
| 1 | ¿Qué define una fórmula 3-CNF? | CNF con cláusulas de hasta tres literales. | Cuaderno compartido “Estructura de Datos y Algoritmos II Syllabus”; marcador [1]; panel: “Cormen Introduction to Algorithm...” y “ALGORITMOS Y PROGRAMAS.docx”; página no visible en la captura. | Sí | Coincide con la validación de cláusulas ternarias del programa. | `anexo_notebooklm/capturas/pregunta1.png` |
| 2 | ¿Cómo explica Cook–Levin la importancia de SAT? | SAT es NP-completo y su dificultad se transfiere mediante SAT→3-SAT. | Cuaderno compartido “Estructura de Datos y Algoritmos II Syllabus”; marcadores [2], [3], [4], [5]; panel: “Cormen Introduction to Algorithm...” y “ALGORITMOS Y PROGRAMAS.docx”; página no visible en la captura. | Sí | Coincide con `fase1_investigacion.md`. | `anexo_notebooklm/capturas/pregunta2.png`, `pregunta2_parte2.png` |
| 3 | ¿Por qué 3-SAT es NP-completo? | La reducción conserva satisfacibilidad y es polinomial. | Cuaderno compartido “Estructura de Datos y Algoritmos II Syllabus”; marcadores [3], [5], [6], [7]; panel: “Cormen Introduction to Algorithm...” y “ALGORITMOS Y PROGRAMAS.docx”; página no visible en la captura. | Sí | Coincide con la reducción implementada. | `anexo_notebooklm/capturas/pregunta3.png` |
| 4 | ¿Cuál es la complejidad de la búsqueda exhaustiva y del backtracking, y de dónde sale? | Fuerza bruta: `O(2^n)` u `O(n·2^n)` por enumerar las `2^n` combinaciones y evaluar `n` elementos. Backtracking: peor caso `O(2^n)` por el árbol binario con poda. | Cuaderno compartido “Estructura de Datos y Algoritmos II Syllabus”; marcadores [10], [11], [12]; panel: “Cormen Introduction to Algorithm...”; página no visible en la captura. | Sí, con distinto nivel de detalle | Compatible con fuerza bruta `Θ(2^n·m)` y `2^(n+1)−1` nodos de backtracking en el peor caso; nuestro análisis explicita además el costo de evaluar cláusulas. | `anexo_notebooklm/capturas/pregunta4.png` |
| 5 | ¿Por qué verificar es más barato que encontrar? | Verificar un certificado evita explorar todo el espacio de `2^n`. | Cuaderno compartido “Estructura de Datos y Algoritmos II Syllabus”; marcadores [4], [9], [10]; panel: “Cormen Introduction to Algorithm...” y “ALGORITMOS Y PROGRAMAS.docx”; página no visible en la captura. | Sí | El benchmark confirma la ventaja medida y el análisis `O(m)` frente a `O(2^n·m)`. | `anexo_notebooklm/capturas/pregunta5.png` |

### Contraste con fuentes

| Afirmación de NotebookLM | Fuente académica tomada de `text/referencias.md` | Conclusión del grupo | Discrepancias |
|---|---|---|---|
| Cook–Levin establece la completitud de SAT. | Cook (1971), *The complexity of theorem-proving procedures*. | Consistente con la fase 1. | Sin discrepancias relevantes. |
| SAT→3-SAT conserva satisfacibilidad con costo polinomial. | Karp (1972), *Reducibility among combinatorial problems*. | Consistente con la reducción del proyecto. | Sin discrepancias relevantes; cambia la notación. |
| Verificar un certificado es más barato que encontrarlo. | Cormen et al., *Introduction to Algorithms*. | Consistente con el código y el CSV del benchmark. | Sin discrepancias relevantes. |

### Reflexión grupal

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
