package juego.entidades;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

// Torre defensiva: dispara a un enemigo dentro de su alcance cada cierto tiempo.
public class Torre extends Entidad {

    // Constantes del juego
    public static final int COSTO = 50;
    private static final double ALCANCE_INICIAL = 110;
    private static final int DANIO_INICIAL = 4;
    private static final double CADENCIA_INICIAL = 0.6;
    private static final double DURACION_RAYO = 0.08;

    // Atributos
    private double alcance;
    private int danio;
    private double cadencia;
    private double enfriamiento;
    private double rayoRestante;
    private double rayoX;
    private double rayoY;

    // Constructores (sobrecarga: uno con valores por defecto y otro personalizado)
    public Torre(double x, double y) {
        this(x, y, ALCANCE_INICIAL, DANIO_INICIAL);
    }

    public Torre(double x, double y, double alcance, int danio) {
        super(x, y);
        setAlcance(alcance);
        setDanio(danio);
        this.cadencia = CADENCIA_INICIAL;
    }

    // Setters que validan
    public final void setAlcance(double alcance) {
        if (!(alcance > 0)) {
            throw new IllegalArgumentException("el alcance debe ser positivo: " + alcance);
        }
        this.alcance = alcance;
    }

    public final void setDanio(int danio) {
        if (danio <= 0) {
            throw new IllegalArgumentException("el daño debe ser positivo: " + danio);
        }
        this.danio = danio;
    }

    public double getAlcance() {
        return alcance;
    }

    public int getDanio() {
        return danio;
    }

    // Alcance (sobrecarga: contra un enemigo o contra un punto)
    public boolean estaEnAlcance(Enemigo enemigo) {
        return estaEnAlcance(enemigo.getX(), enemigo.getY());
    }

    public boolean estaEnAlcance(double px, double py) {
        return distanciaA(px, py) <= alcance;
    }

    // Disparo
    public boolean puedeDisparar() {
        return enfriamiento <= 0;
    }

    public void disparar(Enemigo objetivo) {
        if (objetivo == null) {
            throw new IllegalArgumentException("el objetivo no puede ser nulo");
        }
        objetivo.recibirDanio(danio);
        enfriamiento = cadencia;
        rayoRestante = DURACION_RAYO;
        rayoX = objetivo.getX();
        rayoY = objetivo.getY();
    }

    // Paso del tiempo: baja el enfriamiento y el rayo visible
    @Override
    public void actualizar(double dt) {
        if (enfriamiento > 0) {
            enfriamiento = enfriamiento - dt;
        }
        if (rayoRestante > 0) {
            rayoRestante = rayoRestante - dt;
        }
    }

    // Dibujo
    @Override
    public void dibujar(Graphics2D g) {
        if (rayoRestante > 0) {
            g.setColor(new Color(255, 240, 120));
            g.setStroke(new BasicStroke(2f));
            g.drawLine((int) getX(), (int) getY(), (int) rayoX, (int) rayoY);
        }
        g.setColor(new Color(60, 60, 80));
        g.fillOval((int) getX() - 14, (int) getY() - 14, 28, 28);
        g.setColor(new Color(150, 170, 230));
        g.fillOval((int) getX() - 8, (int) getY() - 8, 16, 16);
    }
}
