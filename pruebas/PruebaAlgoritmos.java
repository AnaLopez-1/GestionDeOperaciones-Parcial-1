package pruebas;

import algoritmos.Algoritmos;
import modelo.Paquete;
import servicio.CentroDistribucion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PruebaAlgoritmos { 
    private static int fallos = 0; 
    
    private static void verificar(String descripcion, boolean condicion) { 
        System.out.println((condicion ? "OK     " : "FALLO  ") + descripcion); 
        if (!condicion) { 
            fallos++; 
        }
}
    private static boolean casiIgual(double a, double b) { 
        return Math.abs(a - b) < 1e-9; 
}

    private static Paquete pq(String codigo, String destino, double peso, int prioridad) { 
        return new Paquete(codigo, destino, peso, prioridad, 10); 
}

    public static void main(String[] args) { 
        

        //  1. Peso total por municipio 
        List<Paquete> pesos = Arrays.asList( 
                pq("PQ101", "Armenia", 4.5, 3), 
                pq("PQ203", "Salento", 3.0, 3), 
                pq("PQ415", "Armenia", 7.2, 3), 
                pq("PQ730", "Armenia", 2.3, 3)); 
        verificar("peso total Armenia = 14.0", 
                casiIgual(Algoritmos.pesoTotalPorMunicipio(pesos, "Armenia"), 14.0));
        verificar("peso total Salento = 3.0", 
                casiIgual(Algoritmos.pesoTotalPorMunicipio(pesos, "Salento"), 3.0));
        verificar("peso total de municipio sin paquetes = 0", 
                casiIgual(Algoritmos.pesoTotalPorMunicipio(pesos, "Montenegro"), 0.0));
        verificar("peso total con lista vacia = 0", 
                casiIgual(Algoritmos.pesoTotalPorMunicipio(new ArrayList<Paquete>(), "Armenia"), 0.0));
    
        //  2. Contar prioridad >= minima 
        int[] prioridades = {5, 2, 4, 1, 5, 3, 4}; 
        List<Paquete> conPrioridad = new ArrayList<>(); 
        for (int i = 0; i < prioridades.length; i++) { 
            conPrioridad.add(pq("PQ" + (100 + i), "Armenia", 1.0, prioridades[i])); 
        }
        verificar("prioridad >= 4 -> 4 paquetes", 
                Algoritmos.contarConPrioridadMinima(conPrioridad, 4) == 4);
        verificar("prioridad >= 1 -> todos (7)", 
                Algoritmos.contarConPrioridadMinima(conPrioridad, 1) == 7);
        verificar("prioridad >= 5 -> 2 paquetes", 
                Algoritmos.contarConPrioridadMinima(conPrioridad, 5) == 2);
        verificar("contar con lista vacia = 0", 
                Algoritmos.contarConPrioridadMinima(new ArrayList<Paquete>(), 3) == 0);  

        // 3. Busqueda binaria
        List<Paquete> ordenados = Arrays.asList( 
                pq("PQ105", "Armenia", 1.0, 3), pq("PQ214", "Armenia", 1.0, 3),
                pq("PQ318", "Armenia", 1.0, 3), pq("PQ421", "Armenia", 1.0, 3),
                pq("PQ567", "Armenia", 1.0, 3), pq("PQ731", "Armenia", 1.0, 3),
                pq("PQ842", "Armenia", 1.0, 3), pq("PQ942", "Armenia", 1.0, 3));
        verificar("la lista de reporte cumple la precondicion", 
                Algoritmos.estaOrdenadaPorCodigo(ordenados));
        verificar("busqueda: PQ731 esta en la posicion 5", 
                Algoritmos.busquedaBinaria(ordenados, "PQ731") == 5);
        verificar("busqueda: primero (PQ105) -> 0", 
                Algoritmos.busquedaBinaria(ordenados, "PQ105") == 0);
        verificar("busqueda: ultimo (PQ942) -> 7", 
                Algoritmos.busquedaBinaria(ordenados, "PQ942") == 7);
        verificar("busqueda: menor que todos -> -1", 
                Algoritmos.busquedaBinaria(ordenados, "PQ001") == -1);
        verificar("busqueda: mayor que todos -> -1", 
                Algoritmos.busquedaBinaria(ordenados, "PQ999") == -1);
        verificar("busqueda: intermedio inexistente -> -1", 
                Algoritmos.busquedaBinaria(ordenados, "PQ500") == -1);
        verificar("busqueda: lista vacia -> -1", 
                Algoritmos.busquedaBinaria(new ArrayList<Paquete>(), "PQ105") == -1);
        List<Paquete> uno = Arrays.asList(pq("PQ105", "Armenia", 1.0, 3)); 
        verificar("busqueda: un solo elemento (existe y no existe)", 
                Algoritmos.busquedaBinaria(uno, "PQ105") == 0
                        && Algoritmos.busquedaBinaria(uno, "PQ106") == -1);

        // Sin la precondicion, la busqueda binaria falla aunque el dato exista
        List<Paquete> desordenados = Arrays.asList( 
                pq("PQ731", "Armenia", 1.0, 3), pq("PQ105", "Armenia", 1.0, 3),
                pq("PQ942", "Armenia", 1.0, 3), pq("PQ318", "Armenia", 1.0, 3));
        verificar("desordenada: no cumple la precondicion", 
                !Algoritmos.estaOrdenadaPorCodigo(desordenados));
        verificar("desordenada: PQ318 existe pero la busqueda binaria da -1", 
                Algoritmos.busquedaBinaria(desordenados, "PQ318") == -1);

        // Flujo real: ordenar una copia con el orden natural y buscar
        CentroDistribucion centro = new CentroDistribucion(); 
        for (Paquete p : desordenados) { 
            centro.registrarPaquete(p); 
        }
        List<Paquete> copiaOrdenada = centro.pendientesPorOrdenNatural(); 
        verificar("tras ordenar la copia, PQ318 si se encuentra (posicion 1)", 
                Algoritmos.busquedaBinaria(copiaOrdenada, "PQ318") == 1);

}
}