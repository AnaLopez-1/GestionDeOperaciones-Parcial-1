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

        public static int contarConPrioridadMinima(List<Paquete> paquetes, int prioridadMinima) {
        return contar(paquetes, prioridadMinima, 0);
    }

    private static int contar(List<Paquete> paquetes, int prioridadMinima, int indice) {
        if (indice == paquetes.size()) {
            return 0;
        }

        int este = paquetes.get(indice).getPrioridad() >= prioridadMinima ? 1 : 0;
        return este + contar(paquetes, prioridadMinima, indice + 1);
    }

        public static int busquedaBinaria(List<Paquete> ordenados, String codigo) {
        int bajo = 0;
        int alto = ordenados.size() - 1;
        while (bajo <= alto) {
            int medio = bajo + (alto - bajo) / 2;
            int comparacion = codigo.compareTo(ordenados.get(medio).getCodigo());
            if (comparacion == 0) {
                return medio;
            } else if (comparacion < 0) {
                alto = medio - 1;
            } else {
                bajo = medio + 1;
            }
        }
        return -1;
    }

    public static boolean estaOrdenadaPorCodigo(List<Paquete> paquetes) {
        for (int i = 1; i < paquetes.size(); i++) {
            if (paquetes.get(i - 1).compareTo(paquetes.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    public static Paquete paqueteMayorPeso(List<Paquete> paquetes) {
        if (paquetes == null || paquetes.isEmpty()) {
            throw new IllegalArgumentException("No hay paquetes para comparar");
        }
        return mayorPeso(paquetes, 0, paquetes.size() - 1);
    }


    private static Paquete mayorPeso(List<Paquete> paquetes, int inicio, int fin) {
        if (inicio == fin) {
            return paquetes.get(inicio);
        }

        int medio = inicio + (fin - inicio) / 2;

        Paquete mayorIzquierdo = mayorPeso(paquetes, inicio, medio);
        Paquete mayorDerecho = mayorPeso(paquetes, medio + 1, fin);

        if (mayorIzquierdo.getPeso() >= mayorDerecho.getPeso()) {
            return mayorIzquierdo;
        }
        return mayorDerecho;
    }
}