package tema2;

import java.util.List;

/** Resultado separado de la validación estructural de una fórmula 3-CNF. */
public record ResultadoValidacion(List<String> errores, List<String> avisos) {
    public ResultadoValidacion {
        errores = List.copyOf(errores);
        avisos = List.copyOf(avisos);
    }

    /** Indica si la fórmula puede tratarse como 3-CNF válida. */
    public boolean esValida() {
        return errores.isEmpty();
    }
}
