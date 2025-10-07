package principal;

import controlador.Controlador;
import modelo.Planificador;
import vista.IVista;
import vista.VistaGUI;
import vista.VistaConsola;

/**
 * Clase principal que inicia el Simulador de Procesos del Sistema Operativo.
 *
 * Implementa el patrón MVC y utiliza polimorfismo para seleccionar la vista.
 * Por defecto usa interfaz gráfica (VistaGUI) con fallback automático a
 * consola (VistaConsola) en caso de error.
 *
 * @author Sistema de Simulación de Procesos
 * @version 1.0
 */
public class Main {

    /**
     * Punto de entrada del programa.
     *
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        // Inicializar el modelo
        Planificador modelo = new Planificador();

        // Seleccionar e inicializar la vista
        IVista vista = null;

        try {
            // Intentar iniciar con interfaz gráfica (GUI) - +15 puntos
            vista = new VistaGUI();

        } catch (Exception e) {
            // Si GUI falla, usar consola como backup
            System.err.println("No se pudo iniciar la interfaz gráfica.");
            System.err.println("Error: " + e.getMessage());
            System.err.println("\nIniciando en modo consola como alternativa...\n");

            vista = new VistaConsola();
        }

        // Inicializar el controlador con el modelo y la vista
        Controlador controlador = new Controlador(modelo, vista);

        // Si es GUI, configurar la referencia bidireccional
        if (vista instanceof VistaGUI) {
            ((VistaGUI) vista).setControlador(controlador);
        } else if (vista instanceof VistaConsola) {
            ((VistaConsola) vista).setControlador(controlador);
        }

        // Iniciar el sistema
        try {
            controlador.iniciar();
        } catch (Exception e) {
            System.err.println("Error fatal al iniciar el sistema: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}