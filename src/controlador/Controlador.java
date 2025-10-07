package controlador;

import modelo.*;
import vista.IVista;

import javax.swing.JOptionPane;
import java.util.List;
import java.util.Map;

/**
 * Controlador principal del simulador de procesos.
 *
 * Esta clase implementa el patrón MVC actuando como intermediario entre
 * el modelo (Planificador) y las vistas (consola/GUI). Se encarga de
 * coordinar las acciones del usuario, validar datos y manejar errores.
 *
 * @author Sistema de Simulación de Procesos
 * @version 1.0
 */
public class Controlador {

    /** Constantes para validación */
    private static final int LONGITUD_MAX_NOMBRE = 50;

    /** Referencia al modelo del sistema */
    private Planificador modelo;

    /** Referencia a la vista activa */
    private IVista vista;

    /**
     * Constructor del controlador.
     *
     * @param modelo Planificador del sistema
     * @param vista Vista a utilizar
     * @throws IllegalArgumentException si algún parámetro es null
     */
    public Controlador(Planificador modelo, IVista vista) {
        if (modelo == null) {
            throw new IllegalArgumentException("El modelo no puede ser null");
        }
        if (vista == null) {
            throw new IllegalArgumentException("La vista no puede ser null");
        }

        this.modelo = modelo;
        this.vista = vista;
    }

    /**
     * Registra un nuevo proceso CPU en el sistema.
     *
     * @param nombre Nombre del proceso
     * @param operaciones Número de operaciones matemáticas
     * @param nucleos Número de núcleos a utilizar
     */
    public void registrarProcesoCPU(String nombre, int operaciones, int nucleos) {
        try {
            validarNombreProceso(nombre);
            int pid = Planificador.getProximoPID();
            ProcesoCPU proceso = new ProcesoCPU(pid, nombre, operaciones, nucleos);
            boolean exito = modelo.registrarProceso(proceso);

            if (exito) {
                vista.mostrarMensaje("✓ Proceso CPU registrado: " + nombre + " (PID: " + pid + ")");
                actualizarVistaProcesos();
            }
        } catch (Exception e) {
            manejarError(e);
        }
    }

    /**
     * Registra un nuevo proceso I/O en el sistema.
     *
     * @param nombre Nombre del proceso
     * @param dispositivo Dispositivo de I/O
     * @param bytes Bytes a transferir
     */
    public void registrarProcesoIO(String nombre, String dispositivo, long bytes) {
        try {
            validarNombreProceso(nombre);
            int pid = Planificador.getProximoPID();
            ProcesoIO proceso = new ProcesoIO(pid, nombre, dispositivo, bytes);
            boolean exito = modelo.registrarProceso(proceso);

            if (exito) {
                vista.mostrarMensaje("✓ Proceso I/O registrado: " + nombre + " (PID: " + pid + ")");
                actualizarVistaProcesos();
            }
        } catch (Exception e) {
            manejarError(e);
        }
    }

    /**
     * Registra un nuevo daemon en el sistema.
     *
     * @param nombre Nombre del proceso
     * @param servicio Nombre del servicio
     * @param autoiniciable Si es autoiniciable
     */
    public void registrarDaemon(String nombre, String servicio, boolean autoiniciable) {
        try {
            validarNombreProceso(nombre);
            int pid = Planificador.getProximoPID();
            Daemon proceso = new Daemon(pid, nombre, servicio, autoiniciable);
            boolean exito = modelo.registrarProceso(proceso);

            if (exito) {
                vista.mostrarMensaje("✓ Daemon registrado: " + nombre + " (PID: " + pid + ")");
                actualizarVistaProcesos();
            }
        } catch (Exception e) {
            manejarError(e);
        }
    }

    /**
     * Registra un nuevo proceso de red en el sistema.
     *
     * @param nombre Nombre del proceso
     * @param protocolo Protocolo de red
     * @param pOrigen Puerto de origen
     * @param pDestino Puerto de destino
     */
    public void registrarProcesoRed(String nombre, String protocolo, int pOrigen, int pDestino) {
        try {
            validarNombreProceso(nombre);
            int pid = Planificador.getProximoPID();
            ProcesoRed proceso = new ProcesoRed(pid, nombre, protocolo, pOrigen, pDestino);
            boolean exito = modelo.registrarProceso(proceso);

            if (exito) {
                vista.mostrarMensaje("✓ Proceso Red registrado: " + nombre + " (PID: " + pid + ")");
                actualizarVistaProcesos();
            }
        } catch (Exception e) {
            manejarError(e);
        }
    }

