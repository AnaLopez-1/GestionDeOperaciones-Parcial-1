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
        titulo("12. Reglas de asignación manual");
        intentarAsignar(centro, "PQ214", "R06");   // R06 no disponible
        intentarAsignar(centro, "PQ420", "R01");   // zona distinta
        intentarAsignar(centro, "PQ999", "R01");   // paquete inexistente

        titulo("13. Entregas e historial");
        String[] entregas = {"PQ926", "PQ105", "PQ583"};
        for (String codigo : entregas) {
            Repartidor r = centro.repartidorAsignadoA(codigo);
            boolean ok = centro.registrarEntrega(codigo);
            System.out.println("Entrega " + codigo + ": " + ok
                    + (r != null ? "  (" + r.getIdentificacion() + " disponible: "
                    + r.isDisponible() + ")" : ""));
        }
        System.out.println("Historial (orden real de entrega): " + codigos(centro.getHistorial()));
        System.out.println("Entregados: " + centro.cantidadEntregados()
                + " | Pendientes: " + centro.cantidadPendientes());
        System.out.println("Municipios con pendientes: " + centro.getMunicipiosOrdenados());

        titulo("14. La lista propia es genérica");
        ListaEnlazada<String> palabras = new ListaEnlazada<>();
        palabras.agregar("Armenia");
        palabras.agregar("Salento");
        palabras.agregar("Calarcá");
        palabras.eliminar("Salento");
        System.out.println("ListaEnlazada<String> tras eliminar Salento: " + palabras
                + " (tamaño " + palabras.tamanio() + ")");
    }
    
    // Datos iniciales
    private static void cargarRepartidores(CentroDistribucion centro) {
        centro.registrarRepartidor(new Repartidor("R01", "Ana Gómez", "Armenia", true));
        centro.registrarRepartidor(new Repartidor("R02", "Luis Rojas", "Armenia", true));
        centro.registrarRepartidor(new Repartidor("R03", "Marta Díaz", "Calarcá", true));
        centro.registrarRepartidor(new Repartidor("R04", "Pedro Mora", "Salento", true));
        centro.registrarRepartidor(new Repartidor("R05", "Laura Ríos", "Montenegro", true));
        centro.registrarRepartidor(new Repartidor("R06", "Jorge Pineda", "Circasia", false));
        centro.registrarRepartidor(new Repartidor("R07", "Sofía Cano", "La Tebaida", true));
    }

    private static void cargarPaquetes(CentroDistribucion centro) {
        // código, destino, peso (kg), prioridad (1-5), tiempo estimado (min)
        agregar(centro, "PQ731", "Armenia", 4.5, 5, 45);
        agregar(centro, "PQ105", "Salento", 3.0, 5, 25);
        agregar(centro, "PQ942", "Armenia", 2.0, 4, 20);
        agregar(centro, "PQ318", "Calarcá", 6.0, 2, 35);
        agregar(centro, "PQ567", "Salento", 1.5, 3, 15);
        agregar(centro, "PQ829", "Armenia", 9.0, 1, 50);
        agregar(centro, "PQ420", "Montenegro", 12.0, 3, 30);
        agregar(centro, "PQ214", "Circasia", 5.5, 4, 40);
        agregar(centro, "PQ842", "Armenia", 7.2, 2, 28);
        agregar(centro, "PQ421", "La Tebaida", 3.8, 5, 33);
        agregar(centro, "PQ650", "Calarcá", 2.4, 3, 22);
        agregar(centro, "PQ377", "Salento", 8.1, 1, 55);
        agregar(centro, "PQ908", "Montenegro", 4.0, 4, 18);
        agregar(centro, "PQ153", "Armenia", 18.7, 3, 42);
        agregar(centro, "PQ474", "Circasia", 1.2, 5, 12);
        agregar(centro, "PQ796", "La Tebaida", 6.6, 2, 37);
        agregar(centro, "PQ261", "Calarcá", 10.3, 4, 48);
        agregar(centro, "PQ583", "Armenia", 3.3, 5, 26);
        agregar(centro, "PQ034", "Salento", 2.9, 2, 31);
        agregar(centro, "PQ715", "Montenegro", 5.0, 1, 44);
        agregar(centro, "PQ389", "Circasia", 7.7, 3, 29);
        agregar(centro, "PQ926", "Calarcá", 4.2, 5, 19);
        agregar(centro, "PQ148", "La Tebaida", 9.4, 4, 52);
        agregar(centro, "PQ672", "Armenia", 1.8, 1, 14);
        agregar(centro, "PQ503", "Salento", 11.0, 5, 36);
        agregar(centro, "PQ859", "Montenegro", 2.2, 3, 23);
        agregar(centro, "PQ237", "Armenia", 6.1, 4, 38);
        agregar(centro, "PQ464", "Calarcá", 3.6, 2, 17);
        agregar(centro, "PQ981", "Circasia", 8.8, 5, 47);
        agregar(centro, "PQ092", "La Tebaida", 5.9, 3, 27);
    }

    private static void agregar(CentroDistribucion centro, String codigo, String destino,
                                double peso, int prioridad, int tiempo) {
        centro.registrarPaquete(new Paquete(codigo, destino, peso, prioridad, tiempo));
    }
    
    // Utilidades de impresión
    
    private static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }

    private static String codigos(Iterable<Paquete> paquetes) {
        StringBuilder sb = new StringBuilder();
        for (Paquete p : paquetes) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(p.getCodigo());
        }
        return sb.toString();
    }

    private static String primeros(List<Paquete> paquetes, int cantidad) {
        List<Paquete> parte = new ArrayList<>();
        for (int i = 0; i < cantidad && i < paquetes.size(); i++) {
            parte.add(paquetes.get(i));
        }
        return codigos(parte);
    }

    private static void mostrarOrden(CentroDistribucion centro, String etiqueta,
    Comparator<Paquete> criterio) {
        System.out.println(etiqueta + primeros(centro.pendientesOrdenadosPor(criterio), 5));
    }

    private static void intentarRegistrar(CentroDistribucion centro, String motivo, String codigo,
    String destino, double peso, int prioridad, int tiempo) {
        try {
            centro.registrarPaquete(new Paquete(codigo, destino, peso, prioridad, tiempo));
            System.out.println("Aceptado (" + motivo + "): " + codigo);
        } catch (IllegalArgumentException e) {
            System.out.println("Rechazado (" + motivo + "): " + e.getMessage());
        }
    }

    private static void imprimirAsignacion(Asignacion asignacion) {
        System.out.println(asignacion == null
                ? "Sin paquetes asignables en este momento"
                : "Asignado: " + asignacion);
    }

    private static void intentarAsignar(CentroDistribucion centro, String codigo, String repartidor) {
        try {
            System.out.println("Asignado: " + centro.asignar(codigo, repartidor));
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Rechazado (" + codigo + " -> " + repartidor + "): " + e.getMessage());
        }
    }
}


    

    
}