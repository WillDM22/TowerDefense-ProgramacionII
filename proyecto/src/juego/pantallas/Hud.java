package juego.pantallas;

import contratos.Dibujable;
import java.awt.Color;
import java.awt.Graphics2D;
import juego.reglas.MundoDefensa;
import juego.reglas.Oleada;
import ui.Texto;

// Barra superior con los datos de la partida y ayuda en la parte de abajo.
public class Hud implements Dibujable {

    // Atributos
    private final MundoDefensa mundo;
    private final int ancho;
    private final int alto;

    // Constructor
    public Hud(MundoDefensa mundo, int ancho, int alto) {
        if (mundo == null) {
            throw new IllegalArgumentException("el mundo no puede ser nulo");
        }
        this.mundo = mundo;
        this.ancho = ancho;
        this.alto = alto;
    }

    // Dibujo
    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, ancho, 40);
        g.fillRect(0, alto - 32, ancho, 32);

        Texto.izquierda(g, "Dinero: $" + mundo.getDinero(), 16, 27, 18, new Color(255, 220, 90));
        Texto.izquierda(g, "Puntaje: " + mundo.getPuntaje(), 190, 27, 18, Color.WHITE);
        Texto.derecha(g, "Vidas de la base: " + mundo.getVidasBase(), ancho - 16, 27, 18, new Color(240, 110, 110));

        String estado;
        if (mundo.estaEnPausa()) {
            Oleada proxima = mundo.getProximaOleada();
            estado = "Oleada " + proxima.getNumero() + " de " + mundo.getTotalOleadas() + " en "
                    + (int) Math.ceil(mundo.getSegundosParaOleada()) + " s (" + proxima.getTotalEnemigos() + " enemigos)";
        } else {
            estado = "Oleada " + mundo.getNumeroOleada() + " de " + mundo.getTotalOleadas()
                    + " - enemigos en pantalla: " + mundo.getEnemigosEnPantalla();
        }
        Texto.centrado(g, estado, ancho / 2.0 + 20, 27, 16, Color.LIGHT_GRAY);

        String ayuda = "Click: torre ($50)   Z: deshacer última (" + mundo.getConstruccionesParaDeshacer()
                + ")   ESC: menú";
        Texto.centrado(g, ayuda, ancho / 2.0, alto - 11, 14, Color.LIGHT_GRAY);
    }
}
