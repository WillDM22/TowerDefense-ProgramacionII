# Historial de Desarrollo (Changelog)

* **Día 1: Estructura Base y Repositorio**
  * *William:* Creación del repositorio en GitHub, configuración del `.gitignore` y armado estructural de las colecciones de datos (Pila, Cola, Lista).
  * *Elias:* Configuración inicial de la carpeta `juego` y creación de las clases base heredando de `Entidad` (Base y parámetros iniciales de Enemigo).

* **Día 2: El Cerebro del Juego y la Interfaz**
  * *William:* Programación de la clase `MundoDefensa.java`. Se implementó el bucle de actualización para recorrer las listas de entidades activas.
  * *Elias:* Integración gráfica de `EscenaPartida` y programación de la interfaz visual (`Hud.java`) para mostrar el dinero, vidas y enemigos restantes.

* **Día 3: Interacción, Disparos y Colisiones**
  * *William:* Resolución del problema de eliminación de enemigos al morir, implementando un bucle `for` a la inversa para evitar el salto de índices en la Lista.
  * *Elias:* Implementación de las mecánicas de disparo en `Torre.java`, control de enfriamiento (cooldown) y configuración de las herencias (Corredor, Soldado, Tanque).

* **Día 4: Refinamiento y Pruebas Finales**
  * *William & Elias (Pair Programming):* Testeo exhaustivo de la mecánica de "Deshacer construcción" (tecla Z) asegurando el correcto funcionamiento de la pila LIFO de torres. Ajuste de balanceo general, limpieza de código y redacción de esta documentación.