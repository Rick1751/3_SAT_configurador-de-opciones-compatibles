package tema2;

import java.util.*;

public final class SolverService {
    private SolverService() {}

    public static boolean verificar(Formula f, boolean[] a) {
        if (a == null || a.length != f.variables) return false;
        for (int[] c : f.clausulas) {
            boolean ok = false;
            for (int x : c) ok |= x > 0 ? a[x - 1] : !a[-x - 1];
            if (!ok) return false;
        }
        return true;
    }

    public static int primeraClausulaFalsa(Formula f, boolean[] a) {
        if (a == null || a.length != f.variables) return -2;
        for (int i = 0; i < f.clausulas.size(); i++) {
            boolean satisfecha = false;
            for (int x : f.clausulas.get(i)) {
                satisfecha |= x > 0 ? a[x - 1] : !a[-x - 1];
            }
            if (!satisfecha) return i;
        }
        return -1;
    }

    public static Resultado fuerzaBruta(Formula f) {
        long inicio = System.nanoTime(), probadas = 0;
        if (f.variables > 62) throw new IllegalArgumentException("Fuerza bruta admite como máximo 62 variables.");
        long total = 1L << f.variables;
        for (long mascara = 0; mascara < total; mascara++) {
            boolean[] a = new boolean[f.variables];
            for (int i = 0; i < f.variables; i++) a[i] = (mascara & (1L << i)) != 0;
            probadas++;
            if (verificar(f, a)) return new Resultado(true, a, probadas, probadas, System.nanoTime() - inicio);
        }
        return new Resultado(false, null, probadas, probadas, System.nanoTime() - inicio);
    }

    public static Resultado backtracking(Formula f) {
        long inicio = System.nanoTime();
        Contador c = new Contador(f);
        boolean[] parcial = new boolean[f.variables];
        boolean[] asignada = new boolean[f.variables];
        boolean ok = bt(f, 0, parcial, asignada, c);
        return new Resultado(ok, ok ? parcial.clone() : null, c.nodos, c.hojas, System.nanoTime() - inicio);
    }

    public static Resultado resolverSatGeneral(Formula f) {
        return backtracking(f);
    }

    public static Resultado resolver2SAT(Formula f) {
        long inicio = System.nanoTime();
        for (int[] clausula : f.clausulas) {
            if (clausula.length != 2) throw new IllegalArgumentException("2-SAT requiere cláusulas de dos literales.");
        }
        int nodos = 2 * f.variables;
        List<List<Integer>> grafo = new ArrayList<>(), inverso = new ArrayList<>();
        for (int i = 0; i < nodos; i++) { grafo.add(new ArrayList<>()); inverso.add(new ArrayList<>()); }
        for (int[] c : f.clausulas) {
            int a = indice(-c[0]), b = indice(c[1]), na = indice(-c[1]), nb = indice(c[0]);
            grafo.get(a).add(b); inverso.get(b).add(a);
            grafo.get(na).add(nb); inverso.get(nb).add(na);
        }
        boolean[] visitado = new boolean[nodos];
        List<Integer> orden = new ArrayList<>();
        for (int i = 0; i < nodos; i++) dfs(i, grafo, visitado, orden);
        int[] componente = new int[nodos]; Arrays.fill(componente, -1);
        for (int i = orden.size() - 1, id = 0; i >= 0; i--) {
            int v = orden.get(i);
            if (componente[v] < 0) dfsComponente(v, inverso, componente, id++);
        }
        boolean[] asignacion = new boolean[f.variables];
        for (int i = 1; i <= f.variables; i++) {
            if (componente[indice(i)] == componente[indice(-i)])
                return new Resultado(false, null, nodos, 0, System.nanoTime() - inicio);
            asignacion[i - 1] = componente[indice(i)] > componente[indice(-i)];
        }
        return new Resultado(true, asignacion, nodos, 1, System.nanoTime() - inicio);
    }

    public static ResultadoReduccion reducirSatA3SAT(Formula original) {
        List<int[]> clausulas = new ArrayList<>();
        int siguiente = original.variables + 1;
        for (int[] c : original.clausulas) {
            if (c.length == 0) {
                clausulas.add(new int[]{siguiente, siguiente, siguiente});
                clausulas.add(new int[]{-siguiente, -siguiente, -siguiente});
                siguiente++;
            } else if (c.length <= 3) {
                clausulas.add(rellenar(c));
            } else {
                int auxiliar = siguiente++;
                clausulas.add(new int[]{c[0], c[1], auxiliar});
                for (int i = 2; i < c.length - 2; i++) {
                    int siguienteAuxiliar = siguiente++;
                    clausulas.add(new int[]{-auxiliar, c[i], siguienteAuxiliar});
                    auxiliar = siguienteAuxiliar;
                }
                clausulas.add(new int[]{-auxiliar, c[c.length - 2], c[c.length - 1]});
            }
        }
        Formula reducida = new Formula(siguiente - 1, clausulas);
        return new ResultadoReduccion(reducida, reducida.variables - original.variables,
                clausulas.size() - original.clausulas.size());
    }

    private static int indice(int literal) {
        int variable = Math.abs(literal) - 1;
        return literal > 0 ? 2 * variable : 2 * variable + 1;
    }

    private static void dfs(int v, List<List<Integer>> g, boolean[] visitado, List<Integer> orden) {
        if (visitado[v]) return;
        Deque<int[]> pila = new ArrayDeque<>();
        pila.push(new int[]{v, 0});
        visitado[v] = true;
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

    private static void dfsComponente(int v, List<List<Integer>> g, int[] componente, int id) {
        if (componente[v] >= 0) return;
        Deque<Integer> pila = new ArrayDeque<>();
        pila.push(v);
        componente[v] = id;
        while (!pila.isEmpty()) {
            int actual = pila.pop();
            for (int vecino : g.get(actual)) {
                if (componente[vecino] < 0) {
                    componente[vecino] = id;
                    pila.push(vecino);
                }
            }
        }
    }

    private static int[] rellenar(int[] c) {
        if (c.length == 3) return c.clone();
        if (c.length == 2) return new int[]{c[0], c[1], c[1]};
        return new int[]{c[0], c[0], c[0]};
    }

    private static boolean bt(Formula f, int pos, boolean[] a, boolean[] asignada, Contador c) {
        c.nodos++;
        if (clausulaFalsa(f, a, asignada)) return false;
        if (pos == f.variables) { c.hojas++; return verificar(f, a); }
        asignada[pos] = true; a[pos] = false;
        if (bt(f, pos + 1, a, asignada, c)) return true;
        a[pos] = true;
        if (bt(f, pos + 1, a, asignada, c)) return true;
        asignada[pos] = false;
        return false;
    }

    private static boolean clausulaFalsa(Formula f, boolean[] a, boolean[] asignada) {
        for (int[] c : f.clausulas) {
            boolean posible = false;
            for (int x : c) {
                int i = Math.abs(x) - 1;
                if (!asignada[i] || (x > 0 ? a[i] : !a[i])) { posible = true; break; }
            }
            if (!posible) return true;
        }
        return false;
    }

    private static final class Contador {
        long nodos, hojas;
        Contador(Formula ignored) {}
    }
}
