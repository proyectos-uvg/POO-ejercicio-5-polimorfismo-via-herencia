package vista;

import modelo.Proceso;
import java.util.List;

/**
 * Interfaz que define el contrato para todas las implementaciones de vista
 * en el simulador de procesos.
 *
 * Esta interfaz establece los métodos comunes que deben implementar tanto
 * la vista de consola como la vista gráfica, permitiendo el polimorfismo
 * en el patrón MVC y facilitando el intercambio de implementaciones de vista.
 *
 * @author Sistema de Simulación de Procesos
 * @version 1.0
 */
public interface IVista {

    /**
     * Muestra un mensaje informativo al usuario.
     *
     * @param mensaje Mensaje a mostrar
     */
    void mostrarMensaje(String mensaje);

    /**
     * Muestra un mensaje de error al usuario.
     *
     * @param error Mensaje de error a mostrar
     */
    void mostrarError(String error);

    /**
     * Muestra la lista de procesos en formato apropiado para la vista.
     *
     * @param procesos Lista de procesos a mostrar
     */
    void mostrarProcesos(List<Proceso> procesos);

    /**
     * Muestra el resultado de la ejecución de un proceso.
     *
     * @param resultado String con el resultado de la ejecución
     */
    void mostrarResultadoEjecucion(String resultado);

    /**
     * Inicia la interfaz de usuario correspondiente.
     * Para consola: inicia el bucle de menú
     * Para GUI: hace visible la ventana
     */
    void iniciar();
}