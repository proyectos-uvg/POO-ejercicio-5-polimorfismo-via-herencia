package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un proceso por lotes (batch).
 *
 * Este tipo de proceso ejecuta una secuencia de tareas de forma automática y secuencial,
 * típicamente sin intervención del usuario. Son ideales para procesamiento masivo de datos
 * y tareas programadas. Ejemplos reales incluyen backups automáticos, procesamiento de logs
 * y generación de reportes nocturnos.
 *
 * @author Sistema de Simulación de Procesos
 * @version 1.0
 */
public class ProcesoBatch extends Proceso {

    /** Prioridad típica para procesos batch */
    private static final int PRIORIDAD_BATCH = 2;

    /** Lista de tareas a ejecutar en el batch */
    private List<String> tareas;

    /** Nombre del archivo de script que contiene las tareas */
    private String archivoScript;

    /** Contador de tareas completadas */
    private int tareasCompletadas;

    /**
     * Constructor para crear un proceso batch.
     *
     * @param pid Identificador único del proceso
     * @param nombre Nombre descriptivo del proceso
     * @param tareas Lista de tareas a ejecutar
     * @param archivo Nombre del archivo de script
     * @throws IllegalArgumentException si los parámetros no son válidos
     */
    public ProcesoBatch(int pid, String nombre, List<String> tareas, String archivo) {
        super(pid, nombre);

        if (tareas == null || tareas.isEmpty()) {
            throw new IllegalArgumentException("La lista de tareas no puede estar vacía o ser null");
        }

        if (archivo == null || archivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo de script no puede estar vacío");
        }

        this.tareas = new ArrayList<>(tareas);
        this.archivoScript = archivo.trim();
        this.tareasCompletadas = 0;
        this.prioridad = PRIORIDAD_BATCH;
    }

    /**
     * Ejecuta el proceso batch procesando todas las tareas secuencialmente.
     *
     * @return String con el resultado de la ejecución
     */
    @Override
    public String ejecutar() {
        setEstado(EstadoProceso.EJECUTANDO);

        StringBuilder resultado = new StringBuilder();
        resultado.append("\n");

        for (String tarea : tareas) {
            if (tarea != null && !tarea.trim().isEmpty()) {
                simularTiempoTarea();
                tareasCompletadas++;
                resultado.append("  ✓ ").append(tarea.trim()).append("\n");
            }
        }

        setEstado(EstadoProceso.TERMINADO);

        String detalle = String.format("Script '%s' completado. Tareas: %d/%d%s",
                                     archivoScript, tareasCompletadas, tareas.size(),
                                     resultado.toString());

        return generarMensajeEjecucion(detalle);
    }

    /**
     * Simula el tiempo de ejecución de una tarea individual.
     * Utiliza un tiempo menor que el tiempo normal de proceso.
     */
    private void simularTiempoTarea() {
        try {
            Thread.sleep(50 + (int)(Math.random() * 100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Ejecución de tarea interrumpida: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene la lista de tareas.
     *
     * @return Copia defensiva de la lista de tareas
     */
    public List<String> getTareas() {
        return new ArrayList<>(tareas);
    }

    /**
     * Establece una nueva lista de tareas.
     *
     * @param tareas Nueva lista de tareas
     * @throws IllegalArgumentException si la lista es null o vacía
     */
    public void setTareas(List<String> tareas) {
        if (tareas == null || tareas.isEmpty()) {
            throw new IllegalArgumentException("La lista de tareas no puede estar vacía o ser null");
        }
        this.tareas = new ArrayList<>(tareas);
        this.tareasCompletadas = 0;
    }

    /**
     * Obtiene el nombre del archivo de script.
     *
     * @return Nombre del archivo
     */
    public String getArchivoScript() {
        return archivoScript;
    }

    /**
     * Establece el nombre del archivo de script.
     *
     * @param archivo Nuevo nombre del archivo
     * @throws IllegalArgumentException si el archivo está vacío o es null
     */
    public void setArchivoScript(String archivo) {
        if (archivo == null || archivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo de script no puede estar vacío");
        }
        this.archivoScript = archivo.trim();
    }

    /**
     * Obtiene el número de tareas completadas.
     *
     * @return Número de tareas completadas
     */
    public int getTareasCompletadas() {
        return tareasCompletadas;
    }

    /**
     * Agrega una nueva tarea a la lista.
     *
     * @param tarea Nueva tarea a agregar
     * @throws IllegalArgumentException si la tarea está vacía o es null
     */
    public void agregarTarea(String tarea) {
        if (tarea == null || tarea.trim().isEmpty()) {
            throw new IllegalArgumentException("La tarea no puede estar vacía o ser null");
        }
        this.tareas.add(tarea.trim());
    }

    /**
     * Obtiene el número total de tareas.
     *
     * @return Número total de tareas
     */
    public int getNumeroTotalTareas() {
        return tareas.size();
    }

    /**
     * Verifica si todas las tareas han sido completadas.
     *
     * @return true si todas las tareas están completadas
     */
    public boolean todasTareasCompletadas() {
        return tareasCompletadas >= tareas.size();
    }

    /**
     * Representación en cadena del proceso batch.
     *
     * @return String con información detallada del proceso
     */
    @Override
    public String toString() {
        return String.format("ProcesoBatch [PID=%d, Nombre=%s, Estado=%s, Prioridad=%d, " +
                           "Script=%s, Tareas=%d/%d]",
                           pid, nombre, estado, prioridad, archivoScript,
                           tareasCompletadas, tareas.size());
    }
}