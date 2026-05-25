public class PortaAviones extends Barco {

    public PortaAviones() {
        super("Portaaviones", 5);
    }

    @Override
    public String atacar(String coordenada) {
        return "Portaaviones lanza escuadrilla en " + coordenada;
    }

    // Overloading utilizamos misma firma atacar pero con parámetro adicional potencia
    @Override
    public String atacar(String coordenada, int potencia) {
        return "Portaaviones lanza " + potencia + " aviones sobre " + coordenada;
    }

    @Override
    public String getEstado() {
        return "Portaaviones [5] - " + calcularEstadoBarco(getImpactos(), getTamano());
    }
}
