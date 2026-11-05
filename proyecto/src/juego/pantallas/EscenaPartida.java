package juego.pantallas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import juego.reglas.MundoDefensa;
import motor.Entrada;
import motor.Escena;

// Pantalla 2: la partida. Traduce el teclado y el mouse en acciones del mundo.
public class EscenaPartida extends Escena {

    // Atributos
    private MundoDefensa mundo;
    private Hud hud;

    // Cada vez que se entra se empieza una partida nueva
    @Override
    public void alEntrar() {
        mundo = new MundoDefensa(getContexto().getAncho(), getContexto().getAlto());
        hud = new Hud(mundo, getContexto().getAncho(), getContexto().getAlto());
    }

    // Actualización
    @Override
    public void actualizar(double dt) {
        Entrada entrada = getEntrada();

        if (entrada.huboClick()) {
            mundo.construirTorre(entrada.getMouseX(), entrada.getMouseY());
        }
        if (entrada.fuePresionada(KeyEvent.VK_Z)) {
            mundo.deshacerConstruccion();
        }
        if (entrada.fuePresionada(KeyEvent.VK_ESCAPE)) {
            getContexto().irA(new EscenaMenu());
            return;
        }

        mundo.actualizar(dt);

        if (mundo.estaTerminado()) {
            getContexto().irA(new EscenaFin(mundo.getPuntaje(), mundo.esVictoria()));
        }
    }

    // Dibujo
    @Override
    public void dibujar(Graphics2D g) {
        mundo.dibujar(g);
        dibujarVistaPrevia(g);
        hud.dibujar(g);
    }

    // Círculo de alcance bajo el mouse: verde si se puede construir, rojo si no
    private void dibujarVistaPrevia(Graphics2D g) {
        int x = getEntrada().getMouseX();
        int y = getEntrada().getMouseY();
        if (mundo.puedeConstruir(x, y) && mundo.puedeComprar()) {
            g.setColor(new Color(90, 220, 120, 70));
        } else {
            g.setColor(new Color(230, 80, 80, 70));
        }
        g.fillOval(x - 110, y - 110, 220, 220);
    }
}
