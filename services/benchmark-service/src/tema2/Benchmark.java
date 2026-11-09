package tema2;

import com.sun.management.ThreadMXBean;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

/** Ejecuta mediciones reproducibles sin incluir generación ni escritura en el cronómetro. */
public final class Benchmark {
    private static final ThreadMXBean HILO = (ThreadMXBean) ManagementFactory.getThreadMXBean();

    private Benchmark() {}

    /** Ejecuta las familias, repeticiones, resúmenes y experimentos auxiliares. */
    public static void ejecutar(Path salida) throws IOException {
        Files.createDirectories(salida);
        List<Fila> filas = new ArrayList<>();
        try (BufferedWriter salidaCruda = Files.newBufferedWriter(salida.resolve("resultados_tiempo.csv"))) {
            salidaCruda.write("algoritmo,familia,n,m,repeticion,tiempo_ns,asignaciones_probadas,"
                    + "nodos_visitados,satisfacible,bytes_asignados,profundidad_maxima,"
                    + "memoria_analitica_bytes,timeout\n");
            ejecutarFamilias(salidaCruda, filas);
        }
        escribirResumen(salida.resolve("resumen_tiempo.csv"), filas);
        escribirEncontrarVerificar(salida.resolve("resultados_encontrar_vs_verificar.csv"));
        escribirDosSatTresSat(salida.resolve("resultados_2sat_vs_3sat.csv"));
        escribirEscalabilidad(salida.resolve("resultados_2sat_escalabilidad.csv"));
        escribirAlpha(salida.resolve("resultados_alpha.csv"));
    }

    private static void ejecutarFamilias(BufferedWriter salida, List<Fila> filas) throws IOException {
        int[] tamanos = {12, 14, 16, 18, 20, 22};
        for (String familia : List.of("aleatoria_4_26", "sat_plantada", "unsat_peor_caso")) {
            for (int n : tamanos) {
                for (int repeticion = 1; repeticion <= 5; repeticion++) {
                    int m = Math.max(8, (int) Math.round(4.26 * n));
                    Formula formula = crearFamilia(familia, n, m, repeticion);
                    System.out.printf("Benchmark %s n=%d repetición=%d%n", familia, n, repeticion);
                    medirYEscribir(salida, filas, "backtracking", familia, n, formula, repeticion);
                    medirYEscribir(salida, filas, "fuerza_bruta", familia, n, formula, repeticion);
                }
            }
        }
    }

    private static Formula crearFamilia(String familia, int n, int m, int repeticion) {
        long semilla = 20261108L + n * 101L + repeticion;
        return switch (familia) {
            case "aleatoria_4_26" -> Generador.aleatoria(n, m, semilla);
            case "sat_plantada" -> Generador.satGarantizada(n, m, semilla);
            case "unsat_peor_caso" -> Generador.unsatGarantizada(n, m, semilla);
            default -> throw new IllegalArgumentException("Familia desconocida: " + familia);
        };
    }

    private static void medirYEscribir(BufferedWriter salida, List<Fila> filas, String algoritmo,
                                       String familia, int n, Formula formula, int repeticion)
            throws IOException {
        Supplier<Resultado> solver = algoritmo.equals("fuerza_bruta")
                ? () -> SolverService.fuerzaBruta(formula)
                : () -> SolverService.backtracking(formula);
        for (int i = 0; i < 20; i++) solver.get();
        long hilo = Thread.currentThread().getId();
        long antes = bytesAsignados(hilo);
        long inicio = System.nanoTime();
        Resultado resultado = solver.get();
        long tiempo = System.nanoTime() - inicio;
        long bytes = Math.max(0, bytesAsignados(hilo) - antes);
        long nodos = resultado.nodosVisitados();
        long memoriaAnalitica = (long) formula.variables * 8 + (long) formula.clausulas.size() * 3 * Integer.BYTES;
        Fila fila = new Fila(algoritmo, familia, n, formula.clausulas.size(), tiempo, resultado,
                bytes, memoriaAnalitica);
        filas.add(fila);
        salida.write(fila.csv(repeticion));
    }

