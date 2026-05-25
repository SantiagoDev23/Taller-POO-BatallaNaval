import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Maquina {

    //Composicion, la Maquina tiene un Tablero como atributo (no hereda de Tablero)
    private Tablero tablero;
    //Colecciones/Generics, utilizamos ArrayList<int[]> tipado para las coordenadas de disparo
    private ArrayList<int[]> turnosDeDisparo;

    public Maquina() {
        this.tablero = new Tablero();
        this.turnosDeDisparo = new ArrayList<>();
        for (int f = 0; f < 10; f++)
            for (int c = 0; c < 10; c++)
                turnosDeDisparo.add(new int[]{f, c});
        Collections.shuffle(turnosDeDisparo, new Random());
    }

    public void colocarBarcos() {
        //Colecciones/Generics, utilizamos ArrayList<Barco> tipado con subtipos distintos
        ArrayList<Barco> barcos = new ArrayList<>();
        barcos.add(new PortaAviones());
        barcos.add(new Acorazado());
        barcos.add(new Submarino());
        barcos.add(new Destructor());
        barcos.add(new Lancha());

        Random aleatorio = new Random();
        for (Barco barco : barcos) {
            boolean colocado = false;
            while (!colocado) {
                int fila = aleatorio.nextInt(10);
                int columna = aleatorio.nextInt(10);
                boolean horizontal = aleatorio.nextBoolean();
                colocado = tablero.colocarBarco(barco, fila, columna, horizontal);
            }
        }
    }

    public int[] siguienteDisparo() {
        return turnosDeDisparo.remove(0);
    }

    public Tablero getTablero() { return tablero; }

    public boolean perdio() { return tablero.todasHundidas(); }
}
