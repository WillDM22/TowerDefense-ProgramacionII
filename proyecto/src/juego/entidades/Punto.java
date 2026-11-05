package juego.entidades;

// Vértice del camino. Es inmutable: una vez creado no cambia.
public class Punto {

    // Atributos
    private final double x;
    private final double y;

    // Constructor
    public Punto(double x, double y) {
        if (!Double.isFinite(x) || !Double.isFinite(y)) {
            throw new IllegalArgumentException("punto inválido: " + x + ", " + y);
        }
        this.x = x;
        this.y = y;
    }

    // Consultas
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
