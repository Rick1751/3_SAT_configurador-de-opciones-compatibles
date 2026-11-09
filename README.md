# Tema 2: 3-SAT — configurador de opciones compatibles

Proyecto de consola en Java 17, sin bibliotecas externas, que representa, valida y
resuelve fórmulas 3-CNF mediante fuerza bruta y backtracking. Cada variable puede
interpretarse como una opción de producto y cada cláusula como una restricción.

## Ejecución

```bash
./run.sh
```

Para ejecutar las pruebas:

```bash
rm -rf build && mkdir build
javac -d build $(find services tests -name '*.java')
java -ea -cp build tema2.Pruebas
```

El formato de una instancia es una primera línea `variables cláusulas`, seguida de
una línea por cláusula con tres enteros DIMACS. Las repeticiones son válidas.
La opción 11 crea CSV en `data/resultados`; después se pueden graficar con:

```bash
python3 services/charts-service/generar_graficas.py
```

## Decisiones de diseño

Se usa un paquete Java común para mantener compilación directa con `javac`; los
directorios representan servicios independientes y sus contratos son clases
inmutables (`Formula` y `Resultado`). La instancia UNSAT se conserva como caso de
prueba y la experimentación deja explícitos los CSV que aún requieren ejecución.
No se usan solucionadores SAT externos.

## Estructura

`services/formula-service` contiene el modelo y validador; `solver-service`, los
algoritmos; `generator-service`, las familias reproducibles; `benchmark-service`,
las mediciones; `console-app`, el menú; y `charts-service`, las gráficas Python.
La fundamentación, pseudocódigo y preguntas de defensa están en `docs/` y `text/`.
