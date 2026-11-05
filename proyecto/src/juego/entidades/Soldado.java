package juego.entidades;

import estructuras.ListaCircular;
import java.awt.Color;
import java.awt.Graphics2D;

// Enemigo equilibrado: velocidad y vida medias. Se dibuja como círculo.
public class Soldado extends Enemigo {

    // Constructor
    public Soldado(ListaCircular<Punto> camino) {
        super(camino, 10, 60, 5);
    }

    // Dibujo
    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(new Color(90, 190, 90));
        g.fillOval((int) getX() - 9, (int) getY() - 9, 18, 18);
        dibujarBarraDeVida(g, 20);
    }
}
