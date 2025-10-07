package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Clase que gestiona el planificador de procesos del sistema operativo.
 *
 * El planificador es responsable de registrar, ejecutar y administrar todos los procesos
 * del sistema. Mantiene un registro de todos los procesos y proporciona funcionalidades
 * para su ejecución individual o grupal, así como estadísticas del sistema.
 *
 * @author Sistema de Simulación de Procesos
 * @version 1.0
 */
public class Planificador {

    /** Contador estático para generar PIDs únicos */
    private static int contadorPID = 1000;

    /** Lista de procesos registrados en el sistema */
    private List<Proceso> procesos;

    /** Historial de todas las ejecuciones realizadas */
    private List<String> historialEjecuciones;

    /**
     * Constructor del planificador.
     * Inicializa las estructuras de datos necesarias.
     */
    public Planificador() {
        this.procesos = new ArrayList<>();
        this.historialEjecuciones = new ArrayList<>();
    }

    /**
     * Registra un nuevo proceso en el planificador.
     *
     * @param proceso Proceso a registrar
     * @return true si el proceso se registró exitosamente
     * @throws IllegalArgumentException si el proceso es null o ya existe un proceso con ese PID
     */
    public boolean registrarProceso(Proceso proceso) {
        if (proceso == null) {
            throw new IllegalArgumentException("El proceso no puede ser null");
        }

        if (existeProceso(proceso.getPid())) {
            throw new IllegalArgumentException(
                "Ya existe un proceso con PID " + proceso.getPid());
        }

        return procesos.add(proceso);
    }

    /**
     * Elimina un proceso del planificador.
     *
     * @param pid PID del proceso a eliminar
     * @return true si el proceso se eliminó exitosamente
     */
    public boolean eliminarProceso(int pid) {
        return procesos.removeIf(proceso -> proceso.getPid() == pid);
    }

    /**
     * Ejecuta todos los procesos registrados en el planificador.
     *
     * @return Lista con los resultados de la ejecución de todos los procesos
     */
    public List<String> ejecutarTodos() {
        List<String> resultados = new ArrayList<>();

        for (Proceso proceso : procesos) {
            String resultado = proceso.ejecutar();
            resultados.add(resultado);
            historialEjecuciones.add(resultado);
        }

        return resultados;
    }

    /**
     * Ejecuta un proceso específico identificado por su PID.
     *
     * @param pid PID del proceso a ejecutar
     * @return String con el resultado de la ejecución o mensaje de error
     */
    public String ejecutarPorPID(int pid) {
        Proceso proceso = obtenerProceso(pid);

        if (proceso == null) {
            return "Proceso no encontrado con PID: " + pid;
        }

        String resultado = proceso.ejecutar();
        historialEjecuciones.add(resultado);
        return resultado;
    }

    /**
     * Busca y retorna un proceso por su PID.
     *
     * @param pid PID del proceso a buscar
     * @return Proceso encontrado o null si no existe
     */
    public Proceso obtenerProceso(int pid) {
        return procesos.stream()
                      .filter(proceso -> proceso.getPid() == pid)
                      .findFirst()
                      .orElse(null);
    }

    /**
     * Obtiene una copia defensiva de todos los procesos registrados.
     *
     * @return Lista con copia de todos los procesos
     */
    public List<Proceso> obtenerTodosProcesos() {
        return new ArrayList<>(procesos);
    }

    /**
     * Obtiene todos los procesos de un tipo específico.
     *
     * @param tipo Clase del tipo de proceso a filtrar
     * @return Lista filtrada de procesos del tipo especificado
     */
    @SuppressWarnings("unchecked")
    public <T extends Proceso> List<T> obtenerProcesosPorTipo(Class<T> tipo) {
        return procesos.stream()
                      .filter(tipo::isInstance)
                      .map(proceso -> (T) proceso)
                      .collect(Collectors.toList());
    }

    /**
     * Limpia todos los procesos del planificador.
     */
    public void limpiarProcesos() {
        procesos.clear();
    }

