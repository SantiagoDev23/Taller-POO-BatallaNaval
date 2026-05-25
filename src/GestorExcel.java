import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

//Composicion, para la clase GestorExcel gestiona un ArrayList<Estadistica> (lo contiene, no lo hereda)
public class GestorExcel {

    private static final String ARCHIVO = "tabla_puntos.csv";

    //Punto 9: Guarda cada partida en tabla_puntos.csv - cada jugador distinto ocupa su propia fila
    public static void guardar(Estadistica est) {

        try {
            boolean existe = new File(ARCHIVO).exists();
            FileWriter fw = new FileWriter(ARCHIVO, true);
            PrintWriter pw = new PrintWriter(fw);
            if (!existe) {
                pw.println("Jugador,Disparos,Aciertos,Fallos,Puntos,Fecha");
            }

            // La fecha va entre comillas para que Excel no la convierta a numero automaticamente
            pw.printf("\"%s\",%d,%d,%d,%.1f,\"%s\"%n",
                est.getNombreJugador(),
                est.getTotalDisparos(),
                est.getAciertos(),
                est.getFallos(),
                est.getPuntos(),
                est.getFecha()
            );
            pw.close();
            System.out.println("Estadisticas guardadas en " + ARCHIVO);
        } catch (IOException e) {
            System.out.println("Error al guardar estadisticas: " + e.getMessage());
        }
    }

    //Colecciones/Generics, ArrayList<Estadistica> tipado para almacenar y retornar registros
    public static ArrayList<Estadistica> cargar() {
        ArrayList<Estadistica> lista = new ArrayList<>();
        try {
            File archivo = new File(ARCHIVO);
            if (!archivo.exists()) return lista;
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            br.readLine();
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(",");
                if (p.length >= 6) {
                    String nombre  = p[0].replace("\"", "").trim();
                    int aciertos   = Integer.parseInt(p[2].trim());
                    int fallos     = Integer.parseInt(p[3].trim());
                    double puntos  = Double.parseDouble(p[4].trim());
                    String fecha   = p[5].replace("\"", "").trim();
                    lista.add(new Estadistica(nombre, aciertos, fallos, puntos, fecha));
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error al cargar estadisticas: " + e.getMessage());
        }
        return lista;
    }

    //Punto 9 y 11: Muestra tabla de tops - el que tenga mas puntos aparece primero (orden descendente)
    //Punto 10: Mismo nombre = jugador distinto, no hay filtro de unicidad, cada fila es una partida
    public static void mostrarTabla() {
        ArrayList<Estadistica> lista = cargar();
        //Punto 11: Ordenado por puntos descendente - mayor puntaje = primera posicion en la tabla
        lista.sort(Comparator.comparingDouble(Estadistica::getPuntos).reversed());

        System.out.println();
        System.out.println("==================== TABLA DE TOPS ====================");
        System.out.printf("%-4s %-18s %-9s %-9s %-7s %-8s %-12s%n",
            "#", "Jugador", "Disparos", "Aciertos", "Fallos", "Puntos", "Fecha");
        System.out.println("-------------------------------------------------------");
        int pos = 1;
        for (Estadistica e : lista) {
            System.out.printf("%-4d %-18s %-9d %-9d %-7d %-8.1f %-12s%n",
                pos++,
                e.getNombreJugador(),
                e.getTotalDisparos(),
                e.getAciertos(),
                e.getFallos(),
                e.getPuntos(),
                e.getFecha()
            );
        }
        System.out.println("=======================================================");
    }
}
