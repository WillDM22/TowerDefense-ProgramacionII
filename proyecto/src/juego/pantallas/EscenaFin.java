package juego.pantallas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import motor.Escena;
import ui.Texto;

// Pantalla 3: fin de la partida con el puntaje (solo se ve en pantalla).
public class EscenaFin extends Escena {

    // Atributos
    private final int puntaje;
    private final boolean victoria;

    // Constructor
    public EscenaFin(int puntaje, boolean victoria) {
        if (puntaje < 0) {
            throw new IllegalArgumentException("el puntaje no puede ser negativo: " + puntaje);
        }
        this.puntaje = puntaje;
        this.victoria = victoria;
    }

    // Actualización
    @Override
    public void actualizar(double dt) {
        if (getEntrada().fuePresionada(KeyEvent.VK_ENTER)) {
            getContexto().irA(new EscenaPartida());
        }
        if (getEntrada().fuePresionada(KeyEvent.VK_ESCAPE)) {
            getContexto().irA(new EscenaMenu());
        }
    }

    // Dibujo
    @Override
    public void dibujar(Graphics2D g) {
        int ancho = getContexto().getAncho();
        int alto = getContexto().getAlto();
        g.setColor(new Color(24, 20, 30));
        g.fillRect(0, 0, ancho, alto);

        String titulo = "Derrota";
        Color color = new Color(240, 110, 110);
        if (victoria) {
            titulo = "¡Victoria!";
            color = new Color(110, 220, 130);
        }
        Texto.centrado(g, titulo, ancho / 2.0, alto / 2.0 - 40, 52, color);
        Texto.centrado(g, "Puntaje final: " + puntaje, ancho / 2.0, alto / 2.0 + 10, 26, Color.WHITE);
        Texto.centrado(g, "ENTER: jugar de nuevo     ESC: menú", ancho / 2.0, alto / 2.0 + 70, 18, Color.LIGHT_GRAY);
    }
}
