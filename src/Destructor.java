public class Destructor extends Barco {

    public Destructor() {
        super("Destructor", 2);
    }

    @Override
    public String atacar(String coordenada) {
        return "Destructor dispara en " + coordenada;
    }

    // Overloading tenemos la misma firma atacar pero con parámetro adicional potencia
    @Override
    public String atacar(String coordenada, int potencia) {
        return "Destructor dispara con potencia " + potencia + " en " + coordenada;
    }

    @Override
    public String getEstado() {
        return "Destructor [2] - " + calcularEstadoBarco(getImpactos(), getTamano());
    }
}
