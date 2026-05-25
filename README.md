# Batalla Naval

Juego de Batalla Naval jugador vs máquina desarrollado en Java como taller de la materia Programación Orientada a Objetos. Cuenta con versión de consola y versión con interfaz gráfica en JavaFX.

El jugador coloca sus barcos manualmente en un tablero de 10x10 y luego dispara contra la flota enemiga. La máquina coloca sus barcos aleatoriamente y responde con disparos automáticos. Gana quien hunda toda la flota del otro primero. Al terminar cada partida se guarda el puntaje en un historial con tabla de tops.

## Tecnologías

- Java 26
- JavaFX 26
- IntelliJ IDEA

## Instalación y ejecución

### Requisitos

- JDK 26
- JavaFX SDK 26 descomprimido en `C:\javafx-sdk-26.0.1`
- IntelliJ IDEA

### Pasos

1. Clonar el repositorio y abrirlo en IntelliJ IDEA
2. Ir a `File → Project Structure → Libraries → +` y seleccionar la carpeta `C:\javafx-sdk-26.0.1\lib`
3. Crear una Run Configuration de tipo **Application**:
   - Main class: `BatallaNavalApp`
   - VM options: `--module-path "C:\javafx-sdk-26.0.1\lib" --add-modules javafx.controls,javafx.fxml`
4. Ejecutar con esa configuración

### Versión consola

Si se prefiere la versión de consola, ejecutar la clase `Main` sin VM options adicionales.

## Estudiantes

- Santiago Moná León
- María Clara Barreiro

## Profesor

- William Gerald Giraldo Escobar

Institución Universitaria Pascual Bravo
