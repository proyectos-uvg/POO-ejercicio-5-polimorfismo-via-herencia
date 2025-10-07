package modelo;

/**
 * Enumeración que define los posibles estados de un proceso en el sistema operativo.
 *
 * Representa el ciclo de vida completo de un proceso desde su creación hasta su terminación,
 * siguiendo el modelo estándar de estados de procesos en sistemas operativos.
 *
 * @author Sistema de Simulación de Procesos
 * @version 1.0
 */
public enum EstadoProceso {
    /**
     * Estado inicial cuando el proceso es creado pero aún no ha sido admitido
     * en el sistema para su ejecución.
     */
    NUEVO,

    /**
     * El proceso está preparado para ejecutarse y esperando ser asignado
     * al procesador por el planificador.
     */
    LISTO,

    /**
     * El proceso está actualmente siendo ejecutado por el procesador.
     */
    EJECUTANDO,

    /**
     * El proceso está temporalmente suspendido, típicamente esperando
     * por una operación de entrada/salida o algún evento externo.
     */
    BLOQUEADO,

    /**
     * El proceso ha completado su ejecución y todos sus recursos
     * han sido liberados del sistema.
     */
    TERMINADO
}