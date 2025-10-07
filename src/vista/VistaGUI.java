package vista;

import controlador.Controlador;
import modelo.Proceso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Implementación de vista gráfica para el simulador de procesos.
 *
 * Esta clase proporciona una interfaz gráfica completa usando Swing para
 * interactuar con el simulador de procesos. Incluye tabla de procesos,
 * botones para todas las operaciones y área de salida para resultados.
 *
 * @author Sistema de Simulación de Procesos
 * @version 1.0
 */
public class VistaGUI implements IVista {

    // Constantes de diseño
    private static final Color FONDO_PRINCIPAL = new Color(245, 245, 245);
    private static final Color FONDO_PANEL = new Color(255, 255, 255);
    private static final Color BORDE = new Color(200, 200, 200);
    private static final Color ACENTO = new Color(33, 150, 243);
    private static final Color EXITO = new Color(76, 175, 80);
    private static final Color ERROR = new Color(244, 67, 54);
    private static final Color TEXTO = new Color(33, 33, 33);

    private static final Font TITULO = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font MONO = new Font("Consolas", Font.PLAIN, 11);

    // Componentes principales
    private JFrame frame;
    private JPanel panelPrincipal;
    private JTable tablaProcesos;
    private DefaultTableModel modeloTabla;
    private JTextArea areaSalida;
    private JScrollPane scrollPane;
    private JScrollPane scrollSalida;
    private Controlador controlador;

    // Botones de registro
    private JButton btnRegistrarCPU;
    private JButton btnRegistrarIO;
    private JButton btnRegistrarDaemon;
    private JButton btnRegistrarRed;
    private JButton btnRegistrarMemoria;
    private JButton btnRegistrarBatch;
    private JButton btnRegistrarTiempoReal;

    // Botones de acción
    private JButton btnEjecutarTodos;
    private JButton btnEjecutarSeleccionado;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnEstadisticas;

    /**
     * Constructor de la vista GUI.
     */
    public VistaGUI() {
        construirInterfaz();
    }

    /**
     * Establece la referencia al controlador y configura eventos.
     *
     * @param controlador Controlador del sistema
     */
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
        configurarEventos();
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarError(String error) {
        JOptionPane.showMessageDialog(frame, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void mostrarProcesos(List<Proceso> procesos) {
        modeloTabla.setRowCount(0);
        for (Proceso proceso : procesos) {
            Object[] fila = {
                proceso.getPid(),
                proceso.getNombre(),
                proceso.getClass().getSimpleName(),
                proceso.getEstado(),
                proceso.getPrioridad()
            };
            modeloTabla.addRow(fila);
        }
    }

    @Override
    public void mostrarResultadoEjecucion(String resultado) {
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
        String timestamp = formato.format(new Date());

        areaSalida.append("[" + timestamp + "] " + resultado + "\n");
        areaSalida.setCaretPosition(areaSalida.getDocument().getLength());
    }

    @Override
    public void iniciar() {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }

    /**
     * Construye la interfaz gráfica completa.
     */
    private void construirInterfaz() {
        // Configurar frame principal
        frame = new JFrame("🖥️ Simulador de Procesos del Sistema Operativo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(FONDO_PRINCIPAL);

        // Crear componentes
        crearPanelSuperior();
        crearPanelCentral();
        crearPanelDerecho();
        crearPanelInferior();

        // Configurar estilos
        aplicarEstilos();
    }

    /**
     * Crea el panel superior con botones de registro.
     */
    private void crearPanelSuperior() {
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelSuperior.setBackground(FONDO_PANEL);
        panelSuperior.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDE), "Registrar Procesos"));

        // Crear botones de registro
        btnRegistrarCPU = crearBoton("⚙️ CPU", ACENTO);
        btnRegistrarIO = crearBoton("💾 I/O", ACENTO);
        btnRegistrarDaemon = crearBoton("🔄 Daemon", ACENTO);
        btnRegistrarRed = crearBoton("🌐 Red", ACENTO);
        btnRegistrarMemoria = crearBoton("🧠 Memoria", ACENTO);
        btnRegistrarBatch = crearBoton("📋 Batch", ACENTO);
        btnRegistrarTiempoReal = crearBoton("⏱️ T.Real", ACENTO);

        panelSuperior.add(btnRegistrarCPU);
        panelSuperior.add(btnRegistrarIO);
        panelSuperior.add(btnRegistrarDaemon);
        panelSuperior.add(btnRegistrarRed);
        panelSuperior.add(btnRegistrarMemoria);
        panelSuperior.add(btnRegistrarBatch);
        panelSuperior.add(btnRegistrarTiempoReal);

        frame.add(panelSuperior, BorderLayout.NORTH);
    }