    private static long bytesAsignados(long hilo) {
        if (!HILO.isThreadAllocatedMemorySupported()) return 0;
        if (!HILO.isThreadAllocatedMemoryEnabled()) HILO.setThreadAllocatedMemoryEnabled(true);
        return HILO.getThreadAllocatedBytes(hilo);
    }

    private static void escribirResumen(Path archivo, List<Fila> filas) throws IOException {
        Map<String, List<Fila>> grupos = new TreeMap<>();
        for (Fila fila : filas) {
            String clave = fila.algoritmo + "," + fila.familia + "," + fila.n;
            grupos.computeIfAbsent(clave, ignored -> new ArrayList<>()).add(fila);
        }
        try (BufferedWriter salida = Files.newBufferedWriter(archivo)) {
            salida.write("algoritmo,familia,n,mediana_ms,promedio_ms,desviacion_ms,minimo_ms,"
                    + "maximo_ms,mediana_bytes_asignados,mediana_nodos_o_asignaciones,resultado\n");
            for (Map.Entry<String, List<Fila>> entrada : grupos.entrySet()) {
                List<Fila> grupo = entrada.getValue();
                List<Long> tiempos = grupo.stream().map(Fila::tiempo).sorted().toList();
                double promedio = tiempos.stream().mapToLong(Long::longValue).average().orElse(0);
                double desviacion = Math.sqrt(tiempos.stream()
                        .mapToDouble(t -> (t - promedio) * (t - promedio)).average().orElse(0));
                String resultado = grupo.stream().allMatch(f -> f.resultado.satisfacible()) ? "SAT"
                        : grupo.stream().noneMatch(f -> f.resultado.satisfacible()) ? "UNSAT"
                        : "MIXTO " + grupo.stream().filter(f -> f.resultado.satisfacible()).count() + "/5";
                salida.write(String.format(Locale.ROOT, "%s,%.6f,%.6f,%.6f,%.6f,%.6f,%d,%d,%s%n",
                        entrada.getKey(), mediana(tiempos) / 1_000_000.0, promedio / 1_000_000.0,
                        desviacion / 1_000_000.0, tiempos.get(0) / 1_000_000.0,
                        tiempos.get(tiempos.size() - 1) / 1_000_000.0,
                        mediana(grupo.stream().map(Fila::bytes).sorted().toList()),
                        mediana(grupo.stream().map(Fila::nodos).sorted().toList()), resultado));
            }
        }
    }

    private static void escribirEncontrarVerificar(Path archivo) throws IOException {
        try (BufferedWriter salida = Files.newBufferedWriter(archivo)) {
            salida.write("n,m,familia,tiempo_encontrar_fb_ns,tiempo_encontrar_bt_ns,tiempo_verificar_ns\n");
            for (int n : new int[]{12, 16, 20, 22}) {
                Formula formula = Generador.satGarantizada(n, (int) (4.26 * n), 9000 + n);
                Resultado fb = SolverService.fuerzaBruta(formula);
                Resultado bt = SolverService.backtracking(formula);
                long inicio = System.nanoTime();
                for (int i = 0; i < 10_000; i++) SolverService.verificar(formula, fb.asignacion());
                long verificar = Math.max(1, (System.nanoTime() - inicio) / 10_000);
                salida.write(String.format(Locale.ROOT, "%d,%d,sat_plantada,%d,%d,%d%n",
                        n, formula.clausulas.size(), fb.tiempoNs(), bt.tiempoNs(), verificar));
            }
        }
    }

