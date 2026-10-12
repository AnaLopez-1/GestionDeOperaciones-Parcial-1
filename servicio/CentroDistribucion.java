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
}