# Anexo de evidencias NotebookLM

## Pregunta 01

**¿Qué define una fórmula 3-CNF?**

![Pregunta 01](capturas/pregunta1.png)

**Respuesta de NotebookLM:** La captura define SAT como la existencia de una
asignación que hace verdadera una fórmula en CNF y presenta 3-SAT como el caso
restringido en que cada cláusula contiene exactamente (o como máximo) tres
literales. También distingue que SAT general admite un número arbitrario de
literales por cláusula.

**Fuente citada:** Cuaderno compartido **“Estructura de Datos y Algoritmos II
Syllabus”**. Marcador visible: **[1]**. En el panel izquierdo se ven
“ALGORITMOS Y PROGRAMAS.docx”, “Cormen Introduction to Algorithm...”,
“Estructuras de Datos y Algoritmos ...” e “Introducción a los algoritmos,
Tho...”. Página exacta: **no visible en la captura**.

## Pregunta 02

**¿Cómo explica Cook–Levin la importancia de SAT?**

![Pregunta 02](capturas/pregunta2.png)
![Pregunta 02 (parte 2)](capturas/pregunta2_parte2.png)

**Respuesta de NotebookLM:** En las capturas se afirma que el teorema de
Cook–Levin estableció que SAT es NP-completo. La respuesta relaciona ese hecho
con la reducción de SAT a 3-SAT: al transformar una instancia general en una
instancia ternaria equivalente, la dificultad de SAT se transfiere a 3-SAT.

**Fuente citada:** Cuaderno compartido **“Estructura de Datos y Algoritmos II
Syllabus”**. Marcadores visibles: **[2], [3], [4] y [5]**. En el panel
izquierdo se ven “ALGORITMOS Y PROGRAMAS.docx”, “Cormen Introduction to
Algorithm...”, “Estructuras de Datos y Algoritmos ...” e “Introducción a los
algoritmos, Tho...”. Página exacta: **no visible en la captura**.

## Pregunta 03

**¿Por qué 3-SAT es NP-completo?**

![Pregunta 03](capturas/pregunta3.png)

**Respuesta de NotebookLM:** La parte legible de la respuesta explica que la
fórmula transformada conserva la satisfacibilidad de la cláusula original y que
la construcción usa un número de variables y cláusulas proporcional al tamaño
de la entrada, por lo que se ejecuta en tiempo polinomial. Con ello concluye
que 3-SAT es NP-completo.

**Fuente citada:** Cuaderno compartido **“Estructura de Datos y Algoritmos II
Syllabus”**. Marcadores visibles: **[3], [5], [6] y [7]**. En el panel
izquierdo se ven “ALGORITMOS Y PROGRAMAS.docx”, “Cormen Introduction to
Algorithm...”, “Estructuras de Datos y Algoritmos ...” e “Introducción a los
algoritmos, Tho...”. Página exacta: **no visible en la captura**.

## Pregunta 04

**¿Cuál es la complejidad de la búsqueda exhaustiva y del backtracking, y de
dónde sale?**

![Pregunta 04](capturas/pregunta4.png)

**Respuesta de NotebookLM:** La búsqueda exhaustiva cuesta `O(2^n)` u
`O(n·2^n)`: enumera las `2^n` combinaciones y evalúa los `n` elementos. El
backtracking tiene peor caso `O(2^n)`, porque recorre un árbol de decisión
binario, aunque puede podar ramas.

**Fuente citada:** Cuaderno compartido **“Estructura de Datos y Algoritmos II
Syllabus”**. Marcadores visibles: **[10], [11] y [12]**. En el panel izquierdo
se ven “ALGORITMOS Y PROGRAMAS.docx”, “Cormen Introduction to Algorithm...”,
“Estructuras de Datos y Algoritmos ...” e “Introducción a los algoritmos,
Tho...”. Página exacta: **no visible en la captura**.

## Pregunta 05

**¿Por qué verificar es más barato que encontrar?**

![Pregunta 05](capturas/pregunta5.png)

**Respuesta de NotebookLM:** Encontrar exige explorar un espacio combinatorio
que puede tener `2^n` posibilidades; verificar recibe un certificado, como una
asignación, y solo comprueba sus cláusulas. La captura relaciona esta asimetría
con NP: verificar una respuesta afirmativa puede hacerse en tiempo polinomial,
aunque encontrarla no sea conocido como polinomial.

**Fuente citada:** Cuaderno compartido **“Estructura de Datos y Algoritmos II
Syllabus”**. Marcadores visibles: **[4], [9] y [10]**. En el panel izquierdo se
ven “ALGORITMOS Y PROGRAMAS.docx”, “Cormen Introduction to Algorithm...”,
“Estructuras de Datos y Algoritmos ...” e “Introducción a los algoritmos,
Tho...”. Página exacta: **no visible en la captura**.
