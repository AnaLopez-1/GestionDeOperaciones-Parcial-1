package algoritmos;

import modelo.Paquete;
import java.util.List;

public final class Algoritmos {

    private Algoritmos() { }

    /** Suma el peso de los paquetes cuyo destino es el municipio dado. O(n) tiempo, O(n) pila. */
    public static double pesoTotalPorMunicipio(List<Paquete> paquetes, String municipio) {
        return pesoTotal(paquetes, municipio, 0);
    }


    private static double pesoTotal(List<Paquete> paquetes, String municipio, int indice) {
        if (indice == paquetes.size()) {
            return 0.0;
        }

        Paquete actual = paquetes.get(indice);
        double aporte = actual.getDestino().equals(municipio) ? actual.getPeso() : 0.0;
        return aporte + pesoTotal(paquetes, municipio, indice + 1);
    }
}