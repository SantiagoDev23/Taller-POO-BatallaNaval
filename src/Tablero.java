import java.util.*;

public class Tablero {

    private static final int TAMANO = 10;
    private char[][] cuadricula;
    private Barco[][] rejillaBarcos;
    private ArrayList<Barco> barcos;

    public static final char AGUA = '~';
    public static final char BARCO = 'O';
    public static final char IMPACTO = 'X';
    public static final char FALLO = '-';

    public Tablero() {
        cuadricula = new char[TAMANO][TAMANO];
        rejillaBarcos = new Barco[TAMANO][TAMANO];
        barcos = new ArrayList<>();
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int columna = 0; columna < TAMANO; columna++) {
                cuadricula[fila][columna] = AGUA;
            }
        }
    }

    public ArrayList<Barco> getBarcos() { return barcos; }

    public boolean colocarBarco(Barco barco, int fila, int columna, boolean horizontal) {
        int tamanio = barco.getTamano();
        if (horizontal) {
            if (columna + tamanio > TAMANO) return false;
            for (int j = columna; j < columna + tamanio; j++) {
                if (cuadricula[fila][j] != AGUA) return false;
            }
            for (int j = columna; j < columna + tamanio; j++) {
                cuadricula[fila][j] = BARCO;
                rejillaBarcos[fila][j] = barco;
            }
        } else {
            if (fila + tamanio > TAMANO) return false;
            for (int i = fila; i < fila + tamanio; i++) {
                if (cuadricula[i][columna] != AGUA) return false;
            }
            for (int i = fila; i < fila + tamanio; i++) {
                cuadricula[i][columna] = BARCO;
                rejillaBarcos[i][columna] = barco;
            }
        }
        barcos.add(barco);
        return true;
    }

    public String recibirDisparo(int fila, int columna) {
        char celda = cuadricula[fila][columna];
        //Punto 8: Si ya fue disparado (IMPACTO o FALLO) retorna REPETIDO para que Main reste -0.5
        if (celda == IMPACTO || celda == FALLO) return "REPETIDO";
        if (celda == BARCO) {
            cuadricula[fila][columna] = IMPACTO;
            Barco barco = rejillaBarcos[fila][columna];
            if (barco.recibirImpacto()) return "HUNDIDO:" + barco.getNombre();
            return "TOCADO:" + barco.getNombre();
        }
        cuadricula[fila][columna] = FALLO;
        return "AGUA";
    }

    public boolean todasHundidas() {
        for (Barco barco : barcos) {
            if (!barco.isHundido()) return false;
        }
        return !barcos.isEmpty();
    }

    //Punto 3: getLineas() lee cuadricula que es persistente - el tablero se actualiza, no se recrea
    //Punto 7: mostrarBarcos=false oculta los barcos de la maquina en la vista de ataque
    public String[] getLineas(boolean mostrarBarcos, boolean esAtaque) {
        String[] lineas = new String[TAMANO + 1];
        StringBuilder header = new StringBuilder("     ");
        for (int c = 1; c <= TAMANO; c++) header.append(String.format("%-3d", c));
        lineas[0] = header.toString();
        for (int f = 0; f < TAMANO; f++) {
            StringBuilder fila = new StringBuilder(" " + (char)('A' + f) + "   ");
            for (int c = 0; c < TAMANO; c++) {
                char celda = cuadricula[f][c];
                if (!mostrarBarcos && celda == BARCO) {
                    fila.append(AGUA).append("  ");
                } else if (esAtaque && celda == FALLO) {
                    //Punto 2: Si ataca y fallo, se muestra espacio en blanco en lugar de '-'
                    fila.append(' ').append("  ");
                } else {
                    fila.append(celda).append("  ");
                }
            }
            lineas[f + 1] = fila.toString();
        }
        return lineas;
    }

    // Overloading se muestra mostrar con dos parametros (con opcion de vista de ataque)
    public void mostrar(boolean mostrarBarcos, boolean esAtaque) {
        for (String linea : getLineas(mostrarBarcos, esAtaque)) {
            System.out.println(linea);
        }
    }

    // Overloading, mostramos con un solo parametro (sobrecarga de mostrar)
    public void mostrar(boolean mostrarBarcos) {
        mostrar(mostrarBarcos, false);
    }
}
