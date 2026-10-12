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

    }
}