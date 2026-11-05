package juego.entidades;

import estructuras.ListaCircular;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

// Enemigo rápido y frágil. Se dibuja como triángulo.
public class Corredor extends Enemigo {

    // Constructor
    public Corredor(ListaCircular<Punto> camino) {
        super(camino, 6, 110, 7);
    }

    // Dibujo
    @Override
    public void dibujar(Graphics2D g) {
        Path2D.Double triangulo = new Path2D.Double();
        triangulo.moveTo(getX(), getY() - 10);
        triangulo.lineTo(getX() + 9, getY() + 8);
        triangulo.lineTo(getX() - 9, getY() + 8);
        triangulo.closePath();
        g.setColor(new Color(240, 200, 60));
        g.fill(triangulo);
        dibujarBarraDeVida(g, 18);
    }
}
