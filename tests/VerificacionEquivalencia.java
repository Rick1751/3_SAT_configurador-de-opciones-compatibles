package tema2;

import java.util.*;

public final class VerificacionEquivalencia {
    public static void main(String[] args) {
        Random random = new Random(20261108);
        int sat = 0, unsat = 0;
        for (int caso = 0; caso < 500; caso++) {
            int n = 3 + random.nextInt(12);
            int m = Math.max(1, (1 + random.nextInt(7)) * n);
            Formula formula = Generador.aleatoria(n, m, random.nextLong());
            Resultado fuerza = SolverService.fuerzaBruta(formula);
            Resultado poda = SolverService.backtracking(formula);
            if (fuerza.satisfacible() != poda.satisfacible()) throw new AssertionError("Discrepancia en " + caso);
            if (fuerza.asignacion() != null && !SolverService.verificar(formula, fuerza.asignacion()))
                throw new AssertionError("Asignación de fuerza bruta inválida");
            if (poda.asignacion() != null && !SolverService.verificar(formula, poda.asignacion()))
                throw new AssertionError("Asignación de backtracking inválida");
            if (!fuerza.satisfacible() && fuerza.asignacionesProbadas() != (1L << n))
                throw new AssertionError("UNSAT no agotó 2^n");
            if (fuerza.satisfacible()) sat++; else unsat++;
        }
        System.out.printf("total=500 coincidencias=500 discrepancias=0 SAT=%d UNSAT=%d%n", sat, unsat);
    }
}
