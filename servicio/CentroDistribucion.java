package servicio;

import estructuras.ListaEnlazada;
import modelo.Asignacion;
import modelo.ComparadoresPaquete;
import modelo.Paquete;
import modelo.Repartidor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

public class CentroDistribucion {

    private final List<Paquete> pendientes = new ArrayList<>();
    private final Map<String, Paquete> paquetesPorCodigo = new HashMap<>();
    private final Set<String> codigosRegistrados = new HashSet<>();

    private final Set<String> municipios = new HashSet<>();
    private final TreeSet<String> municipiosOrdenados = new TreeSet<>();
    private final Map<String, List<Paquete>> paquetesPorMunicipio = new HashMap<>();

    private final Queue<Paquete> colaLlegada = new ArrayDeque<>();
    private final PriorityQueue<Paquete> colaPrioridad = new PriorityQueue<>(ComparadoresPaquete.DESPACHO);

    private final ListaEnlazada<Paquete> historial = new ListaEnlazada<>();

    private final Map<String, Repartidor> repartidores = new HashMap<>();
    private final Map<String, Asignacion> asignaciones = new HashMap<>();

    /**
     * @throws IllegalArgumentException si es nulo o si el código ya fue
     *         registrado en la jornada (aunque ya se haya entregado)
     */
    
    public void registrarPaquete(Paquete paquete) {
        if (paquete == null) {
            throw new IllegalArgumentException("El paquete no puede ser nulo");
        }

        if (!codigosRegistrados.add(paquete.getCodigo())) {
            throw new IllegalArgumentException(
                    "Ya existe un paquete con el código " + paquete.getCodigo());
        }

        pendientes.add(paquete);
        paquetesPorCodigo.put(paquete.getCodigo(), paquete);

        String destino = paquete.getDestino();
        municipios.add(destino);
        municipiosOrdenados.add(destino);
        List<Paquete> delMunicipio = paquetesPorMunicipio.get(destino);
        if (delMunicipio == null) {
            delMunicipio = new ArrayList<>();
            paquetesPorMunicipio.put(destino, delMunicipio);
        }
        delMunicipio.add(paquete);

        colaLlegada.offer(paquete);
        colaPrioridad.offer(paquete);
    }

    
    public Paquete consultar(String codigo) {
        return paquetesPorCodigo.get(codigo);
    }

    public boolean existe(String codigo) {
        return paquetesPorCodigo.containsKey(codigo);
    }

    public int cantidadPendientes() {
        return pendientes.size();
    }

        public Set<String> getMunicipios() {
        return Collections.unmodifiableSet(municipios);
    }

    public Set<String> getMunicipiosOrdenados() {
        return Collections.unmodifiableSet(municipiosOrdenados);
    }

    public List<Paquete> paquetesDeMunicipio(String municipio) {
        List<Paquete> lista = paquetesPorMunicipio.get(municipio);
        if (lista == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(lista);
    }

        public List<Paquete> pendientesEnOrdenDeRegistro() {
        return Collections.unmodifiableList(pendientes);
    }

    public List<Paquete> pendientesOrdenadosPor(Comparator<Paquete> criterio) {
        List<Paquete> copia = new ArrayList<>(pendientes);
        copia.sort(criterio);
        return copia;
    }

    public List<Paquete> pendientesPorOrdenNatural() {
        List<Paquete> copia = new ArrayList<>(pendientes);
        Collections.sort(copia);
        return copia;
    }

    public Paquete siguientePorLlegada() {
        return colaLlegada.peek();
    }

    public Paquete siguientePrioritario() {
        return colaPrioridad.peek();
    }

    public Paquete despacharPorLlegada() {
        Paquete paquete = colaLlegada.poll();
        if (paquete != null) {
            colaPrioridad.remove(paquete);
        }
        return paquete;
    }

    public Paquete despacharPorPrioridad() {
        Paquete paquete = colaPrioridad.poll();
        if (paquete != null) {
            colaLlegada.remove(paquete);
        }
        return paquete;
    }

    /**
     * @throws IllegalArgumentException si es nulo o la identificación ya existe
     */

    public void registrarRepartidor(Repartidor repartidor) {
        if (repartidor == null) {
            throw new IllegalArgumentException("El repartidor no puede ser nulo");
        }
        if (repartidores.containsKey(repartidor.getIdentificacion())) {
            throw new IllegalArgumentException("Ya existe un repartidor con la identificación " + repartidor.getIdentificacion());
        }
        repartidores.put(repartidor.getIdentificacion(), repartidor);
    }

    public Repartidor consultarRepartidor(String identificacion) {
        return repartidores.get(identificacion);
    }

    public Collection<Repartidor> getRepartidores() {
        return Collections.unmodifiableCollection(repartidores.values());
    }

    public int cantidadAsignaciones() {
        return asignaciones.size();
    }

    public Repartidor repartidorAsignadoA(String codigoPaquete) {
        Asignacion a = asignaciones.get(codigoPaquete);
        return a == null ? null : a.getRepartidor();
    }

    /**
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */

    public Asignacion asignar(String codigoPaquete, String idRepartidor) {
        Paquete paquete = paquetesPorCodigo.get(codigoPaquete);
        if (paquete == null) {
            throw new IllegalArgumentException("No existe el paquete " + codigoPaquete);
        }
        Repartidor repartidor = repartidores.get(idRepartidor);
        if (repartidor == null) {
            throw new IllegalArgumentException("No existe el repartidor " + idRepartidor);
        }
        if (asignaciones.containsKey(codigoPaquete)) {
            throw new IllegalStateException("El paquete " + codigoPaquete + " ya está asignado");
        }
        if (!repartidor.isDisponible()) {
            throw new IllegalStateException("El repartidor " + idRepartidor + " no está disponible");
        }
        if (!repartidor.getZona().equals(paquete.getDestino())) {
            throw new IllegalStateException("La zona del repartidor no coincide con el destino");
        }
        return crearAsignacion(paquete, repartidor);
    }

    public Asignacion asignarPorLlegada() {
        for (Paquete p : colaLlegada) {
            Repartidor r = repartidorLibreParaZona(p.getDestino());
            if (r != null) {
                return crearAsignacion(p, r);
            }
        }
        return null;
    }

    public Asignacion asignarPorPrioridad() {
        List<Paquete> orden = new ArrayList<>(colaPrioridad);
        orden.sort(ComparadoresPaquete.DESPACHO);
        for (Paquete p : orden) {
            Repartidor r = repartidorLibreParaZona(p.getDestino());
            if (r != null) {
                return crearAsignacion(p, r);
            }
        }
        return null;
    }

    private Repartidor repartidorLibreParaZona(String zona) {
        List<String> ids = new ArrayList<>(repartidores.keySet());
        Collections.sort(ids);
        for (String id : ids) {
            Repartidor r = repartidores.get(id);
            if (r.isDisponible() && r.getZona().equals(zona)) {
                return r;
            }
        }
        return null;
    }

    private Asignacion crearAsignacion(Paquete paquete, Repartidor repartidor) {
        Asignacion a = new Asignacion(paquete, repartidor);
        asignaciones.put(paquete.getCodigo(), a);
        repartidor.setDisponible(false);
        colaLlegada.remove(paquete);
        colaPrioridad.remove(paquete);
        return a;
    }

    
}