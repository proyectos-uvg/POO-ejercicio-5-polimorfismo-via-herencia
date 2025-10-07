package modelo;

/**
 * Clase que representa un proceso intensivo en CPU.
 *
 * Este tipo de proceso se caracteriza por realizar operaciones matemáticas intensivas
 * que requieren mucho procesamiento y pueden utilizar múltiples núcleos del procesador.
 * Ejemplos reales incluyen compiladores (GCC, Clang), renderizado 3D (Blender),
 * y compresión de video (FFmpeg).
 *
 * @author Sistema de Simulación de Procesos
 * @version 1.0
 */
public class ProcesoCPU extends Proceso {

    /** Constantes para validación */
    private static final int MIN_OPERACIONES = 1;
    private static final int MIN_NUCLEOS = 1;
    private static final int MAX_NUCLEOS = 16;
    private static final int PRIORIDAD_CPU = 7;

    /** Número de operaciones matemáticas a realizar */
    private int operacionesMatematicas;

    /** Número de núcleos del procesador a utilizar */
    private int usoNucleos;

    /**
     * Constructor para crear un proceso CPU.
     *
     * @param pid Identificador único del proceso
     * @param nombre Nombre descriptivo del proceso
     * @param operaciones Número de operaciones matemáticas a realizar
     * @param nucleos Número de núcleos a utilizar (1-16)
     * @throws IllegalArgumentException si los parámetros no son válidos
     */
    public ProcesoCPU(int pid, String nombre, int operaciones, int nucleos) {
        super(pid, nombre);

        if (operaciones < MIN_OPERACIONES) {
            throw new IllegalArgumentException(
                "El número de operaciones debe ser mayor a 0. Valor recibido: " + operaciones);
        }

        if (nucleos < MIN_NUCLEOS || nucleos > MAX_NUCLEOS) {
            throw new IllegalArgumentException(
                String.format("El número de núcleos debe estar entre %d y %d. Valor recibido: %d",
                            MIN_NUCLEOS, MAX_NUCLEOS, nucleos));
        }

        this.operacionesMatematicas = operaciones;
        this.usoNucleos = nucleos;
        this.prioridad = PRIORIDAD_CPU;
    }

    /**
     * Ejecuta el proceso CPU realizando operaciones matemáticas simuladas.
     *
     * @return String con el resultado de la ejecución
     */
    @Override
    public String ejecutar() {
        setEstado(EstadoProceso.EJECUTANDO);

        simularTiempo();

        realizarCalculosMatematicos();

        setEstado(EstadoProceso.TERMINADO);

        String detalle = String.format("Ejecutó %d operaciones en %d núcleos. Tiempo: %dms",
                                     operacionesMatematicas, usoNucleos, tiempoEjecucion);

        return generarMensajeEjecucion(detalle);
    }

    /**
     * Simula la realización de cálculos matemáticos intensivos.
     * Ejemplo de operaciones que podría realizar un proceso real de este tipo.
     */
    private void realizarCalculosMatematicos() {
        for (int i = 0; i < Math.min(operacionesMatematicas, 1000); i++) {
            Math.sqrt(i * Math.PI);
            Math.pow(i, 2);
            Math.sin(i / 180.0 * Math.PI);
        }
    }

    /**
     * Obtiene el número de operaciones matemáticas.
     *
     * @return Número de operaciones
     */
    public int getOperacionesMatematicas() {
        return operacionesMatematicas;
    }

    /**
     * Establece el número de operaciones matemáticas.
     *
     * @param operaciones Número de operaciones (debe ser mayor a 0)
     * @throws IllegalArgumentException si el número de operaciones no es válido
     */
    public void setOperacionesMatematicas(int operaciones) {
        if (operaciones < MIN_OPERACIONES) {
            throw new IllegalArgumentException(
                "El número de operaciones debe ser mayor a 0. Valor recibido: " + operaciones);
        }
        this.operacionesMatematicas = operaciones;
    }

    /**
     * Obtiene el número de núcleos utilizados.
     *
     * @return Número de núcleos
     */
    public int getUsoNucleos() {
        return usoNucleos;
    }

    /**
     * Establece el número de núcleos a utilizar.
     *
     * @param nucleos Número de núcleos (1-16)
     * @throws IllegalArgumentException si el número de núcleos no es válido
     */
    public void setUsoNucleos(int nucleos) {
        if (nucleos < MIN_NUCLEOS || nucleos > MAX_NUCLEOS) {
            throw new IllegalArgumentException(
                String.format("El número de núcleos debe estar entre %d y %d. Valor recibido: %d",
                            MIN_NUCLEOS, MAX_NUCLEOS, nucleos));
        }
        this.usoNucleos = nucleos;
    }

    /**
     * Representación en cadena del proceso CPU.
     *
     * @return String con información detallada del proceso
     */
    @Override
    public String toString() {
        return String.format("ProcesoCPU [PID=%d, Nombre=%s, Estado=%s, Prioridad=%d, " +
                           "Operaciones=%d, Núcleos=%d]",
                           pid, nombre, estado, prioridad, operacionesMatematicas, usoNucleos);
    }
}