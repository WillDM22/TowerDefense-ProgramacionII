package juego.reglas;

import contratos.Actualizable;
import contratos.Dibujable;
import estructuras.Cola;
import estructuras.ColaEnlazada;
import estructuras.Lista;
import estructuras.ListaCircular;
import estructuras.Pila;
import estructuras.PilaEnlazada;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import juego.entidades.Base;
import juego.entidades.Enemigo;
import juego.entidades.Punto;
import juego.entidades.Torre;
import motor.Temporizador;

// El mundo de la partida: guarda las entidades, aplica las reglas y lleva la cuenta.
// Usa las tres estructuras con un papel distinto cada una:
//   Lista circular -> el camino cerrado (y los enemigos y torres en juego)
//   Cola           -> las oleadas pendientes, en orden de llegada
//   Pila           -> las torres construidas, para deshacer la última
public class MundoDefensa implements Actualizable, Dibujable {

    // Constantes de reglas
    private static final int VIDAS_BASE = 10;
    private static final int DINERO_INICIAL = 150;
    private static final double PAUSA_INICIAL = 8.0;
    private static final double PAUSA_ENTRE_OLEADAS = 6.0;
    private static final int PUNTOS_POR_RECOMPENSA = 10;
    private static final int PUNTOS_POR_VIDA = 50;

    // Constantes de espacio
    private static final double MARGEN_CAMINO = 26;
    private static final double SEPARACION_TORRES = 34;
    private static final double MARGEN_PANTALLA = 20;
    private static final double ZONA_HUD = 50;
    private static final double ZONA_AYUDA = 40;

    // Estructuras de datos
    private final ListaCircular<Punto> camino = new ListaCircular<>();
    private final Lista<Enemigo> enemigos = new ListaCircular<>();
    private final Lista<Torre> torres = new ListaCircular<>();
    private final Pila<Torre> torresConstruidas = new PilaEnlazada<>();
    private final Cola<Oleada> oleadasPendientes = new ColaEnlazada<>();

    // Estado de la partida
    private final int ancho;
    private final int alto;
    private final int totalOleadas;
    private final Base base;
    private Oleada oleadaActual;
    private Temporizador relojSalida;
    private double pausa = PAUSA_INICIAL;
    private int numeroOleada;
    private int dinero = DINERO_INICIAL;
    private int puntaje;
    private boolean terminado;
    private boolean victoria;

    // Constructor
    public MundoDefensa(int ancho, int alto) {
        if (ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException("dimensiones inválidas");
        }
        this.ancho = ancho;
        this.alto = alto;
        crearCamino();
        crearOleadas();
        this.totalOleadas = oleadasPendientes.tamanio();
        Punto salida = camino.obtener(0);
        this.base = new Base(salida.getX(), salida.getY(), VIDAS_BASE);
    }

    // Camino cerrado: de P5 se vuelve a P0
    private void crearCamino() {
        camino.agregar(new Punto(ancho * 0.09, alto * 0.20));
        camino.agregar(new Punto(ancho * 0.91, alto * 0.20));
        camino.agregar(new Punto(ancho * 0.91, alto * 0.52));
        camino.agregar(new Punto(ancho * 0.53, alto * 0.52));
        camino.agregar(new Punto(ancho * 0.53, alto * 0.83));
        camino.agregar(new Punto(ancho * 0.09, alto * 0.83));
    }

    // Oleadas en orden: cada una es más difícil que la anterior
    private void crearOleadas() {
        oleadasPendientes.encolar(new Oleada(1, 6, 0, 0, 1.2));
        oleadasPendientes.encolar(new Oleada(2, 8, 3, 0, 1.0));
        oleadasPendientes.encolar(new Oleada(3, 8, 6, 1, 0.9));
        oleadasPendientes.encolar(new Oleada(4, 8, 8, 3, 0.8));
        oleadasPendientes.encolar(new Oleada(5, 10, 10, 5, 0.7));
    }

    // Construcción de torres
    public boolean puedeComprar() {
        return dinero >= Torre.COSTO;
    }

    public boolean puedeConstruir(double x, double y) {
        if (terminado) {
            return false;
        }
        if (x < MARGEN_PANTALLA || x > ancho - MARGEN_PANTALLA) {
            return false;
        }
        if (y < ZONA_HUD || y > alto - ZONA_AYUDA) {
            return false;
        }
        if (estaSobreElCamino(x, y)) {
            return false;
        }
        for (int i = 0; i < torres.tamanio(); i++) {
            if (torres.obtener(i).distanciaA(x, y) < SEPARACION_TORRES) {
                return false;
            }
        }
        return true;
    }

    public boolean construirTorre(double x, double y) {
        if (!puedeComprar() || !puedeConstruir(x, y)) {
            return false;
        }
        Torre torre = new Torre(x, y);
        torres.agregar(torre);
        torresConstruidas.apilar(torre);
        dinero = dinero - Torre.COSTO;
        return true;
    }

    // Deshacer: saca de la pila la última torre construida y devuelve la mitad del costo
    public boolean deshacerConstruccion() {
        if (terminado || torresConstruidas.estaVacia()) {
            return false;
        }
        Torre torre = torresConstruidas.desapilar();
        int posicion = torres.indiceDe(torre);
        torres.eliminar(posicion);
        dinero = dinero + Torre.COSTO / 2;
        return true;
    }

