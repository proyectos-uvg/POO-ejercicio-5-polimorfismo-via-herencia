package vista;

import controlador.Controlador;
import modelo.Proceso;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Implementación de vista basada en consola para el simulador de procesos.
 *
 * Esta clase proporciona una interfaz de línea de comandos para interactuar
 * con el simulador, permitiendo al usuario registrar procesos, ejecutarlos
 * y ver estadísticas del sistema mediante un menú textual.
 *
 * @author Sistema de Simulación de Procesos
 * @version 1.0
 */
public class VistaConsola implements IVista {

    /** Scanner para leer entrada del usuario */
    private Scanner scanner;

    /** Referencia al controlador para coordinar acciones */
    private Controlador controlador;

    /**
     * Constructor de la vista de consola.
     */
    public VistaConsola() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Establece la referencia al controlador.
     *
     * @param controlador Controlador del sistema
     */
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Muestra un mensaje informativo en la consola.
     *
     * @param mensaje Mensaje a mostrar
     */
    @Override
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    /**
     * Muestra un mensaje de error en la consola.
     *
     * @param error Mensaje de error a mostrar
     */
    @Override
    public void mostrarError(String error) {
        System.err.println("ERROR: " + error);
    }

    /**
     * Muestra la lista de procesos en formato tabular.
     *
     * @param procesos Lista de procesos a mostrar
     */
    @Override
    public void mostrarProcesos(List<Proceso> procesos) {
        if (procesos.isEmpty()) {
            System.out.println("No hay procesos registrados en el sistema.");
            return;
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("                           PROCESOS DEL SISTEMA");
        System.out.println("=".repeat(80));
        System.out.printf("%-6s | %-20s | %-15s | %-12s | %-10s%n",
                         "PID", "NOMBRE", "TIPO", "ESTADO", "PRIORIDAD");
        System.out.println("-".repeat(80));

        for (Proceso proceso : procesos) {
            String tipo = proceso.getClass().getSimpleName();
            System.out.printf("%-6d | %-20s | %-15s | %-12s | %-10d%n",
                             proceso.getPid(),
                             truncarTexto(proceso.getNombre(), 20),
                             tipo,
                             proceso.getEstado(),
                             proceso.getPrioridad());
        }
        System.out.println("=".repeat(80));
        System.out.println("Total de procesos: " + procesos.size());
    }

    /**
     * Muestra el resultado de la ejecución de un proceso.
     *
     * @param resultado String con el resultado de la ejecución
     */
    @Override
    public void mostrarResultadoEjecucion(String resultado) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           RESULTADO DE EJECUCIÓN");
        System.out.println("=".repeat(50));
        System.out.println(resultado);
        System.out.println("=".repeat(50));
    }

    /**
     * Inicia la interfaz de consola con el bucle principal del menú.
     */
    @Override
    public void iniciar() {
        mostrarBienvenida();
        boolean continuar = true;

        while (continuar) {
            mostrarMenu();
            int opcion = leerOpcion();

            switch (opcion) {
                case 0:
                    continuar = false;
                    break;
                case 1:
                    manejarRegistroProcesoCPU();
                    break;
                case 2:
                    manejarRegistroProcesoIO();
                    break;
                case 3:
                    manejarRegistroDaemon();
                    break;
                case 4:
                    manejarRegistroProcesoRed();
                    break;
                case 5:
                    manejarRegistroProcesoMemoria();
                    break;
                case 6:
                    manejarRegistroProcesoBatch();
                    break;
                case 7:
                    manejarRegistroProcesoTiempoReal();
                    break;
                case 8:
                    manejarVerProcesos();
                    break;
                case 9:
                    manejarEjecutarTodos();
                    break;
                case 10:
                    manejarEjecutarPorPID();
                    break;
                case 11:
                    manejarEliminarProceso();
                    break;
                case 12:
                    manejarVerEstadisticas();
                    break;
                default:
                    mostrarError("Opción no válida. Intente nuevamente.");
            }

            if (continuar) {
                pausar();
            }
        }

        despedirse();
    }

    /**
     * Muestra el mensaje de bienvenida.
     */
    private void mostrarBienvenida() {
        System.out.println("=".repeat(60));
        System.out.println("    SIMULADOR DE PROCESOS DEL SISTEMA OPERATIVO");
        System.out.println("=".repeat(60));
        System.out.println("Bienvenido al simulador de procesos.");
        System.out.println("Puede registrar diferentes tipos de procesos y ejecutarlos.");
        System.out.println();
    }

