// Clase abstracta definimos la estructura base y obliga a las subclases a implementar sus métodos abstractos
public abstract class Barco implements Atacable {

    private String nombre;
    private int tamano;
    protected boolean hundido;
    private int impactos;

    // Static, campo compartido entre todas las instancias, cuenta cuántos barcos se han creado
    private static int totalBarcosCreados = 0;

    public Barco(String nombre, int tamano) {
        this.nombre = nombre;
        this.tamano = tamano;
        this.hundido = false;
        this.impactos = 0;
        totalBarcosCreados++;
    }

    // Static, utilizamos metodo de clase, no necesita instancia para ser llamado
    public static int getTotalBarcosCreados() { return totalBarcosCreados; }

    // Static, calculamos el estado de cualquier barco según sus impactos y tamaño
    public static String calcularEstadoBarco(int impactos, int tamano) {
        if (impactos == 0) return "Intacto";
        if (impactos >= tamano) return "Hundido";
        return "Daniado (" + impactos + "/" + tamano + ")";
    }

    // Encapsulacion, tenemos atributos privados con getters y setters públicos
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getTamano() { return tamano; }
    public void setTamano(int tamano) { this.tamano = tamano; }
    public boolean isHundido() { return hundido; }
    public void setHundido(boolean hundido) { this.hundido = hundido; }
    public int getImpactos() { return impactos; }
    public void setImpactos(int impactos) { this.impactos = impactos; }

    @Override
    public boolean recibirImpacto() {
        impactos++;
        if (impactos >= tamano) hundido = true;
        return hundido;
    }

    // Overloading, implementamos el segundo metodo de Atacable con parámetro adicional potencia
    @Override
    public String atacar(String coordenada, int potencia) {
        return getNombre() + " dispara con potencia " + potencia + " en " + coordenada;
    }

    // Polimorfismo sobreescritura: las subclases deben implementar estos dos métodos abstractos
    @Override
    public abstract String atacar(String coordenada);

    public abstract String getEstado();

    @Override
    public String toString() { return getEstado(); }
}
