package tema2;

import java.util.*;

public final class VerificacionAvanzada {
    public static void main(String[] args) {
        Random random = new Random(91);
        for (int caso = 0; caso < 200; caso++) {
            int n = 1 + random.nextInt(8);
            List<int[]> clausulas = new ArrayList<>();
            for (int i = 0, m = random.nextInt(20); i < m; i++) {
                clausulas.add(new int[]{literal(random, n), literal(random, n)});
            }
            Formula formula = new Formula(n, clausulas);
            Resultado dosSat = SolverService.resolver2SAT(formula);
            if (dosSat.satisfacible() != SolverService.resolverSatGeneral(formula).satisfacible())
                throw new AssertionError("Discrepancia 2-SAT");
            if (dosSat.satisfacible() && !satisface(formula, dosSat.asignacion()))
                throw new AssertionError("Asignación 2-SAT inválida");
        }
        for (int caso = 0; caso < 100; caso++) {
            int n = 3 + random.nextInt(4);
            List<int[]> clausulas = new ArrayList<>();
            for (int i = 0; i < 1 + random.nextInt(8); i++) {
                int[] c = new int[1 + random.nextInt(6)];
                for (int j = 0; j < c.length; j++) c[j] = literal(random, n);
                clausulas.add(c);
            }
            Formula original = new Formula(n, clausulas);
            ResultadoReduccion reduccion = SolverService.reducirSatA3SAT(original);
            boolean satOriginal = SolverService.resolverSatGeneral(original).satisfacible();
            boolean satReducida = SolverService.backtracking(reduccion.formula()).satisfacible();
            if (satOriginal != satReducida || reduccion.formula().variables < n)
                throw new AssertionError("Discrepancia en reducción");
        }
        long inicio = System.nanoTime();
        SolverService.resolver2SAT(new Formula(100_000, List.of(new int[]{1, 2})));
        System.out.printf("2SAT=200, reducción=100, n=100000 tiempo_ms=%.3f%n",
                (System.nanoTime() - inicio) / 1_000_000.0);
    }

    private static int literal(Random random, int n) {
        return (random.nextBoolean() ? 1 : -1) * (1 + random.nextInt(n));
    }

    private static boolean satisface(Formula formula, boolean[] asignacion) {
        for (int[] clausula : formula.clausulas) {
            boolean ok = false;
            for (int literal : clausula) {
                ok |= literal > 0 ? asignacion[literal - 1] : !asignacion[-literal - 1];
            }
            if (!ok) return false;
        }
        return true;
    }
}
