public class Submarino extends Barco {

    public Submarino() {
        super("Submarino", 3);
    }

    @Override
    public String atacar(String coordenada) {
        return "Submarino lanza torpedo en " + coordenada;
    }

    // Overloading utilizamos misma firma atacar pero con parámetro adicional potencia
    @Override
    public String atacar(String coordenada, int potencia) {
        return "Submarino lanza " + potencia + " torpedos en " + coordenada;
    }

    @Override
    public String getEstado() {
        return "Submarino [3] - " + calcularEstadoBarco(getImpactos(), getTamano());
    }
}
