package pruebas;

import estructuras.ListaEnlazada;
import modelo.Paquete;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PruebaListaEnlazada {
    private static int fallos = 0;

    private static void verificar(String descripcion, boolean condicion) {
        System.out.println((condicion ? "OK     " : "FALLO  ") + descripcion);
        if (!condicion) {
            fallos++;
        }
    }

    private static ListaEnlazada<Integer> lista123() {
        ListaEnlazada<Integer> l = new ListaEnlazada<>();
        l.agregar(1);
        l.agregar(2);
        l.agregar(3);
        return l;
    }

    public static void main(String[] args) {

        // 1. Lista vacía
        ListaEnlazada<String> vacia = new ListaEnlazada<>();
        verificar("vacia: estaVacia", vacia.estaVacia());
        verificar("vacia: tamanio 0", vacia.tamanio() == 0);
        verificar("vacia: eliminar devuelve false", !vacia.eliminar("X"));
        verificar("vacia: iterador sin elementos", !vacia.iterator().hasNext());
        try {
            vacia.obtener(0);
            verificar("vacia: obtener(0) lanza excepcion", false);
        } catch (IndexOutOfBoundsException e) {
            verificar("vacia: obtener(0) lanza excepcion", true);
        }


        // 2. Un solo elemento (y que la cola quede bien despues de eliminar)
        ListaEnlazada<String> una = new ListaEnlazada<>();
        una.agregar("A");
        verificar("uno: tamanio 1 y obtener(0)", una.tamanio() == 1 && una.obtener(0).equals("A"));
        verificar("uno: eliminar A", una.eliminar("A"));
        verificar("uno: queda vacia", una.estaVacia());
        una.agregar("B");
        verificar("uno: agregar tras vaciar -> [B]", una.toString().equals("[B]"));


        // 3. Eliminar el primero
        ListaEnlazada<Integer> l = lista123();
        verificar("primero: eliminar 1", l.eliminar(1));
        verificar("primero: queda [2 -> 3]", l.toString().equals("[2 -> 3]"));


        // 4. Eliminar uno intermedio
        l = lista123();
        verificar("medio: eliminar 2", l.eliminar(2));
        verificar("medio: queda [1 -> 3]", l.toString().equals("[1 -> 3]"));


        // 5. Eliminar el ultimo (y comprobar que agregar sigue funcionando)
        l = lista123();
        verificar("ultimo: eliminar 3", l.eliminar(3));
        verificar("ultimo: queda [1 -> 2]", l.toString().equals("[1 -> 2]"));
        l.agregar(4);
        verificar("ultimo: agregar 4 -> [1 -> 2 -> 4]", l.toString().equals("[1 -> 2 -> 4]"));


        // 6. Eliminar un elemento inexistente
        l = lista123();
        verificar("inexistente: eliminar 99 devuelve false", !l.eliminar(99));
        verificar("inexistente: tamanio sigue en 3", l.tamanio() == 3);

        // 7. Iterador: next() sin elementos y recorrido completo
        Iterator<Integer> it = l.iterator();
        int suma = 0;
        while (it.hasNext()) {
            suma += it.next();
        }
        verificar("iterador: recorre todo (suma 6)", suma == 6);
        try {
            it.next();
            verificar("iterador: next() al final lanza excepcion", false);
        } catch (NoSuchElementException e) {
            verificar("iterador: next() al final lanza excepcion", true);
        }


        // 8. Genericidad: la misma lista con Paquete, recorrida con for-each
        ListaEnlazada<Paquete> historial = new ListaEnlazada<>();
        historial.agregar(new Paquete("PQ105", "Salento", 3.0, 4, 25));
        historial.agregar(new Paquete("PQ942", "Armenia", 2.0, 5, 20));
        historial.agregar(new Paquete("PQ731", "Armenia", 7.5, 3, 45));
        StringBuilder orden = new StringBuilder();
        for (Paquete p : historial) {
            orden.append(p.getCodigo()).append(' ');
        }
        verificar("generica: historial conserva orden de entrega",
                orden.toString().trim().equals("PQ105 PQ942 PQ731"));

        System.out.println();
        System.out.println(fallos == 0 ? "Todas las pruebas pasaron."
        : "Pruebas fallidas: " + fallos);
    }
}