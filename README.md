# Tema 2: 3-SAT — configurador de opciones compatibles

Proyecto de consola en Java 17, sin bibliotecas externas, que representa, valida y
resuelve fórmulas 3-CNF mediante fuerza bruta y backtracking. Cada variable puede
interpretarse como una opción de producto y cada cláusula como una restricción.

## Ejecución

```bash
./run.sh
```

En Arch Linux, instale el entorno recomendado con:

```bash
sudo pacman -S --needed jdk17-openjdk python python-pip plantuml graphviz
sudo archlinux-java set java-17-openjdk
```

En Debian/Ubuntu:

```bash
sudo apt install openjdk-17-jdk python3 python3-venv plantuml graphviz
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
python services/charts-service/generar_graficas.py
```

## Cómo ejecutar pruebas

```bash
rm -rf build && mkdir build
javac --release 17 -Xlint:all -d build $(find services tests -name '*.java')
java -ea -cp build tema2.Pruebas
java -ea -cp build tema2.VerificacionEquivalencia
java -ea -cp build tema2.VerificacionAvanzada
```

Para las gráficas, use siempre un entorno virtual:

```bash
python -m venv .venv
source .venv/bin/activate
pip install -r services/charts-service/requirements.txt
python services/charts-service/generar_graficas.py
python services/charts-service/generar_tablas_md.py
```

## Decisiones de diseño

Se usa un paquete Java común para mantener compilación directa con `javac`; los
directorios representan servicios independientes y sus contratos son clases
inmutables (`Formula` y `Resultado`). La instancia UNSAT se conserva como caso de
prueba y la experimentación deja explícitos los CSV que aún requieren ejecución.
No se usan solucionadores SAT externos.

## Estructura

Una fórmula con n >= 1 y cero cláusulas es SAT trivial: la conjunción vacía es
verdadera. n=0 se rechaza para mantener una instancia con variables explícitas.
La familia UNSAT peor caso prohíbe las ocho combinaciones de las tres últimas
variables, por lo que deja libre el prefijo y fuerza la exploración exponencial.

`services/formula-service` contiene el modelo y validador; `solver-service`, los
algoritmos; `generator-service`, las familias reproducibles; `benchmark-service`,
las mediciones; `console-app`, el menú; y `charts-service`, las gráficas Python.
La fundamentación, pseudocódigo y preguntas de defensa están en `docs/` y `text/`.
