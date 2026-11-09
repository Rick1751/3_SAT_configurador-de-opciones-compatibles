# Fase 2: diseño algorítmico

## Pseudocódigo

```text
VALIDAR(f):
  para cada cláusula c: comprobar |c|=3 y cada literal en [-n,n]\{0}
  informar repeticiones y tautologías
VERIFICAR(f,a):
  para cada cláusula: aceptar si algún literal resulta verdadero
FUERZA_BRUTA(f):
  para máscara de 0 hasta 2^n-1: construir a; si VERIFICAR, devolver a
BACKTRACK(f,i):
  si una cláusula queda falsa, podar
  si i=n, verificar y devolver
  fijar xi=F y luego xi=V recursivamente
```

La operación dominante es evaluar literales. En una instancia n=3, se enumeran
las ocho asignaciones; una asignación que satisface todas las cláusulas termina
la fuerza bruta. Backtracking visita primero una rama y poda al detectar una
cláusula falsa, sin perder soluciones.

La reducción SAT→3-SAT encadena cláusulas largas con variables auxiliares y
rellena las cortas. 2-SAT modela cada literal como nodo y cada cláusula
`(a∨b)` como `¬a→b` y `¬b→a`, usando componentes fuertemente conexas.

## Prueba manual (n=3, m=4)

Para `(x1∨x2∨x3) ∧ (¬x1∨x2∨x3) ∧ (x1∨¬x2∨x3) ∧ (x1∨x2∨¬x3)`,
la tabla de verdad tiene siete asignaciones SAT y una UNSAT:

| x1 | x2 | x3 | Resultado |
|---|---|---|---|
| F | F | F | UNSAT |
| F | F | V | SAT |
| F | V | F | SAT |
| F | V | V | SAT |
| V | F | F | SAT |
| V | F | V | SAT |
| V | V | F | SAT |
| V | V | V | SAT |

Con el orden de variables del programa, backtracking visita cinco nodos hasta
encontrar `FFV`: raíz, `x1=F`, `x2=F`, la hoja podada `x3=F` y la hoja SAT
`x3=V`. La poda ocurre porque la primera cláusula queda totalmente falsa.
