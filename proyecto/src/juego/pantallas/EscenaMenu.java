package juego.pantallas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import motor.Escena;
import ui.Texto;

// Pantalla 1: menú principal.
public class EscenaMenu extends Escena {

    // Actualización
    @Override
    public void actualizar(double dt) {
        if (getEntrada().fuePresionada(KeyEvent.VK_ENTER)) {
            getContexto().irA(new EscenaPartida());
        }
        if (getEntrada().fuePresionada(KeyEvent.VK_ESCAPE)) {
            getContexto().salir();
        }
    }

    // Dibujo
    @Override
    public void dibujar(Graphics2D g) {
        int ancho = getContexto().getAncho();
        int alto = getContexto().getAlto();
        g.setColor(new Color(20, 28, 26));
        g.fillRect(0, 0, ancho, alto);
        Texto.centrado(g, "Defensa en el circuito", ancho / 2.0, alto / 2.0 - 70, 46, Color.WHITE);
        Texto.centrado(g, "Los enemigos dan vueltas al circuito: no dejes que cierren una vuelta.",
                ancho / 2.0, alto / 2.0 - 20, 18, Color.LIGHT_GRAY);
        Texto.centrado(g, "Click: construir torre   Z: deshacer la última torre", ancho / 2.0, alto / 2.0 + 20, 18, Color.LIGHT_GRAY);
        Texto.centrado(g, "ENTER: jugar     ESC: salir", ancho / 2.0, alto / 2.0 + 80, 20, new Color(255, 220, 90));
    }
}