    /**
     * Registra un nuevo proceso de memoria en el sistema.
     *
     * @param nombre Nombre del proceso
     * @param memoria Memoria en MB
     * @param tipo Tipo de memoria
     */
    public void registrarProcesoMemoria(String nombre, long memoria, String tipo) {
        try {
            validarNombreProceso(nombre);
            int pid = Planificador.getProximoPID();
            ProcesoMemoria proceso = new ProcesoMemoria(pid, nombre, memoria, tipo);
            boolean exito = modelo.registrarProceso(proceso);

            if (exito) {
                vista.mostrarMensaje("✓ Proceso Memoria registrado: " + nombre + " (PID: " + pid + ")");
                actualizarVistaProcesos();
            }
        } catch (Exception e) {
            manejarError(e);
        }
    }

    /**
     * Registra un nuevo proceso batch en el sistema.
     *
     * @param nombre Nombre del proceso
     * @param tareas Lista de tareas
     * @param archivo Archivo de script
     */
    public void registrarProcesoBatch(String nombre, List<String> tareas, String archivo) {
        try {
            validarNombreProceso(nombre);
            int pid = Planificador.getProximoPID();
            ProcesoBatch proceso = new ProcesoBatch(pid, nombre, tareas, archivo);
            boolean exito = modelo.registrarProceso(proceso);

            if (exito) {
                vista.mostrarMensaje("✓ Proceso Batch registrado: " + nombre + " (PID: " + pid + ")");
                actualizarVistaProcesos();
            }
        } catch (Exception e) {
            manejarError(e);
        }
    }

    /**
     * Registra un nuevo proceso de tiempo real en el sistema.
     *
     * @param nombre Nombre del proceso
     * @param deadline Deadline en milisegundos
     * @param critico Si es crítico
     */
    public void registrarProcesoTiempoReal(String nombre, int deadline, boolean critico) {
        try {
            validarNombreProceso(nombre);
            int pid = Planificador.getProximoPID();
            ProcesoTiempoReal proceso = new ProcesoTiempoReal(pid, nombre, deadline, critico);
            boolean exito = modelo.registrarProceso(proceso);

            if (exito) {
                vista.mostrarMensaje("✓ Proceso Tiempo Real registrado: " + nombre + " (PID: " + pid + ")");
                actualizarVistaProcesos();
            }
        } catch (Exception e) {
            manejarError(e);
        }
    }

    /**
     * Ejecuta todos los procesos registrados en el sistema.
     */
    public void ejecutarTodosLosProcesos() {
        try {
            if (modelo.getCantidadProcesos() == 0) {
                vista.mostrarError("No hay procesos registrados para ejecutar");
                return;
            }

            vista.mostrarMensaje("Ejecutando todos los procesos del sistema...\n");

            List<String> resultados = modelo.ejecutarTodos();
            for (String resultado : resultados) {
                vista.mostrarResultadoEjecucion(resultado);
                Thread.sleep(200);
            }

            actualizarVistaProcesos();
            vista.mostrarMensaje("\n✓ Todos los procesos ejecutados exitosamente");

        } catch (Exception e) {
            manejarError(e);
        }
    }

    /**
     * Ejecuta un proceso específico por su PID.
     *
     * @param pid PID del proceso a ejecutar
     */
    public void ejecutarProcesoPorPID(int pid) {
        try {
            Proceso proceso = modelo.obtenerProceso(pid);
            if (proceso == null) {
                vista.mostrarError("Proceso con PID " + pid + " no encontrado");
                return;
            }

            vista.mostrarMensaje("Ejecutando proceso " + pid + "...");
            String resultado = modelo.ejecutarPorPID(pid);
            vista.mostrarResultadoEjecucion(resultado);
            actualizarVistaProcesos();
            vista.mostrarMensaje("✓ Proceso " + pid + " ejecutado exitosamente");

        } catch (Exception e) {
            manejarError(e);
        }
    }

    /**
     * Elimina un proceso del sistema.
     *
     * @param pid PID del proceso a eliminar
     */
    public void eliminarProceso(int pid) {
        try {
            boolean exito = modelo.eliminarProceso(pid);
            if (exito) {
                vista.mostrarMensaje("✓ Proceso " + pid + " eliminado exitosamente");
                actualizarVistaProcesos();
            } else {
                vista.mostrarError("No se pudo eliminar el proceso " + pid);
            }
        } catch (Exception e) {
            manejarError(e);
        }
    }