    /**
     * Verifica si existe un proceso con el PID especificado.
     *
     * @param pid PID a verificar
     * @return true si existe un proceso con ese PID
     */
    private boolean existeProceso(int pid) {
        return procesos.stream()
                      .anyMatch(proceso -> proceso.getPid() == pid);
    }

    /**
     * Genera el próximo PID disponible.
     *
     * @return Próximo PID único
     */
    public static int getProximoPID() {
        return contadorPID++;
    }

    /**
     * Obtiene el historial completo de ejecuciones.
     *
     * @return Copia defensiva del historial de ejecuciones
     */
    public List<String> getHistorialEjecuciones() {
        return new ArrayList<>(historialEjecuciones);
    }

    /**
     * Genera estadísticas detalladas del sistema.
     *
     * @return Map con estadísticas por tipo de proceso
     */
    public Map<String, Integer> obtenerEstadisticas() {
        Map<String, Integer> estadisticas = new HashMap<>();

        estadisticas.put("Total", procesos.size());
        estadisticas.put("ProcesoCPU", contarProcesosPorTipo(ProcesoCPU.class));
        estadisticas.put("ProcesoIO", contarProcesosPorTipo(ProcesoIO.class));
        estadisticas.put("Daemon", contarProcesosPorTipo(Daemon.class));
        estadisticas.put("ProcesoRed", contarProcesosPorTipo(ProcesoRed.class));
        estadisticas.put("ProcesoMemoria", contarProcesosPorTipo(ProcesoMemoria.class));
        estadisticas.put("ProcesoBatch", contarProcesosPorTipo(ProcesoBatch.class));
        estadisticas.put("ProcesoTiempoReal", contarProcesosPorTipo(ProcesoTiempoReal.class));

        return estadisticas;
    }

    /**
     * Cuenta el número de procesos de un tipo específico.
     *
     * @param tipo Clase del tipo de proceso a contar
     * @return Número de procesos del tipo especificado
     */
    private int contarProcesosPorTipo(Class<? extends Proceso> tipo) {
        return (int) procesos.stream()
                           .filter(tipo::isInstance)
                           .count();
    }

    /**
     * Obtiene la cantidad total de procesos registrados.
     *
     * @return Número total de procesos
     */
    public int getCantidadProcesos() {
        return procesos.size();
    }

    /**
     * Obtiene estadísticas de estados de procesos.
     *
     * @return Map con conteo de procesos por estado
     */
    public Map<EstadoProceso, Integer> obtenerEstadisticasEstados() {
        Map<EstadoProceso, Integer> estadisticasEstados = new HashMap<>();

        for (EstadoProceso estado : EstadoProceso.values()) {
            estadisticasEstados.put(estado, 0);
        }

        for (Proceso proceso : procesos) {
            EstadoProceso estado = proceso.getEstado();
            estadisticasEstados.put(estado, estadisticasEstados.get(estado) + 1);
        }

        return estadisticasEstados;
    }

    /**
     * Obtiene los procesos ordenados por prioridad (descendente).
     *
     * @return Lista de procesos ordenada por prioridad
     */
    public List<Proceso> obtenerProcesosPorPrioridad() {
        return procesos.stream()
                      .sorted((p1, p2) -> Integer.compare(p2.getPrioridad(), p1.getPrioridad()))
                      .collect(Collectors.toList());
    }

    /**
     * Limpia el historial de ejecuciones.
     */
    public void limpiarHistorial() {
        historialEjecuciones.clear();
    }

    /**
     * Obtiene información resumida del planificador.
     *
     * @return String con información del estado actual del planificador
     */
    public String obtenerResumenSistema() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("=== RESUMEN DEL SISTEMA ===\n");
        resumen.append("Procesos registrados: ").append(procesos.size()).append("\n");
        resumen.append("Ejecuciones realizadas: ").append(historialEjecuciones.size()).append("\n");
        resumen.append("Próximo PID disponible: ").append(contadorPID).append("\n");

        if (!procesos.isEmpty()) {
            double prioridadPromedio = procesos.stream()
                                             .mapToInt(Proceso::getPrioridad)
                                             .average()
                                             .orElse(0.0);
            resumen.append(String.format("Prioridad promedio: %.2f\n", prioridadPromedio));
        }

        return resumen.toString();
    }
}