package modelo;

/**
 * Clase que representa un proceso de tiempo real.
 *
 * Los procesos de tiempo real tienen restricciones temporales estrictas y deben
 * completar su ejecución dentro de un deadline específico. Son críticos en sistemas
 * donde el tiempo de respuesta es fundamental. Ejemplos reales incluyen sistemas de
 * control industrial, procesamiento de audio/video en vivo y sistemas médicos.
 *
 * @author Sistema de Simulación de Procesos
 * @version 1.0
 */
public class ProcesoTiempoReal extends Proceso {

    /** Prioridades para procesos de tiempo real */
    private static final int PRIORIDAD_CRITICO = 10;
    private static final int PRIORIDAD_NO_CRITICO = 9;

    /** Deadline máximo en milisegundos para la ejecución */
    private int deadline;

    /** Indica si el proceso es crítico para el sistema */
    private boolean esCritico;

    /** Variación temporal en la ejecución (jitter) en milisegundos */
    private double jitter;

    /**
     * Constructor para crear un proceso de tiempo real.
     *
     * @param pid Identificador único del proceso
     * @param nombre Nombre descriptivo del proceso
     * @param deadline Tiempo límite para completar la ejecución (en ms)
     * @param critico Indica si es un proceso crítico
     * @throws IllegalArgumentException si el deadline no es válido
     */
    public ProcesoTiempoReal(int pid, String nombre, int deadline, boolean critico) {
        super(pid, nombre);

        if (deadline <= 0) {
            throw new IllegalArgumentException(
                "El deadline debe ser mayor a 0. Valor recibido: " + deadline);
        }

        this.deadline = deadline;
        this.esCritico = critico;
        this.jitter = 0.0;
        this.prioridad = critico ? PRIORIDAD_CRITICO : PRIORIDAD_NO_CRITICO;
    }

    /**
     * Ejecuta el proceso de tiempo real monitoreando el cumplimiento del deadline.
     *
     * @return String con el resultado de la ejecución y análisis del deadline
     */
    @Override
    public String ejecutar() {
        long tiempoInicio = System.currentTimeMillis();

        setEstado(EstadoProceso.EJECUTANDO);

        simularTiempo();

        long tiempoFin = System.currentTimeMillis();
        long tiempoRealEjecucion = tiempoFin - tiempoInicio;

        this.jitter = Math.abs(tiempoRealEjecucion - tiempoEjecucion);

        boolean cumplioDeadline = tiempoRealEjecucion <= deadline;

        StringBuilder detalle = new StringBuilder();
        detalle.append(String.format("Deadline: %dms | Tiempo real: %dms",
                                    deadline, tiempoRealEjecucion));

        if (cumplioDeadline) {
            detalle.append(" ✓ CUMPLIDO");
        } else {
            detalle.append(" ✗ EXCEDIDO");
        }

        if (esCritico) {
            detalle.append(" [CRÍTICO]");
        }

        detalle.append(String.format(" | Jitter: %.2fms", jitter));

        setEstado(EstadoProceso.TERMINADO);

        return generarMensajeEjecucion(detalle.toString());
    }

    /**
     * Verifica si el proceso cumplió con su deadline en la última ejecución.
     *
     * @return true si cumplió el deadline
     */
    public boolean cumplioDeadline() {
        return tiempoEjecucion <= deadline;
    }

    /**
     * Calcula el porcentaje de utilización del deadline.
     *
     * @return Porcentaje de tiempo utilizado respecto al deadline
     */
    public double porcentajeUtilizacionDeadline() {
        if (deadline == 0) {
            return 0.0;
        }
        return (double) tiempoEjecucion / deadline * 100.0;
    }

    /**
     * Obtiene el deadline del proceso.
     *
     * @return Deadline en milisegundos
     */
    public int getDeadline() {
        return deadline;
    }

    /**
     * Establece el deadline del proceso.
     *
     * @param deadline Nuevo deadline en milisegundos
     * @throws IllegalArgumentException si el deadline no es válido
     */
    public void setDeadline(int deadline) {
        if (deadline <= 0) {
            throw new IllegalArgumentException(
                "El deadline debe ser mayor a 0. Valor recibido: " + deadline);
        }
        this.deadline = deadline;
    }

    /**
     * Verifica si el proceso es crítico.
     *
     * @return true si el proceso es crítico
     */
    public boolean isCritico() {
        return esCritico;
    }

    /**
     * Establece si el proceso es crítico.
     *
     * @param critico true para marcar como crítico
     */
    public void setCritico(boolean critico) {
        this.esCritico = critico;
        this.prioridad = critico ? PRIORIDAD_CRITICO : PRIORIDAD_NO_CRITICO;
    }

    /**
     * Obtiene el jitter (variación temporal) del proceso.
     *
     * @return Jitter en milisegundos
     */
    public double getJitter() {
        return jitter;
    }

    /**
     * Obtiene el tiempo restante hasta el deadline.
     *
     * @return Tiempo restante en milisegundos, 0 si ya se ejecutó
     */
    public int getTiempoRestanteDeadline() {
        return Math.max(0, deadline - tiempoEjecucion);
    }

    /**
     * Obtiene la clasificación de criticidad del proceso.
     *
     * @return String describiendo el nivel de criticidad
     */
    public String getClasificacionCriticidad() {
        if (esCritico) {
            return "CRÍTICO - Fallo puede causar daño al sistema";
        } else {
            return "NO CRÍTICO - Fallo solo afecta rendimiento";
        }
    }

    /**
     * Representación en cadena del proceso de tiempo real.
     *
     * @return String con información detallada del proceso
     */
    @Override
    public String toString() {
        return String.format("ProcesoTiempoReal [PID=%d, Nombre=%s, Estado=%s, Prioridad=%d, " +
                           "Deadline=%dms, Crítico=%s, Jitter=%.2fms]",
                           pid, nombre, estado, prioridad, deadline,
                           esCritico ? "Sí" : "No", jitter);
    }
}