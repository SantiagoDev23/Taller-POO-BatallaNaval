import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Maquina {

    private Tablero tablero;
    private ArrayList<int[]> turnosDeDisparo;
    private ArrayList<int[]> objetivos;
    private boolean[][] yaDisparado;

    public Maquina() {
        this.tablero = new Tablero();
        this.yaDisparado = new boolean[10][10];
        this.objetivos = new ArrayList<>();
        this.turnosDeDisparo = new ArrayList<>();
        for (int f = 0; f < 10; f++)
            for (int c = 0; c < 10; c++)
                turnosDeDisparo.add(new int[]{f, c});
        Collections.shuffle(turnosDeDisparo, new Random());
    }

    public void colocarBarcos() {
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
        while (!objetivos.isEmpty()) {
            int[] pos = objetivos.remove(0);
            if (!yaDisparado[pos[0]][pos[1]]) {
                yaDisparado[pos[0]][pos[1]] = true;
                return pos;
            }
        }
        while (!turnosDeDisparo.isEmpty()) {
            int[] pos = turnosDeDisparo.remove(0);
            if (!yaDisparado[pos[0]][pos[1]]) {
                yaDisparado[pos[0]][pos[1]] = true;
                return pos;
            }
        }
        return new int[]{0, 0};
    }

    public void registrarResultado(String resultado, int fila, int col) {
        if (resultado.startsWith("TOCADO")) {
            agregarAdyacentes(fila, col);
        } else if (resultado.startsWith("HUNDIDO")) {
            objetivos.clear();
        }
    }

    private void agregarAdyacentes(int fila, int col) {
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] d : dirs) {
            int f = fila + d[0];
            int c = col + d[1];
            if (f >= 0 && f < 10 && c >= 0 && c < 10 && !yaDisparado[f][c]) {
                boolean yaEsta = false;
                for (int[] o : objetivos) {
                    if (o[0] == f && o[1] == c) { yaEsta = true; break; }
                }
                if (!yaEsta) objetivos.add(new int[]{f, c});
            }
        }
    }

    public Tablero getTablero() { return tablero; }

    public boolean perdio() { return tablero.todasHundidas(); }
}
