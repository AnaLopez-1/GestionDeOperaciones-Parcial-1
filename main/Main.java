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


    }
}