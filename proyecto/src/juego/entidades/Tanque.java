package juego.entidades;

import estructuras.ListaCircular;
import java.awt.Color;
import java.awt.Graphics2D;

// Enemigo lento y resistente. Tiene armadura: cada golpe le hace menos daño.
public class Tanque extends Enemigo {

    // Constantes
    private static final int ARMADURA = 1;

    // Constructor
    public Tanque(ListaCircular<Punto> camino) {
        super(camino, 40, 35, 15);
    }

    // La armadura resta daño, pero siempre entra al menos 1 punto
    @Override
    public void recibirDanio(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("el daño no puede ser negativo: " + puntos);
        }
        int reducido = puntos - ARMADURA;
        if (reducido < 1) {
            reducido = 1;
        }
        super.recibirDanio(reducido);
    }

    // Dibujo
    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(new Color(170, 60, 60));
        g.fillRect((int) getX() - 12, (int) getY() - 12, 24, 24);
        g.setColor(new Color(90, 20, 20));
        g.drawRect((int) getX() - 12, (int) getY() - 12, 24, 24);
        dibujarBarraDeVida(g, 26);
    }
}
