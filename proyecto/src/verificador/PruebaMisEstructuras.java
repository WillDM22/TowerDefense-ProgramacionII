package verificador;

import estructuras.ColaEnlazada;
import estructuras.ListaCircular;
import estructuras.PilaEnlazada;

public class PruebaMisEstructuras {

    // Punto de entrada
    public static void main(String[] args) {
        VerificadorEstructuras.verificarLista(new ListaCircular<Integer>());
        VerificadorEstructuras.verificarPila(new PilaEnlazada<Integer>());
        VerificadorEstructuras.verificarCola(new ColaEnlazada<Integer>());
    }
}