    private static void escribirDosSatTresSat(Path archivo) throws IOException {
        try (BufferedWriter salida = Files.newBufferedWriter(archivo)) {
            salida.write("n,m,algoritmo,alpha,tiempo_ns,satisfacible\n");
            for (int n : new int[]{10, 14, 18, 22, 26, 30}) {
                int m = Math.max(1, (int) (4.26 * n));
                Formula tres = Generador.aleatoria(n, m, n);
                Formula dos = formula2Sat(n, Math.max(1, (int) (1.5 * n)), n);
                Resultado r3 = medirCalentado(() -> SolverService.backtracking(tres));
                Resultado r2 = medirCalentado(() -> SolverService.resolver2SAT(dos));
                salida.write(String.format(Locale.ROOT, "%d,%d,3SAT,4.26,%d,%s%n",
                        n, m, r3.tiempoNs(), r3.satisfacible()));
                salida.write(String.format(Locale.ROOT, "%d,%d,2SAT,1.5,%d,%s%n",
                        n, dos.clausulas.size(), r2.tiempoNs(), r2.satisfacible()));
            }
        }
    }

    private static Resultado medirCalentado(Supplier<Resultado> solver) {
        for (int i = 0; i < 20; i++) solver.get();
        return solver.get();
    }

    private static void escribirEscalabilidad(Path archivo) throws IOException {
        try (BufferedWriter salida = Files.newBufferedWriter(archivo)) {
            salida.write("n,m,tiempo_ns\n");
            for (int n : new int[]{1_000, 10_000, 100_000, 1_000_000}) {
                Formula formula = formula2Sat(n, 2 * n, n);
                Resultado resultado = medirCalentado(() -> SolverService.resolver2SAT(formula));
                salida.write(n + "," + 2 * n + "," + resultado.tiempoNs() + "\n");
            }
        }
    }

    private static void escribirAlpha(Path archivo) throws IOException {
        try (BufferedWriter salida = Files.newBufferedWriter(archivo)) {
            salida.write("alpha,n,instancias,mediana_nodos,mediana_tiempo_ns,fraccion_sat\n");
            for (double alpha : new double[]{2, 3, 3.5, 4, 4.26, 4.5, 5, 6, 8}) {
                List<Long> nodos = new ArrayList<>();
                List<Long> tiempos = new ArrayList<>();
                int sat = 0;
                for (int instancia = 0; instancia < 20; instancia++) {
                    int n = 30;
                    Formula formula = Generador.aleatoria(n, (int) (alpha * n), 50_000 + instancia);
                    Resultado resultado = SolverService.backtracking(formula);
                    nodos.add(resultado.nodosVisitados());
                    tiempos.add(resultado.tiempoNs());
                    if (resultado.satisfacible()) sat++;
                }
                salida.write(String.format(Locale.ROOT, "%.2f,30,20,%d,%d,%.3f%n", alpha,
                        mediana(nodos), mediana(tiempos), sat / 20.0));
            }
        }
    }

    private static Formula formula2Sat(int n, int m, long semilla) {
        Random random = new Random(semilla);
        List<int[]> clausulas = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            clausulas.add(new int[]{literal(random, n), literal(random, n)});
        }
        return new Formula(n, clausulas);
    }

    private static int literal(Random random, int n) {
        return (random.nextBoolean() ? 1 : -1) * (1 + random.nextInt(n));
    }

    private static long mediana(List<Long> valores) {
        return valores.get(valores.size() / 2);
    }

    private record Fila(String algoritmo, String familia, int n, int m, long tiempo,
                        Resultado resultado, long bytes, long memoriaAnalitica) {
        long nodos() {
            return Math.max(resultado.nodosVisitados(), resultado.asignacionesProbadas());
        }

        String csv(int repeticion) {
            return String.format(Locale.ROOT, "%s,%s,%d,%d,%d,%d,%d,%d,%s,%d,%d,%d,false%n",
                    algoritmo, familia, n, m, repeticion, tiempo, resultado.asignacionesProbadas(),
                    resultado.nodosVisitados(), resultado.satisfacible(), bytes,
                    resultado.profundidadMaxima(), memoriaAnalitica);
        }
    }
}
