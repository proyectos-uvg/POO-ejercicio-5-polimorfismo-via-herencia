package modelo;

/**
 * Clase que representa un proceso daemon del sistema.
 *
 * Los daemons son procesos que se ejecutan en segundo plano proporcionando servicios
 * al sistema operativo y a otros procesos. Típicamente tienen baja prioridad y pueden
 * configurarse para iniciarse automáticamente con el sistema. Ejemplos reales incluyen
 * sshd (servidor SSH), cron (programador de tareas) y systemd (gestor de servicios).
 *
 * @author Sistema de Simulación de Procesos
 * @version 1.0
 */
public class Daemon extends Proceso {

    /** Prioridad típica para procesos daemon */
    private static final int PRIORIDAD_DAEMON = 3;

    /** Intervalo de monitoreo por defecto en segundos */
    private static final int INTERVALO_DEFECTO = 30;

    /** Nombre del servicio que proporciona el daemon */
    private String servicio;

    /** Indica si el daemon se inicia automáticamente con el sistema */
    private boolean esAutoiniciable;

    /** Intervalo de monitoreo del servicio en segundos */
    private int intervaloMonitoreo;

    /**
     * Constructor para crear un proceso daemon.
     *
     * @param pid Identificador único del proceso
     * @param nombre Nombre descriptivo del proceso
     * @param servicio Nombre del servicio que proporciona
     * @param autoiniciable Indica si se inicia automáticamente
     * @throws IllegalArgumentException si el servicio está vacío o es null
     */
    public Daemon(int pid, String nombre, String servicio, boolean autoiniciable) {
        super(pid, nombre);

        if (servicio == null || servicio.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del servicio no puede estar vacío");
        }

        this.servicio = servicio.trim();
        this.esAutoiniciable = autoiniciable;
        this.intervaloMonitoreo = INTERVALO_DEFECTO;
        this.prioridad = PRIORIDAD_DAEMON;
    }

    /**
     * Ejecuta el proceso daemon, simulando la prestación de servicios en segundo plano.
     *
     * @return String con el resultado de la ejecución
     */
    @Override
    public String ejecutar() {
        setEstado(EstadoProceso.EJECUTANDO);

        simularTiempo();

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Servicio '").append(servicio).append("' ejecutándose en segundo plano.");

        if (esAutoiniciable) {
            mensaje.append(" [Autoiniciable]");
        }

        mensaje.append(" Monitoreo cada ").append(intervaloMonitoreo).append("s");

        setEstado(EstadoProceso.TERMINADO);

        return generarMensajeEjecucion(mensaje.toString());
    }

    /**
     * Obtiene el nombre del servicio.
     *
     * @return Nombre del servicio
     */
    public String getServicio() {
        return servicio;
    }

    /**
     * Establece el nombre del servicio.
     *
     * @param servicio Nuevo nombre del servicio
     * @throws IllegalArgumentException si el servicio está vacío o es null
     */
    public void setServicio(String servicio) {
        if (servicio == null || servicio.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del servicio no puede estar vacío");
        }
        this.servicio = servicio.trim();
    }

    /**
     * Verifica si el daemon es autoiniciable.
     *
     * @return true si el daemon se inicia automáticamente
     */
    public boolean isAutoiniciable() {
        return esAutoiniciable;
    }

    /**
     * Establece si el daemon es autoiniciable.
     *
     * @param autoiniciable true para que se inicie automáticamente
     */
    public void setAutoiniciable(boolean autoiniciable) {
        this.esAutoiniciable = autoiniciable;
    }

    /**
     * Obtiene el intervalo de monitoreo.
     *
     * @return Intervalo en segundos
     */
    public int getIntervaloMonitoreo() {
        return intervaloMonitoreo;
    }

    /**
     * Establece el intervalo de monitoreo.
     *
     * @param intervalo Intervalo en segundos (debe ser mayor a 0)
     * @throws IllegalArgumentException si el intervalo no es válido
     */
    public void setIntervaloMonitoreo(int intervalo) {
        if (intervalo <= 0) {
            throw new IllegalArgumentException(
                "El intervalo de monitoreo debe ser mayor a 0. Valor recibido: " + intervalo);
        }
        this.intervaloMonitoreo = intervalo;
    }

    /**
     * Representación en cadena del proceso daemon.
     *
     * @return String con información detallada del proceso
     */
    @Override
    public String toString() {
        return String.format("Daemon [PID=%d, Nombre=%s, Estado=%s, Prioridad=%d, " +
                           "Servicio=%s, Autoiniciable=%s, Monitoreo=%ds]",
                           pid, nombre, estado, prioridad, servicio,
                           esAutoiniciable ? "Sí" : "No", intervaloMonitoreo);
    }
}