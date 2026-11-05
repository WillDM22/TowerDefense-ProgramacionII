package juego.entidades;

import contratos.Actualizable;
import contratos.Dibujable;

// Clase abstracta base: todo lo que está en el mapa tiene una posición,
// cambia con el tiempo (Actualizable) y se ve en pantalla (Dibujable).
public abstract class Entidad implements Actualizable, Dibujable {

    // Atributos
    private double x;
    private double y;

    // Constructor
    protected Entidad(double x, double y) {
        setPosicion(x, y);
    }

    // Posición
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public final void setPosicion(double x, double y) {
        if (!Double.isFinite(x) || !Double.isFinite(y)) {
            throw new IllegalArgumentException("posición inválida: " + x + ", " + y);
        }
        this.x = x;
        this.y = y;
    }

    // Distancia a otra entidad (sobrecarga: se puede pedir a un punto cualquiera)
    public double distanciaA(Entidad otra) {
        return distanciaA(otra.getX(), otra.getY());
    }

    public double distanciaA(double otroX, double otroY) {
        return Math.hypot(otroX - x, otroY - y);
    }
}
