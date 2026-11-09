package tema2;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/** Aplicación de consola que orquesta los servicios del proyecto. */
public final class Main {
    private static final Scanner ENTRADA = new Scanner(System.in);
    private static Formula actual;
    private static String[] nombres = new String[0];

    private Main() {}

    public static void main(String[] args) {
        System.out.println("=== TEMA 2: 3-SAT, configurador de opciones compatibles ===");
        while (true) {
            mostrarMenu();
            int opcion = entero("Opción: ");
            if (opcion == 13) return;
            try {
                ejecutar(opcion);
            } catch (IllegalArgumentException e) {
                System.out.println("Entrada no válida: " + mensaje(e));
            } catch (RuntimeException e) {
                System.out.println("No se pudo completar la operación: " + mensaje(e));
            } catch (IOException e) {
                System.out.println("No se pudo leer o escribir el archivo.");
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n1) Ingresar fórmula  2) Cargar archivo  3) Configurador");
        System.out.println("4) Validar  5) Fuerza bruta  6) Backtracking  7) Comparar");
        System.out.println("8) Verificar asignación  9) Generar  10) SAT general vs 3-SAT");
        System.out.println("11) Benchmark  12) Complejidad  13) Salir");
    }

    private static void ejecutar(int opcion) throws IOException {
        switch (opcion) {
            case 1 -> ingresar();
            case 2 -> cargar();
            case 3 -> configurador();
            case 4 -> validar();
            case 5 -> resolverFuerzaBruta();
            case 6 -> resolverBacktracking();
            case 7 -> comparar();
            case 8 -> verificarManual();
            case 9 -> generar();
            case 10 -> compararTipos();
            case 11 -> benchmark();
            case 12 -> complejidad();
            default -> System.out.println("Opción inválida. Elija un número del 1 al 13.");
        }
    }

    private static void ingresar() {
        actual = leerFormulaNumerica();
        nombres = new String[0];
        System.out.println("Fórmula ingresada.");
    }

    private static Formula leerFormulaNumerica() {
        int variables = enteroPositivo("Número de variables (n): ", false);
        int cantidad = enteroPositivo("Número de cláusulas (m): ", true);
        List<int[]> clausulas = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            clausulas.add(leerClausulaNumerica(i + 1));
        }
        return new Formula(variables, clausulas);
    }

    private static int[] leerClausulaNumerica(int numero) {
        while (true) {
            System.out.print("Cláusula " + numero + " (literales separados por espacios): ");
            String linea = ENTRADA.nextLine().trim();
            try {
                if (linea.isEmpty()) throw new NumberFormatException();
                String[] partes = linea.split("\\s+");
                int[] resultado = new int[partes.length];
                for (int i = 0; i < partes.length; i++) resultado[i] = Integer.parseInt(partes[i]);
                return resultado;
            } catch (NumberFormatException e) {
                System.out.println("Ingrese literales enteros separados por espacios.");
            }
        }
    }

    private static void cargar() throws IOException {
        System.out.print("Ruta del archivo: ");
        actual = FormulaService.desdeArchivo(Path.of(ENTRADA.nextLine().trim()));
        nombres = new String[0];
        System.out.println("Fórmula cargada.");
    }

    private static void configurador() {
        int cantidad = enteroPositivo("Número de opciones: ", false);
        nombres = new String[cantidad];
        Map<String, Integer> indices = new HashMap<>();
        for (int i = 0; i < cantidad; i++) {
            System.out.print("Nombre de x" + (i + 1) + " (sin espacios): ");
            nombres[i] = ENTRADA.nextLine().trim();
            indices.put(nombres[i].toLowerCase(Locale.ROOT), i + 1);
        }
        int restricciones = enteroPositivo("Número de restricciones: ", true);
        List<int[]> clausulas = new ArrayList<>();
        for (int i = 0; i < restricciones; i++) {
            clausulas.add(leerRestriccion(i + 1, indices));
        }
        actual = new Formula(cantidad, clausulas);
        System.out.println("Configuración final:");
        imprimirNombres();
        validar();
    }

    private static int[] leerRestriccion(int numero, Map<String, Integer> indices) {
        while (true) {
            System.out.print("Restricción " + numero + " (números o nombres): ");
            String[] tokens = ENTRADA.nextLine().trim().split("\\s+");
            try {
                int[] resultado = new int[tokens.length];
                for (int i = 0; i < tokens.length; i++) resultado[i] = convertirLiteral(tokens[i], indices);
                return resultado;
            } catch (IllegalArgumentException e) {
                System.out.println("Restricción no válida: " + mensaje(e));
            }
        }
    }

    private static int convertirLiteral(String token, Map<String, Integer> indices) {
        if (token.matches("-?\\d+")) return Integer.parseInt(token);
        boolean negado = token.startsWith("-");
        String nombre = negado ? token.substring(1) : token;
        Integer indice = indices.get(nombre.toLowerCase(Locale.ROOT));
        if (indice == null) throw new IllegalArgumentException("opción desconocida: " + nombre);
        return negado ? -indice : indice;
    }

    private static void validar() {
        exigirFormula();
        ResultadoValidacion resultado = FormulaService.validar3CNF(actual);
        if (resultado.esValida()) System.out.println("FÓRMULA 3-CNF VÁLIDA");
        else {
            System.out.println("FÓRMULA INVÁLIDA");
            resultado.errores().forEach(error -> System.out.println("  Error: " + error));
        }
        resultado.avisos().forEach(aviso -> System.out.println("  Aviso: " + aviso));
    }

    private static void resolverFuerzaBruta() {
        exigirFormula();
        validarParaResolver();
        if (actual.variables > 25) {
            String respuesta = texto("n > 25 puede tardar mucho. ¿Continuar? (s/n): ");
            if (!respuesta.equalsIgnoreCase("s")) return;
        }
        mostrarResultado("Fuerza bruta", SolverService.fuerzaBruta(actual));
    }

    private static void resolverBacktracking() {
        exigirFormula();
        validarParaResolver();
        mostrarResultado("Backtracking", SolverService.backtracking(actual));
    }

    private static void comparar() {
        exigirFormula();
        validarParaResolver();
        Resultado fuerza = SolverService.fuerzaBruta(actual);
        Resultado backtracking = SolverService.backtracking(actual);
        double tiempoFuerza = fuerza.tiempoNs() / 1_000_000.0;
        double tiempoBacktracking = backtracking.tiempoNs() / 1_000_000.0;
        double speedup = tiempoBacktracking == 0 ? 0 : tiempoFuerza / tiempoBacktracking;
        System.out.println("algoritmo       resultado  asignaciones/nodos  tiempo (ms)  speedup");
        imprimirFila("Fuerza bruta", fuerza, tiempoFuerza, 1.0);
        imprimirFila("Backtracking", backtracking, tiempoBacktracking, speedup);
    }

    private static void imprimirFila(String nombre, Resultado resultado, double tiempo, double speedup) {
        String estado = resultado.satisfacible() ? "SAT" : "UNSAT";
        long explorados = Math.max(resultado.asignacionesProbadas(), resultado.nodosVisitados());
        System.out.printf(Locale.ROOT, "%-15s %-9s %18d %12.3f %7.3f%n",
                nombre, estado, explorados, tiempo, speedup);
    }

    private static void verificarManual() {
        exigirFormula();
        boolean[] asignacion = new boolean[actual.variables];
        for (int i = 0; i < asignacion.length; i++) {
            asignacion[i] = entero("x" + (i + 1) + " (1=V, 0=F): ") == 1;
        }
        validarParaResolver();
        int clausula = SolverService.primeraClausulaFalsa(actual, asignacion);
        if (clausula < 0) System.out.println("Asignación válida: satisface todas las cláusulas.");
        else System.out.println("Asignación no válida: falla la cláusula " + (clausula + 1) + ".");
    }

    private static void generar() {
        System.out.println("1) Aleatoria  2) SAT garantizada  3) UNSAT peor caso");
        int familia = entero("Familia: ");
        int n = enteroPositivo("Variables: ", false);
        int m = enteroPositivo("Cláusulas: ", true);
        long semilla = enteroLargo("Semilla (entero): ");
        actual = switch (familia) {
            case 1 -> Generador.aleatoria(n, m, semilla);
            case 2 -> Generador.satGarantizada(n, m, semilla);
            case 3 -> Generador.unsatGarantizada(n, m, semilla);
            default -> throw new IllegalArgumentException("familia inexistente.");
        };
        System.out.println("Instancia generada con semilla reproducible.");
    }

    private static void compararTipos() {
        exigirFormula();
        Resultado general = SolverService.resolverSatGeneral(actual);
        System.out.println("SAT general: " + estado(general));
        if (actual.esTresCNF()) {
            System.out.println("3-SAT: " + estado(SolverService.backtracking(actual)));
        } else {
            ResultadoReduccion reduccion = SolverService.reducirSatA3SAT(actual);
            System.out.println("Reducción: +" + reduccion.variablesAuxiliares() + " variables, +"
                    + reduccion.clausulasAnadidas() + " cláusulas.");
            System.out.println("3-SAT resultante: " + estado(SolverService.backtracking(reduccion.formula())));
        }
        System.out.println("2-SAT usa implicaciones y SCC en Θ(n+m).");
    }

    private static void benchmark() throws IOException {
        Benchmark.ejecutar(Path.of("data/resultados"));
        System.out.println("CSV generados. Ejecute el script Python de gráficas.");
    }

    private static void complejidad() {
        System.out.println("algoritmo       mejor       promedio       peor          espacio");
        System.out.println("Validación      Θ(m)        Θ(m)           Θ(m)          O(1)");
        System.out.println("Verificación    Ω(1)        O(m)           Θ(m)          O(1)");
        System.out.println("Fuerza bruta    Ω(m)        O(2^n m)       Θ(2^n m)      O(n+m)");
        System.out.println("Backtracking    O(nm)       depende poda   O(2^n m)      O(n)");
        System.out.println("2-SAT           Θ(n+m)      Θ(n+m)         Θ(n+m)        O(n+m)");
    }

    private static void mostrarResultado(String nombre, Resultado resultado) {
        String estado = resultado.satisfacible() ? "SAT" : "FÓRMULA INSATISFACIBLE (UNSAT)";
        System.out.printf(Locale.ROOT, "%s: %s; nodos=%d, asignaciones=%d, profundidad=%d, tiempo=%.3f ms%n",
                nombre, estado, resultado.nodosVisitados(), resultado.asignacionesProbadas(),
                resultado.profundidadMaxima(), resultado.tiempoNs() / 1_000_000.0);
        if (resultado.asignacion() != null && SolverService.verificar(actual, resultado.asignacion())) {
            System.out.println(formatear(resultado.asignacion()));
        }
    }

    private static void imprimirNombres() {
        for (int i = 0; i < nombres.length; i++) {
            System.out.println("x" + (i + 1) + " = " + nombres[i]);
        }
    }

    private static void validarParaResolver() {
        ResultadoValidacion resultado = FormulaService.validar3CNF(actual);
        if (!resultado.esValida()) {
            throw new IllegalArgumentException(String.join("; ", resultado.errores()));
        }
    }

    private static void exigirFormula() {
        if (actual == null) throw new IllegalStateException("Primero ingrese, cargue o genere una fórmula.");
    }

    private static int entero(String pregunta) {
        while (true) {
            System.out.print(pregunta);
            try {
                return Integer.parseInt(ENTRADA.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero.");
            }
        }
    }

    private static int enteroPositivo(String pregunta, boolean permiteCero) {
        while (true) {
            int valor = entero(pregunta);
            if (valor > 0 || (permiteCero && valor == 0)) return valor;
            System.out.println(permiteCero ? "Ingrese un número no negativo." : "Ingrese un número positivo.");
        }
    }

    private static long enteroLargo(String pregunta) {
        while (true) {
            System.out.print(pregunta);
            try {
                return Long.parseLong(ENTRADA.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un entero válido.");
            }
        }
    }

    private static String texto(String pregunta) {
        System.out.print(pregunta);
        return ENTRADA.nextLine().trim();
    }

    private static String estado(Resultado resultado) {
        return resultado.satisfacible() ? "SAT" : "UNSAT";
    }

    private static String formatear(boolean[] asignacion) {
        StringBuilder resultado = new StringBuilder("Asignación: ");
        for (int i = 0; i < asignacion.length; i++) {
            String nombre = i < nombres.length && !nombres[i].isBlank() ? nombres[i] : "x" + (i + 1);
            resultado.append(nombre).append('=').append(asignacion[i] ? 'V' : 'F');
            if (i + 1 < asignacion.length) resultado.append(", ");
        }
        return resultado.toString();
    }

    private static String mensaje(Exception e) {
        return e.getMessage() == null || e.getMessage().isBlank()
                ? "operación no válida." : e.getMessage();
    }
}
