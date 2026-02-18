package cr.ac.ulatinabccr.UI;

import cr.ac.ulatinabccr.controlador.ConsultaIndicadoresController;
import cr.ac.ulatinabccr.modelo.ResultadoConsulta;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del cliente BCCR.
 *
 * Responsabilidad:
 * - Recibir inputs del usuario (indicador, fechas, subniveles)
 * - Llamar al Controller (capa de negocio) para ejecutar la consulta
 * - Mostrar resultados formateados (NO muestra XML crudo)
 *
 * Nota importante:
 * La consulta al BCCR se ejecuta con SwingWorker para no congelar la interfaz.
 */
public class ClienteIndicadoresFrame extends JFrame {

    // ====== Dependencia (Controller) ======
    private final ConsultaIndicadoresController controller;

    // ====== Componentes UI ======
    private JComboBox<ItemIndicador> cboIndicador;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFinal;
    private JComboBox<String> cboSubniveles;

    private JButton btnConsultar;
    private JButton btnSalir;

    private JTextArea txtResultados;
    private JLabel lblEstado;

    public ClienteIndicadoresFrame(ConsultaIndicadoresController controller) {
        this.controller = controller;

        setTitle("Cliente Indicadores Económicos - BCCR");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 480);
        setLocationRelativeTo(null);

        initComponents();
        initLayout();
        initEvents();
    }

    private void initComponents() {
        // Indicadores precargados (la UI solo envía el código)
        cboIndicador = new JComboBox<>(new ItemIndicador[]{
                new ItemIndicador(317, "Tipo de cambio compra"),
                new ItemIndicador(318, "Tipo de cambio venta")
        });

        txtFechaInicio = new JTextField("01/01/2024", 10);
        txtFechaFinal = new JTextField("10/01/2024", 10);

        cboSubniveles = new JComboBox<>(new String[]{"N", "S"});

        btnConsultar = new JButton("Consultar");
        btnSalir = new JButton("Listo (Salir)");

        txtResultados = new JTextArea();
        txtResultados.setEditable(false);
        txtResultados.setLineWrap(true);
        txtResultados.setWrapStyleWord(true);

        lblEstado = new JLabel("Listo");
    }

    private void initLayout() {
        // Panel superior: parámetros
        JPanel panelParams = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        panelParams.add(new JLabel("Indicador:"), c);

        c.gridx = 1;
        panelParams.add(cboIndicador, c);

        c.gridx = 0; c.gridy = 1;
        panelParams.add(new JLabel("Fecha inicio (dd/mm/yyyy):"), c);

        c.gridx = 1;
        panelParams.add(txtFechaInicio, c);

        c.gridx = 0; c.gridy = 2;
        panelParams.add(new JLabel("Fecha final (dd/mm/yyyy):"), c);

        c.gridx = 1;
        panelParams.add(txtFechaFinal, c);

        c.gridx = 0; c.gridy = 3;
        panelParams.add(new JLabel("Subniveles:"), c);

        c.gridx = 1;
        panelParams.add(cboSubniveles, c);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnConsultar);
        panelBotones.add(btnSalir);

        c.gridx = 0; c.gridy = 4;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        panelParams.add(panelBotones, c);

        // Panel resultados
        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));
        panelResultados.add(new JScrollPane(txtResultados), BorderLayout.CENTER);

        // Barra estado
        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBorder(BorderFactory.createEmptyBorder(4, 8, 6, 8));
        panelEstado.add(lblEstado, BorderLayout.WEST);

        // Layout final
        setLayout(new BorderLayout());
        add(panelParams, BorderLayout.NORTH);
        add(panelResultados, BorderLayout.CENTER);
        add(panelEstado, BorderLayout.SOUTH);
    }

    private void initEvents() {
        btnConsultar.addActionListener(e -> onConsultar());
        btnSalir.addActionListener(e -> System.exit(0));
    }

    /**
     * Ejecuta la consulta usando SwingWorker para mantener la UI responsiva.
     * Permite hacer múltiples consultas (se re-habilita el botón al final).
     */
    private void onConsultar() {
        btnConsultar.setEnabled(false);
        lblEstado.setText("Consultando servicio BCCR...");
        txtResultados.setText("");

        ItemIndicador item = (ItemIndicador) cboIndicador.getSelectedItem();
        int indicador = item.getCodigo();
        String fi = txtFechaInicio.getText().trim();
        String ff = txtFechaFinal.getText().trim();
        String sub = ((String) cboSubniveles.getSelectedItem()).trim();

        SwingWorker<ResultadoConsulta, Void> worker = new SwingWorker<>() {
            @Override
            protected ResultadoConsulta doInBackground() {
                return controller.consultar(indicador, fi, ff, sub);
            }

            @Override
            protected void done() {
                try {
                    ResultadoConsulta res = get();

                    lblEstado.setText(res.isOk() ? "Listo" : "Error");

                    StringBuilder sb = new StringBuilder();
                    sb.append("Indicador: ").append(item.getCodigo())
                      .append(" - ").append(item.getNombre()).append("\n");
                    sb.append("Rango: ").append(fi).append(" - ").append(ff).append("\n\n");
                    sb.append(res.getMensaje()).append("\n\n");

                    res.getDatos().forEach(d -> sb.append(d).append("\n"));

                    txtResultados.setText(sb.toString());

                } catch (Exception ex) {
                    lblEstado.setText("Error");
                    txtResultados.setText("Error inesperado: " + ex.getMessage());
                } finally {
                    btnConsultar.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    /**
     * Elemento simple para el JComboBox: código + nombre.
     * La UI muestra un texto amigable pero envía el código real al backend.
     */
    public static class ItemIndicador {
        private final int codigo;
        private final String nombre;

        public ItemIndicador(int codigo, String nombre) {
            this.codigo = codigo;
            this.nombre = nombre;
        }

        public int getCodigo() {
            return codigo;
        }

        public String getNombre() {
            return nombre;
        }

        @Override
        public String toString() {
            return codigo + " - " + nombre;
        }
    }
}
