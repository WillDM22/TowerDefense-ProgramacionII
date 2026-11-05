# Guía de estudio y defensa

## 0. Cómo compilar, jugar y probar

| Qué | Windows | Linux / macOS |
|---|---|---|
| Compilar de cero | `compilar.bat` | `./compilar.sh` |
| Jugar | `ejecutar.bat juego.Main` | `./ejecutar.sh juego.Main` |
| Verificador del kit | `ejecutar.bat verificador.PruebaMisEstructuras` | `./ejecutar.sh verificador.PruebaMisEstructuras` |
| Pruebas propias | `ejecutar.bat juego.pruebas.PruebaReglas` | `./ejecutar.sh juego.pruebas.PruebaReglas` |

Controles: **click** construye una torre ($50), **Z** deshace la última, **ENTER** avanza, **ESC** vuelve o sale.

---

## 1. Cada requisito del enunciado: dónde está y cómo defenderlo

| Requisito | Dónde está | Cómo justificarlo |
|---|---|---|
| Compila de cero y se puede jugar (menú, partida, fin con puntaje) | `compilar.bat`, `EscenaMenu`, `EscenaPartida`, `EscenaFin` | El puntaje solo se muestra en pantalla: `EscenaFin` lo recibe por constructor y no lo guarda en ningún archivo |
| 3 escenas conectadas | Menú → Partida → Fin → (Partida o Menú) | Cada escena llama a `getContexto().irA(new ...)`. El motor aplica el cambio al comienzo del paso siguiente |
| Clase abstracta propia con 3 subclases que se comportan distinto | `Enemigo` → `Soldado`, `Corredor`, `Tanque` | Difieren en vida, velocidad y recompensa (pasadas al constructor), en el dibujo (círculo, triángulo, cuadrado) y en `recibirDanio` (el Tanque tiene armadura) |
| Interfaz propia implementada por 2 o más clases | `Danable` → `Enemigo` y `Base` | Es el contrato "recibe daño y puede quedar destruido", compartido por dos ramas distintas (los enemigos y la base) |
| Encapsulamiento: `private`, constructores y setters que validan | Todas las clases de `juego.*` | Ver las preguntas 11 y 12 |
| Lista, Pila y Cola propias que pasan el verificador | `ListaCircular`, `PilaEnlazada`, `ColaEnlazada` | 36, 23 y 22 verificaciones correctas |
| Cada TAD cumple una función real | Ver sección 2, pasos 3, 6 y 7 | Camino cerrado, oleadas pendientes y deshacer construcciones |
| Paquetes por responsabilidad, sin ciclos | `estructuras`, `juego.entidades`, `juego.reglas`, `juego.pantallas` | Las dependencias van en un solo sentido (ver `03-diseno-y-diagrama-de-clases.md`) |
| Sin colecciones de `java.util` ni arreglos `[]` fuera de los TADs | Todo `juego/` y `estructuras/` | La única aparición de `[]` es `String[] args` del `main`. Los TADs usan nodos enlazados, sin arreglos |
| Para subir la nota: sobrecarga con sentido | `Entidad.distanciaA`, `Torre.estaEnAlcance`, constructores de `Torre` | Mismo nombre, distintos parámetros: la distancia a una entidad o a un punto; el alcance contra un enemigo o contra un punto |
| Para subir la nota: pruebas propias | `juego.pruebas.PruebaReglas` | 26 verificaciones: armadura, validaciones, vuelta completa, construir y deshacer, partidas completas |
| Para subir la nota: mecánicas propias | Oleadas con dificultad creciente, economía, tres tipos de enemigos | Cada oleada trae más enemigos y más rápidos |
| Para subir la nota: menos `instanceof` | No hay ninguno | Se usa polimorfismo: la torre le hace daño a un `Enemigo` sin saber de qué tipo es |

---

## 2. La lógica paso a paso

**Paso 1: todo lo que está en el mapa es una `Entidad`.** Guarda una posición `(x, y)` y obliga a las hijas a implementar `actualizar(dt)` y `dibujar(g)` (los dos contratos del kit). Así `MundoDefensa` puede tratar a todas igual.

**Paso 2: el mapa es un camino cerrado de vértices.** Son 6 objetos `Punto` guardados en una `ListaCircular`. Un `Punto` es inmutable (sus atributos son `final`), porque un vértice del camino no debe cambiar mientras hay enemigos recorriéndolo.

**Paso 3: la lista circular conecta el último vértice con el primero.** `obtenerCircular(i)` calcula `i % tamanio()`, y si el resultado es negativo le suma `tamanio()`. Entonces el índice 6 vuelve al vértice 0 y el −1 es el último. Gracias a eso el enemigo no necesita un caso especial para "cerrar" el circuito.

**Paso 4: un enemigo se mueve hacia su próximo vértice.** En cada `actualizar(dt)` calcula `avance = velocidad * dt`. Si la distancia al vértice destino es menor o igual al avance, se coloca justo en el vértice y apunta al siguiente. Si no, se desplaza `avance` en la dirección del vértice (`dx / distancia * avance`). Usar `dt` hace que la velocidad no dependa de la computadora.

