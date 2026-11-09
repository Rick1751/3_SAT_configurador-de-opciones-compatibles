package tema2;

/** Resultado común de los algoritmos exactos. */
public record Resultado(boolean satisfacible, boolean[] asignacion, long nodosVisitados,
                        long asignacionesProbadas, long tiempoNs, int profundidadMaxima) {}
