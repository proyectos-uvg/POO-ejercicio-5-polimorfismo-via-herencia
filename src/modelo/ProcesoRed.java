package modelo;

import java.util.Random;

/**
 * Clase que representa un proceso de comunicación de red.
 *
 * Este tipo de proceso maneja comunicaciones a través de la red utilizando diferentes
 * protocolos y puertos. Se encarga de enviar y recibir paquetes de datos mediante
 * conexiones de red. Ejemplos reales incluyen DNS resolver, servicios de streaming
 * y clientes VPN.
 *
 * @author Sistema de Simulación de Procesos
 * @version 1.0
 */
public class ProcesoRed extends Proceso {

    /** Protocolos de red válidos */
    private static final String PROTOCOLO_TCP = "TCP";
    private static final String PROTOCOLO_UDP = "UDP";
    private static final String PROTOCOLO_HTTP = "HTTP";
    private static final String PROTOCOLO_HTTPS = "HTTPS";

    /** Rangos válidos para puertos */
    private static final int PUERTO_MINIMO = 1;
    private static final int PUERTO_MAXIMO = 65535;

    /** Rango para número de paquetes enviados */
    private static final int MIN_PAQUETES = 100;
    private static final int MAX_PAQUETES = 1000;

    /** Prioridad típica para procesos de red */
    private static final int PRIORIDAD_RED = 6;

    /** Generador de números aleatorios */
    private static final Random random = new Random();

    /** Protocolo de comunicación utilizado */
    private String protocolo;

    /** Puerto de origen para la comunicación */
    private int puertoOrigen;

    /** Puerto de destino para la comunicación */
    private int puertoDestino;

    /** Número de paquetes enviados durante la comunicación */
    private int paquetesEnviados;

    /**
     * Constructor para crear un proceso de red.
     *
     * @param pid Identificador único del proceso
     * @param nombre Nombre descriptivo del proceso
     * @param protocolo Protocolo de red (TCP, UDP, HTTP, HTTPS)
     * @param pOrigen Puerto de origen (1-65535)
     * @param pDestino Puerto de destino (1-65535)
     * @throws IllegalArgumentException si los parámetros no son válidos
     */
    public ProcesoRed(int pid, String nombre, String protocolo, int pOrigen, int pDestino) {
        super(pid, nombre);

        if (!esProtocoloValido(protocolo)) {
            throw new IllegalArgumentException(
                "Protocolo no válido: " + protocolo +
                ". Protocolos válidos: TCP, UDP, HTTP, HTTPS");
        }

        if (!esPuertoValido(pOrigen)) {
            throw new IllegalArgumentException(
                String.format("Puerto de origen debe estar entre %d y %d. Valor recibido: %d",
                            PUERTO_MINIMO, PUERTO_MAXIMO, pOrigen));
        }

        if (!esPuertoValido(pDestino)) {
            throw new IllegalArgumentException(
                String.format("Puerto de destino debe estar entre %d y %d. Valor recibido: %d",
                            PUERTO_MINIMO, PUERTO_MAXIMO, pDestino));
        }

        this.protocolo = protocolo.toUpperCase();
        this.puertoOrigen = pOrigen;
        this.puertoDestino = pDestino;
        this.paquetesEnviados = 0;
        this.prioridad = PRIORIDAD_RED;
    }

    /**
     * Ejecuta el proceso de red simulando comunicación por la red.
     *
     * @return String con el resultado de la ejecución
     */
    @Override
    public String ejecutar() {
        setEstado(EstadoProceso.EJECUTANDO);

        simularTiempo();

        this.paquetesEnviados = random.nextInt(MAX_PAQUETES - MIN_PAQUETES + 1) + MIN_PAQUETES;

        String detalle = String.format("Protocolo %s | Puerto %d → %d | Paquetes enviados: %d",
                                     protocolo, puertoOrigen, puertoDestino, paquetesEnviados);

        setEstado(EstadoProceso.TERMINADO);

        return generarMensajeEjecucion(detalle);
    }

    /**
     * Verifica si el protocolo especificado es válido.
     *
     * @param protocolo Protocolo a validar
     * @return true si el protocolo es válido
     */
    private boolean esProtocoloValido(String protocolo) {
        if (protocolo == null) {
            return false;
        }
        String protocoloUpper = protocolo.toUpperCase();
        return PROTOCOLO_TCP.equals(protocoloUpper) ||
               PROTOCOLO_UDP.equals(protocoloUpper) ||
               PROTOCOLO_HTTP.equals(protocoloUpper) ||
               PROTOCOLO_HTTPS.equals(protocoloUpper);
    }

    /**
     * Verifica si el puerto especificado está en el rango válido.
     *
     * @param puerto Puerto a validar
     * @return true si el puerto es válido
     */
    private boolean esPuertoValido(int puerto) {
        return puerto >= PUERTO_MINIMO && puerto <= PUERTO_MAXIMO;
    }

    /**
     * Obtiene el protocolo de comunicación.
     *
     * @return Protocolo utilizado
     */
    public String getProtocolo() {
        return protocolo;
    }

    /**
     * Establece el protocolo de comunicación.
     *
     * @param protocolo Nuevo protocolo (TCP, UDP, HTTP, HTTPS)
     * @throws IllegalArgumentException si el protocolo no es válido
     */
    public void setProtocolo(String protocolo) {
        if (!esProtocoloValido(protocolo)) {
            throw new IllegalArgumentException(
                "Protocolo no válido: " + protocolo +
                ". Protocolos válidos: TCP, UDP, HTTP, HTTPS");
        }
        this.protocolo = protocolo.toUpperCase();
    }

    /**
     * Obtiene el puerto de origen.
     *
     * @return Puerto de origen
     */
    public int getPuertoOrigen() {
        return puertoOrigen;
    }

    /**
     * Establece el puerto de origen.
     *
     * @param puerto Nuevo puerto de origen (1-65535)
     * @throws IllegalArgumentException si el puerto no es válido
     */
    public void setPuertoOrigen(int puerto) {
        if (!esPuertoValido(puerto)) {
            throw new IllegalArgumentException(
                String.format("Puerto de origen debe estar entre %d y %d. Valor recibido: %d",
                            PUERTO_MINIMO, PUERTO_MAXIMO, puerto));
        }
        this.puertoOrigen = puerto;
    }

    /**
     * Obtiene el puerto de destino.
     *
     * @return Puerto de destino
     */
    public int getPuertoDestino() {
        return puertoDestino;
    }

    /**
     * Establece el puerto de destino.
     *
     * @param puerto Nuevo puerto de destino (1-65535)
     * @throws IllegalArgumentException si el puerto no es válido
     */
    public void setPuertoDestino(int puerto) {
        if (!esPuertoValido(puerto)) {
            throw new IllegalArgumentException(
                String.format("Puerto de destino debe estar entre %d y %d. Valor recibido: %d",
                            PUERTO_MINIMO, PUERTO_MAXIMO, puerto));
        }
        this.puertoDestino = puerto;
    }

    /**
     * Obtiene el número de paquetes enviados.
     *
     * @return Número de paquetes enviados
     */
    public int getPaquetesEnviados() {
        return paquetesEnviados;
    }

    /**
     * Representación en cadena del proceso de red.
     *
     * @return String con información detallada del proceso
     */
    @Override
    public String toString() {
        return String.format("ProcesoRed [PID=%d, Nombre=%s, Estado=%s, Prioridad=%d, " +
                           "Protocolo=%s, Puerto %d→%d, Paquetes=%d]",
                           pid, nombre, estado, prioridad, protocolo,
                           puertoOrigen, puertoDestino, paquetesEnviados);
    }
}