**Paso 5: completar la vuelta se detecta con un contador.** `indiceDestino` empieza en 1 y crece. Cuando el enemigo llega al vértice número `tamanio()`, que es el vértice 0 otra vez, activa `completoVuelta`. Ahí `MundoDefensa` lo elimina y le resta una vida a la `Base`.

**Paso 6: las oleadas esperan en una Cola.** Se encolan las 5 al empezar. Cuando termina la pausa, `desencolar()` saca la que está al frente (la más antigua) y se crea un `Temporizador` con su intervalo. Cada vez que el temporizador avisa, `Oleada.crearSiguiente` devuelve un enemigo nuevo. El HUD usa `frente()` para mostrar la próxima oleada **sin sacarla**.

**Paso 7: las torres construidas se apilan.** Al construir, la torre va a la lista del mundo (para dibujarla y que dispare) y también se apila. Al apretar Z, `desapilar()` devuelve la última construida, se busca su posición con `indiceDe` en la lista, se elimina y se devuelve la mitad del costo.

**Paso 8: cada torre dispara al primer enemigo a su alcance.** Si la torre `puedeDisparar()` (su enfriamiento llegó a 0), recorre los enemigos y le dispara al primero que `estaEnAlcance`. El enemigo recibe el daño con `recibirDanio`; qué pasa exactamente lo decide su clase (polimorfismo).

**Paso 9: los enemigos se retiran recorriendo la lista de atrás hacia adelante.** Si se elimina el elemento `i`, los siguientes se corren una posición. Recorriendo hacia adelante se saltaría el elemento que ocupó el lugar del eliminado; hacia atrás no.

**Paso 10: el fin de la partida se decide al final de cada paso.** Pierde si la base queda sin vidas. Gana si no hay oleada en curso, la cola está vacía y no quedan enemigos. Entonces `EscenaPartida` pasa a `EscenaFin` con el puntaje.

**Paso 11: dónde se puede construir.** Se rechaza si está fuera del área de juego, a menos de 26 píxeles de cualquier tramo del camino (se mide con `Line2D.ptSegDist`, de la librería estándar) o a menos de 34 píxeles de otra torre.

---

## 3. Guion de exposición (5 min de demo + 5 min de defensa)

**Demo (5 minutos)**
1. **(30 s)** Abrí la carpeta del repo: `docs` y `src`. Mostrá el historial de commits.
2. **(30 s)** Ejecutá `verificador.PruebaMisEstructuras`: las tres estructuras en OK.
3. **(3 min)** Jugá: menú → partida. Construí 3 torres, mostrá que no deja construir sobre el camino ni pegado a otra torre, deshacé una con Z y volvé a construir. Dejá que lleguen enemigos de los tres tipos y señalá que el Tanque tarda más en morir. Si podés, dejá pasar a un enemigo para ver bajar las vidas.
4. **(1 min)** Mostrá la pantalla de fin y volvé a jugar con ENTER.

**En el código (qué tener abierto, en este orden)**
1. `ListaCircular.obtenerCircular` y `nodoEn`: la estructura clave del juego.
2. `Enemigo.actualizar`: el movimiento y la detección de vuelta completa.
3. `Tanque.recibirDanio`: polimorfismo y sobreescritura en pocas líneas.
4. `MundoDefensa.actualizarOleadas` (Cola), `construirTorre` y `deshacerConstruccion` (Pila).
5. `MundoDefensa.retirarEnemigos`: el bucle hacia atrás.
6. El diagrama de clases.

**Frases de cierre sugeridas:** "Cada estructura está ahí porque su regla coincide con la del juego: el circuito es cíclico, las oleadas salen en orden de llegada y deshacer saca la última construcción".

---

## 4. Preguntas trampa y respuestas

**1. ¿Por qué una Cola para las oleadas y no una Lista?**
Las oleadas se atienden en el mismo orden en que se programaron y solo me interesa el frente y sacar por delante. La Cola expresa esa regla y no permite acceder a posiciones del medio, que no necesito.

**2. ¿Por qué una Pila para las torres construidas?**
Deshacer siempre actúa sobre la última acción, que es exactamente el orden LIFO: `desapilar()` me da la última torre construida.

**3. ¿Por qué la lista del camino es circular?**
Porque el camino es cerrado: el vértice posterior al último es el primero. Con `obtenerCircular` esa regla vive dentro de la estructura y no hay que repetir un `% tamanio()` en cada lugar que la usa.

**4. ¿Por qué doblemente enlazada?**
Cada nodo conoce a su anterior y a su siguiente. Eso permite que `nodoEn` recorra por el lado más corto (como máximo la mitad de la lista) y que `eliminar` enlace anterior y siguiente directamente.

**5. ¿Qué complejidad tiene `obtener(i)`?**
O(n) en el peor caso (en realidad hasta n/2 por el recorrido por el lado más corto). Un `for` con `obtener(i)` es entonces O(n²). Con menos de 50 elementos no se nota; si la lista fuera enorme, haría un iterador para recorrer en O(n).

