package tema2;

import java.util.*;

/** Solvers exactos y transformaciones educativas de fórmulas booleanas. */
public final class SolverService {
    private SolverService() {}

    /** Verifica una asignación completa en una fórmula 3-CNF válida. */
    public static boolean verificar(Formula f, boolean[] a) {
        validarTresCNF(f);
        if (a == null || a.length != f.variables) {
            throw new IllegalArgumentException("La asignación debe tener exactamente n valores.");
        }
        return verificarInterno(f, a);
    }

    /** Devuelve el índice de la primera cláusula falsa, o -1 si todas satisfacen. */
    public static int primeraClausulaFalsa(Formula f, boolean[] a) {
        validarTresCNF(f);
        if (a == null || a.length != f.variables) {
            throw new IllegalArgumentException("La asignación debe tener exactamente n valores.");
        }
        return primeraClausulaFalsaInterna(f, a);
    }

    /** Enumera máscaras de bits sin crear arrays durante cada iteración. */
    public static Resultado fuerzaBruta(Formula f) {
        validarTresCNF(f);
        if (f.variables > 62) throw new IllegalArgumentException("Fuerza bruta admite como máximo 62 variables.");
        long inicio = System.nanoTime();
        long total = 1L << f.variables;
        long probadas = 0;
        for (long mascara = 0; mascara < total; mascara++) {
            probadas++;
            if (satisfaceMascara(f, mascara)) {
                boolean[] asignacion = asignacionDesdeMascara(f.variables, mascara);
                return new Resultado(true, asignacion, probadas, probadas,
                        System.nanoTime() - inicio, 0);
            }
        }
        return new Resultado(false, null, probadas, probadas, System.nanoTime() - inicio, 0);
    }

    /** Busca recursivamente y poda cuando una cláusula ya es imposible. */
    public static Resultado backtracking(Formula f) {
        validarTresCNF(f);
        long inicio = System.nanoTime();
        Contador contador = new Contador();
        boolean[] parcial = new boolean[f.variables];
        boolean[] asignada = new boolean[f.variables];
        boolean ok = bt(f, 0, parcial, asignada, contador);
        return new Resultado(ok, ok ? parcial.clone() : null, contador.nodos, contador.hojas,
                System.nanoTime() - inicio, contador.profundidadMaxima);
    }

    /** Resuelve CNF de cláusulas de tamaño variable mediante backtracking. */
    public static Resultado resolverSatGeneral(Formula f) {
        validarGeneral(f);
        long inicio = System.nanoTime();
        Contador contador = new Contador();
        boolean[] parcial = new boolean[f.variables];
        boolean[] asignada = new boolean[f.variables];
        boolean ok = btGeneral(f, 0, parcial, asignada, contador);
        return new Resultado(ok, ok ? parcial.clone() : null, contador.nodos, contador.hojas,
                System.nanoTime() - inicio, contador.profundidadMaxima);
    }

    /** Resuelve 2-SAT mediante implicaciones y componentes fuertemente conexas. */
    public static Resultado resolver2SAT(Formula f) {
        validarDosCNF(f);
        long inicio = System.nanoTime();
        int nodos = 2 * f.variables;
        List<List<Integer>> grafo = new ArrayList<>();
        List<List<Integer>> inverso = new ArrayList<>();
        for (int i = 0; i < nodos; i++) {
            grafo.add(new ArrayList<>());
            inverso.add(new ArrayList<>());
        }
        for (int[] clausula : f.clausulas) {
            agregarImplicacion(grafo, inverso, -clausula[0], clausula[1]);
            agregarImplicacion(grafo, inverso, -clausula[1], clausula[0]);
        }
        List<Integer> orden = new ArrayList<>();
        boolean[] visitado = new boolean[nodos];
        for (int i = 0; i < nodos; i++) dfsOrden(i, grafo, visitado, orden);
        int[] componente = new int[nodos];
        Arrays.fill(componente, -1);
        int id = 0;
        for (int i = orden.size() - 1; i >= 0; i--) {
            if (componente[orden.get(i)] < 0) dfsComponente(orden.get(i), inverso, componente, id++);
        }
        boolean[] asignacion = new boolean[f.variables];
        for (int variable = 1; variable <= f.variables; variable++) {
            int positivo = indice(variable);
            int negativo = indice(-variable);
            if (componente[positivo] == componente[negativo]) {
                return new Resultado(false, null, nodos, 0, System.nanoTime() - inicio, 0);
            }
            // Kosaraju asigna primero la componente con mayor orden topológico.
            asignacion[variable - 1] = componente[positivo] > componente[negativo];
        }
        return new Resultado(true, asignacion, nodos, 1, System.nanoTime() - inicio, 0);
    }

