package juego.entidades;

import java.awt.Color;
import java.awt.Graphics2D;

// La base del jugador. Cada enemigo que completa una vuelta le quita vida.
public class Base extends Entidad implements Danable {

    // Atributos
    private int vidas;

    // Constructor
    public Base(double x, double y, int vidas) {
        super(x, y);
        if (vidas <= 0) {
            throw new IllegalArgumentException("las vidas deben ser positivas: " + vidas);
        }
        this.vidas = vidas;
    }

    // La base no cambia con el tiempo
    @Override
    public void actualizar(double dt) {
    }

    // Daño
    @Override
    public void recibirDanio(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("el daño no puede ser negativo: " + puntos);
        }
        vidas = vidas - puntos;
        if (vidas < 0) {
            vidas = 0;
        }
    }

    @Override
    public boolean estaDestruido() {
        return vidas == 0;
    }

    public int getVidas() {
        return vidas;
    }

    // Dibujo
    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(new Color(70, 120, 220));
        g.fillRect((int) getX() - 18, (int) getY() - 18, 36, 36);
        g.setColor(Color.WHITE);
        g.drawRect((int) getX() - 18, (int) getY() - 18, 36, 36);
    }
}