**6. ¿Qué clases hay que tocar para agregar un enemigo nuevo?**
Creo una subclase de `Enemigo` (constructor con vida, velocidad y recompensa, y `dibujar`) y en `Oleada` agrego su contador y su rama en `crearSiguiente`. *Es honesto reconocer que `Oleada` todavía tiene una cadena de `if`; una mejora sería que la oleada fuera una Cola de tipos de enemigo.*

**7. ¿Por qué `Enemigo` es abstracta?**
No tiene sentido crear "un enemigo genérico": no sabría dibujarse. Al ser abstracta obliga a las subclases a implementar `dibujar` y comparte lo común (movimiento, vida, barra de vida).

**8. ¿Dónde hay polimorfismo real?**
La lista guarda `Enemigo` pero contiene Soldados, Corredores y Tanques. Cuando `Torre.disparar(Enemigo)` llama a `recibirDanio`, Java ejecuta la versión de la clase real del objeto: si es un Tanque, aplica la armadura sin que la torre lo sepa.

**9. ¿Diferencia entre sobreescritura y sobrecarga en tu código?**
Sobreescritura: `Tanque.recibirDanio` reemplaza al de `Enemigo` (mismo nombre y parámetros, distinta clase). Sobrecarga: `Entidad.distanciaA(Entidad)` y `distanciaA(double, double)` (mismo nombre, distintos parámetros, misma clase).

**10. ¿Para qué sirve la interfaz `Danable`?**
Define un contrato común ("recibe daño y puede ser destruido") para clases que no son de la misma rama: `Enemigo` y `Base`. *Cuidado: hoy el código usa las clases concretas en las variables; si me preguntan, aclaro que la interfaz permitiría agregar, por ejemplo, un obstáculo destructible sin tocar a quien causa el daño.*

**11. ¿Por qué `vida` es `private` y qué evita el setter?**
Para que nadie ponga la vida en un valor imposible. El constructor de `Enemigo` rechaza vida ≤ 0 y velocidad ≤ 0 (un enemigo con velocidad 0 nunca avanzaría y la partida quedaría trabada). `recibirDanio` impide vida negativa. `Torre.setAlcance` rechaza alcances ≤ 0 y `setDanio` daños ≤ 0.

**12. ¿Por qué `setPosicion` y los setters de `Torre` son `final`?**
Porque se llaman desde el constructor. Si una subclase los sobreescribiera, se ejecutarían con el objeto a medio construir.

**13. ¿Por qué `Punto` es inmutable?**
Sus atributos son `final`: los vértices del camino son compartidos por todos los enemigos y no deben cambiar mientras alguien los recorre.

**14. ¿Cómo se refresca la pantalla y qué es `dt`?**
La `Ventana` tiene un `Timer` de Swing que despierta cada 8 ms. En cada despertar, el `Motor` avanza la lógica en pasos fijos de 1/120 s llamando a `Escena.actualizar(dt)` y después se redibuja el panel llamando a `dibujar(g)`. `dt` es el tiempo transcurrido en cada paso; multiplicar la velocidad por `dt` hace que el movimiento no dependa de la computadora.

**15. ¿Por qué recorrés los enemigos de atrás hacia adelante para eliminarlos?**
Al eliminar el elemento `i`, los siguientes se corren una posición. Hacia adelante se salteaba el elemento que quedó en `i`.

**16. ¿Por qué los TADs lanzan esas excepciones?**
Es el contrato del kit: índice fuera de rango lanza `IndexOutOfBoundsException`; desapilar, `tope`, desencolar y `frente` sobre estructuras vacías lanzan `IllegalStateException`. El verificador lo comprueba.

**17. ¿Por qué `Enemigo` conoce a `ListaCircular` y no a `Lista`?**
Porque necesita `obtenerCircular`, que no está en la interfaz `Lista` (las interfaces del kit no se pueden modificar). Es una decisión consciente: acá la circularidad es parte del problema.

**18. ¿Hay dependencias circulares entre paquetes?**
No. `estructuras` no depende de nadie, `juego.entidades` solo de `estructuras` y `contratos`, `juego.reglas` de `entidades`, `juego.pantallas` de `reglas`.

**19. ¿Usaste arreglos `[]` o colecciones de `java.util`?**
No en el juego. Los TADs usan nodos enlazados. Lo único de `java.util` que usa el kit está en el verificador, y `[]` aparece solo en el `String[] args` del `main`.

**20. ¿Qué mejorarías?**
Un iterador para recorrer las listas en O(n), una segunda implementación de Lista (por ejemplo, simple y no circular) para los enemigos y las torres con su justificación, otros tipos de torre con su propia clase abstracta y un diseño de `Oleada` sin cadena de `if`.

---

## 5. Antes de entregar

- Verificá con el profesor lo de los **archivos que se quitaron del kit** (ver `03-diseno-y-diagrama-de-clases.md`). Si no compilaban en su copia, él seguramente sabrá por qué.
- Compilá de cero (`compilar.bat`) y corré las tres cosas de la sección 0.
- Hacé los commits reales (`04-historial-de-desarrollo.md`).
- Leé **todas** las clases al menos una vez: te pueden preguntar por cualquiera.
