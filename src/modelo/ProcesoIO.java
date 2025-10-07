package modelo;

/**
 * Clase que representa un proceso que realiza operaciones de entrada/salida.
 *
 * Este tipo de proceso se caracteriza por interactuar con dispositivos externos
 * como discos, teclado o red, lo que implica períodos de bloqueo mientras espera
 * que se completen las operaciones de I/O. Ejemplos reales incluyen servidores web
 * (Apache, Nginx), bases de datos (MySQL) y editores de texto.
 *
 * @author Sistema de Simulación de Procesos
 * @version 1.0
 */
public class ProcesoIO extends Proceso {

    /** Dispositivos válidos para operaciones de I/O */
    private static final String DISPOSITIVO_TECLADO = "TECLADO";
    private static final String DISPOSITIVO_DISCO = "DISCO";
    private static final String DISPOSITIVO_RED = "RED";

    /** Tiempos de espera por dispositivo (en milisegundos) */
    private static final int TIEMPO_ESPERA_RED = 50;
    private static final int TIEMPO_ESPERA_DISCO = 100;
    private static final int TIEMPO_ESPERA_TECLADO = 200;

    /** Prioridad típica para procesos I/O */
    private static final int PRIORIDAD_IO = 4;

    /** Tipo de dispositivo utilizado */
    private String dispositivo;

    /** Cantidad de bytes transferidos */
    private long bytesTransferidos;

    /** Tiempo adicional de bloqueo por I/O */
    private int tiempoEspera;

    /**
     * Constructor para crear un proceso de I/O.
     *
     * @param pid Identificador único del proceso
     * @param nombre Nombre descriptivo del proceso
     * @param dispositivo Tipo de dispositivo (TECLADO, DISCO, RED)
     * @param bytes Cantidad de bytes a transferir
     * @throws IllegalArgumentException si los parámetros no son válidos
     */
    public ProcesoIO(int pid, String nombre, String dispositivo, long bytes) {
        super(pid, nombre);

        if (!esDispositivoValido(dispositivo)) {
            throw new IllegalArgumentException(
                "Dispositivo no válido: " + dispositivo +
                ". Dispositivos válidos: TECLADO, DISCO, RED");
        }

        if (bytes <= 0) {
            throw new IllegalArgumentException(
                "La cantidad de bytes debe ser mayor a 0. Valor recibido: " + bytes);
        }

        this.dispositivo = dispositivo.toUpperCase();
        this.bytesTransferidos = bytes;
        this.tiempoEspera = calcularTiempoEspera(this.dispositivo);
        this.prioridad = PRIORIDAD_IO;
    }

    /**
     * Ejecuta el proceso de I/O simulando operaciones de entrada/salida.
     *
     * @return String con el resultado de la ejecución
     */
    @Override
    public String ejecutar() {
        setEstado(EstadoProceso.EJECUTANDO);

        setEstado(EstadoProceso.BLOQUEADO);
        simularEsperaIO();

        setEstado(EstadoProceso.EJECUTANDO);
        simularTiempo();

        setEstado(EstadoProceso.TERMINADO);

        String detalle = String.format("Transferidos %d bytes por %s. Bloqueado: %dms",
                                     bytesTransferidos, dispositivo, tiempoEspera);

        return generarMensajeEjecucion(detalle);
    }

    /**
     * Simula el tiempo de espera por operaciones de I/O.
     */
    private void simularEsperaIO() {
        try {
            Thread.sleep(tiempoEspera);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Operación I/O interrumpida: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica si el dispositivo especificado es válido.
     *
     * @param dispositivo Dispositivo a validar
     * @return true si el dispositivo es válido
     */
    private boolean esDispositivoValido(String dispositivo) {
        if (dispositivo == null) {
            return false;
        }
        String dispositivoUpper = dispositivo.toUpperCase();
        return DISPOSITIVO_TECLADO.equals(dispositivoUpper) ||
               DISPOSITIVO_DISCO.equals(dispositivoUpper) ||
               DISPOSITIVO_RED.equals(dispositivoUpper);
    }

    /**
     * Calcula el tiempo de espera basado en el tipo de dispositivo.
     *
     * @param dispositivo Tipo de dispositivo
     * @return Tiempo de espera en milisegundos
     */
    private int calcularTiempoEspera(String dispositivo) {
        switch (dispositivo) {
            case DISPOSITIVO_RED:
                return TIEMPO_ESPERA_RED;
            case DISPOSITIVO_DISCO:
                return TIEMPO_ESPERA_DISCO;
            case DISPOSITIVO_TECLADO:
                return TIEMPO_ESPERA_TECLADO;
            default:
                return TIEMPO_ESPERA_DISCO;
        }
    }

    /**
     * Obtiene el tipo de dispositivo.
     *
     * @return Tipo de dispositivo
     */
    public String getDispositivo() {
        return dispositivo;
    }

    /**
     * Establece el tipo de dispositivo.
     *
     * @param dispositivo Nuevo tipo de dispositivo (TECLADO, DISCO, RED)
     * @throws IllegalArgumentException si el dispositivo no es válido
     */
    public void setDispositivo(String dispositivo) {
        if (!esDispositivoValido(dispositivo)) {
            throw new IllegalArgumentException(
                "Dispositivo no válido: " + dispositivo +
                ". Dispositivos válidos: TECLADO, DISCO, RED");
        }
        this.dispositivo = dispositivo.toUpperCase();
        this.tiempoEspera = calcularTiempoEspera(this.dispositivo);
    }

    /**
     * Obtiene la cantidad de bytes transferidos.
     *
     * @return Bytes transferidos
     */
    public long getBytesTransferidos() {
        return bytesTransferidos;
    }

    /**
     * Establece la cantidad de bytes a transferir.
     *
     * @param bytes Cantidad de bytes (debe ser mayor a 0)
     * @throws IllegalArgumentException si la cantidad de bytes no es válida
     */
    public void setBytesTransferidos(long bytes) {
        if (bytes <= 0) {
            throw new IllegalArgumentException(
                "La cantidad de bytes debe ser mayor a 0. Valor recibido: " + bytes);
        }
        this.bytesTransferidos = bytes;
    }

    /**
     * Obtiene el tiempo de espera por I/O.
     *
     * @return Tiempo de espera en milisegundos
     */
    public int getTiempoEspera() {
        return tiempoEspera;
    }

    /**
     * Representación en cadena del proceso I/O.
     *
     * @return String con información detallada del proceso
     */
    @Override
    public String toString() {
        return String.format("ProcesoIO [PID=%d, Nombre=%s, Estado=%s, Prioridad=%d, " +
                           "Dispositivo=%s, Bytes=%d]",
                           pid, nombre, estado, prioridad, dispositivo, bytesTransferidos);
    }
}