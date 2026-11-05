package estructuras;

// Cola con nodos enlazados: se agrega por el final y se saca por el principio.
public class ColaEnlazada<T> implements Cola<T> {

    // Nodo de la cola
    private static class Nodo<T> {
        private final T dato;
        private Nodo<T> siguiente;

        private Nodo(T dato) {
            this.dato = dato;
        }
    }

    // Atributos
    private Nodo<T> primero;
    private Nodo<T> ultimo;
    private int tamanio;

    // Operaciones
    @Override
    public void encolar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (tamanio == 0) {
            primero = nuevo;
        } else {
            ultimo.siguiente = nuevo;
        }
        ultimo = nuevo;
        tamanio++;
    }

    @Override
    public T desencolar() {
        if (tamanio == 0) {
            throw new IllegalStateException("la cola está vacía");
        }
        T dato = primero.dato;
        primero = primero.siguiente;
        tamanio--;
        if (tamanio == 0) {
            ultimo = null;
        }
        return dato;
    }

    @Override
    public T frente() {
        if (tamanio == 0) {
            throw new IllegalStateException("la cola está vacía");
        }
        return primero.dato;
    }

    // Consultas
    @Override
    public boolean estaVacia() {
        return tamanio == 0;
    }

    @Override
    public int tamanio() {
        return tamanio;
    }

    @Override
    public void vaciar() {
        primero = null;
        ultimo = null;
        tamanio = 0;
    }
}
