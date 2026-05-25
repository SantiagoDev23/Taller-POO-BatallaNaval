import java.util.*;

public class Jugador {

    private String nombre;
    //Utilizamos Composicion, el Jugador tiene un Tablero como atributo (no hereda de Tablero)
    private Tablero tablero;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.tablero = new Tablero();
    }

    public String getNombre() { return nombre; }
    public Tablero getTablero() { return tablero; }

    public void colocarBarcos() {
        //Colecciones/Generics, tenemos un array de Barco con subtipos distintos
        Barco[] barcos = {
            new PortaAviones(),
            new Acorazado(),
            new Submarino(),
            new Destructor(),
            new Lancha()
        };
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

    public boolean perdio() { return tablero.todasHundidas(); }
}