    /**
     * Muestra el menú principal de opciones.
     */
    private void mostrarMenu() {
        System.out.println("\n" + "─".repeat(50));
        System.out.println("                 MENÚ PRINCIPAL");
        System.out.println("─".repeat(50));
        System.out.println(" 1. Registrar Proceso CPU");
        System.out.println(" 2. Registrar Proceso I/O");
        System.out.println(" 3. Registrar Daemon");
        System.out.println(" 4. Registrar Proceso Red");
        System.out.println(" 5. Registrar Proceso Memoria");
        System.out.println(" 6. Registrar Proceso Batch");
        System.out.println(" 7. Registrar Proceso Tiempo Real");
        System.out.println(" 8. Ver todos los procesos");
        System.out.println(" 9. Ejecutar todos los procesos");
        System.out.println("10. Ejecutar proceso por PID");
        System.out.println("11. Eliminar proceso");
        System.out.println("12. Ver estadísticas");
        System.out.println(" 0. Salir");
        System.out.println("─".repeat(50));
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Lee y valida la opción seleccionada por el usuario.
     *
     * @return Opción válida seleccionada
     */
    private int leerOpcion() {
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine();
            return opcion;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            mostrarError("Debe ingresar un número válido.");
            return -1;
        }
    }

    /**
     * Lee una cadena de texto del usuario con validación.
     *
     * @param prompt Mensaje para solicitar la entrada
     * @return String ingresado por el usuario
     */
    private String leerString(String prompt) {
        System.out.print(prompt);
        String entrada = scanner.nextLine().trim();
        while (entrada.isEmpty()) {
            System.out.print("La entrada no puede estar vacía. " + prompt);
            entrada = scanner.nextLine().trim();
        }
        return entrada;
    }

    /**
     * Lee un número entero del usuario con validación.
     *
     * @param prompt Mensaje para solicitar la entrada
     * @return Entero válido ingresado por el usuario
     */
    private int leerInt(String prompt) {
        boolean valorValido = false;
        int valor = 0;

        while (!valorValido) {
            try {
                System.out.print(prompt);
                valor = scanner.nextInt();
                scanner.nextLine();
                valorValido = true;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                mostrarError("Debe ingresar un número entero válido.");
            }
        }

        return valor;
    }

    /**
     * Lee un número long del usuario con validación.
     *
     * @param prompt Mensaje para solicitar la entrada
     * @return Long válido ingresado por el usuario
     */
    private long leerLong(String prompt) {
        boolean valorValido = false;
        long valor = 0;

        while (!valorValido) {
            try {
                System.out.print(prompt);
                valor = scanner.nextLong();
                scanner.nextLine();
                valorValido = true;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                mostrarError("Debe ingresar un número válido.");
            }
        }

        return valor;
    }

    /**
     * Lee un valor booleano del usuario.
     *
     * @param prompt Mensaje para solicitar la entrada
     * @return Boolean ingresado por el usuario
     */
    private boolean leerBoolean(String prompt) {
        boolean respuestaValida = false;
        boolean resultado = false;

        while (!respuestaValida) {
            System.out.print(prompt + " (s/n): ");
            String entrada = scanner.nextLine().trim().toLowerCase();
            if (entrada.equals("s") || entrada.equals("si") || entrada.equals("sí")) {
                resultado = true;
                respuestaValida = true;
            } else if (entrada.equals("n") || entrada.equals("no")) {
                resultado = false;
                respuestaValida = true;
            } else {
                mostrarError("Responda con 's' para sí o 'n' para no.");
            }
        }

        return resultado;
    }

    // Métodos para manejar cada opción del menú

    private void manejarRegistroProcesoCPU() {
        System.out.println("\n--- Registrar Proceso CPU ---");
        String nombre = leerString("Nombre del proceso: ");
        int operaciones = leerInt("Operaciones matemáticas: ");
        int nucleos = leerInt("Número de núcleos (1-16): ");

        if (controlador != null) {
            controlador.registrarProcesoCPU(nombre, operaciones, nucleos);
        }
    }

    private void manejarRegistroProcesoIO() {
        System.out.println("\n--- Registrar Proceso I/O ---");
        String nombre = leerString("Nombre del proceso: ");
        System.out.println("Dispositivos disponibles: TECLADO, DISCO, RED");
        String dispositivo = leerString("Dispositivo: ");
        long bytes = leerLong("Bytes a transferir: ");

        if (controlador != null) {
            controlador.registrarProcesoIO(nombre, dispositivo, bytes);
        }
    }

