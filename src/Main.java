import java.util.*;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        limpiarPantalla();
        System.out.println("=== BATALLA NAVAL ===");
        System.out.println("O=Barco  X=Impacto  espacio vacio =Fallo  ~=Agua");
        System.out.println();

        //Punto 5: Digitar nombre del jugador al momento de iniciar el juego
        System.out.print("Ingresa tu nombre: ");
        String nombre = sc.nextLine().trim();
        if (nombre.isEmpty()) nombre = "Jugador";

        //Punto 1: Jugador contra la maquina - se crea un Jugador humano y una Maquina con disparo automatico
        Jugador jugador = new Jugador(nombre);
        Maquina maquina = new Maquina();

        //Punto 9: Estadistica para contar aciertos (+1) y fallos (-0.5) y calcular puntos totales
        Estadistica estadistica = new Estadistica(nombre, LocalDate.now().toString());

        maquina.colocarBarcos();
        colocarBarcosManual(jugador, sc);

        limpiarPantalla();
        System.out.println("Que comience la batalla, " + nombre + "!");

        //Punto 7: Mostrar los dos mapas al inicio - propio con barcos visibles, enemigo sin revelar sus barcos
        mostrarTableros(jugador, maquina);

        while (true) {
            int[] posJugador = null;
            while (posJugador == null) {
                System.out.print("Tu disparo (ej: A1): ");
                String entrada = sc.nextLine().trim().toUpperCase();

                //Utilizamos try y catch para manejar coordenada invalida
                try {
                    posJugador = parsearCoordenada(entrada);
                } catch (CoordenadaInvalidaException e) {
                    System.out.println("Coordenada invalida: " + e.getMessage() + ". Intenta de nuevo.");
                }
            }

            String resultadoJugador = maquina.getTablero().recibirDisparo(posJugador[0], posJugador[1]);

            //Punto 8: Si la persona dispara al mismo lugar pierde turno, no se restringe el disparo
            if (resultadoJugador.equals("REPETIDO")) {
                estadistica.registrarFallo();
                System.out.println("Ya disparaste ahi! Turno perdido (-0.5 puntos).");
            } else if (resultadoJugador.startsWith("HUNDIDO")) {
                //Punto 9: Disparo acertado = +1 punto
                estadistica.registrarAcierto();
                System.out.println("Hundido! " + resultadoJugador.split(":")[1]);
            } else if (resultadoJugador.startsWith("TOCADO")) {
                estadistica.registrarAcierto();
                System.out.println("Tocado! " + resultadoJugador.split(":")[1]);
            } else {
                //Disparo fallido = -0.5 puntos
                estadistica.registrarFallo();
                System.out.println("Agua!");
            }

            if (maquina.perdio()) {
                limpiarPantalla();
                //Punto 4: Al final revelar el estado de ambos tableros incluyendo barcos de la maquina
                mostrarTableros(jugador, maquina, true);
                System.out.println();
                System.out.println("Ganaste, " + nombre + "! Hundiste todos los barcos enemigos.");
                //Punto 6: Tabla de estados
                mostrarEstadoBarcos("Tus barcos", jugador.getTablero().getBarcos());
                mostrarEstadoBarcos("Barcos enemigos", maquina.getTablero().getBarcos());
                break;
            }

            //Punto 1: Turno de la maquina - disparo automatico aleatorio sin repetir coordenadas
            int[] posMaquina = maquina.siguienteDisparo();
            String resultadoMaquina = jugador.getTablero().recibirDisparo(posMaquina[0], posMaquina[1]);
            String coordMaquina = "" + (char)('A' + posMaquina[0]) + (posMaquina[1] + 1);
            System.out.println("Maquina dispara en " + coordMaquina + " -> " + interpretarResultado(resultadoMaquina));

            if (jugador.perdio()) {
                limpiarPantalla();
                //Punto 4: Al final revelar el estado de ambos tableros
                mostrarTableros(jugador, maquina, true);
                System.out.println();
                System.out.println("La maquina gano! Tus barcos fueron hundidos.");
                //Punto 6: Tabla de estados de ambos equipos
                mostrarEstadoBarcos("Tus barcos", jugador.getTablero().getBarcos());
                mostrarEstadoBarcos("Barcos enemigos", maquina.getTablero().getBarcos());
                break;
            }

            limpiarPantalla();
            //Punto 3: El tablero se actualiza en cada turno leyendo el estado actual de cuadricula[][]
            //Punto 7: Se muestran ambos mapas, los barcos de la maquina permanecen ocultos
            mostrarTableros(jugador, maquina);
        }

        System.out.println();
        System.out.printf("Resultado: %d aciertos, %d fallos, %.1f puntos%n",
            estadistica.getAciertos(), estadistica.getFallos(), estadistica.getPuntos());
        //Punto 9: Guardar la partida en tabla_puntos.csv con los datos del jugador
        //Punto 11: mostrarTabla() ordena descendente por puntos, el mejor aparece primero
        GestorExcel.guardar(estadistica);
        GestorExcel.mostrarTabla();
        sc.close();
    }

    private static void colocarBarcosManual(Jugador jugador, Scanner sc) {
        //El orden de colocacion de los barcos es el siguiente: PortaAviones(5), Acorazado(4), Submarino(3), Destructor(2), Lancha(1)
        Barco[] barcos = {
            new PortaAviones(), new Acorazado(), new Submarino(), new Destructor(), new Lancha()
        };
        for (Barco barco : barcos) {
            boolean colocado = false;
            while (!colocado) {
                limpiarPantalla();
                System.out.println("Coloca tu " + barco.getNombre() + "  " + "[■]".repeat(barco.getTamano()));
                jugador.getTablero().mostrar(true, false);
                System.out.print("Coordenada inicial (ej: A1): ");
                String entrada = sc.nextLine().trim().toUpperCase();
                int[] pos;

                try {
                    pos = parsearCoordenada(entrada);
                } catch (CoordenadaInvalidaException e) {
                    System.out.println("Coordenada invalida: " + e.getMessage());
                    continue;
                }
                System.out.print("Orientacion (H=horizontal / V=vertical): ");
                String ori = sc.nextLine().trim().toUpperCase();
                boolean horizontal = ori.equals("H");
                colocado = jugador.getTablero().colocarBarco(barco, pos[0], pos[1], horizontal);
                if (!colocado) System.out.println("No cabe ahi, intenta otra posicion.");
            }
        }
        limpiarPantalla();
        System.out.println("Tus barcos listos:");
        jugador.getTablero().mostrar(true, false);
        System.out.println();
        System.out.println("Presiona Enter para iniciar la batalla...");
        sc.nextLine();
    }

    //Bonus throw: parsea "A1", lanza CoordenadaInvalidaException si el formato es invalido
    private static int[] parsearCoordenada(String entrada) throws CoordenadaInvalidaException {
        if (entrada == null || entrada.length() < 2) {
            throw new CoordenadaInvalidaException("Debe tener al menos 2 caracteres (ej: A1)");
        }
        char letraChar = entrada.charAt(0);
        if (letraChar < 'A' || letraChar > 'J') {
            throw new CoordenadaInvalidaException("Fila debe ser de A a J");
        }
        int fila = letraChar - 'A';
        int columna;
        try {
            columna = Integer.parseInt(entrada.substring(1)) - 1;
        } catch (NumberFormatException e) {
            throw new CoordenadaInvalidaException("Columna debe ser un numero del 1 al 10");
        }
        if (columna < 0 || columna > 9) {
            throw new CoordenadaInvalidaException("Columna debe estar entre 1 y 10");
        }
        return new int[]{fila, columna};
    }

    //Punto 7: Overloading - mostramos el tablero sin revelar, durante el juego oculta barcos de la maquina
    private static void mostrarTableros(Jugador jugador, Maquina maquina) {
        mostrarTableros(jugador, maquina, false);
    }

    //Punto 4 y 7: Overloading - mostramos el tablero revelando los barcos, al final muestra todos los barcos de la maquina
    private static void mostrarTableros(Jugador jugador, Maquina maquina, boolean revelar) {
        String[] lineasJugador = jugador.getTablero().getLineas(true, true);
        String[] lineasMaquina = maquina.getTablero().getLineas(revelar, !revelar);
        System.out.printf("%-35s    %-15s%n", "Tu tablero", "Tablero enemigo");
        for (int i = 0; i < lineasJugador.length; i++) {
            System.out.println(lineasJugador[i] + "    " + lineasMaquina[i]);
        }
        System.out.println();
    }

    //Punto 6: Tabla de estados - muestra el estado final de cada barco
    //Bonus instanceof: barcos activos se castean a Atacable para llamar atacar(String,int) - Polimorfismo
    private static void mostrarEstadoBarcos(String titulo, ArrayList<Barco> barcos) {
        System.out.println();
        System.out.println("--- " + titulo + " ---");
        for (Barco barco : barcos) {
            System.out.println("  " + barco.getEstado());
            if (!barco.isHundido() && barco instanceof Atacable) {
                Atacable atacable = (Atacable) barco;
                System.out.println("    -> " + atacable.atacar("--", barco.getImpactos()));
            }
        }
    }

    private static String interpretarResultado(String resultado) {
        if (resultado.startsWith("TOCADO")) return "Tocado! " + resultado.split(":")[1];
        if (resultado.startsWith("HUNDIDO")) return "Hundido! " + resultado.split(":")[1];
        if (resultado.equals("REPETIDO")) return "Disparo repetido";
        return "Agua";
    }

    private static void limpiarPantalla() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            // \033[H mueve el cursor al inicio, \033[2J limpia la pantalla (secuencia ANSI)
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}
