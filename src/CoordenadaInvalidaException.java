//Creamos una excepcion personalizada que extiende Exception para validar coordenadas invalidas
public class CoordenadaInvalidaException extends Exception {
    public CoordenadaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
