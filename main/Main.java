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


    }
}