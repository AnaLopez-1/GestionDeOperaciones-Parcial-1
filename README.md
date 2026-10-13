# Gestión de Operaciones - Centro de Distribución (QuindíoExpress)

## Información Académica
- **Institución:** Universidad del Quindío
- **Programa:** Ingeniería de Sistemas y Computación
- **Asignatura:** Estructuras de Datos
- **Docente:** Robinson Arias Muñoz
- **Integrantes:**+ Ana Ligia López Bedoya y Maritza Calderón Zapata

-------------------------------------------------------------------------

## Descripción del Proyecto
Implementación del sistema logístico para la empresa **QuindíoExpress**, encargado de gestionar el registro, ordenamiento, priorización, despacho y entrega de paquetes hacia los municipios del departamento del Quindío.

El desarrollo aplica:
1. **Colecciones de Java Collections Framework:** Coordinación simultánea de `ArrayList`, `HashMap`, `HashSet`, `TreeSet`, `ArrayDeque` y `PriorityQueue` para optimizar consultas en O(1) y ordenamientos logarítmicos.
2. **TAD Propio y Genérico:** Implementación desde cero de una lista simplemente enlazada (`ListaEnlazada<T>`) con clases internas privadas, inserción en tiempo constante O(1) y soporte nativo para el bucle `for-each` mediante `Iterable<T>` e `Iterator<T>`.
3. **Algoritmos de Análisis:** Métodos puros y desacoplados con recursión lineal sin ciclos, búsqueda binaria sobre arreglos ordenados ($O(\log n)) y resolución del paquete de mayor peso mediante la estrategia **Divide y Vencerás** (O(n) tiempo, O(\log n) pila).

--------------------------------------------------------------------------

## Estructura del Repositorio
.
├── src/
│   ├── algoritmos/
│   │   └── Algoritmos.java             # Recursión lineal, Búsqueda Binaria y Divide y Vencerás
│   ├── estructuras/
│   │   ├── Coleccion.java              # Interfaz genérica del TAD Colección
│   │   └── ListaEnlazada.java          # Implementación propia con nodos y puntero a cola
│   ├── modelo/
│   │   ├── Asignacion.java             # Entidad inmutable de asignación de despacho
│   │   ├── ComparadoresPaquete.java    # Comparadores personalizados y regla compuesta DESPACHO
│   │   ├── Paquete.java                # Entidad del dominio con validaciones y Comparable
│   │   └── Repartidor.java             # Entidad de personal de entrega
│   ├── servicio/
│   │   └── CentroDistribucion.java     # Lógica central del sistema y orquestación de colecciones
│   ├── pruebas/
│   │   ├── PruebaAlgoritmos.java       # Suite de pruebas para algoritmos recursivos
│   │   ├── PruebaAsignacion.java       # Pruebas de reglas de asignación y disponibilidad
│   │   └── PruebaListaEnlazada.java    # Pruebas de casos frontera del TAD ListaEnlazada
│   └── Main.java                       # Simulación integral de la jornada logística
└── README.md                           # Documentación técnica del proyecto