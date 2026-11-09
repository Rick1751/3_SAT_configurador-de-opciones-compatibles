package tema2;

import java.util.*;

/** Modelo inmutable de una fórmula CNF y sus cláusulas. */
public final class Formula {
    public final int variables;
    public final List<int[]> clausulas;

    /** Construye una fórmula copiando las cláusulas recibidas. */
    public Formula(int variables, List<int[]> clausulas) {
        this.variables = variables;
        this.clausulas = List.copyOf(clausulas.stream().map(int[]::clone).toList());
    }

    /** Indica si todas las cláusulas tienen exactamente tres posiciones. */
    public boolean esTresCNF() {
        return clausulas.stream().allMatch(c -> c.length == 3);
    }
}
