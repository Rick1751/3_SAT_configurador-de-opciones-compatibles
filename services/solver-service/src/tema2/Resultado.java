package tema2;

public record Resultado(boolean satisfacible, boolean[] asignacion, long nodosVisitados,
                        long asignacionesProbadas, long tiempoNs) {}