    /**
     * Crea el panel central con la tabla de procesos.
     */
    private void crearPanelCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(FONDO_PANEL);
        panelCentral.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDE), "Procesos del Sistema"));

        configurarTabla();
        scrollPane = new JScrollPane(tablaProcesos);
        scrollPane.setPreferredSize(new Dimension(700, 300));

        panelCentral.add(scrollPane, BorderLayout.CENTER);
        frame.add(panelCentral, BorderLayout.CENTER);
    }

    /**
     * Crea el panel derecho con botones de acción.
     */
    private void crearPanelDerecho() {
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBackground(FONDO_PANEL);
        panelDerecho.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDE), "Acciones"));
        panelDerecho.setPreferredSize(new Dimension(150, 0));

        // Crear botones de acción
        btnEjecutarTodos = crearBoton("▶️ Ejecutar Todos", EXITO);
        btnEjecutarSeleccionado = crearBoton("▶️ Ejecutar Sel.", EXITO);
        btnEliminar = crearBoton("🗑️ Eliminar", ERROR);
        btnLimpiar = crearBoton("🧹 Limpiar Todo", ERROR);
        btnEstadisticas = crearBoton("📊 Estadísticas", ACENTO);

        // Configurar dimensiones uniformes
        Dimension btnSize = new Dimension(130, 35);
        btnEjecutarTodos.setPreferredSize(btnSize);
        btnEjecutarSeleccionado.setPreferredSize(btnSize);
        btnEliminar.setPreferredSize(btnSize);
        btnLimpiar.setPreferredSize(btnSize);
        btnEstadisticas.setPreferredSize(btnSize);

        panelDerecho.add(Box.createVerticalStrut(10));
        panelDerecho.add(btnEjecutarTodos);
        panelDerecho.add(Box.createVerticalStrut(5));
        panelDerecho.add(btnEjecutarSeleccionado);
        panelDerecho.add(Box.createVerticalStrut(15));
        panelDerecho.add(btnEliminar);
        panelDerecho.add(Box.createVerticalStrut(5));
        panelDerecho.add(btnLimpiar);
        panelDerecho.add(Box.createVerticalStrut(15));
        panelDerecho.add(btnEstadisticas);
        panelDerecho.add(Box.createVerticalGlue());

        frame.add(panelDerecho, BorderLayout.EAST);
    }

    /**
     * Crea el panel inferior con área de salida.
     */
    private void crearPanelInferior() {
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(FONDO_PANEL);
        panelInferior.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDE), "Salida del Sistema"));
        panelInferior.setPreferredSize(new Dimension(0, 180));

        areaSalida = new JTextArea();
        areaSalida.setEditable(false);
        areaSalida.setFont(MONO);
        areaSalida.setBackground(new Color(248, 248, 248));
        areaSalida.setForeground(TEXTO);
        areaSalida.setText("Sistema iniciado. Registre procesos para comenzar.\n");

        scrollSalida = new JScrollPane(areaSalida);
        scrollSalida.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panelInferior.add(scrollSalida, BorderLayout.CENTER);
        frame.add(panelInferior, BorderLayout.SOUTH);
    }

    /**
     * Configura la tabla de procesos.
     */
    private void configurarTabla() {
        String[] columnas = {"PID", "Nombre", "Tipo", "Estado", "Prioridad"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaProcesos = new JTable(modeloTabla);
        tablaProcesos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProcesos.setFont(NORMAL);
        tablaProcesos.setRowHeight(25);
        tablaProcesos.getTableHeader().setFont(TITULO);

        // Configurar anchos de columna
        tablaProcesos.getColumnModel().getColumn(0).setPreferredWidth(60);  // PID
        tablaProcesos.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        tablaProcesos.getColumnModel().getColumn(2).setPreferredWidth(120); // Tipo
        tablaProcesos.getColumnModel().getColumn(3).setPreferredWidth(100); // Estado
        tablaProcesos.getColumnModel().getColumn(4).setPreferredWidth(80);  // Prioridad
    }

    /**
     * Crea un botón estilizado.
     *
     * @param texto Texto del botón
     * @param color Color de fondo
     * @return Botón configurado
     */
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(NORMAL);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(120, 35));
        return boton;
    }

    /**
     * Aplica estilos generales a la interfaz.
     */
    private void aplicarEstilos() {
        // Configurar estilos básicos de la interfaz
        // No es necesario cambiar el look and feel por defecto
    }

    /**
     * Configura los eventos de los botones.
     */
    private void configurarEventos() {
        if (controlador == null) return;

        btnRegistrarCPU.addActionListener(e -> mostrarDialogoRegistroCPU());
        btnRegistrarIO.addActionListener(e -> mostrarDialogoRegistroIO());
        btnRegistrarDaemon.addActionListener(e -> mostrarDialogoRegistroDaemon());
        btnRegistrarRed.addActionListener(e -> mostrarDialogoRegistroRed());
        btnRegistrarMemoria.addActionListener(e -> mostrarDialogoRegistroMemoria());
        btnRegistrarBatch.addActionListener(e -> mostrarDialogoRegistroBatch());
        btnRegistrarTiempoReal.addActionListener(e -> mostrarDialogoRegistroTiempoReal());

        btnEjecutarTodos.addActionListener(e -> controlador.ejecutarTodosLosProcesos());
        btnEjecutarSeleccionado.addActionListener(e -> ejecutarProcesoSeleccionado());
        btnEliminar.addActionListener(e -> eliminarProcesoSeleccionado());
        btnLimpiar.addActionListener(e -> controlador.limpiarProcesos());
        btnEstadisticas.addActionListener(e -> controlador.mostrarEstadisticas());
    }

    // Métodos para diálogos de registro

    private void mostrarDialogoRegistroCPU() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        JTextField txtNombre = new JTextField();
        JTextField txtOperaciones = new JTextField();
        JComboBox<Integer> cmbNucleos = new JComboBox<>();
        for (int i = 1; i <= 16; i++) {
            cmbNucleos.addItem(i);
        }

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Operaciones:"));
        panel.add(txtOperaciones);
        panel.add(new JLabel("Núcleos:"));
        panel.add(cmbNucleos);

        int resultado = JOptionPane.showConfirmDialog(frame, panel,
            "Registrar Proceso CPU", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                String nombre = txtNombre.getText();
                int operaciones = Integer.parseInt(txtOperaciones.getText());
                int nucleos = (Integer) cmbNucleos.getSelectedItem();

                controlador.registrarProcesoCPU(nombre, operaciones, nucleos);
            } catch (NumberFormatException e) {
                mostrarError("Por favor ingrese valores numéricos válidos.");
            }
        }
    }

    private void mostrarDialogoRegistroIO() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        JTextField txtNombre = new JTextField();
        JComboBox<String> cmbDispositivo = new JComboBox<>(
            new String[]{"TECLADO", "DISCO", "RED"});
        JTextField txtBytes = new JTextField();

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Dispositivo:"));
        panel.add(cmbDispositivo);
        panel.add(new JLabel("Bytes:"));
        panel.add(txtBytes);

        int resultado = JOptionPane.showConfirmDialog(frame, panel,
            "Registrar Proceso I/O", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                String nombre = txtNombre.getText();
                String dispositivo = (String) cmbDispositivo.getSelectedItem();
                long bytes = Long.parseLong(txtBytes.getText());

                controlador.registrarProcesoIO(nombre, dispositivo, bytes);
            } catch (NumberFormatException e) {
                mostrarError("Por favor ingrese un valor numérico válido para bytes.");
            }
        }
    }

    private void mostrarDialogoRegistroDaemon() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        JTextField txtNombre = new JTextField();
        JTextField txtServicio = new JTextField();
        JCheckBox chkAutoiniciable = new JCheckBox();

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Servicio:"));
        panel.add(txtServicio);
        panel.add(new JLabel("Autoiniciable:"));
        panel.add(chkAutoiniciable);

        int resultado = JOptionPane.showConfirmDialog(frame, panel,
            "Registrar Daemon", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText();
            String servicio = txtServicio.getText();
            boolean autoiniciable = chkAutoiniciable.isSelected();

            controlador.registrarDaemon(nombre, servicio, autoiniciable);
        }
    }

    private void mostrarDialogoRegistroRed() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField txtNombre = new JTextField();
        JComboBox<String> cmbProtocolo = new JComboBox<>(
            new String[]{"TCP", "UDP", "HTTP", "HTTPS"});
        JTextField txtPuertoOrigen = new JTextField();
        JTextField txtPuertoDestino = new JTextField();

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Protocolo:"));
        panel.add(cmbProtocolo);
        panel.add(new JLabel("Puerto Origen:"));
        panel.add(txtPuertoOrigen);
        panel.add(new JLabel("Puerto Destino:"));
        panel.add(txtPuertoDestino);

        int resultado = JOptionPane.showConfirmDialog(frame, panel,
            "Registrar Proceso Red", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                String nombre = txtNombre.getText();
                String protocolo = (String) cmbProtocolo.getSelectedItem();
                int puertoOrigen = Integer.parseInt(txtPuertoOrigen.getText());
                int puertoDestino = Integer.parseInt(txtPuertoDestino.getText());

                controlador.registrarProcesoRed(nombre, protocolo, puertoOrigen, puertoDestino);
            } catch (NumberFormatException e) {
                mostrarError("Por favor ingrese valores numéricos válidos para los puertos.");
            }
        }
    }

    private void mostrarDialogoRegistroMemoria() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        JTextField txtNombre = new JTextField();
        JTextField txtMemoria = new JTextField();
        JComboBox<String> cmbTipo = new JComboBox<>(
            new String[]{"RAM", "VIRTUAL"});

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Memoria (MB):"));
        panel.add(txtMemoria);
        panel.add(new JLabel("Tipo:"));
        panel.add(cmbTipo);

        int resultado = JOptionPane.showConfirmDialog(frame, panel,
            "Registrar Proceso Memoria", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                String nombre = txtNombre.getText();
                long memoria = Long.parseLong(txtMemoria.getText());
                String tipo = (String) cmbTipo.getSelectedItem();

                controlador.registrarProcesoMemoria(nombre, memoria, tipo);
            } catch (NumberFormatException e) {
                mostrarError("Por favor ingrese un valor numérico válido para la memoria.");
            }
        }
    }

    private void mostrarDialogoRegistroBatch() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextField txtNombre = new JTextField();
        JTextField txtArchivo = new JTextField();
        JTextArea txtTareas = new JTextArea(5, 20);
        txtTareas.setToolTipText("Ingrese una tarea por línea");

        JPanel panelSuperior = new JPanel(new GridLayout(2, 2, 5, 5));
        panelSuperior.add(new JLabel("Nombre:"));
        panelSuperior.add(txtNombre);
        panelSuperior.add(new JLabel("Archivo Script:"));
        panelSuperior.add(txtArchivo);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(new JLabel("Tareas (una por línea):"), BorderLayout.CENTER);
        panel.add(new JScrollPane(txtTareas), BorderLayout.SOUTH);

        int resultado = JOptionPane.showConfirmDialog(frame, panel,
            "Registrar Proceso Batch", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText();
            String archivo = txtArchivo.getText();
            String[] lineasTareas = txtTareas.getText().split("\n");
            List<String> tareas = new ArrayList<>();

            for (String linea : lineasTareas) {
                if (linea.trim().length() > 0) {
                    tareas.add(linea.trim());
                }
            }

            if (!tareas.isEmpty()) {
                controlador.registrarProcesoBatch(nombre, tareas, archivo);
            } else {
                mostrarError("Debe ingresar al menos una tarea.");
            }
        }
    }

    private void mostrarDialogoRegistroTiempoReal() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        JTextField txtNombre = new JTextField();
        JTextField txtDeadline = new JTextField();
        JCheckBox chkCritico = new JCheckBox();

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Deadline (ms):"));
        panel.add(txtDeadline);
        panel.add(new JLabel("Es Crítico:"));
        panel.add(chkCritico);

        int resultado = JOptionPane.showConfirmDialog(frame, panel,
            "Registrar Proceso Tiempo Real", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                String nombre = txtNombre.getText();
                int deadline = Integer.parseInt(txtDeadline.getText());
                boolean critico = chkCritico.isSelected();

                controlador.registrarProcesoTiempoReal(nombre, deadline, critico);
            } catch (NumberFormatException e) {
                mostrarError("Por favor ingrese un valor numérico válido para el deadline.");
            }
        }
    }

    /**
     * Ejecuta el proceso seleccionado en la tabla.
     */
    private void ejecutarProcesoSeleccionado() {
        int pid = obtenerProcesoSeleccionado();
        if (pid != -1) {
            controlador.ejecutarProcesoPorPID(pid);
        } else {
            mostrarError("Seleccione un proceso de la tabla para ejecutar.");
        }
    }

    /**
     * Elimina el proceso seleccionado en la tabla.
     */
    private void eliminarProcesoSeleccionado() {
        int pid = obtenerProcesoSeleccionado();
        if (pid != -1) {
            controlador.eliminarProceso(pid);
        } else {
            mostrarError("Seleccione un proceso de la tabla para eliminar.");
        }
    }

    /**
     * Obtiene el PID del proceso seleccionado en la tabla.
     *
     * @return PID del proceso seleccionado o -1 si no hay selección
     */
    private int obtenerProcesoSeleccionado() {
        int filaSeleccionada = tablaProcesos.getSelectedRow();
        if (filaSeleccionada >= 0) {
            return (Integer) tablaProcesos.getValueAt(filaSeleccionada, 0);
        }
        return -1;
    }
}