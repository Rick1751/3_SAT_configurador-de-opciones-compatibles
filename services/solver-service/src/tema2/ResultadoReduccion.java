package tema2;

/** Resultado de convertir una CNF general a 3-CNF. */
public record ResultadoReduccion(Formula formula, int variablesAuxiliares, int clausulasAnadidas) {}
