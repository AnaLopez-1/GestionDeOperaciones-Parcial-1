package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaEnlazada<T> implements Coleccion<T>, Iterable<T> {

    private static class Nodo<E> {
        private final E valor;
        private Nodo<E> siguiente;

        private Nodo(E valor) {
            this.valor = valor;
        }
    }

    private Nodo<T> primero;
    private Nodo<T> ultimo;
    private int tamanio;

    public ListaEnlazada() {
        this.primero = null;
        this.ultimo = null;
        this.tamanio = 0;
    }
}