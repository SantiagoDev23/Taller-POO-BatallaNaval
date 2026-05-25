import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Comparator;

public class MenuController {

    @FXML private TextField campoNombre;
    @FXML private Label labelError;
    @FXML private TableView<Estadistica> tablaScores;
    @FXML private TableColumn<Estadistica, Number> colPos;
    @FXML private TableColumn<Estadistica, String> colNombre;
    @FXML private TableColumn<Estadistica, Number> colPuntos;
    @FXML private TableColumn<Estadistica, Number> colAciertos;
    @FXML private TableColumn<Estadistica, Number> colFallos;
    @FXML private TableColumn<Estadistica, String> colFecha;

    @FXML
    public void initialize() {
        colPos.setCellValueFactory(data -> {
            int index = tablaScores.getItems().indexOf(data.getValue()) + 1;
            return new SimpleIntegerProperty(index);
        });
        colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreJugador()));
        colPuntos.setCellValueFactory(d -> new SimpleIntegerProperty((int) d.getValue().getPuntos()));
        colAciertos.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getAciertos()));
        colFallos.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getFallos()));
        colFecha.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFecha()));

        ArrayList<Estadistica> lista = GestorExcel.cargar();
        lista.sort(Comparator.comparingDouble(Estadistica::getPuntos).reversed());
        tablaScores.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void handleJugar() {
        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty()) {
            labelError.setText("Debes ingresar tu nombre para continuar.");
            return;
        }
        labelError.setText(" ");
        try {
            BatallaNavalApp.irAJuego(nombre);
        } catch (Exception e) {
            labelError.setText("Error al cargar el juego.");
            e.printStackTrace();
        }
    }
}
