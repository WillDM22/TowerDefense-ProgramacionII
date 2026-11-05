package juego.reglas;

import estructuras.ListaCircular;
import juego.entidades.Corredor;
import juego.entidades.Enemigo;
import juego.entidades.Punto;
import juego.entidades.Soldado;
import juego.entidades.Tanque;

// Una oleada: cuántos enemigos de cada tipo salen y cada cuántos segundos.
// Las oleadas esperan su turno en una Cola.
public class Oleada {

    // Atributos
    private final int numero;
    private final int totalEnemigos;
    private final double intervalo;
    private int soldados;
    private int corredores;
    private int tanques;

    // Constructor
    public Oleada(int numero, int soldados, int corredores, int tanques, double intervalo) {
        if (numero <= 0) {
            throw new IllegalArgumentException("el número de oleada debe ser positivo: " + numero);
        }
        if (soldados < 0 || corredores < 0 || tanques < 0) {
            throw new IllegalArgumentException("las cantidades no pueden ser negativas");
        }
        if (soldados + corredores + tanques == 0) {
            throw new IllegalArgumentException("la oleada no puede estar vacía");
        }
        if (!(intervalo > 0)) {
            throw new IllegalArgumentException("el intervalo debe ser positivo: " + intervalo);
        }
        this.numero = numero;
        this.soldados = soldados;
        this.corredores = corredores;
        this.tanques = tanques;
        this.intervalo = intervalo;
        this.totalEnemigos = soldados + corredores + tanques;
    }

    // Consultas
    public int getNumero() {
        return numero;
    }

    public int getTotalEnemigos() {
        return totalEnemigos;
    }

    public double getIntervalo() {
        return intervalo;
    }

    public boolean quedanEnemigos() {
        return soldados + corredores + tanques > 0;
    }

    // Crea el siguiente enemigo de la oleada (devuelve el tipo general Enemigo)
    public Enemigo crearSiguiente(ListaCircular<Punto> camino) {
        if (soldados > 0) {
            soldados--;
            return new Soldado(camino);
        }
        if (corredores > 0) {
            corredores--;
            return new Corredor(camino);
        }
        if (tanques > 0) {
            tanques--;
            return new Tanque(camino);
        }
        return null;
    }
}
