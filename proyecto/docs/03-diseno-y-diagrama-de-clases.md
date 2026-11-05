# Diseño y diagrama de clases

## Idea del juego
Defensa en el circuito: los enemigos salen de la base y recorren un camino **cerrado**. Si un enemigo completa una vuelta, le quita una vida a la base. El jugador construye torres con dinero (click) y puede deshacer la última construcción (Z). Los enemigos llegan en 5 oleadas que esperan en una cola.

## Las tres estructuras y su función real

| Estructura | Clase propia | Función en el juego |
|---|---|---|
| Lista (circular) | `ListaCircular` | El **camino cerrado**: el vértice posterior al último es el primero. También guarda los enemigos y torres en juego |
| Cola | `ColaEnlazada` | **Oleadas pendientes**: salen en el orden en que se programaron |
| Pila | `PilaEnlazada` | **Torres construidas**: Z deshace siempre la última y devuelve la mitad del costo |

## Paquetes y dependencias

```
juego            -> juego.pantallas, motor
juego.pantallas  -> juego.reglas, motor, ui, contratos
juego.reglas     -> juego.entidades, estructuras, motor, contratos
juego.entidades  -> estructuras, contratos
estructuras      -> (nada)
```
Las flechas van en un solo sentido: no hay dependencias circulares.

## Diagrama de clases

```mermaid
classDiagram
    class Actualizable { <<interface>> +actualizar(dt) }
    class Dibujable { <<interface>> +dibujar(g) }
    class Danable { <<interface>> +recibirDanio(puntos) +estaDestruido() }

    class Entidad { <<abstract>> -x -y +setPosicion() +distanciaA() }
    class Enemigo { <<abstract>> -vida -velocidad -recompensa +actualizar() +recibirDanio() }
    class Soldado
    class Corredor
    class Tanque { -ARMADURA +recibirDanio() }
    class Torre { -alcance -danio +disparar() +estaEnAlcance() }
    class Base { -vidas +recibirDanio() }
    class Punto { -x -y }

    Actualizable <|.. Entidad
    Dibujable <|.. Entidad
    Entidad <|-- Enemigo
    Entidad <|-- Torre
    Entidad <|-- Base
    Danable <|.. Enemigo
    Danable <|.. Base
    Enemigo <|-- Soldado
    Enemigo <|-- Corredor
    Enemigo <|-- Tanque

    class Lista { <<interface>> }
    class Pila { <<interface>> }
    class Cola { <<interface>> }
    class ListaCircular
    class PilaEnlazada
    class ColaEnlazada
    Lista <|.. ListaCircular
    Pila <|.. PilaEnlazada
    Cola <|.. ColaEnlazada

    class Oleada { +crearSiguiente() }
    class MundoDefensa { +construirTorre() +deshacerConstruccion() +actualizar() }
    Actualizable <|.. MundoDefensa
    Dibujable <|.. MundoDefensa
    MundoDefensa --> ListaCircular : camino, enemigos, torres
    MundoDefensa --> PilaEnlazada : torresConstruidas
    MundoDefensa --> ColaEnlazada : oleadasPendientes
    MundoDefensa --> Base
    ListaCircular --> Punto : camino
    Oleada ..> Enemigo : crea

    class Escena { <<abstract>> }
    class EscenaMenu
    class EscenaPartida
    class EscenaFin
    class Hud
    Actualizable <|.. Escena
    Dibujable <|.. Escena
    Escena <|-- EscenaMenu
    Escena <|-- EscenaPartida
    Escena <|-- EscenaFin
    Dibujable <|.. Hud
    EscenaPartida --> MundoDefensa
    EscenaPartida --> Hud
```

(Si el profesor pide el diagrama a mano o en foto, este sirve de guía para dibujarlo.)

## Cambios hechos a la carpeta del kit
El zip del repositorio traía archivos que **no compilan** y que el enunciado dice que el kit no incluye (entidades, física, mundo y estructuras ya hechas). Se eliminaron para poder compilar de cero:

- `src/provisorio/`, `src/fisica/`, `src/entidades/`
- `src/motor/Mundo.java`
- `src/estructuras/ListaBase.java`
- `src/demo/PelotaDemo.java`

No se modificó ningún otro archivo del kit. Solo se editó `verificador/PruebaMisEstructuras.java`, que el enunciado permite completar.
