# Roles y acta de contrato (Solo Dev)

El enunciado reparte el trabajo en cuatro módulos para equipos de 3 o 4 personas. Al trabajar solo, William es dueño de los cuatro.

| Rol | Paquete | Qué programa | Responsable |
|---|---|---|---|
| A. Entidades | `juego.entidades` | Clase abstracta `Entidad`, `Enemigo` y sus 3 subclases, `Torre`, `Base`, `Punto` e interfaz `Danable` | William |
| B. Estructuras | `estructuras` | `ListaCircular`, `PilaEnlazada` y `ColaEnlazada`, hasta pasar el verificador | William |
| C. Reglas | `juego.reglas` | `MundoDefensa` (colisiones por alcance, economía, puntaje, fin del juego) y `Oleada` | William |
| D. Pantalla | `juego.pantallas` | `EscenaMenu`, `EscenaPartida`, `EscenaFin` y `Hud` | William |

Además, en cada rol se cubren las tareas de testing y documentación.

## Acta de contrato

Una fila por cada clase que un módulo le pide a otro, con su constructor y los métodos que usan los demás.

| Clase | Módulo | Constructor | Métodos que usan los demás |
|---|---|---|---|
| `ListaCircular<T>` | B | `ListaCircular()` | `agregar`, `obtener`, `obtenerCircular`, `indiceDe`, `eliminar`, `tamanio` |
| `PilaEnlazada<T>` | B | `PilaEnlazada()` | `apilar`, `desapilar`, `tope`, `estaVacia`, `tamanio` |
| `ColaEnlazada<T>` | B | `ColaEnlazada()` | `encolar`, `desencolar`, `frente`, `estaVacia`, `tamanio` |
| `Punto` | A | `Punto(x, y)` | `getX`, `getY` |
| `Soldado`, `Corredor`, `Tanque` | A | `(ListaCircular<Punto> camino)` | `actualizar`, `dibujar`, `recibirDanio`, `estaDestruido`, `haCompletadoVuelta`, `getRecompensa` |
| `Torre` | A | `Torre(x, y)` y `Torre(x, y, alcance, danio)` | `puedeDisparar`, `disparar`, `estaEnAlcance`, `actualizar`, `dibujar` |
| `Base` | A | `Base(x, y, vidas)` | `recibirDanio`, `estaDestruido`, `getVidas` |
| `Oleada` | C | `Oleada(numero, soldados, corredores, tanques, intervalo)` | `crearSiguiente`, `quedanEnemigos`, `getNumero`, `getTotalEnemigos` |
| `MundoDefensa` | C | `MundoDefensa(ancho, alto)` | `construirTorre`, `puedeConstruir`, `deshacerConstruccion`, `actualizar`, `dibujar`, consultas para el HUD |
| `Hud` | D | `Hud(mundo, ancho, alto)` | `dibujar` |
| `EscenaFin` | D | `EscenaFin(puntaje, victoria)` | `actualizar`, `dibujar` |
