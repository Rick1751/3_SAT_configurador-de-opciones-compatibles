package tema2;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public final class FormulaService {
    /** Analiza texto en el formato de instancias del proyecto. */
    public static Formula parse(String texto) {
        String[] lineas = texto.lines().map(String::trim)
                .filter(s -> !s.isEmpty() && !s.startsWith("#")).toArray(String[]::new);
        if (lineas.length == 0) throw new IllegalArgumentException("La fórmula está vacía.");
        String[] cab = lineas[0].split("\\s+");
        if (cab.length < 2) throw new IllegalArgumentException("La primera línea debe ser: variables cláusulas.");
        int n = Integer.parseInt(cab[0]), m = Integer.parseInt(cab[1]);
        if (n < 0 || m < 0 || lineas.length - 1 != m)
            throw new IllegalArgumentException("La cantidad de cláusulas no coincide con m.");
        List<int[]> cs = new ArrayList<>();
        for (int i = 1; i < lineas.length; i++) {
            String[] p = lineas[i].split("\\s+");
            int[] c = new int[p.length];
            for (int j = 0; j < p.length; j++) c[j] = Integer.parseInt(p[j]);
            cs.add(c);
        }
        return new Formula(n, cs);
    }

    /** Carga y analiza una instancia desde disco. */
    public static Formula desdeArchivo(Path path) throws IOException {
        return parse(Files.readString(path));
    }

    /** Separa errores invalidantes de avisos permitidos, como repeticiones. */
    public static ResultadoValidacion validar3CNF(Formula f) {
        List<String> errores = new ArrayList<>();
        List<String> avisos = new ArrayList<>();
        if (f.variables < 1) errores.add("El número de variables debe ser positivo.");
        for (int i = 0; i < f.clausulas.size(); i++) {
            int[] c = f.clausulas.get(i);
            if (c.length != 3) errores.add("Cláusula " + (i + 1) + " tiene " + c.length + " literales; se requieren 3.");
            boolean tautologia = false;
            for (int x : c) {
                if (x == 0) errores.add("Cláusula " + (i + 1) + " contiene el literal 0.");
                if (Math.abs(x) > f.variables) errores.add("Variable fuera de rango en cláusula " + (i + 1) + ": " + x);
                for (int y : c) if (x == -y && x != 0) tautologia = true;
            }
            if (tautologia) avisos.add("Cláusula " + (i + 1) + " es tautología.");
            if (Arrays.stream(c).distinct().count() < c.length)
                avisos.add("Cláusula " + (i + 1) + " contiene literales repetidos (válido).");
        }
        return new ResultadoValidacion(errores, avisos);
    }

    /** Serializa una fórmula al formato simple del proyecto. */
    public static String texto(Formula f) {
        StringBuilder b = new StringBuilder(f.variables + " " + f.clausulas.size()).append('\n');
        for (int[] c : f.clausulas) b.append(c[0]).append(' ').append(c[1]).append(' ').append(c[2]).append('\n');
        return b.toString();
    }
}
