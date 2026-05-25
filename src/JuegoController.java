import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JuegoController {

    @FXML private Label labelFase;
    @FXML private Label labelScore;
    @FXML private Label labelMensaje;
    @FXML private VBox contenedorJugador;
    @FXML private VBox contenedorMaquina;
    @FXML private VBox panelColocacion;
    @FXML private VBox panelBatalla;
    @FXML private Label labelBarcoActual;
    @FXML private Label labelTamanoBarco;
    @FXML private ToggleButton toggleOrientacion;
    @FXML private Label labelBarcosRestantes;
    @FXML private VBox estadoBarcosPropios;
    @FXML private VBox estadoBarcosEnemigos;

    private Jugador jugador;
    private Maquina maquina;
    private Estadistica estadistica;

    private final Button[][] celdasJugador = new Button[10][10];
    private final Button[][] celdasMaquina = new Button[10][10];

    private final Barco[] barcosAColocar = {
        new PortaAviones(), new Acorazado(), new Submarino(), new Destructor(), new Lancha()
    };
    private int indiceBarcoActual = 0;
    private boolean horizontal = true;
    private boolean enFaseColocacion = true;
    private List<int[]> previewCeldas = new ArrayList<>();

    public void iniciar(String nombre) {
        jugador = new Jugador(nombre);
        maquina = new Maquina();
        estadistica = new Estadistica(nombre, LocalDate.now().toString());
        maquina.colocarBarcos();

        contenedorJugador.getChildren().add(crearTablero(true));
        contenedorMaquina.getChildren().add(crearTablero(false));

        deshabilitarTableroEnemigo(true);
        actualizarInfoColocacion();
    }

    private GridPane crearTablero(boolean esJugador) {
        GridPane grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setAlignment(Pos.CENTER);

        for (int c = 0; c < 10; c++) {
            Label lbl = new Label(String.valueOf(c + 1));
            lbl.getStyleClass().add("header-celda");
            lbl.setAlignment(Pos.CENTER);
            grid.add(lbl, c + 1, 0);
        }

        for (int f = 0; f < 10; f++) {
            Label lblFila = new Label(String.valueOf((char) ('A' + f)));
            lblFila.getStyleClass().add("header-celda");
            lblFila.setAlignment(Pos.CENTER);
            lblFila.setMinWidth(22);
            grid.add(lblFila, 0, f + 1);

            for (int c = 0; c < 10; c++) {
                Button btn = new Button();
                btn.getStyleClass().addAll("celda", "celda-agua");
                int fila = f, col = c;

                if (esJugador) {
                    btn.setOnAction(e -> handleCeldaJugador(fila, col));
                    btn.setOnMouseEntered(e -> mostrarPreview(fila, col));
                    btn.setOnMouseExited(e -> limpiarPreview());
                    celdasJugador[f][c] = btn;
                } else {
                    btn.setOnAction(e -> handleCeldaMaquina(fila, col));
                    celdasMaquina[f][c] = btn;
                }
                grid.add(btn, c + 1, f + 1);
            }
        }
        return grid;
    }

    private void handleCeldaJugador(int fila, int col) {
        if (!enFaseColocacion) return;
        limpiarPreview();
        Barco barco = barcosAColocar[indiceBarcoActual];
        boolean colocado = jugador.getTablero().colocarBarco(barco, fila, col, horizontal);
        if (!colocado) {
            setMensaje("⚠  No cabe ahí. Elige otra posición.", "#ffd166");
            return;
        }
        for (int i = 0; i < barco.getTamano(); i++) {
            int f = horizontal ? fila : fila + i;
            int c = horizontal ? col + i : col;
            Button btn = celdasJugador[f][c];
            btn.getStyleClass().removeAll("celda-agua");
            btn.getStyleClass().add("celda-barco");
            btn.setOnMouseEntered(null);
            btn.setOnMouseExited(null);
        }
        indiceBarcoActual++;
        if (indiceBarcoActual >= barcosAColocar.length) {
            iniciarFaseBatalla();
        } else {
            actualizarInfoColocacion();
            setMensaje("✔  " + barco.getNombre() + " colocado. Siguiente: "
                + barcosAColocar[indiceBarcoActual].getNombre(), "#2ed573");
        }
    }

    private void handleCeldaMaquina(int fila, int col) {
        if (enFaseColocacion) return;
        String resultado = maquina.getTablero().recibirDisparo(fila, col);
        actualizarCeldaMaquina(fila, col, resultado);

        if (resultado.startsWith("HUNDIDO")) {
            estadistica.registrarAcierto();
            setMensaje("💥  ¡HUNDIDO! → " + resultado.split(":")[1], "#e63946");
        } else if (resultado.startsWith("TOCADO")) {
            estadistica.registrarAcierto();
            setMensaje("🎯  ¡Tocado! → " + resultado.split(":")[1], "#ffd166");
        } else {
            estadistica.registrarFallo();
            setMensaje("💧  Agua. Tu disparo falló.", "#7ecfef");
        }

        actualizarScore();
        actualizarEstadoBarcos();

        if (maquina.perdio()) {
            terminarJuego(true);
            return;
        }

        int[] posMaquina = maquina.siguienteDisparo();
        String resMaquina = jugador.getTablero().recibirDisparo(posMaquina[0], posMaquina[1]);
        actualizarCeldaJugador(posMaquina[0], posMaquina[1], resMaquina);
        actualizarEstadoBarcos();

        if (jugador.perdio()) {
            terminarJuego(false);
        }
    }

    private void actualizarCeldaMaquina(int fila, int col, String resultado) {
        Button btn = celdasMaquina[fila][col];
        btn.setDisable(true);
        btn.getStyleClass().removeAll("celda-agua");
        if (resultado.startsWith("TOCADO") || resultado.startsWith("HUNDIDO")) {
            btn.getStyleClass().add("celda-impacto");
            btn.setText("✕");
        } else {
            btn.getStyleClass().add("celda-fallo");
            btn.setText("·");
        }
    }

    private void actualizarCeldaJugador(int fila, int col, String resultado) {
        Button btn = celdasJugador[fila][col];
        btn.getStyleClass().removeAll("celda-agua", "celda-barco");
        if (resultado.startsWith("TOCADO") || resultado.startsWith("HUNDIDO")) {
            btn.getStyleClass().add("celda-impacto");
            btn.setText("✕");
        } else {
            btn.getStyleClass().add("celda-fallo");
            btn.setText("·");
        }
    }

    private void mostrarPreview(int fila, int col) {
        if (!enFaseColocacion || indiceBarcoActual >= barcosAColocar.length) return;
        limpiarPreview();
        Barco barco = barcosAColocar[indiceBarcoActual];
        List<int[]> celdas = new ArrayList<>();
        boolean valido = true;
        for (int i = 0; i < barco.getTamano(); i++) {
            int f = horizontal ? fila : fila + i;
            int c = horizontal ? col + i : col;
            if (f >= 10 || c >= 10) valido = false;
            celdas.add(new int[]{f, c});
        }
        String cls = valido ? "celda-preview" : "celda-preview-invalida";
        for (int[] pos : celdas) {
            if (pos[0] < 10 && pos[1] < 10) {
                Button btn = celdasJugador[pos[0]][pos[1]];
                if (!btn.getStyleClass().contains("celda-barco")) {
                    btn.getStyleClass().removeAll("celda-agua");
                    btn.getStyleClass().add(cls);
                }
            }
        }
        previewCeldas = celdas;
    }

    private void limpiarPreview() {
        for (int[] pos : previewCeldas) {
            if (pos[0] < 10 && pos[1] < 10) {
                Button btn = celdasJugador[pos[0]][pos[1]];
                btn.getStyleClass().removeAll("celda-preview", "celda-preview-invalida");
                if (!btn.getStyleClass().contains("celda-barco")) {
                    btn.getStyleClass().add("celda-agua");
                }
            }
        }
        previewCeldas.clear();
    }

    @FXML
    private void handleToggleOrientacion() {
        horizontal = !toggleOrientacion.isSelected();
        toggleOrientacion.setText(horizontal ? "⟷  HORIZONTAL" : "↕  VERTICAL");
    }

    private void actualizarInfoColocacion() {
        if (indiceBarcoActual >= barcosAColocar.length) return;
        Barco barco = barcosAColocar[indiceBarcoActual];
        labelBarcoActual.setText(barco.getNombre() + "  " + "■".repeat(barco.getTamano()));
        labelTamanoBarco.setText("Tamaño: " + barco.getTamano() + " casillas");
        StringBuilder sb = new StringBuilder();
        for (int i = indiceBarcoActual + 1; i < barcosAColocar.length; i++) {
            sb.append("• ").append(barcosAColocar[i].getNombre()).append("\n");
        }
        labelBarcosRestantes.setText(sb.toString().trim());
        setMensaje("Coloca tu " + barco.getNombre() + " (" + barco.getTamano() + " casillas)", "#ecf0f1");
    }

    private void iniciarFaseBatalla() {
        enFaseColocacion = false;
        labelFase.setText("FASE: BATALLA NAVAL");
        panelColocacion.setVisible(false);
        panelColocacion.setManaged(false);
        panelBatalla.setVisible(true);
        panelBatalla.setManaged(true);
        for (int f = 0; f < 10; f++)
            for (int c = 0; c < 10; c++)
                celdasJugador[f][c].setOnAction(null);
        deshabilitarTableroEnemigo(false);
        actualizarEstadoBarcos();
        setMensaje("¡Que comience la batalla! Haz clic en el tablero enemigo para atacar.", "#00b4d8");
    }

    private void deshabilitarTableroEnemigo(boolean deshabilitar) {
        for (int f = 0; f < 10; f++)
            for (int c = 0; c < 10; c++)
                celdasMaquina[f][c].setDisable(deshabilitar);
    }

    private void actualizarEstadoBarcos() {
        actualizarListaBarcos(estadoBarcosPropios, jugador.getTablero().getBarcos());
        actualizarListaBarcos(estadoBarcosEnemigos, maquina.getTablero().getBarcos());
    }

    private void actualizarListaBarcos(VBox contenedor, ArrayList<Barco> barcos) {
        contenedor.getChildren().clear();
        for (Barco b : barcos) {
            Label lbl = new Label();
            if (b.isHundido()) {
                lbl.setText("✕ " + b.getNombre());
                lbl.getStyleClass().add("barco-estado-hundido");
            } else if (b.getImpactos() > 0) {
                lbl.setText("⚡ " + b.getNombre() + " " + b.getImpactos() + "/" + b.getTamano());
                lbl.getStyleClass().add("barco-estado-danado");
            } else {
                lbl.setText("✔ " + b.getNombre());
                lbl.getStyleClass().add("barco-estado-vivo");
            }
            contenedor.getChildren().add(lbl);
        }
    }

    private void actualizarScore() {
        labelScore.setText(String.format("Puntos: %.1f  |  🎯 %d  |  💧 %d",
            estadistica.getPuntos(), estadistica.getAciertos(), estadistica.getFallos()));
    }

    private void setMensaje(String texto, String color) {
        labelMensaje.setText(texto);
        labelMensaje.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 13px; -fx-padding: 6 18;"
            + "-fx-background-color: #0d2137; -fx-background-radius: 7;"
            + "-fx-border-color: #1a4a6b; -fx-border-radius: 7;");
    }

    private void terminarJuego(boolean gano) {
        GestorExcel.guardar(estadistica);
        try {
            BatallaNavalApp.irAFinJuego(estadistica, gano, jugador, maquina);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
