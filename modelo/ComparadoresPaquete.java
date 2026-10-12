package modelo;

import java.util.Comparator;

public final class ComparadoresPaquete {

    private ComparadoresPaquete() { }

    public static final Comparator<Paquete> POR_PRIORIDAD_DESC = (a, b) -> Integer.compare(b.getPrioridad(), a.getPrioridad());
    public static final Comparator<Paquete> POR_PESO_DESC = (a, b) -> Double.compare(b.getPeso(), a.getPeso());
    public static final Comparator<Paquete> POR_TIEMPO_ASC = (a, b) -> Integer.compare(a.getTiempoEstimado(), b.getTiempoEstimado());

    public static final Comparator<Paquete> DESPACHO = (a, b) -> {
    if (b.getPrioridad() != a.getPrioridad()) {
        return b.getPrioridad() - a.getPrioridad();
    }
    return a.getTiempoEstimado() - b.getTiempoEstimado();
    };
}