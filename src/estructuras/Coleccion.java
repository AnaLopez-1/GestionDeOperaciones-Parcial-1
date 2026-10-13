package estructuras;

/**
 * @param <T> 
 */

//Esta interfaz solo dice QUÉ operaciones existen. CÓMO se implementan
public interface Coleccion<T> {

    
    void agregar(T elemento); 
    /**
    @return true si lo encontró y lo eliminó; false si no existía
     */
    boolean eliminar(T elemento); 
    /**
    @throws IndexOutOfBoundsException si la posición no es válida
     */

    T obtener(int posicion); 

    int tamanio(); 
    boolean estaVacia(); 
}