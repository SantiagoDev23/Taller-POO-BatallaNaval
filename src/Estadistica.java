//Utilizamos encapsulacion atributos privados con getters publicos, sin setters para proteger la integridad del puntaje
public class Estadistica {

    private String nombreJugador;
    private int aciertos;
    private int fallos;
    private double puntos;
    private String fecha;

    //Utilizamos Overloading, dos constructores con diferente parametros de entrada
    public Estadistica(String nombreJugador, String fecha) {
        this.nombreJugador = nombreJugador;
        this.aciertos = 0;
        this.fallos = 0;
        this.puntos = 0.0;
        this.fecha = fecha;
    }

    public Estadistica(String nombreJugador, int aciertos, int fallos, double puntos, String fecha) {
        this.nombreJugador = nombreJugador;
        this.aciertos = aciertos;
        this.fallos = fallos;
        this.puntos = puntos;
        this.fecha = fecha;
    }

    public void registrarAcierto() {
        aciertos++;
        puntos += 1.0;
    }

    public void registrarFallo() {
        fallos++;
        puntos -= 0.5;
    }

    //Encapsulacion, utilizamos getters publicos para acceder a los atributos privados
    public String getNombreJugador() { return nombreJugador; }
    public int getAciertos()        { return aciertos; }
    public int getFallos()          { return fallos; }
    public double getPuntos()       { return puntos; }
    public String getFecha()        { return fecha; }
    public int getTotalDisparos()   { return aciertos + fallos; }
}