    private void manejarRegistroDaemon() {
        System.out.println("\n--- Registrar Daemon ---");
        String nombre = leerString("Nombre del proceso: ");
        String servicio = leerString("Nombre del servicio: ");
        boolean autoiniciable = leerBoolean("¿Es autoiniciable?");

        if (controlador != null) {
            controlador.registrarDaemon(nombre, servicio, autoiniciable);
        }
    }

    private void manejarRegistroProcesoRed() {
        System.out.println("\n--- Registrar Proceso Red ---");
        String nombre = leerString("Nombre del proceso: ");
        System.out.println("Protocolos disponibles: TCP, UDP, HTTP, HTTPS");
        String protocolo = leerString("Protocolo: ");
        int puertoOrigen = leerInt("Puerto origen (1-65535): ");
        int puertoDestino = leerInt("Puerto destino (1-65535): ");

        if (controlador != null) {
            controlador.registrarProcesoRed(nombre, protocolo, puertoOrigen, puertoDestino);
        }
    }

    private void manejarRegistroProcesoMemoria() {
        System.out.println("\n--- Registrar Proceso Memoria ---");
        String nombre = leerString("Nombre del proceso: ");
        long memoria = leerLong("Memoria en MB: ");
        System.out.println("Tipos disponibles: RAM, VIRTUAL");
        String tipo = leerString("Tipo de memoria: ");

        if (controlador != null) {
            controlador.registrarProcesoMemoria(nombre, memoria, tipo);
        }
    }

    private void manejarRegistroProcesoBatch() {
        System.out.println("\n--- Registrar Proceso Batch ---");
        String nombre = leerString("Nombre del proceso: ");
        String archivo = leerString("Archivo de script: ");

        List<String> tareas = new ArrayList<>();
        System.out.println("Ingrese las tareas (escriba 'fin' para terminar):");
        String tarea = leerString("Tarea 1: ");
        int contador = 2;

        while (!tarea.equalsIgnoreCase("fin")) {
            tareas.add(tarea);
            tarea = leerString("Tarea " + contador + " (o 'fin'): ");
            contador++;
        }

        if (controlador != null && !tareas.isEmpty()) {
            controlador.registrarProcesoBatch(nombre, tareas, archivo);
        } else if (tareas.isEmpty()) {
            mostrarError("Debe ingresar al menos una tarea.");
        }
    }

    private void manejarRegistroProcesoTiempoReal() {
        System.out.println("\n--- Registrar Proceso Tiempo Real ---");
        String nombre = leerString("Nombre del proceso: ");
        int deadline = leerInt("Deadline en milisegundos: ");
        boolean critico = leerBoolean("¿Es crítico?");

        if (controlador != null) {
            controlador.registrarProcesoTiempoReal(nombre, deadline, critico);
        }
    }

    private void manejarVerProcesos() {
        if (controlador != null) {
            controlador.actualizarVistaProcesos();
        }
    }

    private void manejarEjecutarTodos() {
        if (controlador != null) {
            controlador.ejecutarTodosLosProcesos();
        }
    }

    private void manejarEjecutarPorPID() {
        int pid = leerInt("Ingrese el PID del proceso a ejecutar: ");
        if (controlador != null) {
            controlador.ejecutarProcesoPorPID(pid);
        }
    }

    private void manejarEliminarProceso() {
        int pid = leerInt("Ingrese el PID del proceso a eliminar: ");
        if (controlador != null) {
            controlador.eliminarProceso(pid);
        }
    }

    private void manejarVerEstadisticas() {
        if (controlador != null) {
            controlador.mostrarEstadisticas();
        }
    }

    /**
     * Pausa la ejecución esperando que el usuario presione Enter.
     */
    private void pausar() {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    /**
     * Muestra el mensaje de despedida y cierra recursos.
     */
    private void despedirse() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        Gracias por usar el simulador");
        System.out.println("              ¡Hasta luego!");
        System.out.println("=".repeat(50));
        scanner.close();
    }

    /**
     * Trunca un texto a una longitud máxima.
     *
     * @param texto Texto a truncar
     * @param longitudMaxima Longitud máxima permitida
     * @return Texto truncado
     */
    private String truncarTexto(String texto, int longitudMaxima) {
        if (texto.length() <= longitudMaxima) {
            return texto;
        }
        return texto.substring(0, longitudMaxima - 3) + "...";
    }
}