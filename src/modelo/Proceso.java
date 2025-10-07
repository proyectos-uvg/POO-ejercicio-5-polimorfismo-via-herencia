package modelo;

import java.util.Objects;
import java.util.Random;

/**
 * Clase abstracta que representa un proceso genérico en el sistema operativo.
 *
 * Define la estructura básica y comportamiento común para todos los tipos de procesos,
 * implementando el patrón Template Method para la ejecución de procesos.
 * Cada subclase debe implementar su propia lógica de ejecución específica.
 *
 * @author Sistema de Simulación de Procesos
 * @version 1.0
 */
public abstract class Proceso {

    /** Constantes para validación de prioridades */
    private static final int PRIORIDAD_MINIMA = 1;
    private static final int PRIORIDAD_MAXIMA = 10;
    private static final int PRIORIDAD_DEFECTO = 5;

    /** Constantes para simulación de tiempo */
    private static final int TIEMPO_MIN_MS = 100;
    private static final int TIEMPO_MAX_MS = 500;

    /** Generador de números aleatorios para simulación */
    private static final Random random = new Random();

    /** Identificador único del proceso */
    protected int pid;

    /** Nombre descriptivo del proceso */
    protected String nombre;

    /** Estado actual del proceso */
    protected EstadoProceso estado;

    /** Tiempo de ejecución en milisegundos */
    protected int tiempoEjecucion;

    /** Prioridad del proceso (1-10, donde 10 es máxima prioridad) */
    protected int prioridad;

    /**
     * Constructor base para todos los procesos.
     *
     * @param pid Identificador único del proceso
     * @param nombre Nombre descriptivo del proceso
     * @throws IllegalArgumentException si el nombre es null o vacío
     */
    public Proceso(int pid, String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proceso no puede estar vacío");
        }

        this.pid = pid;
        this.nombre = nombre.trim();
        this.estado = EstadoProceso.NUEVO;
        this.tiempoEjecucion = 0;
        this.prioridad = PRIORIDAD_DEFECTO;
    }

    /**
     * Método abstracto que define la lógica de ejecución específica de cada tipo de proceso.
     * Cada subclase debe implementar su propio comportamiento de ejecución.
     *
     * @return String con la descripción del resultado de la ejecución
     */
    public abstract String ejecutar();

    /**
     * Simula el tiempo de ejecución del proceso.
     * Genera un tiempo aleatorio entre 100-500ms y simula el trabajo mediante sleep.
     *
     * @throws RuntimeException si ocurre una InterruptedException durante el sleep
     */
    protected void simularTiempo() {
        this.tiempoEjecucion = random.nextInt(TIEMPO_MAX_MS - TIEMPO_MIN_MS + 1) + TIEMPO_MIN_MS;
        try {
            Thread.sleep(tiempoEjecucion);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Ejecución del proceso interrumpida: " + e.getMessage(), e);
        }
    }

    /**
     * Genera un mensaje de ejecución consistente para todas las subclases.
     *
     * @param detalle Información específica del tipo de proceso
     * @return String formateado con la información de ejecución
     */
    protected String generarMensajeEjecucion(String detalle) {
        return String.format("[PID: %d | %s] - Estado: %s - %s",
                           pid, nombre, estado, detalle);
    }

    // Getters y Setters

    /**
     * Obtiene el identificador del proceso.
     *
     * @return PID del proceso
     */
    public int getPid() {
        return pid;
    }

    /**
     * Obtiene el nombre del proceso.
     *
     * @return Nombre del proceso
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el estado actual del proceso.
     *
     * @return Estado del proceso
     */
    public EstadoProceso getEstado() {
        return estado;
    }

    /**
     * Establece el estado del proceso.
     *
     * @param estado Nuevo estado del proceso
     * @throws IllegalArgumentException si el estado es null
     */
    public void setEstado(EstadoProceso estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser null");
        }
        this.estado = estado;
    }

    /**
     * Obtiene el tiempo de ejecución del proceso.
     *
     * @return Tiempo de ejecución en milisegundos
     */
    public int getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    /**
     * Establece el tiempo de ejecución del proceso.
     *
     * @param tiempoEjecucion Tiempo de ejecución en milisegundos
     * @throws IllegalArgumentException si el tiempo es negativo
     */
    public void setTiempoEjecucion(int tiempoEjecucion) {
        if (tiempoEjecucion < 0) {
            throw new IllegalArgumentException("El tiempo de ejecución no puede ser negativo");
        }
        this.tiempoEjecucion = tiempoEjecucion;
    }

    /**
     * Obtiene la prioridad del proceso.
     *
     * @return Prioridad del proceso (1-10)
     */
    public int getPrioridad() {
        return prioridad;
    }

    /**
     * Establece la prioridad del proceso.
     *
     * @param prioridad Nueva prioridad (1-10)
     * @throws IllegalArgumentException si la prioridad está fuera del rango válido
     */
    public void setPrioridad(int prioridad) {
        if (prioridad < PRIORIDAD_MINIMA || prioridad > PRIORIDAD_MAXIMA) {
            throw new IllegalArgumentException(
                String.format("La prioridad debe estar entre %d y %d. Valor recibido: %d",
                            PRIORIDAD_MINIMA, PRIORIDAD_MAXIMA, prioridad));
        }
        this.prioridad = prioridad;
    }

    /**
     * Representación en cadena del proceso.
     *
     * @return String con la información básica del proceso
     */
    @Override
    public String toString() {
        return String.format("Proceso [PID=%d, Nombre=%s, Estado=%s, Prioridad=%d]",
                           pid, nombre, estado, prioridad);
    }

    /**
     * Compara si dos procesos son iguales basándose en su PID.
     *
     * @param obj Objeto a comparar
     * @return true si los procesos tienen el mismo PID
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Proceso proceso = (Proceso) obj;
        return pid == proceso.pid;
    }

    /**
     * Calcula el hash code basado en el PID del proceso.
     *
     * @return Hash code del proceso
     */
    @Override
    public int hashCode() {
        return Objects.hash(pid);
    }
}