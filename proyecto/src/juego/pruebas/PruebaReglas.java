package juego.pruebas;

import estructuras.ListaCircular;
import juego.entidades.Base;
import juego.entidades.Corredor;
import juego.entidades.Punto;
import juego.entidades.Soldado;
import juego.entidades.Tanque;
import juego.entidades.Torre;
import juego.reglas.MundoDefensa;
import juego.reglas.Oleada;

// Pruebas propias: se ejecutan con  ejecutar.bat juego.pruebas.PruebaReglas
public class PruebaReglas {

    // Contadores
    private static int total = 0;
    private static int fallas = 0;

    // Punto de entrada
    public static void main(String[] args) {
        probarDanio();
        probarValidaciones();
        probarVuelta();
        probarConstruccion();
        probarPartidas();
        System.out.println();
        System.out.println("Pruebas propias: " + (total - fallas) + " de " + total + " correctas");
    }

    // Camino cuadrado de prueba
    private static ListaCircular<Punto> caminoCuadrado() {
        ListaCircular<Punto> camino = new ListaCircular<>();
        camino.agregar(new Punto(0, 0));
        camino.agregar(new Punto(100, 0));
        camino.agregar(new Punto(100, 100));
        camino.agregar(new Punto(0, 100));
        return camino;
    }

    // El Tanque tiene armadura y el Soldado no
    private static void probarDanio() {
        System.out.println("Daño y polimorfismo");
        Soldado soldado = new Soldado(caminoCuadrado());
        Tanque tanque = new Tanque(caminoCuadrado());
        soldado.recibirDanio(4);
        tanque.recibirDanio(4);
        comprobar("el Soldado pierde 4 de vida", soldado.getVida() == 6);
        comprobar("el Tanque pierde 3 por la armadura", tanque.getVida() == 37);
        tanque.recibirDanio(1);
        comprobar("la armadura nunca deja el daño en 0", tanque.getVida() == 36);
        soldado.recibirDanio(1000);
        comprobar("la vida no baja de 0", soldado.getVida() == 0 && soldado.estaDestruido());
    }

    // Los constructores y setters rechazan valores imposibles
    private static void probarValidaciones() {
        System.out.println("Validaciones");
        comprobar("Torre con alcance negativo", lanza(1));
        comprobar("Torre con daño 0", lanza(2));
        comprobar("Base con 0 vidas", lanza(3));
        comprobar("Oleada vacía", lanza(4));
        comprobar("Enemigo con camino de 1 punto", lanza(5));
    }

    // Devuelve true si la creación inválida número 'caso' lanza IllegalArgumentException
    private static boolean lanza(int caso) {
        try {
            if (caso == 1) {
                new Torre(10, 10, -5, 4);
            }
            if (caso == 2) {
                new Torre(10, 10, 100, 0);
            }
            if (caso == 3) {
                new Base(10, 10, 0);
            }
            if (caso == 4) {
                new Oleada(1, 0, 0, 0, 1.0);
            }
            if (caso == 5) {
                ListaCircular<Punto> corto = new ListaCircular<>();
                corto.agregar(new Punto(0, 0));
                new Soldado(corto);
            }
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    // Un enemigo da la vuelta completa al camino cerrado
    private static void probarVuelta() {
        System.out.println("Camino cerrado");
        Corredor corredor = new Corredor(caminoCuadrado());
        int pasos = 0;
        while (!corredor.haCompletadoVuelta() && pasos < 5000) {
            corredor.actualizar(1.0 / 120);
            pasos++;
        }
        comprobar("el Corredor completa la vuelta", corredor.haCompletadoVuelta());
        comprobar("vuelve al punto de salida", corredor.distanciaA(0, 0) < 1);

        ListaCircular<Punto> camino = caminoCuadrado();
        comprobar("obtenerCircular(4) vuelve al primero", camino.obtenerCircular(4) == camino.obtener(0));
        comprobar("obtenerCircular(-1) es el último", camino.obtenerCircular(-1) == camino.obtener(3));
    }

    // Construir, rechazar posiciones inválidas y deshacer con la Pila
    private static void probarConstruccion() {
        System.out.println("Construcción y deshacer");
        MundoDefensa mundo = new MundoDefensa(900, 600);
        comprobar("no se construye sobre el camino", !mundo.construirTorre(400, 120));
        comprobar("se construye en un lugar libre", mundo.construirTorre(300, 200));
        comprobar("el dinero baja el costo de la torre", mundo.getDinero() == 100);
        comprobar("no se construye pegada a otra torre", !mundo.construirTorre(310, 205));
        mundo.construirTorre(700, 200);
        comprobar("hay 2 construcciones para deshacer", mundo.getConstruccionesParaDeshacer() == 2);
        comprobar("deshacer devuelve true", mundo.deshacerConstruccion());
        comprobar("deshacer devuelve la mitad del costo", mundo.getDinero() == 75);
        comprobar("se deshizo la última (700,200): vuelve a estar libre", mundo.puedeConstruir(700, 200));
        comprobar("la anterior (300,200) sigue ocupada", !mundo.puedeConstruir(300, 200));
        mundo.deshacerConstruccion();
        comprobar("con la pila vacía no se puede deshacer", !mundo.deshacerConstruccion());
    }

    // Partidas completas sin ventana
    private static void probarPartidas() {
        System.out.println("Partidas completas");
        MundoDefensa sinTorres = new MundoDefensa(900, 600);
        simular(sinTorres, false);
        comprobar("sin torres se pierde", sinTorres.estaTerminado() && !sinTorres.esVictoria());

        MundoDefensa conTorres = new MundoDefensa(900, 600);
        simular(conTorres, true);
        comprobar("con buenas torres se gana", conTorres.estaTerminado() && conTorres.esVictoria());
        comprobar("al ganar hay puntaje", conTorres.getPuntaje() > 0);
    }

    // Avanza la partida hasta que termina (máximo 10 minutos simulados)
    private static void simular(MundoDefensa mundo, boolean construir) {
        int pasos = 0;
        while (!mundo.estaTerminado() && pasos < 72000) {
            if (construir) {
                construirDondeSePueda(mundo);
            }
            mundo.actualizar(1.0 / 120);
            pasos++;
        }
    }

    // Va comprando torres en lugares buenos cada vez que alcanza el dinero
    private static void construirDondeSePueda(MundoDefensa mundo) {
        intentar(mundo, 700, 200);
        intentar(mundo, 300, 200);
        intentar(mundo, 200, 300);
        intentar(mundo, 650, 400);
        intentar(mundo, 400, 400);
        intentar(mundo, 580, 200);
        intentar(mundo, 150, 400);
    }

    private static void intentar(MundoDefensa mundo, double x, double y) {
        if (mundo.puedeComprar() && mundo.puedeConstruir(x, y)) {
            mundo.construirTorre(x, y);
        }
    }

    // Informe de cada verificación
    private static void comprobar(String descripcion, boolean condicion) {
        total++;
        if (condicion) {
            System.out.println("  OK     " + descripcion);
        } else {
            fallas++;
            System.out.println("  FALLA  " + descripcion);
        }
    }
}
