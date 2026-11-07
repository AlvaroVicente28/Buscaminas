# Buscaminas
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Swing-blue?style=for-the-badge)
![Licencia](https://img.shields.io/github/license/AlvaroVicente28/Buscaminas?style=for-the-badge)

Una implementación con interfaz gráfica para jugar al buscaminas hecha en Java usando Swing.

| Pantalla de inicio | Partida |
|:---:|:---:|
| <img src="docs/screenshot/Pantalla%20Inicial.png" alt="Pantalla de inicio" width="420"> | <img src="docs/screenshot/Partida.png" alt="Partida en curso" width="420"> |

## Descripción

El juego consiste en despejar un tablero de casillas sin revelar ninguna mina. El juego termina al revelar todas las casillas sin minas sin detonar ninguna. Para ello, las casillas cuentan con un número que indica el número de minas adyacentes a la propia casilla.

## Controles

- **Click izquierdo**: Despejar casilla.
- **Click derecho**: Colocar/quitar banderilla.
- **Click central**: Despejar vecinos si la casilla la tiene todos sus banderillas puestas.

## Características

- Hay **tres niveles de dificultad**: fácil (9x9, 10 minas), normal (16x16, 40 minas) y difícil (22x22, 99 minas).
- **Cronómetro** en la partida
- **Registro** de **mejores tiempos**  por dificultad
- Sistema de banderillas para marcar minas

## Recursos gráficos

Las imágenes utilizadas en el juego (números, banderilla e icono de la aplicación) se encuentran en la carpeta `src/main/resources/Imagenes/` y son necesarias para la correcta ejecución del proyecto.

## Estructura

- `src/main/java/Clases/Casilla.java` — Modelo de cada casilla del tablero
- `src/main/java/Clases/Juego.java` — Lógica del juego
- `src/main/java/Clases/Tablero.java` — Interfaz gráfica
- `src/main/java/Clases/Records.java` — Gestor de los records en cada dificultad
- `src/test/java/Clases/` — Tests
- `src/main/java/App.java` — Main
- `src/main/resources/Imagenes/` — Recursos gráficos del juego
- `docs/screenshot/` — Imagenes pare el README
- `records.txt` — Documento de texto con los records
- `pom.xml` — Configuración de Maven

## Instalación y ejecución

Es necesario contar con Java 17 o superior. Clona el repositorio y compile el proyecto desde la carpeta raíz del proyecto:

**Linux / macOS y Windows**
```bash
./mvnw compile exec:java 
```
O también:
```bash
./mvnw package
java -jar target/Buscaminas.jar
```

## Tests

El proyecto incluye tests con JUnit 5 que cubren el modelo de casilla y el registro de récords. Para ejecutarlos:

**Linux / macOS y Windows**
```bash
./mvnw test
```

## Licencia

Distribuido bajo la licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más información.

## Autor

Álvaro Vicente Bonilla - [AlvaroVicente28](https://github.com/AlvaroVicente28)