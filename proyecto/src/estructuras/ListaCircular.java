package estructuras;

// Lista doblemente enlazada y circular: el último nodo apunta al primero
// y el primero apunta al último. Se usa para el camino cerrado del juego.
public class ListaCircular<T> implements Lista<T> {

    // Nodo con enlace hacia adelante y hacia atrás
    private static class Nodo<T> {
        private T dato;
        private Nodo<T> anterior;
        private Nodo<T> siguiente;

        private Nodo(T dato) {
            this.dato = dato;
        }
    }

    // Atributos
    private Nodo<T> inicio;
    private int tamanio;

    // Altas
    @Override
    public void agregar(T elemento) {
        insertar(tamanio, elemento);
    }

    @Override
    public void insertar(int indice, T elemento) {
        if (indice < 0 || indice > tamanio) {
            throw new IndexOutOfBoundsException("índice " + indice + ", tamaño " + tamanio);
        }
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (tamanio == 0) {
            nuevo.siguiente = nuevo;
            nuevo.anterior = nuevo;
            inicio = nuevo;
        } else {
            Nodo<T> posterior = inicio;
            if (indice < tamanio) {
                posterior = nodoEn(indice);
            }
            Nodo<T> previo = posterior.anterior;
            previo.siguiente = nuevo;
            nuevo.anterior = previo;
            nuevo.siguiente = posterior;
            posterior.anterior = nuevo;
            if (indice == 0) {
                inicio = nuevo;
            }
        }
        tamanio++;
    }

    // Consultas
    @Override
    public T obtener(int indice) {
        validar(indice);
        return nodoEn(indice).dato;
    }

    // Da la vuelta: el índice tamanio() vuelve al 0, el -1 es el último
    public T obtenerCircular(int indice) {
        if (tamanio == 0) {
            throw new IllegalStateException("la lista está vacía");
        }
        int posicion = indice % tamanio;
        if (posicion < 0) {
            posicion = posicion + tamanio;
        }
        return nodoEn(posicion).dato;
    }

    @Override
    public int indiceDe(T elemento) {
        Nodo<T> actual = inicio;
        for (int i = 0; i < tamanio; i++) {
            boolean iguales;
            if (actual.dato == null) {
                iguales = (elemento == null);
            } else {
                iguales = actual.dato.equals(elemento);
            }
            if (iguales) {
                return i;
            }
            actual = actual.siguiente;
        }
        return -1;
    }

    @Override
    public int tamanio() {
        return tamanio;
    }

    @Override
    public boolean estaVacia() {
        return tamanio == 0;
    }

    // Bajas
    @Override
    public T eliminar(int indice) {
        validar(indice);
        Nodo<T> nodo = nodoEn(indice);
        if (tamanio == 1) {
            inicio = null;
        } else {
            nodo.anterior.siguiente = nodo.siguiente;
            nodo.siguiente.anterior = nodo.anterior;
            if (nodo == inicio) {
                inicio = nodo.siguiente;
            }
        }
        tamanio--;
        return nodo.dato;
    }

    @Override
    public void vaciar() {
        inicio = null;
        tamanio = 0;
    }

    // Auxiliares
    private void validar(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("índice " + indice + ", tamaño " + tamanio);
        }
    }

    // Llega al nodo recorriendo por el lado más corto
    private Nodo<T> nodoEn(int indice) {
        Nodo<T> actual = inicio;
        if (indice <= tamanio / 2) {
            for (int i = 0; i < indice; i++) {
                actual = actual.siguiente;
            }
        } else {
            for (int i = tamanio; i > indice; i--) {
                actual = actual.anterior;
            }
        }
        return actual;
    }
}
