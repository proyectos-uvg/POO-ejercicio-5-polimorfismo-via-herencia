package modelo;

import java.util.Random;

/**
 * Clase que representa un proceso de gestión de memoria.
 *
 * Este tipo de proceso se encarga de administrar y optimizar el uso de la memoria
 * del sistema, incluyendo la asignación, liberación y desfragmentación de memoria.
 * Ejemplos reales incluyen garbage collectors, memory cachers y swap managers.
 *
 * @author Sistema de Simulación de Procesos
 * @version 1.0
 */
public class ProcesoMemoria extends Proceso {

    /** Tipos de memoria válidos */
    private static final String TIPO_RAM = "RAM";
    private static final String TIPO_VIRTUAL = "VIRTUAL";

    /** Prioridad típica para procesos de memoria */
    private static final int PRIORIDAD_MEMORIA = 8;

    /** Rangos para fragmentación (porcentaje) */
    private static final double FRAGMENTACION_MINIMA = 0.0;
    private static final double FRAGMENTACION_MAXIMA = 15.0;

    /** Generador de números aleatorios */
    private static final Random random = new Random();

    /** Cantidad de memoria asignada en MB */
    private long memoriaAsignada;

    /** Tipo de memoria gestionada */
    private String tipoMemoria;

    /** Porcentaje de fragmentación de la memoria */
    private double fragmentacion;

    /**
     * Constructor para crear un proceso de gestión de memoria.
     *
     * @param pid Identificador único del proceso
     * @param nombre Nombre descriptivo del proceso
     * @param memoria Cantidad de memoria en MB
     * @param tipo Tipo de memoria (RAM, VIRTUAL)
     * @throws IllegalArgumentException si los parámetros no son válidos
     */
    public ProcesoMemoria(int pid, String nombre, long memoria, String tipo) {
        super(pid, nombre);

        if (memoria <= 0) {
            throw new IllegalArgumentException(
                "La cantidad de memoria debe ser mayor a 0. Valor recibido: " + memoria);
        }

        if (!esTipoMemoriaValido(tipo)) {
            throw new IllegalArgumentException(
                "Tipo de memoria no válido: " + tipo + ". Tipos válidos: RAM, VIRTUAL");
        }

        this.memoriaAsignada = memoria;
        this.tipoMemoria = tipo.toUpperCase();
        this.fragmentacion = generarFragmentacionAleatoria();
        this.prioridad = PRIORIDAD_MEMORIA;
    }

    /**
     * Ejecuta el proceso de gestión de memoria.
     *
     * @return String con el resultado de la ejecución
     */
    @Override
    public String ejecutar() {
        setEstado(EstadoProceso.EJECUTANDO);

        simularTiempo();

        realizarGestionMemoria();

        String detalle = String.format("Gestionando %d MB de memoria %s | Fragmentación: %.2f%%",
                                     memoriaAsignada, tipoMemoria, fragmentacion);

        setEstado(EstadoProceso.TERMINADO);

        return generarMensajeEjecucion(detalle);
    }

    /**
     * Simula las operaciones de gestión de memoria.
     */
    private void realizarGestionMemoria() {
        this.fragmentacion = Math.max(0.0, this.fragmentacion - random.nextDouble() * 2.0);
    }

    /**
     * Genera un valor aleatorio de fragmentación inicial.
     *
     * @return Porcentaje de fragmentación entre 0.0 y 15.0
     */
    private double generarFragmentacionAleatoria() {
        return FRAGMENTACION_MINIMA +
               (random.nextDouble() * (FRAGMENTACION_MAXIMA - FRAGMENTACION_MINIMA));
    }

    /**
     * Verifica si el tipo de memoria especificado es válido.
     *
     * @param tipo Tipo de memoria a validar
     * @return true si el tipo es válido
     */
    private boolean esTipoMemoriaValido(String tipo) {
        if (tipo == null) {
            return false;
        }
        String tipoUpper = tipo.toUpperCase();
        return TIPO_RAM.equals(tipoUpper) || TIPO_VIRTUAL.equals(tipoUpper);
    }

    /**
     * Obtiene la cantidad de memoria asignada.
     *
     * @return Memoria asignada en MB
     */
    public long getMemoriaAsignada() {
        return memoriaAsignada;
    }

    /**
     * Establece la cantidad de memoria asignada.
     *
     * @param memoria Nueva cantidad de memoria en MB
     * @throws IllegalArgumentException si la cantidad no es válida
     */
    public void setMemoriaAsignada(long memoria) {
        if (memoria <= 0) {
            throw new IllegalArgumentException(
                "La cantidad de memoria debe ser mayor a 0. Valor recibido: " + memoria);
        }
        this.memoriaAsignada = memoria;
    }

    /**
     * Obtiene el tipo de memoria.
     *
     * @return Tipo de memoria
     */
    public String getTipoMemoria() {
        return tipoMemoria;
    }

    /**
     * Establece el tipo de memoria.
     *
     * @param tipo Nuevo tipo de memoria (RAM, VIRTUAL)
     * @throws IllegalArgumentException si el tipo no es válido
     */
    public void setTipoMemoria(String tipo) {
        if (!esTipoMemoriaValido(tipo)) {
            throw new IllegalArgumentException(
                "Tipo de memoria no válido: " + tipo + ". Tipos válidos: RAM, VIRTUAL");
        }
        this.tipoMemoria = tipo.toUpperCase();
    }

    /**
     * Obtiene el porcentaje de fragmentación.
     *
     * @return Fragmentación como porcentaje
     */
    public double getFragmentacion() {
        return fragmentacion;
    }

    /**
     * Establece el porcentaje de fragmentación.
     *
     * @param fragmentacion Nuevo porcentaje de fragmentación (0.0-100.0)
     * @throws IllegalArgumentException si el porcentaje no es válido
     */
    public void setFragmentacion(double fragmentacion) {
        if (fragmentacion < 0.0 || fragmentacion > 100.0) {
            throw new IllegalArgumentException(
                "La fragmentación debe estar entre 0.0 y 100.0. Valor recibido: " + fragmentacion);
        }
        this.fragmentacion = fragmentacion;
    }

    /**
     * Representación en cadena del proceso de memoria.
     *
     * @return String con información detallada del proceso
     */
    @Override
    public String toString() {
        return String.format("ProcesoMemoria [PID=%d, Nombre=%s, Estado=%s, Prioridad=%d, " +
                           "Memoria=%d MB, Tipo=%s, Fragmentación=%.2f%%]",
                           pid, nombre, estado, prioridad, memoriaAsignada,
                           tipoMemoria, fragmentacion);
    }
}