package pruebas;

import modelo.Asignacion;
import modelo.Paquete;
import modelo.Repartidor;
import servicio.CentroDistribucion;

public class PruebaAsignacion {

    private static int fallos = 0;

    private static void verificar(String descripcion, boolean condicion) { //Actua como acumulador de pruebas fallidas
        System.out.println((condicion ? "OK     " : "FALLO  ") + descripcion); //Sale OK si es true y FALLO si es false
        if (!condicion) {
            fallos++;
        }
    }

    public static void main(String[] args) {

        CentroDistribucion c = new CentroDistribucion();

        //REPARTIDORES
        c.registrarRepartidor(new Repartidor("R1", "Ana Gomez", "Armenia", true));
        c.registrarRepartidor(new Repartidor("R2", "Luis Rojas", "Armenia", true));
        c.registrarRepartidor(new Repartidor("R3", "Marta Diaz", "Salento", false));
        c.registrarRepartidor(new Repartidor("R4", "Pedro Mora", "Calarcá", true));

        verificar("repartidores: 4 registrados", c.getRepartidores().size() == 4); 
        verificar("repartidores: consulta por identificacion",
                c.consultarRepartidor("R1") != null 
                        && c.consultarRepartidor("R1").getNombre().equals("Ana Gomez"));
        verificar("repartidores: identificacion inexistente -> null",
                c.consultarRepartidor("R9") == null); 
        try {
            c.registrarRepartidor(new Repartidor("R2", "Otro Nombre", "Armenia", true)); 
        } catch (IllegalArgumentException e) {
            verificar("repartidores: identificacion repetida lanza excepcion", true);
        }
        verificar("repartidores: el repetido no se agrego", c.getRepartidores().size() == 4);

     //PAQUETES
        c.registrarPaquete(new Paquete("PQ001", "Armenia", 3.0, 3, 10));
        c.registrarPaquete(new Paquete("PQ002", "Salento", 2.0, 5, 10));
        c.registrarPaquete(new Paquete("PQ003", "Calarcá", 4.0, 4, 10));
        c.registrarPaquete(new Paquete("PQ004", "Armenia", 1.0, 5, 20));
        c.registrarPaquete(new Paquete("PQ005", "Armenia", 6.0, 2, 5));
    
    //ASIGNACION MANUAL
        try {
            c.asignar("PQ002", "R3"); //Valida método asignar
            verificar("manual: repartidor no disponible no recibe asignacion", false); //Repartidor R3 no esta disponible, por lo que no puede recibir asignacion
        } catch (IllegalStateException e) {
            verificar("manual: repartidor no disponible no recibe asignacion", true);
        }
        try {
            c.asignar("PQ003", "R1");
            verificar("manual: zona distinta al destino se rechaza", false); //Valida que si el repartidor no pertenece a la zona del paquete, no se puede asignar
        } catch (IllegalStateException e) {
            verificar("manual: zona distinta al destino se rechaza", true);
        }
        try {
            c.asignar("PQ999", "R1");
            verificar("manual: paquete inexistente lanza excepcion", false); //Valida que si el paquete no existe, lanza excepcion
        } catch (IllegalArgumentException e) {
            verificar("manual: paquete inexistente lanza excepcion", true);
        }
        try {
            c.asignar("PQ001", "R9");
            verificar("manual: repartidor inexistente lanza excepcion", false); //Valida que si el repartidor no existe, lanza excepcion
        } catch (IllegalArgumentException e) {
            verificar("manual: repartidor inexistente lanza excepcion", true);
        }
        verificar("manual: las asignaciones rechazadas no cambiaron nada",
                c.cantidadAsignaciones() == 0 && c.consultarRepartidor("R1").isDisponible());

    
    }

}