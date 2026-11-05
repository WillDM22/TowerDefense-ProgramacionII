package estructuras;

// Pila con nodos enlazados: cada nodo apunta al que está debajo.
public class PilaEnlazada<T> implements Pila<T> {

    // Nodo de la pila
    private static class Nodo<T> {
        private final T dato;
        private final Nodo<T> debajo;

        private Nodo(T dato, Nodo<T> debajo) {
            this.dato = dato;
            this.debajo = debajo;
        }
    }

    // Atributos
    private Nodo<T> cima;
    private int tamanio;

    // Operaciones
    @Override
    public void apilar(T elemento) {
        cima = new Nodo<>(elemento, cima);
        tamanio++;
    }

    @Override
    public T desapilar() {
        if (tamanio == 0) {
            throw new IllegalStateException("la pila está vacía");
        }
        T dato = cima.dato;
        cima = cima.debajo;
        tamanio--;
        return dato;
    }

    @Override
    public T tope() {
        if (tamanio == 0) {
            throw new IllegalStateException("la pila está vacía");
        }
        return cima.dato;
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
        cima = null;
        tamanio = 0;
    }
}