    private boolean estaSobreElCamino(double x, double y) {
        for (int i = 0; i < camino.tamanio(); i++) {
            Punto desde = camino.obtenerCircular(i);
            Punto hasta = camino.obtenerCircular(i + 1);
            double distancia = Line2D.ptSegDist(desde.getX(), desde.getY(), hasta.getX(), hasta.getY(), x, y);
            if (distancia < MARGEN_CAMINO) {
                return true;
            }
        }
        return false;
    }

    // Ciclo de juego
    @Override
    public void actualizar(double dt) {
        if (terminado) {
            return;
        }
        actualizarOleadas(dt);
        actualizarEnemigos(dt);
        actualizarTorres(dt);
        retirarEnemigos();
        verificarFin();
    }

    // Saca las oleadas de la cola y va creando sus enemigos
    private void actualizarOleadas(double dt) {
        if (oleadaActual == null) {
            if (oleadasPendientes.estaVacia()) {
                return;
            }
            pausa = pausa - dt;
            if (pausa <= 0) {
                oleadaActual = oleadasPendientes.desencolar();
                relojSalida = new Temporizador(oleadaActual.getIntervalo());
                numeroOleada = oleadaActual.getNumero();
            }
            return;
        }
        if (relojSalida.avanzar(dt)) {
            Enemigo nuevo = oleadaActual.crearSiguiente(camino);
            if (nuevo != null) {
                enemigos.agregar(nuevo);
            }
        }
        if (!oleadaActual.quedanEnemigos()) {
            oleadaActual = null;
            pausa = PAUSA_ENTRE_OLEADAS;
        }
    }

    private void actualizarEnemigos(double dt) {
        for (int i = 0; i < enemigos.tamanio(); i++) {
            enemigos.obtener(i).actualizar(dt);
        }
    }

    // Cada torre apunta al primer enemigo de la lista que esté a su alcance
    private void actualizarTorres(double dt) {
        for (int i = 0; i < torres.tamanio(); i++) {
            Torre torre = torres.obtener(i);
            torre.actualizar(dt);
            if (!torre.puedeDisparar()) {
                continue;
            }
            for (int j = 0; j < enemigos.tamanio(); j++) {
                Enemigo enemigo = enemigos.obtener(j);
                if (!enemigo.estaDestruido() && torre.estaEnAlcance(enemigo)) {
                    torre.disparar(enemigo);
                    break;
                }
            }
        }
    }

    // Recorre de atrás hacia adelante para poder eliminar sin saltear elementos
    private void retirarEnemigos() {
        for (int i = enemigos.tamanio() - 1; i >= 0; i--) {
            Enemigo enemigo = enemigos.obtener(i);
            if (enemigo.haCompletadoVuelta()) {
                base.recibirDanio(1);
                enemigos.eliminar(i);
            } else if (enemigo.estaDestruido()) {
                dinero = dinero + enemigo.getRecompensa();
                puntaje = puntaje + enemigo.getRecompensa() * PUNTOS_POR_RECOMPENSA;
                enemigos.eliminar(i);
            }
        }
    }

    private void verificarFin() {
        if (base.estaDestruido()) {
            terminado = true;
            victoria = false;
        } else if (oleadaActual == null && oleadasPendientes.estaVacia() && enemigos.estaVacia()) {
            terminado = true;
            victoria = true;
            puntaje = puntaje + base.getVidas() * PUNTOS_POR_VIDA;
        }
    }

    // Dibujo: camino, base, torres y enemigos
    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(new Color(28, 40, 34));
        g.fillRect(0, 0, ancho, alto);

        g.setColor(new Color(78, 74, 64));
        g.setStroke(new BasicStroke(34f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < camino.tamanio(); i++) {
            Punto desde = camino.obtenerCircular(i);
            Punto hasta = camino.obtenerCircular(i + 1);
            g.drawLine((int) desde.getX(), (int) desde.getY(), (int) hasta.getX(), (int) hasta.getY());
        }
        g.setStroke(new BasicStroke(1f));

        base.dibujar(g);
        for (int i = 0; i < torres.tamanio(); i++) {
            torres.obtener(i).dibujar(g);
        }
        for (int i = 0; i < enemigos.tamanio(); i++) {
            enemigos.obtener(i).dibujar(g);
        }
    }

    // Consultas para la pantalla
    public int getDinero() {
        return dinero;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public int getVidasBase() {
        return base.getVidas();
    }

    public int getNumeroOleada() {
        return numeroOleada;
    }

    public int getTotalOleadas() {
        return totalOleadas;
    }

    public int getEnemigosEnPantalla() {
        return enemigos.tamanio();
    }

    public int getConstruccionesParaDeshacer() {
        return torresConstruidas.tamanio();
    }

    public boolean estaEnPausa() {
        return oleadaActual == null && !oleadasPendientes.estaVacia();
    }

    public double getSegundosParaOleada() {
        return pausa;
    }

    // Mira el frente de la cola sin sacar la oleada
    public Oleada getProximaOleada() {
        if (oleadasPendientes.estaVacia()) {
            return null;
        }
        return oleadasPendientes.frente();
    }

    public boolean estaTerminado() {
        return terminado;
    }

    public boolean esVictoria() {
        return victoria;
    }
}
