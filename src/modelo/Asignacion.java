package modelo;

/** Resultado de asignar un paquete a un repartidor. Inmutable. */
public class Asignacion {

    private final Paquete paquete;
    private final Repartidor repartidor;

    public Asignacion(Paquete paquete, Repartidor repartidor) {
        if (paquete == null || repartidor == null) { 
            throw new IllegalArgumentException("Paquete y repartidor son obligatorios");
        }
        this.paquete = paquete; 
        this.repartidor = repartidor;
    }

    public Paquete getPaquete() { return paquete; } 
    public Repartidor getRepartidor() { return repartidor; } 
    @Override
    public String toString() { //Retorna el texto
        return paquete.getCodigo() + " (" + paquete.getDestino() + ") -> "
                + repartidor.getIdentificacion() + " " + repartidor.getNombre()
                + " [zona " + repartidor.getZona() + "]";
    }
}