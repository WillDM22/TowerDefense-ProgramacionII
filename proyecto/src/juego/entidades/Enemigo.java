package juego.entidades;

import estructuras.ListaCircular;
import java.awt.Color;
import java.awt.Graphics2D;

// Clase abstracta de los enemigos: recorren el camino cerrado vértice por vértice.
// Cada subclase decide su vida, su velocidad, su dibujo y cómo recibe el daño.
public abstract class Enemigo extends Entidad implements Danable {

    // Atributos
    private final ListaCircular<Punto> camino;
    private final int vidaMaxima;
    private final int recompensa;
    private final double velocidad;
    private int vida;
    private int indiceDestino;
    private boolean completoVuelta;

    // Constructor
    protected Enemigo(ListaCircular<Punto> camino, int vida, double velocidad, int recompensa) {
        super(0, 0);
        if (camino == null || camino.tamanio() < 2) {
            throw new IllegalArgumentException("el camino necesita al menos 2 puntos");
        }
        if (vida <= 0) {
            throw new IllegalArgumentException("la vida debe ser positiva: " + vida);
        }
        if (!(velocidad > 0)) {
            throw new IllegalArgumentException("la velocidad debe ser positiva: " + velocidad);
        }
        if (recompensa < 0) {
            throw new IllegalArgumentException("la recompensa no puede ser negativa: " + recompensa);
        }
        this.camino = camino;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.velocidad = velocidad;
        this.recompensa = recompensa;
        Punto salida = camino.obtener(0);
        setPosicion(salida.getX(), salida.getY());
        this.indiceDestino = 1;
    }

    // Movimiento por el camino
    @Override
    public void actualizar(double dt) {
        double avance = velocidad * dt;
        Punto destino = camino.obtenerCircular(indiceDestino);
        double dx = destino.getX() - getX();
        double dy = destino.getY() - getY();
        double distancia = Math.hypot(dx, dy);

        if (distancia <= avance) {
            // Llegó al vértice: se queda ahí y apunta al siguiente
            setPosicion(destino.getX(), destino.getY());
            if (indiceDestino == camino.tamanio()) {
                // El índice tamanio() es el vértice 0 otra vez: cerró la vuelta
                completoVuelta = true;
            }
            indiceDestino++;
        } else {
            setPosicion(getX() + dx / distancia * avance, getY() + dy / distancia * avance);
        }
    }

    // Daño
    @Override
    public void recibirDanio(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("el daño no puede ser negativo: " + puntos);
        }
        vida = vida - puntos;
        if (vida < 0) {
            vida = 0;
        }
    }

    @Override
    public boolean estaDestruido() {
        return vida == 0;
    }

    // Consultas
    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getRecompensa() {
        return recompensa;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public boolean haCompletadoVuelta() {
        return completoVuelta;
    }

    // Barra de vida que comparten todos los enemigos
    protected void dibujarBarraDeVida(Graphics2D g, int ancho) {
        int izquierda = (int) getX() - ancho / 2;
        int arriba = (int) getY() - 20;
        g.setColor(new Color(90, 30, 30));
        g.fillRect(izquierda, arriba, ancho, 4);
        g.setColor(new Color(90, 220, 90));
        g.fillRect(izquierda, arriba, ancho * vida / vidaMaxima, 4);
    }
}
