package tema2;

import java.util.*;

public final class Pruebas {
    public static void main(String[] args) {
        Random r = new Random(7);
        for (int i = 0; i < 200; i++) {
            Formula f = Generador.aleatoria(1 + r.nextInt(8), r.nextInt(15), r.nextLong());
            Resultado a = SolverService.fuerzaBruta(f), b = SolverService.backtracking(f);
            assert a.satisfacible() == b.satisfacible();
            if (a.asignacion() != null) assert SolverService.verificar(f, a.asignacion());
            if (b.asignacion() != null) assert SolverService.verificar(f, b.asignacion());
        }
        Formula repetida = new Formula(2, List.of(new int[]{1, 1, -2}));
        assert FormulaService.validar3CNF(repetida).stream().anyMatch(s -> s.contains("repetidos"));
        assert FormulaService.validar3CNF(new Formula(1, List.of(new int[]{1, 2, 1}))).stream().anyMatch(s -> s.contains("fuera"));
        System.out.println("200 pruebas de equivalencia y validaciones básicas: OK");
    }
}
