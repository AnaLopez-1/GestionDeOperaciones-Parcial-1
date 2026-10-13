import algoritmos.Algoritmos;
import estructuras.ListaEnlazada;
import modelo.Asignacion;
import modelo.ComparadoresPaquete;
import modelo.Paquete;
import modelo.Repartidor;
import servicio.CentroDistribucion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Demostración del sistema de QuindíoExpress con los datos mínimos del
 * enunciado: 30 paquetes, 6 destinos, 7 repartidores, prioridades y pesos
 * variados, códigos no consecutivos y varios paquetes al mismo municipio.
 */
public class Main {

    public static void main(String[] args) {
        
        CentroDistribucion centro = new CentroDistribucion();
        cargarRepartidores(centro);
        cargarPaquetes(centro);

        titulo("1. Carga inicial");
        System.out.println("Paquetes pendientes: " + centro.cantidadPendientes());
        System.out.println("Repartidores: " + centro.getRepartidores().size());
        for (Repartidor r : centro.getRepartidores()) {
            System.out.println("  " + r);
        }
        titulo("2. Orden de llegada y consulta por código");
        System.out.println("Primeros 6 en orden de registro: "
                + primeros(centro.pendientesEnOrdenDeRegistro(), 6));
        System.out.println("Consultar PQ731: " + centro.consultar("PQ731"));
        System.out.println("Consultar PQ000: " + centro.consultar("PQ000")
                + "  (existe: " + centro.existe("PQ000") + ")");

        titulo("3. Registros inválidos (se rechazan)");
        intentarRegistrar(centro, "código repetido", "PQ731", "Armenia", 1.0, 3, 10);
        intentarRegistrar(centro, "peso cero", "PQ777", "Armenia", 0.0, 3, 10);
        intentarRegistrar(centro, "prioridad fuera de rango", "PQ778", "Armenia", 1.0, 9, 10);
        intentarRegistrar(centro, "destino vacío", "PQ779", "  ", 1.0, 3, 10);
        intentarRegistrar(centro, "tiempo cero", "PQ780", "Armenia", 1.0, 3, 0);
        try {
            centro.registrarRepartidor(new Repartidor("R01", "Duplicado", "Armenia", true));
        } catch (IllegalArgumentException e) {
            System.out.println("Repartidor repetido rechazado: " + e.getMessage());
        }
        System.out.println("Pendientes siguen en: " + centro.cantidadPendientes());
        
        titulo("4. Municipios");
        System.out.println("Sin repetir: " + centro.getMunicipios().size() + " municipios");
        System.out.println("Ordenados: " + centro.getMunicipiosOrdenados());
        for (String municipio : centro.getMunicipiosOrdenados()) {
            System.out.println("  " + municipio + " -> "
                    + codigos(centro.paquetesDeMunicipio(municipio)));
        }
        
        titulo("5. Ordenamientos (primeros 5 de cada uno)");
        System.out.println("Natural (código):     "
                + primeros(centro.pendientesPorOrdenNatural(), 5));
        mostrarOrden(centro, "Prioridad (mayor):   ", ComparadoresPaquete.POR_PRIORIDAD_DESC);
        mostrarOrden(centro, "Peso (mayor):        ", ComparadoresPaquete.POR_PESO_DESC);
        mostrarOrden(centro, "Tiempo (menor):      ", ComparadoresPaquete.POR_TIEMPO_ASC);
        System.out.println("El orden de registro no cambió: "
                + primeros(centro.pendientesEnOrdenDeRegistro(), 3));


        titulo("6. Algoritmos recursivos");
        List<Paquete> todos = centro.pendientesEnOrdenDeRegistro();
        System.out.println("Peso total hacia Armenia: "
                + String.format("%.1f", Algoritmos.pesoTotalPorMunicipio(todos, "Armenia")) + " kg");
        System.out.println("Peso total hacia Salento: "
                + String.format("%.1f", Algoritmos.pesoTotalPorMunicipio(todos, "Salento")) + " kg");
        System.out.println("Paquetes con prioridad >= 4: "
                + Algoritmos.contarConPrioridadMinima(todos, 4));
        System.out.println("Paquetes con prioridad >= 5: "
                + Algoritmos.contarConPrioridadMinima(todos, 5));

        titulo("7. Búsqueda binaria (sobre copia ordenada por código)");
        List<Paquete> ordenados = centro.pendientesPorOrdenNatural();
        System.out.println("Precondición (ordenada por código): "
                + Algoritmos.estaOrdenadaPorCodigo(ordenados));
        System.out.println("PQ731 -> posición " + Algoritmos.busquedaBinaria(ordenados, "PQ731"));
        System.out.println("PQ000 -> posición " + Algoritmos.busquedaBinaria(ordenados, "PQ000"));

        titulo("8. Divide y vencerás: paquete de mayor peso");
        Paquete pesado = Algoritmos.paqueteMayorPeso(todos);
        System.out.println("Mayor peso: " + pesado);

        titulo("9. Atención: siguiente en cada modalidad");
        System.out.println("Siguiente por orden de llegada: " + centro.siguientePorLlegada());
        System.out.println("Siguiente por prioridad:        " + centro.siguientePrioritario());
        System.out.println("(Circasia no tiene repartidor disponible: R06 está fuera de servicio)");

        titulo("10. Asignación por prioridad (omite los que no tienen repartidor)");
        for (int i = 0; i < 6; i++) {
            imprimirAsignacion(centro.asignarPorPrioridad());
        }

        titulo("11. Asignación por orden de llegada");
        for (int i = 0; i < 3; i++) {
            imprimirAsignacion(centro.asignarPorLlegada());
        }
        System.out.println("Asignaciones activas: " + centro.cantidadAsignaciones());

    }
    
}