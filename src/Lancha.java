public class Lancha extends Barco {

    public Lancha() {
        super("Lancha", 1);
    }

    @Override
    public String atacar(String coordenada) {
        return "Lancha dispara en " + coordenada;
    }

    // Overloading, usamos la misma firma atacar pero con parámetro adicional potencia
    @Override
    public String atacar(String coordenada, int potencia) {
        return "Lancha dispara con potencia " + potencia + " en " + coordenada;
    }

    @Override
    public String getEstado() {
        return "Lancha [1] - " + calcularEstadoBarco(getImpactos(), getTamano());
    }
}