    /** Reduce una CNF general a 3-CNF preservando satisfacibilidad. */
    public static ResultadoReduccion reducirSatA3SAT(Formula original) {
        validarGeneral(original);
        List<int[]> clausulas = new ArrayList<>();
        int siguiente = original.variables + 1;
        for (int[] clausula : original.clausulas) {
            if (clausula.length == 0) {
                clausulas.add(new int[]{siguiente, siguiente, siguiente});
                clausulas.add(new int[]{-siguiente, -siguiente, -siguiente});
                siguiente++;
            } else if (clausula.length <= 3) {
                clausulas.add(rellenar(clausula));
            } else {
                int auxiliar = siguiente++;
                clausulas.add(new int[]{clausula[0], clausula[1], auxiliar});
                for (int i = 2; i < clausula.length - 2; i++) {
                    int nuevo = siguiente++;
                    clausulas.add(new int[]{-auxiliar, clausula[i], nuevo});
                    auxiliar = nuevo;
                }
                clausulas.add(new int[]{-auxiliar, clausula[clausula.length - 2],
                        clausula[clausula.length - 1]});
            }
        }
        Formula reducida = new Formula(siguiente - 1, clausulas);
        return new ResultadoReduccion(reducida, reducida.variables - original.variables,
                clausulas.size() - original.clausulas.size());
    }

    private static boolean bt(Formula f, int pos, boolean[] a, boolean[] asignada, Contador c) {
        c.nodos++;
        c.profundidadMaxima = Math.max(c.profundidadMaxima, pos);
        if (clausulaFalsa(f, a, asignada)) return false;
        if (pos == f.variables) {
            c.hojas++;
            return verificarInterno(f, a);
        }
        asignada[pos] = true;
        a[pos] = false;
        if (bt(f, pos + 1, a, asignada, c)) return true;
        a[pos] = true;
        if (bt(f, pos + 1, a, asignada, c)) return true;
        asignada[pos] = false;
        return false;
    }

    private static boolean btGeneral(Formula f, int pos, boolean[] a, boolean[] asignada, Contador c) {
        c.nodos++;
        c.profundidadMaxima = Math.max(c.profundidadMaxima, pos);
        if (clausulaFalsa(f, a, asignada)) return false;
        if (pos == f.variables) {
            c.hojas++;
            return verificarInterno(f, a);
        }
        asignada[pos] = true;
        a[pos] = false;
        if (btGeneral(f, pos + 1, a, asignada, c)) return true;
        a[pos] = true;
        if (btGeneral(f, pos + 1, a, asignada, c)) return true;
        asignada[pos] = false;
        return false;
    }

    private static boolean satisfaceMascara(Formula f, long mascara) {
        for (int[] clausula : f.clausulas) {
            boolean satisfecha = false;
            for (int literal : clausula) {
                boolean valor = ((mascara >>> (Math.abs(literal) - 1)) & 1L) == 1L;
                if (literal < 0) valor = !valor;
                satisfecha |= valor;
            }
            if (!satisfecha) return false;
        }
        return true;
    }

    private static boolean clausulaFalsa(Formula f, boolean[] a, boolean[] asignada) {
        for (int[] clausula : f.clausulas) {
            boolean posible = false;
            for (int literal : clausula) {
                int indice = Math.abs(literal) - 1;
                if (!asignada[indice] || (literal > 0 ? a[indice] : !a[indice])) {
                    posible = true;
                    break;
                }
            }
            if (!posible) return true;
        }
        return false;
    }

