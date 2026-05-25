import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BatallaNavalApp extends Application {

    private static Stage escenarioPrincipal;

    @Override
    public void start(Stage stage) throws Exception {
        escenarioPrincipal = stage;
        stage.setTitle("Batalla Naval");
        stage.setResizable(false);
        irAMenu();
        stage.show();
    }

    public static void irAMenu() throws Exception {
        Parent root = FXMLLoader.load(BatallaNavalApp.class.getResource("MenuPrincipal.fxml"));
        escenarioPrincipal.setScene(new Scene(root, 900, 700));
    }

    public static void irAJuego(String nombre) throws Exception {
        FXMLLoader loader = new FXMLLoader(BatallaNavalApp.class.getResource("JuegoView.fxml"));
        Parent root = loader.load();
        JuegoController ctrl = loader.getController();
        ctrl.iniciar(nombre);
        escenarioPrincipal.setScene(new Scene(root, 1220, 790));
    }

    public static void irAFinJuego(Estadistica estadistica, boolean gano,
                                    Jugador jugador, Maquina maquina) throws Exception {
        FXMLLoader loader = new FXMLLoader(BatallaNavalApp.class.getResource("FinJuego.fxml"));
        Parent root = loader.load();
        FinJuegoController ctrl = loader.getController();
        ctrl.iniciar(estadistica, gano, jugador, maquina);
        escenarioPrincipal.setScene(new Scene(root, 1000, 760));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
