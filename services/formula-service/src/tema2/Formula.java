package tema2;

import java.util.*;

public final class Formula {
    public final int variables;
    public final List<int[]> clausulas;

    public Formula(int variables, List<int[]> clausulas) {
        this.variables = variables;
        this.clausulas = List.copyOf(clausulas.stream().map(int[]::clone).toList());
    }

    public boolean esTresCNF() {
        return clausulas.stream().allMatch(c -> c.length == 3);
    }
}