    private static boolean verificarInterno(Formula f, boolean[] a) {
        return primeraClausulaFalsaInterna(f, a) < 0;
    }

    private static int primeraClausulaFalsaInterna(Formula f, boolean[] a) {
        for (int i = 0; i < f.clausulas.size(); i++) {
            boolean satisfecha = false;
            for (int literal : f.clausulas.get(i)) {
                satisfecha |= literal > 0 ? a[literal - 1] : !a[-literal - 1];
            }
            if (!satisfecha) return i;
        }
        return -1;
    }

    private static void validarTresCNF(Formula f) {
        ResultadoValidacion resultado = FormulaService.validar3CNF(f);
        if (!resultado.esValida()) throw new IllegalArgumentException("Fórmula 3-CNF inválida: "
                + String.join("; ", resultado.errores()));
    }

    private static void validarGeneral(Formula f) {
        if (f.variables < 1) throw new IllegalArgumentException("El número de variables debe ser positivo.");
        for (int[] clausula : f.clausulas) {
            for (int literal : clausula) validarLiteral(literal, f.variables);
        }
    }

    private static void validarDosCNF(Formula f) {
        validarGeneral(f);
        for (int[] clausula : f.clausulas) {
            if (clausula.length != 2) throw new IllegalArgumentException("2-SAT requiere cláusulas de 2 literales.");
        }
    }

    private static void validarLiteral(int literal, int variables) {
        if (literal == 0 || Math.abs(literal) > variables) {
            throw new IllegalArgumentException("Literal fuera de rango o igual a cero: " + literal);
        }
    }

    private static void agregarImplicacion(List<List<Integer>> g, List<List<Integer>> inv, int desde, int hasta) {
        int a = indice(desde);
        int b = indice(hasta);
        g.get(a).add(b);
        inv.get(b).add(a);
    }

    private static int indice(int literal) {
        int variable = Math.abs(literal) - 1;
        return literal > 0 ? 2 * variable : 2 * variable + 1;
    }

    private static void dfsOrden(int inicial, List<List<Integer>> g, boolean[] visitado, List<Integer> orden) {
        if (visitado[inicial]) return;
        Deque<int[]> pila = new ArrayDeque<>();
        pila.push(new int[]{inicial, 0});
        visitado[inicial] = true;
        while (!pila.isEmpty()) {
            int[] estado = pila.peek();
            if (estado[1] < g.get(estado[0]).size()) {
                int vecino = g.get(estado[0]).get(estado[1]++);
                if (!visitado[vecino]) {
                    visitado[vecino] = true;
                    pila.push(new int[]{vecino, 0});
                }
            } else {
                orden.add(estado[0]);
                pila.pop();
            }
        }
    }

    private static void dfsComponente(int inicial, List<List<Integer>> g, int[] componentes, int id) {
        Deque<Integer> pila = new ArrayDeque<>();
        pila.push(inicial);
        componentes[inicial] = id;
        while (!pila.isEmpty()) {
            int actual = pila.pop();
            for (int vecino : g.get(actual)) {
                if (componentes[vecino] < 0) {
                    componentes[vecino] = id;
                    pila.push(vecino);
                }
            }
        }
    }

    private static boolean[] asignacionDesdeMascara(int n, long mascara) {
        boolean[] resultado = new boolean[n];
        for (int i = 0; i < n; i++) resultado[i] = ((mascara >>> i) & 1L) != 0;
        return resultado;
    }

    private static int[] rellenar(int[] clausula) {
        if (clausula.length == 3) return clausula.clone();
        if (clausula.length == 2) return new int[]{clausula[0], clausula[1], clausula[1]};
        return new int[]{clausula[0], clausula[0], clausula[0]};
    }

    private static final class Contador {
        long nodos;
        long hojas;
        int profundidadMaxima;
    }
}
