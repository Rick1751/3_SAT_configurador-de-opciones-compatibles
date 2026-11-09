package tema2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Genera familias reproducibles de instancias 3-CNF. */
public final class Generador {
    private Generador() {}

    /** Genera una familia aleatoria con tres posiciones por cláusula. */
    public static Formula aleatoria(int n, int m, long semilla) {
        Random random = new Random(semilla);
        List<int[]> clausulas = new ArrayList<>();
        for (int i = 0; i < m; i++) clausulas.add(clausulaAleatoria(random, n));
        return new Formula(n, clausulas);
    }

    /** Genera cláusulas que siempre son satisfechas por una asignación plantada. */
    public static Formula satGarantizada(int n, int m, long semilla) {
        Random random = new Random(semilla);
        boolean[] plantada = new boolean[n];
        for (int i = 0; i < n; i++) plantada[i] = random.nextBoolean();
        List<int[]> clausulas = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int[] clausula;
            do {
                clausula = clausulaAleatoria(random, n);
            } while (!satisface(clausula, plantada));
            clausulas.add(clausula);
        }
        return new Formula(n, clausulas);
    }

    /**
     * Prohíbe las ocho combinaciones de las tres últimas variables.
     * El orden de decisión deja el prefijo libre, forzando a explorar 2^(n-3).
     */
    public static Formula unsatGarantizada(int n, int m, long semilla) {
        return unsatGarantizada(n, m, semilla, true);
    }

    /**
     * Genera las ocho cláusulas que contradicen todas las combinaciones finales.
     * Sin relleno, el backtracking recorre exactamente 2^(n+1)-1 nodos.
     */
    public static Formula unsatGarantizada(int n, int m, long semilla, boolean conRelleno) {
        if (n < 3) throw new IllegalArgumentException("La familia UNSAT requiere n >= 3.");
        Random random = new Random(semilla);
        List<int[]> clausulas = new ArrayList<>();
        int primera = n - 2;
        for (int mascara = 0; mascara < 8; mascara++) {
            int[] clausula = new int[3];
            for (int posicion = 0; posicion < 3; posicion++) {
                boolean valorProhibido = ((mascara >>> posicion) & 1) != 0;
                clausula[posicion] = valorProhibido ? -(primera + posicion) : primera + posicion;
            }
            clausulas.add(clausula);
        }
        if (conRelleno) {
            for (int i = 8; i < m; i++) clausulas.add(clausulaAleatoria(random, n));
        }
        return new Formula(n, clausulas);
    }

    private static int[] clausulaAleatoria(Random random, int n) {
        int[] clausula = new int[3];
        for (int i = 0; i < clausula.length; i++) {
            clausula[i] = (random.nextBoolean() ? 1 : -1) * (1 + random.nextInt(n));
        }
        return clausula;
    }

    private static boolean satisface(int[] clausula, boolean[] asignacion) {
        for (int literal : clausula) {
            if (literal > 0 ? asignacion[literal - 1] : !asignacion[-literal - 1]) return true;
        }
        return false;
    }
}
