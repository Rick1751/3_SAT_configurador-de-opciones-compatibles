package tema2;

import java.util.*;

public final class Generador {
    private Generador() {}
    public static Formula aleatoria(int n, int m, long semilla) {
        Random r = new Random(semilla); List<int[]> cs = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int[] c = new int[3];
            for (int j = 0; j < 3; j++) c[j] = (r.nextBoolean() ? 1 : -1) * (1 + r.nextInt(n));
            cs.add(c);
        }
        return new Formula(n, cs);
    }
    public static Formula satGarantizada(int n, int m, long semilla) {
        Random r = new Random(semilla); boolean[] planted = new boolean[n];
        for (int i = 0; i < n; i++) planted[i] = r.nextBoolean();
        List<int[]> cs = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int[] c = new int[3];
            do {
                for (int j = 0; j < 3; j++) c[j] = (r.nextBoolean() ? 1 : -1) * (1 + r.nextInt(n));
            } while (!satisface(c, planted));
            cs.add(c);
        }
        return new Formula(n, cs);
    }
    public static Formula unsatGarantizada(int n) {
        List<int[]> cs = new ArrayList<>();
        cs.add(new int[]{1, 1, 1}); cs.add(new int[]{-1, -1, -1});
        return new Formula(n, cs);
    }
    private static boolean satisface(int[] c, boolean[] a) {
        for (int x : c) if (x > 0 ? a[x - 1] : !a[-x - 1]) return true;
        return false;
    }
}
