package tema2;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public final class Benchmark {
    public static void ejecutar(Path salida) throws IOException {
        Files.createDirectories(salida);
        Path raw = salida.resolve("resultados_tiempo.csv");
        List<Fila> filas = new ArrayList<>();
        try (BufferedWriter w = Files.newBufferedWriter(raw)) {
            w.write("algoritmo,familia,n,m,repeticion,tiempo_ns,asignaciones_probadas,nodos_visitados,satisfacible,memoria_bytes\n");
            for (int n : new int[]{8, 10, 12, 14, 16, 18}) for (int rep = 1; rep <= 5; rep++) {
                int m = Math.max(1, (int) (4.26 * n));
                medir(w, filas, "sat", n, rep, Generador.satGarantizada(n, m, 2026L + n * 31L + rep));
                medir(w, filas, "aleatoria", n, rep, Generador.aleatoria(n, m, 7000L + n * 31L + rep));
                medir(w, filas, "unsat", n, rep, Generador.unsatGarantizada(n));
            }
        }

        escribirResumen(salida.resolve("resumen_tiempo.csv"), filas);
        escribirEncontrarVerificar(salida.resolve("resultados_encontrar_vs_verificar.csv"));
        escribir2Sat(salida.resolve("resultados_2sat_vs_3sat.csv"));
        escribirAlpha(salida.resolve("resultados_alpha.csv"));
    }
    private static void medir(BufferedWriter w, List<Fila> filas, String familia, int n, int rep, Formula f)
            throws IOException {
        Runtime runtime = Runtime.getRuntime();
        System.gc();
        long antes = runtime.totalMemory() - runtime.freeMemory();
        Resultado backtracking = SolverService.backtracking(f);
        long memoriaBacktracking = Math.max(0, runtime.totalMemory() - runtime.freeMemory() - antes);
        escribir(w, filas, "backtracking", familia, n, f.clausulas.size(), rep, backtracking, memoriaBacktracking);
        if (n <= 18) {
            System.gc();
            antes = runtime.totalMemory() - runtime.freeMemory();
            Resultado fuerza = SolverService.fuerzaBruta(f);
            long memoriaFuerza = Math.max(0, runtime.totalMemory() - runtime.freeMemory() - antes);
            escribir(w, filas, "fuerza_bruta", familia, n, f.clausulas.size(), rep, fuerza, memoriaFuerza);
        }
    }

    private static void escribir(BufferedWriter w, List<Fila> filas, String alg, String fam, int n, int m, int rep,
                                 Resultado r, long memoria) throws IOException {
        w.write(String.format(Locale.ROOT, "%s,%s,%d,%d,%d,%d,%d,%d,%s,%d%n",
                alg, fam, n, m, rep, r.tiempoNs(), r.asignacionesProbadas(), r.nodosVisitados(),
                r.satisfacible(), memoria));
        filas.add(new Fila(alg, fam, n, m, r.tiempoNs(), r.satisfacible(), memoria));
    }

    private static void escribirResumen(Path archivo, List<Fila> filas) throws IOException {
        Map<String, List<Fila>> grupos = new TreeMap<>();
        for (Fila f : filas) grupos.computeIfAbsent(f.algoritmo + "," + f.familia + "," + f.n + "," + f.m, k -> new ArrayList<>()).add(f);
        try (BufferedWriter w = Files.newBufferedWriter(archivo)) {
            w.write("algoritmo,familia,n,m,mediana_ms,promedio_ms,desviacion_ms,memoria_bytes_mediana,resultado\n");
            for (Map.Entry<String, List<Fila>> e : grupos.entrySet()) {
                List<Long> tiempos = e.getValue().stream().map(Fila::tiempoNs).sorted().toList();
                double promedio = tiempos.stream().mapToLong(Long::longValue).average().orElse(0);
                double mediana = tiempos.get(tiempos.size() / 2);
                double desviacion = Math.sqrt(tiempos.stream().mapToDouble(t -> (t - promedio) * (t - promedio)).average().orElse(0));
                Fila primero = e.getValue().get(0);
                List<Long> memorias = e.getValue().stream().map(Fila::memoria).sorted().toList();
                w.write(String.format(Locale.ROOT, "%s,%.6f,%.6f,%.6f,%d,%s%n",
                        e.getKey(), mediana / 1_000_000, promedio / 1_000_000,
                        desviacion / 1_000_000, memorias.get(memorias.size() / 2), primero.satisfacible));
            }
        }
    }

    private static void escribirEncontrarVerificar(Path archivo) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(archivo)) {
            w.write("n,tiempo_resolver_ns,tiempo_verificar_ns\n");
            for (int n : new int[]{8, 10, 12, 14, 16}) {
                Formula f = Generador.satGarantizada(n, (int) (4.26 * n), 5000 + n);
                Resultado r = SolverService.backtracking(f);
                long inicio = System.nanoTime();
                SolverService.verificar(f, r.asignacion());
                w.write(n + "," + r.tiempoNs() + "," + Math.max(1, System.nanoTime() - inicio) + "\n");
            }
        }
    }

    private static void escribir2Sat(Path archivo) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(archivo)) {
            w.write("algoritmo,n,tiempo_ns\n");
            for (int n : new int[]{100, 1000, 10000}) {
                Formula f = formula2Sat(n, n * 2);
                Resultado r = SolverService.resolver2SAT(f);
                w.write("2SAT," + n + "," + r.tiempoNs() + "\n");
                Formula tres = Generador.satGarantizada(Math.min(n, 18), Math.min(n * 4, 70), n);
                w.write("3SAT," + n + "," + SolverService.backtracking(tres).tiempoNs() + "\n");
            }
        }
    }

    private static void escribirAlpha(Path archivo) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(archivo)) {
            w.write("alpha,n,tiempo_ns\n");
            for (double alpha : new double[]{2, 3, 4, 4.26, 5, 6}) {
                Formula f = Generador.satGarantizada(14, (int) (alpha * 14), (long) (alpha * 100));
                w.write(alpha + ",14," + SolverService.backtracking(f).tiempoNs() + "\n");
            }
        }
    }

    private static Formula formula2Sat(int n, int m) {
        Random random = new Random(n + m);
        List<int[]> clausulas = new ArrayList<>();
        for (int i = 0; i < m; i++) clausulas.add(new int[]{
                (random.nextBoolean() ? 1 : -1) * (1 + random.nextInt(n)),
                (random.nextBoolean() ? 1 : -1) * (1 + random.nextInt(n))});
        return new Formula(n, clausulas);
    }

    private record Fila(String algoritmo, String familia, int n, int m, long tiempoNs, boolean satisfacible,
                        long memoria) {}
}
