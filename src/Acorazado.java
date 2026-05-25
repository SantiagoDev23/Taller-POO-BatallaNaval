public class Acorazado extends Barco {

    public Acorazado() {
        super("Acorazado", 4);
    }

    @Override
    public String atacar(String coordenada) {
        return "Acorazado dispara salva en " + coordenada;
    }

    // Overloading tenemos el mismo metodo atacar pero con parámetro adicional potencia
    @Override
    public String atacar(String coordenada, int potencia) {
        return "Acorazado dispara " + potencia + " canones sobre " + coordenada;
    }

    @Override
    public String getEstado() {
        return "Acorazado [4] - " + calcularEstadoBarco(getImpactos(), getTamano());
    }
}
