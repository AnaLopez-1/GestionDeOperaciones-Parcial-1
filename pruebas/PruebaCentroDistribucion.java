package pruebas;

import modelo.ComparadoresPaquete;
import modelo.Paquete;
import servicio.CentroDistribucion;

/** Pruebas de CentroDistribucion usando los ejemplos del enunciado. */
public class PruebaCentroDistribucion { 

    private static int fallos = 0;
    private static void verificar(String descripcion, boolean condicion) { 
        System.out.println((condicion ? "OK     " : "FALLO  ") + descripcion); 
        if (!condicion) { // Si la prueba falla
            fallos++; // Incrementa el contador de errores
        }
}

    private static String codigos(Iterable<Paquete> paquetes) { 
        StringBuilder sb = new StringBuilder(); 
        for (Paquete p : paquetes) { 
            sb.append(p.getCodigo()).append(' ');
        }
        return sb.toString().trim(); 
}

    private static String texto(Iterable<String> elementos) { 
        StringBuilder sb = new StringBuilder(); 
        for (String e : elementos) { 
            if (sb.length() > 0) {
                sb.append(", "); 
        }
            sb.append(e); 
        }
        return sb.toString();
    }
    public static void main(String[] args) {

        CentroDistribucion c = new CentroDistribucion();
        c.registrarPaquete(new Paquete("PQ731", "Armenia", 4.5, 5, 45));
        c.registrarPaquete(new Paquete("PQ105", "Salento", 3.0, 5, 25));
        c.registrarPaquete(new Paquete("PQ942", "Armenia", 2.0, 4, 20));
        c.registrarPaquete(new Paquete("PQ318", "Calarcá", 6.0, 2, 35));
        c.registrarPaquete(new Paquete("PQ567", "Salento", 1.5, 3, 15));
        c.registrarPaquete(new Paquete("PQ829", "Armenia", 9.0, 1, 50));

        verificar("registro: 6 pendientes", c.cantidadPendientes() == 6);
        verificar("registro: conserva el orden de llegada",
                codigos(c.pendientesEnOrdenDeRegistro()).equals("PQ731 PQ105 PQ942 PQ318 PQ567 PQ829"));

        try {
            c.registrarPaquete(new Paquete("PQ105", "Armenia", 1.0, 1, 1));
            verificar("registro: codigo repetido lanza excepcion", false);
        } catch (IllegalArgumentException e) {
            verificar("registro: codigo repetido lanza excepcion", true);
        }
        verificar("registro: el repetido no se agrego", c.cantidadPendientes() == 6);

        verificar("consulta: existe PQ942", c.existe("PQ942"));
        verificar("consulta: PQ942 va a Armenia", c.consultar("PQ942").getDestino().equals("Armenia"));
        verificar("consulta: PQ000 no existe", c.consultar("PQ000") == null && !c.existe("PQ000"));

        System.out.println();
        System.out.println(fallos == 0 ? "Todas las pruebas pasaron."
                                    : "Pruebas fallidas: " + fallos);
    }
        private static String texto(Iterable<String> elementos) {
        StringBuilder sb = new StringBuilder();
        for (String e : elementos) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(e);
        }
        return sb.toString();
    
            verificar("municipios: 3 sin repetir", c.getMunicipios().size() == 3);
        verificar("municipios ordenados: Armenia, Calarcá, Salento",
                texto(c.getMunicipiosOrdenados()).equals("Armenia, Calarcá, Salento"));
        verificar("agrupacion: Armenia -> PQ731 PQ942 PQ829",
                codigos(c.paquetesDeMunicipio("Armenia")).equals("PQ731 PQ942 PQ829"));
        verificar("agrupacion: Salento -> PQ105 PQ567",
                codigos(c.paquetesDeMunicipio("Salento")).equals("PQ105 PQ567"));
        verificar("agrupacion: municipio sin paquetes -> vacio",
                c.paquetesDeMunicipio("Montenegro").isEmpty());

        verificar("orden natural (codigo)",
                codigos(c.pendientesPorOrdenNatural()).equals("PQ105 PQ318 PQ567 PQ731 PQ829 PQ942"));
        verificar("por peso desc",
                codigos(c.pendientesOrdenadosPor(ComparadoresPaquete.POR_PESO_DESC))
                        .equals("PQ829 PQ318 PQ731 PQ105 PQ942 PQ567"));
        verificar("por tiempo asc",
                codigos(c.pendientesOrdenadosPor(ComparadoresPaquete.POR_TIEMPO_ASC))
                        .equals("PQ567 PQ942 PQ105 PQ318 PQ731 PQ829"));
        verificar("por prioridad desc",
                codigos(c.pendientesOrdenadosPor(ComparadoresPaquete.POR_PRIORIDAD_DESC))
                        .equals("PQ731 PQ105 PQ942 PQ567 PQ318 PQ829"));
        verificar("ordenar no altera el orden de registro",
                codigos(c.pendientesEnOrdenDeRegistro()).equals("PQ731 PQ105 PQ942 PQ318 PQ567 PQ829"));

      // Entregas e historial 
        verificar("entrega: PQ105", c.registrarEntrega("PQ105")); 
        verificar("entrega: PQ942", c.registrarEntrega("PQ942")); 
        verificar("entrega: PQ731", c.registrarEntrega("PQ731")); 
        
        verificar("historial conserva el orden de entrega", 
                codigos(c.getHistorial()).equals("PQ105 PQ942 PQ731"));
        verificar("entregados = 3, pendientes = 3", 
                c.cantidadEntregados() == 3 && c.cantidadPendientes() == 3);
        verificar("pendientes restantes en orden de registro", 
                codigos(c.pendientesEnOrdenDeRegistro()).equals("PQ318 PQ567 PQ829"));
        verificar("entregado ya no se consulta", c.consultar("PQ105") == null); 
        verificar("entregar de nuevo devuelve false", !c.registrarEntrega("PQ105")); 
        try {
            c.registrarPaquete(new Paquete("PQ105", "Armenia", 1.0, 1, 1)); 
            verificar("codigo entregado no se puede reutilizar", false); 
        } catch (IllegalArgumentException e) { 
            verificar("codigo entregado no se puede reutilizar", true); 
        }
        verificar("Armenia solo conserva PQ829", 
                codigos(c.paquetesDeMunicipio("Armenia")).equals("PQ829"));
        verificar("el entregado salio de la cola de llegada", 
                c.siguientePorLlegada().getCodigo().equals("PQ318"));

        verificar("entrega: PQ567 (ultimo de Salento)", c.registrarEntrega("PQ567")); 
        verificar("Salento desaparece de los municipios", 
                texto(c.getMunicipiosOrdenados()).equals("Armenia, Calarcá")
                        && c.getMunicipios().size() == 2);


}}