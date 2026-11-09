package tema2;

import java.nio.file.*;
import java.util.*;

public final class Main {
    private static final Scanner IN = new Scanner(System.in);
    private static Formula actual;
    private static String[] nombres = new String[0];
    public static void main(String[] args) {
        System.out.println("=== TEMA 2: 3-SAT, configurador de opciones compatibles ===");
        while (true) {
            System.out.println("\n1) Ingresar  2) Cargar archivo  3) Configurador  4) Validar  5) Fuerza bruta");
            System.out.println("6) Backtracking  7) Comparar  8) Verificar asignación  9) Generar");
            System.out.println("10) SAT general vs 3-SAT  11) Benchmark  12) Complejidad  13) Salir");
            try {
                int op = entero("Opción: ");
                if (op == 13) return;
                switch (op) {
                    case 1 -> actual = leerFormula();
                    case 2 -> cargar();
                    case 3 -> configurador();
                    case 4 -> validar();
                    case 5 -> {
                        exigir();
                        if (actual.variables > 25) {
                            System.out.print("n > 25 puede tardar mucho. ¿Continuar? (s/n): ");
                            if (!IN.nextLine().trim().equalsIgnoreCase("s")) break;
                        }
                        resolver("Fuerza bruta", SolverService.fuerzaBruta(actual));
                    }
                    case 6 -> resolver("Backtracking", SolverService.backtracking(actual));
                    case 7 -> comparar();
                    case 8 -> verificarManual();
                    case 9 -> generar();
                    case 10 -> compararTipos();
                    case 11 -> { Benchmark.ejecutar(Path.of("data/resultados")); System.out.println("CSV generados. Ejecute python services/charts-service/generar_graficas.py"); }
                    case 12 -> complejidad();
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
        }
    }
    private static Formula leerFormula() {
        int n = entero("Variables: "), m = entero("Cláusulas: "); List<int[]> cs = new ArrayList<>();
        for (int i = 0; i < m; i++) { System.out.println("Cláusula " + (i + 1) + " (tres enteros):"); cs.add(Arrays.stream(IN.nextLine().trim().split("\\s+")).mapToInt(Integer::parseInt).toArray()); }
        actual = new Formula(n, cs); return actual;
    }
    private static void cargar() throws Exception { System.out.print("Ruta: "); actual = FormulaService.desdeArchivo(Path.of(IN.nextLine().trim())); System.out.println("Fórmula cargada."); }
    private static void configurador() {
        int n = entero("Opciones: ");
        nombres = new String[n];
        for (int i = 0; i < n; i++) {
            System.out.print("Nombre de x" + (i + 1) + ": ");
            nombres[i] = IN.nextLine().trim();
        }
        int m = entero("Restricciones: ");
        List<int[]> cs = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            System.out.print("Restricción " + (i + 1) + " (tres enteros): ");
            cs.add(Arrays.stream(IN.nextLine().trim().split("\\s+")).mapToInt(Integer::parseInt).toArray());
        }
        actual = new Formula(n, cs);
        System.out.println("Configuración cargada.");
    }
    private static void validar() { exigir(); List<String> e = FormulaService.validar3CNF(actual); System.out.println(e.isEmpty() ? "Estructura 3-CNF válida." : String.join("\n", e)); }
    private static void resolver(String nombre, Resultado r) { exigir(); System.out.printf("%s: %s; nodos=%d, asignaciones=%d, tiempo=%dns%n", nombre, r.satisfacible() ? "SAT" : "FÓRMULA INSATISFACIBLE (UNSAT)", r.nodosVisitados(), r.asignacionesProbadas(), r.tiempoNs()); if (r.asignacion() != null && SolverService.verificar(actual, r.asignacion())) System.out.println(formatear(r.asignacion())); }
    private static void comparar() { exigir(); resolver("Fuerza bruta", SolverService.fuerzaBruta(actual)); resolver("Backtracking", SolverService.backtracking(actual)); }
    private static void verificarManual() {
        exigir();
        boolean[] a = new boolean[actual.variables];
        for (int i = 0; i < a.length; i++) a[i] = entero("x" + (i + 1) + " (1=V, 0=F): ") == 1;
        int fallida = SolverService.primeraClausulaFalsa(actual, a);
        if (fallida == -1) System.out.println("Asignación válida: satisface todas las cláusulas.");
        else if (fallida == -2) System.out.println("Error: longitud de asignación incorrecta.");
        else System.out.println("Asignación no válida: falla la cláusula " + (fallida + 1) + ".");
    }
    private static void generar() { int n = entero("Variables: "), m = entero("Cláusulas: "); actual = Generador.satGarantizada(n, m, 2026); System.out.println("Instancia SAT reproducible generada."); }
    private static void compararTipos() {
        exigir();
        Resultado general = SolverService.resolverSatGeneral(actual);
        System.out.println("SAT general: " + (general.satisfacible() ? "SAT" : "UNSAT"));
        if (actual.esTresCNF()) {
            Resultado tres = SolverService.backtracking(actual);
            System.out.println("3-SAT: " + (tres.satisfacible() ? "SAT" : "UNSAT"));
        } else {
            ResultadoReduccion r = SolverService.reducirSatA3SAT(actual);
            System.out.println("Reducción SAT→3-SAT: +" + r.variablesAuxiliares() + " variables, +" +
                    r.clausulasAnadidas() + " cláusulas; resultado " +
                    (SolverService.backtracking(r.formula()).satisfacible() ? "SAT" : "UNSAT"));
        }
        System.out.println("2-SAT se resuelve en tiempo Θ(n+m) mediante implicaciones y SCC.");
    }
    private static void complejidad() {
        System.out.println("Algoritmo             Mejor       Promedio       Peor          Espacio");
        System.out.println("Validación             Θ(m)        Θ(m)           Θ(m)          O(1)");
        System.out.println("Verificación           Ω(1)        O(m)           Θ(m)          O(1)");
        System.out.println("Fuerza bruta           Ω(m)        O(2^n m)       Θ(2^n m)      O(n+m)");
        System.out.println("Backtracking           O(nm)       depende poda   O(2^n m)      O(n)");
        System.out.println("2-SAT                  Θ(n+m)      Θ(n+m)         Θ(n+m)        O(n+m)");
    }
    private static void exigir() { if (actual == null) throw new IllegalStateException("Primero ingrese, cargue o genere una fórmula."); }
    private static int entero(String s) { System.out.print(s); return Integer.parseInt(IN.nextLine().trim()); }
    private static String formatear(boolean[] a) {
        StringBuilder b = new StringBuilder("Asignación: ");
        for (int i = 0; i < a.length; i++) {
            String nombre = i < nombres.length && !nombres[i].isBlank() ? nombres[i] : "x" + (i + 1);
            b.append(nombre).append("=").append(a[i] ? "V" : "F").append(i + 1 == a.length ? "" : ", ");
        }
        return b.toString();
    }
}
