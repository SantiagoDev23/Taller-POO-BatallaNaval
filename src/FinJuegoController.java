import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Comparator;

public class FinJuegoController {

    @FXML private Label labelResultado;
    @FXML private Label labelSubtitulo;
    @FXML private Label labelPuntos;
    @FXML private Label labelAciertos;
    @FXML private Label labelFallos;
    @FXML private Label labelDisparos;
    @FXML private HBox contenedorTableros;
    @FXML private TableView<Estadistica> tablaScores;
    @FXML private TableColumn<Estadistica, Number> colPos;
    @FXML private TableColumn<Estadistica, String> colNombre;
    @FXML private TableColumn<Estadistica, Number> colPuntos;
    @FXML private TableColumn<Estadistica, Number> colAciertos;
    @FXML private TableColumn<Estadistica, Number> colFallos;
    @FXML private TableColumn<Estadistica, String> colFecha;

    public void iniciar(Estadistica est, boolean gano, Jugador jugador, Maquina maquina) {
        if (gano) {
            labelResultado.setText("⚓  ¡VICTORIA!");
            labelResultado.getStyleClass().add("titulo-victoria");
            labelSubtitulo.setText("Hundiste todos los barcos enemigos, " + est.getNombreJugador() + "!");
        } else {
            labelResultado.setText("☠  DERROTA");
            labelResultado.getStyleClass().add("titulo-derrota");
            labelSubtitulo.setText("Tus barcos fueron hundidos, " + est.getNombreJugador() + ". ¡Inténtalo de nuevo!");
        }

        labelPuntos.setText(String.format("%.1f", est.getPuntos()));
        labelAciertos.setText(String.valueOf(est.getAciertos()));
        labelFallos.setText(String.valueOf(est.getFallos()));
        labelDisparos.setText(String.valueOf(est.getTotalDisparos()));

        contenedorTableros.getChildren().addAll(
            crearPanelBarcos("🛡  Tus Barcos", jugador.getTablero().getBarcos()),
            crearPanelBarcos("💀  Barcos Enemigos", maquina.getTablero().getBarcos())
        );

        configurarTabla();
        ArrayList<Estadistica> lista = GestorExcel.cargar();
        lista.sort(Comparator.comparingDouble(Estadistica::getPuntos).reversed());
        tablaScores.setItems(FXCollections.observableArrayList(lista));
    }

    private VBox crearPanelBarcos(String titulo, ArrayList<Barco> barcos) {
        VBox vbox = new VBox(7);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.getStyleClass().add("panel-lateral");
        vbox.setPrefWidth(220);

        Label lblTitulo = new Label(titulo);
        lblTitulo.getStyleClass().add("label-tablero");
        vbox.getChildren().add(lblTitulo);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #1a4a6b;");
        vbox.getChildren().add(sep);

        for (Barco b : barcos) {
            Label lbl = new Label();
            if (b.isHundido()) {
                lbl.setText("✕  " + b.getEstado());
                lbl.getStyleClass().add("barco-estado-hundido");
            } else if (b.getImpactos() > 0) {
                lbl.setText("⚡  " + b.getEstado());
                lbl.getStyleClass().add("barco-estado-danado");
            } else {
                lbl.setText("✔  " + b.getEstado());
                lbl.getStyleClass().add("barco-estado-vivo");
            }
            vbox.getChildren().add(lbl);
        }
        return vbox;
    }

    private void configurarTabla() {
        colPos.setCellValueFactory(data -> {
            int index = tablaScores.getItems().indexOf(data.getValue()) + 1;
            return new SimpleIntegerProperty(index);
        });
        colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreJugador()));
        colPuntos.setCellValueFactory(d -> new SimpleIntegerProperty((int) d.getValue().getPuntos()));
        colAciertos.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getAciertos()));
        colFallos.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getFallos()));
        colFecha.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFecha()));
    }

    @FXML
    private void handleJugarDeNuevo() {
        try {
            BatallaNavalApp.irAMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
