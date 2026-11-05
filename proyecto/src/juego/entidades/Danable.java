package juego.entidades;

// Contrato propio: todo lo que puede recibir daño y ser destruido.
// Lo implementan Enemigo (y sus subclases) y Base.
public interface Danable {

    // Resta puntos de vida
    void recibirDanio(int puntos);

    // Verdadero cuando ya no le queda vida
    boolean estaDestruido();
}
