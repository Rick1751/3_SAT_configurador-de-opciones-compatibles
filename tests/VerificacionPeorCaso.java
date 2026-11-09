package tema2;

/** Comprueba las cotas exactas de la familia UNSAT sin relleno. */
public final class VerificacionPeorCaso {
    private VerificacionPeorCaso() {}

    public static void main(String[] args) {
        for (int n : new int[]{3, 5, 8, 12, 14, 16, 20}) {
            Formula formula = Generador.unsatGarantizada(n, 8, n, false);
            Resultado fuerza = SolverService.fuerzaBruta(formula);
            Resultado backtracking = SolverService.backtracking(formula);
            long esperadoAsignaciones = 1L << n;
            long esperadoNodos = (1L << (n + 1)) - 1;
            if (fuerza.asignacionesProbadas() != esperadoAsignaciones
                    || backtracking.nodosVisitados() != esperadoNodos) {
                throw new AssertionError("Cota incorrecta en n=" + n);
            }
            System.out.printf("n=%d fuerza=%d backtracking=%d%n", n,
                    fuerza.asignacionesProbadas(), backtracking.nodosVisitados());
        }
    }
}
