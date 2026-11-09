# Verificación técnica y académica

Fecha de ejecución: 2026-11-08. Directorio auditado: `tema2-3sat/`.

## Entorno

| Herramienta | Salida observada | Estado |
|---|---|---|
| `java -version` | OpenJDK 27.0.1 | FALLA: no es Java 17 |
| `javac -version` | 27.0.1 | FALLA: no es Java 17 |
| `python --version` | Python 3.14.9 | OK |
| `dot -V` | Graphviz 16.1.0 | OK |
| `plantuml -version` | comando no encontrado | NO VERIFICADO |

Comando sugerido, no ejecutado: `sudo pacman -S --needed jdk17-openjdk python python-pip plantuml graphviz tree`.

## Tabla resumen

| ID | Verificación | Estado | Evidencia |
|---|---|---|---|
| A1 | Herramientas | CORREGIDO | Python y Graphviz disponibles; Java 27 y PlantUML pendientes del entorno |
| B1 | Estructura | OK | Carpetas y archivos esperados presentes; `.venv` se excluye del árbol |
| C1 | Compilación `javac -Xlint:all` | OK | Compilación sin errores ni warnings |
| C2 | Dependencias Java | OK | Solo imports `java.*`; sin Maven, Gradle ni JAR |
| C3 | Solvers externos/UI/servidor | OK | Búsquedas sin coincidencias en código |
| C4 | Script y menú | OK | `run.sh` ejecutable; menú inicia y captura entradas inválidas |
| D1 | Pruebas existentes | OK | 200 casos aleatorios y validaciones básicas |
| D2 | Equivalencia 3-SAT | OK | 500/500 coincidencias; 316 SAT, 184 UNSAT, 0 discrepancias |
| D3 | Casos límite | OK | Repetición, tautología, longitudes, cero y rango validados |
| D4 | 2-SAT | OK | 200 casos contra fuerza bruta; n=100000 en 65–104 ms observados |
| D5 | Reducción SAT→3-SAT | OK | 100 casos, equivalencia SAT preservada |
| D6 | Poda y medición | OK | Backtracking cuenta nodos; `nanoTime` está dentro de cada solver |
| E1 | Menú 1–13 | CORREGIDO | Se añadió manejo de entrada no numérica, confirmación n>25 y tabla de complejidad |
| F1 | CSV benchmark | OK | 180 filas: 3 familias, 2 algoritmos, 6 tamaños, 5 repeticiones |
| F2 | Resúmenes | OK | 36 grupos con mediana, promedio y desviación calculadas |
| F3 | Gráficas | OK | 7 PNG generados desde `.venv`, incluidos desde `/tmp` |
| G1 | PlantUML | NO VERIFICADO | Falta instalar `plantuml` en el entorno |
| H1 | Documentación | CORREGIDO | Se añadieron prueba manual, decisión/búsqueda y datos reales del CSV |
| I1 | NotebookLM | OK | Solo plantillas; no hay respuestas ni capturas inventadas |

## Comandos ejecutados y resultados resumidos

```text
rm -rf out && mkdir out && javac -Xlint:all -d out $(find services tests -name '*.java')
-> OK
java -ea -cp out tema2.Pruebas
-> 200 pruebas de equivalencia y validaciones básicas: OK
java -ea -cp out tema2.VerificacionEquivalencia
-> total=500 coincidencias=500 discrepancias=0 SAT=316 UNSAT=184
java -ea -cp out tema2.VerificacionAvanzada
-> 2SAT=200, reducción=100, n=100000 tiempo_ms=65-104
printf '11\n13\n' | java -cp out tema2.Main
-> CSV generados
python services/charts-service/generar_graficas.py
-> 7 PNG generados; cada archivo es PNG no vacío
```

Los CSV actuales tienen tiempos positivos, seis tamaños de `n` y cinco
repeticiones por combinación. El resumen calcula la mediana sobre cada grupo,
no usa texto de relleno. La gráfica de tamaño/tiempo usa escala logarítmica.

## Problemas encontrados y correcciones

1. Faltaban 2-SAT y reducción SAT→3-SAT: se implementaron SCC de Kosaraju
   iterativas y la reducción polinomial con variables auxiliares.
2. El menú no capturaba errores al leer la opción: la lectura quedó dentro del
   bloque de manejo de errores.
3. El configurador no conservaba nombres: ahora imprime nombres legibles.
4. No había confirmación para fuerza bruta grande: se añadió para `n > 25`.
5. El benchmark escribía resúmenes vacíos: ahora calcula mediana, promedio,
   desviación y memoria estimada, además de las tres familias.
6. El script generaba solo tres gráficas: ahora genera las siete solicitadas
   cuando existen sus CSV.

## Pendientes que solo puede completar el estudiante

- Instalar/verificar Java 17 y PlantUML en el entorno de entrega.
- Ejecutar `plantuml -checkonly` y generar PNG de diagramas.
- Completar capturas y respuestas reales del NotebookLM.
- Redactar la reflexión grupal de 150–200 palabras.
- Confirmar edición/páginas exactas de las referencias bibliográficas.

## Evaluación estimada sobre 20 puntos

| Criterio | Puntos estimados | Justificación |
|---|---:|---|
| Fundamentación | 1.8/2 | Incluye NP-completitud, Cook–Levin, 2-SAT y decisión/búsqueda |
| Diseño/pseudocódigo/prueba | 2.5/3 | Pseudocódigo y prueba manual incluidos; diagramas requieren PlantUML |
| Implementación | 3.5/4 | Solvers, menú, generador y servicios funcionan; 2-SAT/reducción recién integrados |
| Correctitud/pruebas | 2/2 | 500 equivalencias, 200 2-SAT, 100 reducciones y casos básicos |
| Complejidad | 3.5/4 | Análisis consistente; falta evidencia renderizada de PlantUML |
| Experimentación | 1.8/2 | CSV reales, repeticiones, mediana y siete gráficas |
| NotebookLM | 0.5/1 | Guía completa, faltan evidencias del cuaderno compartido |
| Defensa | 1.8/2 | 15 preguntas y respuestas breves |
| **Total estimado** | **17.4/20** | Riesgos principales: Java 17/PlantUML y anexos NotebookLM |
