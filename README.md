# Buscaminas

Una implementación con interfaz gráfica para jugar al buscaminas hecha en Java usando Swing.

## Descripción

El juego consiste en despejar un tablero de casillas sin revelar ninguna mina. El juego termina al revelar todas las casillas sin minas sin detonar ninguna. Para ello, las casillas cuentan con un número que indica el número de minas adyacentes a la propia casilla.

## Controles

Click derecho: despejar casilla.
Click izquierdo: Colocar/quitar banderilla.

## Características

- Hay res niveles de dificultad: fácil (9x9, 10 minas), normal (14x14, 40 minas) y difícil (20x20, 99 minas).
- Cronómetro en la partida
- Registro de mejores tiempos por dificultad
- Sistema de banderillas para marcar minas

## Recursos gráficos

Las imágenes utilizadas en el juego (números, banderilla e icono de la aplicación) se encuentran en la carpeta `src/Imagenes/` y son necesarias para la correcta ejecución del proyecto.

## Estructura

- `Clases/Casilla.java` — Modelo de cada casilla del tablero
- `Clases/Juego.java` — Lógica del juego
- `Clases/Tablero.java` — Interfaz gráfica
- `src/App.java` — Main
- `src/Imagenes/` — Recursos gráficos del juego

## Requisitos

- Java 8 o superior

## Ejecución

Compilar y ejecutar la clase `App.java`, ubicada en la raíz del directorio `src`.

## Autor

Álvaro Vicente Bonilla.