    /**
     * Limpia todos los procesos del sistema.
     */
    public void limpiarProcesos() {
        try {
            if (esInterfazGrafica()) {
                int confirmacion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Está seguro de eliminar todos los procesos?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirmacion != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            modelo.limpiarProcesos();
            actualizarVistaProcesos();
            vista.mostrarMensaje("✓ Todos los procesos eliminados");

        } catch (Exception e) {
            manejarError(e);
        }
    }

    /**
     * Actualiza la vista con la lista actual de procesos.
     */
    public void actualizarVistaProcesos() {
        List<Proceso> procesos = modelo.obtenerTodosProcesos();
        vista.mostrarProcesos(procesos);
    }

    /**
     * Muestra las estadísticas del sistema.
     */
    public void mostrarEstadisticas() {
        try {
            Map<String, Integer> stats = modelo.obtenerEstadisticas();
            StringBuilder sb = new StringBuilder();

            sb.append("=== ESTADÍSTICAS DEL SISTEMA ===\n\n");
            sb.append("Total de procesos: ").append(stats.get("Total")).append("\n\n");
            sb.append("Por tipo:\n");
            sb.append("  • Procesos CPU: ").append(stats.getOrDefault("ProcesoCPU", 0)).append("\n");
            sb.append("  • Procesos I/O: ").append(stats.getOrDefault("ProcesoIO", 0)).append("\n");
            sb.append("  • Daemons: ").append(stats.getOrDefault("Daemon", 0)).append("\n");
            sb.append("  • Procesos Red: ").append(stats.getOrDefault("ProcesoRed", 0)).append("\n");
            sb.append("  • Procesos Memoria: ").append(stats.getOrDefault("ProcesoMemoria", 0)).append("\n");
            sb.append("  • Procesos Batch: ").append(stats.getOrDefault("ProcesoBatch", 0)).append("\n");
            sb.append("  • Procesos Tiempo Real: ").append(stats.getOrDefault("ProcesoTiempoReal", 0)).append("\n");

            sb.append("\n").append(modelo.obtenerResumenSistema());

            vista.mostrarMensaje(sb.toString());

        } catch (Exception e) {
            manejarError(e);
        }
    }

    /**
     * Inicia el controlador mostrando mensaje de bienvenida.
     */
    public void iniciar() {
        vista.mostrarMensaje("=== SIMULADOR DE PROCESOS DEL SISTEMA OPERATIVO ===\n" +
                           "Sistema iniciado correctamente.\n" +
                           "Registre procesos y ejecute el planificador.");
        vista.iniciar();
    }

    /**
     * Maneja los errores del sistema mostrando información apropiada.
     *
     * @param e Excepción ocurrida
     */
    private void manejarError(Exception e) {
        e.printStackTrace();
        vista.mostrarError(e.getMessage());
    }

    /**
     * Valida que el nombre del proceso sea válido.
     *
     * @param nombre Nombre a validar
     * @throws IllegalArgumentException si el nombre no es válido
     */
    private void validarNombreProceso(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proceso no puede estar vacío");
        }
        if (nombre.length() > LONGITUD_MAX_NOMBRE) {
            throw new IllegalArgumentException(
                "El nombre es demasiado largo (máx " + LONGITUD_MAX_NOMBRE + " caracteres)");
        }
    }

    /**
     * Verifica si la vista actual es una interfaz gráfica.
     *
     * @return true si es GUI
     */
    private boolean esInterfazGrafica() {
        return vista.getClass().getSimpleName().contains("GUI");
    }

    /**
     * Obtiene el modelo asociado al controlador.
     *
     * @return Planificador del sistema
     */
    public Planificador getModelo() {
        return modelo;
    }

    /**
     * Obtiene la vista asociada al controlador.
     *
     * @return Vista activa
     */
    public IVista getVista() {
        return vista;
    }

    /**
     * Obtiene información del estado actual del controlador.
     *
     * @return String con información del controlador
     */
    public String getEstadoControlador() {
        return String.format("Controlador - Procesos: %d, Vista: %s",
                           modelo.getCantidadProcesos(),
                           vista.getClass().getSimpleName());
    }
}